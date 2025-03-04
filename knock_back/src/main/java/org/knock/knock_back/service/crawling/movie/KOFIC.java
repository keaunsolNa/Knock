package org.knock.knock_back.service.crawling.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.component.util.converter.StringDateConvertLongTimeStamp;
import org.knock.knock_back.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
import org.knock.knock_back.repository.category.CategoryLevelTwoRepository;
import org.knock.knock_back.repository.movie.KOFICRepository;

@Service
public class KOFIC {

    // Constructor Field
    private final String REQUEST_URL;
    private final String REQUEST_URL_SUB;
    private final String AUTH_KEY;
    private final CategoryLevelTwoRepository categoryLevelTwoRepository;
    private final KOFICRepository koficRepository;

    // Global Field
    private static boolean flag = false;
    private static final Logger logger = LoggerFactory.getLogger(KOFIC.class);
    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private Map<String, CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoList;

    public KOFIC(@Value("${api.kofic.url}") String requestUrl, @Value("${api.kofic.urlsub}") String requestUrlSub, @Value("${api.kofic.key}")String authKey,
                 CategoryLevelTwoRepository categoryLevelTwoRepository, KOFICRepository koficRepository) {
        REQUEST_URL = requestUrl;
        REQUEST_URL_SUB = requestUrlSub;
        AUTH_KEY = authKey;
        this.categoryLevelTwoRepository = categoryLevelTwoRepository;
        this.koficRepository = koficRepository;
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

    @Async
    public void requestAPI() {

        logger.info("Running in thread: {}", Thread.currentThread().getName());

        Iterable<CATEGORY_LEVEL_TWO_INDEX> movieSubCategoryIndex = null;
        if (categoryLevelTwoRepository.findAllByParentNm(CategoryLevelOne.MOVIE).isPresent())
        {
            movieSubCategoryIndex =
                    categoryLevelTwoRepository.findAllByParentNm(CategoryLevelOne.MOVIE).orElseThrow();
        }

        categoryLevelTwoList = new HashMap<>();

        assert movieSubCategoryIndex != null;
        for (CATEGORY_LEVEL_TWO_INDEX category : movieSubCategoryIndex)
        {
            categoryLevelTwoList.put(category.getNm(), category);
        }

        // 변수 설정
        //   - 요청(Request) 인터페이스 Map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("key"          , AUTH_KEY);    // 발급받은 인증키
        paramMap.put("itemPerPage"  , "100");       // 페이지 당 보여줄 row
        paramMap.put("curPage"      , "1");         // 페이지

        try {

            while (true)
            {

                // Request URL 연결 객체 생성
                URL requestURL = URI.create(REQUEST_URL + "?" + makeQueryString(paramMap)).toURL();

                if (Integer.parseInt(paramMap.get("curPage")) > 3000) break;
                JSONObject boxOfficeResult = getJsonObject(requestURL, "movieListResult");

                if (null == boxOfficeResult)
                {
                    paramMap.put("curPage", String.valueOf(Integer.parseInt(paramMap.get("curPage")) + 1));
                    continue;
                }
                int totCnt = boxOfficeResult.getInt("totCnt");

                if (totCnt == 0) break;

                JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("movieList");

                flag = false;
                parseMovieList(dailyBoxOfficeList);

                if (!flag) break;

                paramMap.put("curPage", String.valueOf(Integer.parseInt(paramMap.get("curPage")) + 1));
            }

        }
        catch (IOException e)
        {
            logger.debug("{} ERROR", e.getMessage());
        }

        logger.debug("{} END", getClass().getSimpleName());
    }

    private static JSONObject getJsonObject(URL requestURL, String resultTarget) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

        // GET 방식으로 요청
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String readline;
        StringBuilder response = new StringBuilder();
        while ((readline = br.readLine()) != null) {
            response.append(readline);
        }

        JSONObject responseBody = null;
        try
        {
            responseBody = new JSONObject(response.toString()).getJSONObject(resultTarget);
        }
        catch (Exception e)
        {
            logger.debug(e.getMessage());
        }

        // JSON 객체로  변환
        return responseBody;
    }

