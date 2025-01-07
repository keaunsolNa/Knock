package org.zerock.knock.service.crawling.movie;

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
import org.springframework.stereotype.Service;
import org.zerock.knock.service.LayerClass.CategoryInitializer;
import org.zerock.knock.component.util.StringDateConvertLongTimeStamp;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.repository.Category.CategoryLevelOneRepository;
import org.zerock.knock.repository.Category.CategoryLevelTwoRepository;
import org.zerock.knock.repository.movie.KOFICRepository;

@Service
public class KOFIC {

    // Constructor Field
    private final String REQUEST_URL;
    private final String AUTH_KEY;
    private final CategoryLevelOneRepository categoryLevelOneRepository;
    private final CategoryLevelTwoRepository categoryLevelTwoRepository;
    private final KOFICRepository koficRepository;
    private final CategoryInitializer categoryInitializer;

    // Global Field
    private static boolean flag = false;
    private static final Logger logger = LoggerFactory.getLogger(KOFIC.class);
    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private CATEGORY_LEVEL_ONE_INDEX movieCategoryIndex;
    private Map<String, CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoList;

    public KOFIC(@Value("${api.kofic.url}") String requestUrl, @Value("${api.kofic.key}")String authKey, CategoryLevelOneRepository categoryLevelOneRepository, CategoryLevelTwoRepository categoryLevelTwoRepository, KOFICRepository koficRepository, CategoryInitializer categoryInitializer) {
        REQUEST_URL = requestUrl;
        AUTH_KEY = authKey;
        this.categoryLevelOneRepository = categoryLevelOneRepository;
        this.categoryLevelTwoRepository = categoryLevelTwoRepository;
        this.koficRepository = koficRepository;
        this.categoryInitializer = categoryInitializer;
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

    public void requestAPI() {

        logger.info("{} START", getClass().getSimpleName());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        movieCategoryIndex = categoryLevelOneRepository.findByNm("Movie").orElse(new CATEGORY_LEVEL_ONE_INDEX());
        Iterable<CATEGORY_LEVEL_TWO_INDEX> movieSubCategoryIndex = categoryLevelTwoRepository.findAllByParentNm("Movie");
        categoryLevelTwoList = new HashMap<>();

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

                logger.info("[{}]", requestURL);
                JSONObject boxOfficeResult = getJsonObject(requestURL);

                if (null == boxOfficeResult)
                {
                    paramMap.put("curPage", String.valueOf(Integer.parseInt(paramMap.get("curPage")) + 1));
                    continue;
                }
                int totCnt = boxOfficeResult.getInt("totCnt");

                if (totCnt == 0) break;

                JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("movieList");

                flag = false;
                List<KOFIC_INDEX> movieList = parseMovieList(dailyBoxOfficeList);

                koficRepository.saveAll(movieList);

                if (!flag) break;

                paramMap.put("curPage", String.valueOf(Integer.parseInt(paramMap.get("curPage")) + 1));
            }

        }
        catch (IOException e)
        {
            logger.info("{} ERROR", e.getMessage());
        }

        logger.info("{} END", getClass().getSimpleName());
    }

    private static JSONObject getJsonObject(URL requestURL) throws IOException {

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
            responseBody = new JSONObject(response.toString()).getJSONObject("movieListResult");
        }
        catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        // JSON 객체로  변환

        return responseBody;
    }

    // Parse the list of movies from the JSON response
    private List<KOFIC_INDEX> parseMovieList(JSONArray movieJsonArray) {

        List<KOFIC_INDEX> movieList = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++)
        {

            JSONObject movieJson = movieJsonArray.getJSONObject(i);

            if (koficRepository.findByKOFICCode(movieJson.optString("movieCd")) == null)
            {
//                logger.info("{} NEW_INDEX", movieJson.optString("movieCd"));
                flag = true;
            }
            else continue;

            KOFIC_INDEX movie = new KOFIC_INDEX();
            movie.setMovieNm(movieJson.optString("movieNm"));
            movie.setKOFICCode(movieJson.optString("movieCd"));

            if( !movieJson.optString("openDt").isEmpty())
            {
                movie.setOpeningTime(SDCLTS.Converter(movieJson.optString("openDt")));
            }

            if (!movieJson.getJSONArray("directors").isEmpty())
            {
                JSONArray array = movieJson.getJSONArray("directors");
                String[] directors = new String[array.length()];

                for (int index = 0; index < array.length(); index++)
                {
                    JSONObject object = array.getJSONObject(index);
                    directors[index] = object.optString("peopleNm");
                }

                movie.setDirector(directors);
            }

            if (!movieJson.getJSONArray("companys").isEmpty())
            {
                JSONArray array = movieJson.getJSONArray("companys");
                String[] directors = new String[array.length()];

                for (int index = 0; index < array.length(); index++)
                {
                    JSONObject object = array.getJSONObject(index);
                    directors[index] = object.optString("companyNm");
                }

                movie.setDirector(directors);
            }

            movie.setCategoryLevelOne(movieCategoryIndex);

            if (!movieJson.optString("genreAlt").isEmpty())
            {
                Set<CATEGORY_LEVEL_TWO_INDEX> set = new HashSet<>();
                String[] genres = movieJson.optString("genreAlt").split(",");

                for (String genre : genres)
                {
                    if (categoryLevelTwoList.containsKey(genre))
                    {
                        set.add(categoryLevelTwoList.get(genre));
                    }
                    else
                    {
                        CATEGORY_LEVEL_TWO_INDEX categoryLevelTwoIndex = new CATEGORY_LEVEL_TWO_INDEX();
                        categoryLevelTwoIndex.setNm(genre);
                        categoryLevelTwoIndex.setParentNm("Movie");
                        categoryLevelTwoRepository.save(categoryLevelTwoIndex);
                        categoryLevelTwoList.put(genre, categoryLevelTwoIndex);

                        categoryInitializer.insertCategoryLevelOne(new String[] {"Movie"} );
                        set.add(categoryLevelTwoIndex);
                    }
                }

                movie.setCategoryLevelTwo(set);
            }

            if( !movieJson.optString("prdtYear").isEmpty())
            {
                movie.setPrdtYear(SDCLTS.Converter(movieJson.optString("prdtYear")));
            }

            movieList.add(movie);
        }

        return movieList;
    }
}
 