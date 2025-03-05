package org.knock.knock_back.service.crawling.performingArts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.knock.knock_back.component.util.converter.StringDateConvertLongTimeStamp;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.knock.knock_back.dto.document.performingArts.KOPIS_INDEX;
import org.knock.knock_back.repository.category.CategoryLevelTwoRepository;
import org.knock.knock_back.repository.performingArts.KOPISRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class KOPIS {

    // Constructor Field
    private final String REQUEST_URL;
    private final String AUTH_KEY;
    private final CategoryLevelTwoRepository categoryLevelTwoRepository;
    private final KOPISRepository kopisRepository;

    // Global Field
    private static final Logger logger = LoggerFactory.getLogger(KOPIS.class);
    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private Map<String, CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoList;

    public KOPIS(@Value("${api.kopis.url}") String requestUrl, @Value("${api.kopis.key}")String authKey,
                 CategoryLevelTwoRepository categoryLevelTwoRepository, KOPISRepository kopisRepository) {
        REQUEST_URL = requestUrl;
        AUTH_KEY = authKey;
        this.categoryLevelTwoRepository = categoryLevelTwoRepository;
        this.kopisRepository = kopisRepository;
    }

    public String makeQueryString(Map<String, String> paramMap) {
        final StringBuilder sb = new StringBuilder();

        paramMap.forEach((key, value) -> {

            if (!sb.isEmpty()) {
                sb.append('&');
            }
            sb.append(key).append('=').append(value);
        });

        return sb.toString();
    }

//    @Async
    public void requestAPI() {

        logger.info("Running in thread: {}", Thread.currentThread().getName());

        Iterable<CATEGORY_LEVEL_TWO_INDEX> musicalSubCategoryIndex = null;
        categoryLevelTwoList = new HashMap<>();

        if (categoryLevelTwoRepository.findAllByParentNm(CategoryLevelOne.PERFORMING_ARTS).isPresent())
        {
            musicalSubCategoryIndex =
                    categoryLevelTwoRepository.findAllByParentNm(CategoryLevelOne.PERFORMING_ARTS).orElseThrow();

            for (CATEGORY_LEVEL_TWO_INDEX category : musicalSubCategoryIndex)
            {
                categoryLevelTwoList.put(category.getNm(), category);
            }
        }

        logger.info("musicalSubCategoryIndex: {}", musicalSubCategoryIndex);
        logger.info("TEST");
        // 변수 설정
        //   - 요청(Request) 인터페이스 Map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("service"   , AUTH_KEY);    // 발급받은 인증키
        paramMap.put("stdate"    , "20000101");  // 조회 기간 STD
        paramMap.put("eddate"    , "29991231");  // 조회 기간 END
        paramMap.put("rows"      , "50");       // 페이지 당 보여줄 row
        paramMap.put("cpage"     , "1");         // 페이지

        try {

            while (true)
            {

                // Request URL 연결 객체 생성
                URL requestURL = URI.create(REQUEST_URL + "?" + makeQueryString(paramMap)).toURL();

                if (Integer.parseInt(paramMap.get("cpage")) > 4) break;
                JSONArray performanceList = getJsonObject(requestURL);

                if (null == performanceList || performanceList.isEmpty())
                {
                    paramMap.put("cpage", String.valueOf(Integer.parseInt(paramMap.get("cpage")) + 1));
                    continue;
                }

                processPerformanceList(performanceList);

                paramMap.put("cpage", String.valueOf(Integer.parseInt(paramMap.get("cpage")) + 1));
            }

        }
        catch (IOException e)
        {
            logger.error("API 요청 중 오류 발생: {}", e.getMessage());
        }

        logger.info("KOPIS 데이터 수집 완료");
    }

    private static JSONArray getJsonObject(URL requestURL) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String readline;
        StringBuilder response = new StringBuilder();

        while ((readline = br.readLine()) != null) {
            response.append(readline);
        }

        JSONObject jsonObject = XML.toJSONObject(response.toString());
        return jsonObject.getJSONObject("dbs").optJSONArray("db");
    }

//    @Async
    protected void processPerformanceList(JSONArray performanceList) {
        List<KOPIS_INDEX> performanceEntities = new ArrayList<>();

        for (int i = 0; i < performanceList.length(); i++) {
            JSONObject performanceJson = performanceList.getJSONObject(i);

            String mt20id = performanceJson.optString("mt20id");
            if (kopisRepository.existsById(mt20id)) continue;

            // 장르 매핑
            String genre = performanceJson.optString("genrenm").toUpperCase();
            CATEGORY_LEVEL_TWO_INDEX category = categoryLevelTwoList.getOrDefault(genre,
                    new CATEGORY_LEVEL_TWO_INDEX(genre, CategoryLevelOne.PERFORMING_ARTS));
            if (!categoryLevelTwoList.containsKey(genre)) {
                categoryLevelTwoRepository.save(category);
                categoryLevelTwoList.put(genre, category);
            }

            KOPIS_INDEX performance = new KOPIS_INDEX();
            performance.setCode(performanceJson.optString("mt20id"));

            // 상세 정보 조회
            fetchPerformanceDetails(performance);
            performanceEntities.add(performance);

        }

        kopisRepository.saveAll(performanceEntities);
    }

//    @Async
    protected void fetchPerformanceDetails(KOPIS_INDEX performance) {
        try {

            URL detailUrl = URI.create(REQUEST_URL + "/" + performance.getCode() + "?service=" + AUTH_KEY).toURL();

            StringBuilder response = getStringBuilder(detailUrl);
            JSONObject jsonObject = XML.toJSONObject(response.toString());
            JSONObject detailJson = jsonObject.getJSONObject("dbs").getJSONObject("db");

            performance.setName(detailJson.optString("prfnm"));
            performance.setFrom(new Date(SDCLTS.Converter(detailJson.optString("prfpdfrom"))));
            performance.setTo(new Date(SDCLTS.Converter(detailJson.optString("prfpdto"))));
            performance.setDirectors(detailJson.optString("prfcrew").split(","));
            performance.setActors(detailJson.optString("prfcast").split(","));
            performance.setCompanyNm(detailJson.optString("entrpsnmP").split(","));
            performance.setHoleNm(detailJson.optString("fcltynm"));
            performance.setPoster(detailJson.optString("poster"));
            performance.setStory(detailJson.optString("sty"));
            performance.setArea(detailJson.optString("area"));
            performance.setState(detailJson.optString("prfstate").equals("공연중"));
            performance.setDtguidance(detailJson.optString("dtguidance").split(","));
            performance.setRelates(null);
            performance.setCategoryLevelOne(CategoryLevelOne.PERFORMING_ARTS);
//            performance.setCategoryLevelTwo(null);

            String runTime = detailJson.optString("prfruntime");
            int time = 0;
            if (runTime.contains("시간"))  time = Integer.parseInt(runTime.split("시간")[0].trim()) * 60;
            if (runTime.contains("분"))  time += Integer.parseInt(runTime.substring(runTime.indexOf(" ") + 1, runTime.indexOf("분")));

            System.out.println(runTime + " : " + time );
            performance.setRunningTime(new Date(time));

        } catch (IOException e) {
            logger.debug("상세정보 조회 실패: {}", e.getMessage());
        }
    }

    private static StringBuilder getStringBuilder(URL detailUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) detailUrl.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        if (response.length() < 2000)
        {
            conn.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

        }
        return response;
    }

}