    @Async
    protected void parseMovieList(JSONArray movieJsonArray) {

        List<KOFIC_INDEX> movieList = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++)
        {

            JSONObject movieJson = movieJsonArray.getJSONObject(i);

            if (koficRepository.findByKOFICCode(movieJson.optString("movieCd")) == null)
            {
                flag = true;
            }
            else continue;


            String movieCd = movieJson.optString("movieCd").isEmpty() ? "" : movieJson.optString("movieCd");
            String movieNm = movieJson.optString("movieNm").isEmpty() ? "" : movieJson.optString("movieNm");
            Long prdtYear = movieJson.optString("prdtYear").isEmpty() ? 0L : SDCLTS.Converter(movieJson.optString("prdtYear"));
            Long openingTime = movieJson.optString("openDt").isEmpty() ? 0L : SDCLTS.Converter(movieJson.optString("openDt"));

            JSONArray array = movieJson.getJSONArray("directors");
            String[] directors = new String[array.length()];

            if (!movieJson.getJSONArray("directors").isEmpty())
            {
                for (int index = 0; index < array.length(); index++)
                {
                    JSONObject object = array.getJSONObject(index);
                    directors[index] = object.optString("peopleNm");
                }
            }

            array = movieJson.getJSONArray("companys");
            String[] companys = new String[array.length()];
            if (!movieJson.getJSONArray("companys").isEmpty())
            {
                array = movieJson.getJSONArray("companys");

                for (int index = 0; index < array.length(); index++)
                {
                    JSONObject object = array.getJSONObject(index);
                    companys[index] = object.optString("companyNm");
                }
            }

            Set<CATEGORY_LEVEL_TWO_INDEX> set = new HashSet<>();
            if (!movieJson.optString("genreAlt").isEmpty())
            {
                String[] genres = movieJson.optString("genreAlt").toUpperCase().split(",");

                for (String genre : genres)
                {
                    if (categoryLevelTwoList.containsKey(genre))
                    {
                        set.add(categoryLevelTwoList.get(genre));
                    }
                    else
                    {
                        CATEGORY_LEVEL_TWO_INDEX categoryLevelTwoIndex =
                                new CATEGORY_LEVEL_TWO_INDEX(genre, CategoryLevelOne.MOVIE);
                        categoryLevelTwoRepository.save(categoryLevelTwoIndex);
                        categoryLevelTwoList.put(genre, categoryLevelTwoIndex);

                        set.add(categoryLevelTwoIndex);
                    }
                }
            }

            KOFIC_INDEX movie = new KOFIC_INDEX
                    (movieCd, movieNm, prdtYear, openingTime, directors, companys, CategoryLevelOne.MOVIE, set);



            setDetailInfo(movie, movieJson.optString("movieCd"));

            movieList.add(movie);
        }

        koficRepository.saveAll(movieList);
    }

    @Async
    protected void setDetailInfo (KOFIC_INDEX movieIndex, String movieCd) {

        String[] returnValue;
        try {

            URL requestURL = URI.create(REQUEST_URL_SUB + "?key=" + AUTH_KEY + "&movieCd=" + movieCd).toURL();
            JSONObject boxOfficeResult = getJsonObject(requestURL, "movieInfoResult").getJSONObject("movieInfo");
            JSONArray array = boxOfficeResult.getJSONArray("actors");

            if (!boxOfficeResult.optString("movieCd").isEmpty())
            {
                movieIndex.setRunningTime(Long.parseLong(boxOfficeResult.optString("showTm").isEmpty() ? "0" : boxOfficeResult.optString("showTm")));
            }
            returnValue = new String[array.length()];

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject actor = array.getJSONObject(i);
                returnValue[i] = actor.optString("peopleNm");
            }

            movieIndex.setActors(returnValue);
        }
        catch (IOException e) { logger.debug(e.getMessage()); }

    }
}
 