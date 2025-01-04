package org.zerock.knock.service.crawling.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.StringDateConvertLongTimeStamp;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;

@Service
public class KOFIC {

    private final String REQUEST_URL;
    private final String AUTH_KEY;

    private static final Logger logger = LoggerFactory.getLogger(KOFIC.class);
    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyyMMdd");

    public KOFIC(@Value("${api.kofic.url}") String requestUrl, @Value("${api.kofic.key}")String authKey) {
        REQUEST_URL = requestUrl;
        AUTH_KEY = authKey;
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

        logger.info(AUTH_KEY);
        logger.info(REQUEST_URL);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // 변수 설정
        //   - 요청(Request) 인터페이스 Map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("key"          , AUTH_KEY);                        // 발급받은 인증키

        try {
            // Request URL 연결 객체 생성
            URL requestURL = new URL(REQUEST_URL + "?" + makeQueryString(paramMap));

            JSONObject boxOfficeResult = getJsonObject(requestURL);
            JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("movieList");

            List<MOVIE_DTO> movieList = parseMovieList(dailyBoxOfficeList);

            logger.info("[{}]", movieList);

        }
        catch (IOException e)
        {
            logger.error("[{}]", e.getMessage());
        }
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

        // JSON 객체로  변환
        JSONObject responseBody = new JSONObject(response.toString());
        return responseBody.getJSONObject("movieListResult");
    }

    // Parse the list of movies from the JSON response
    private List<MOVIE_DTO> parseMovieList(JSONArray movieJsonArray) {
        List<MOVIE_DTO> movieList = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++)
        {

            JSONObject movieJson = movieJsonArray.getJSONObject(i);
            MOVIE_DTO movie = new MOVIE_DTO();
            movie.setMovieNm(movieJson.optString("movieNm"));
            movie.setKOFICCode(movieJson.optString("movieCd"));

            if( !movieJson.optString("openDt").isEmpty())
            {
                movie.setOpeningTime(SDCLTS.Converter(movieJson.optString("openDt")));
            }
            movieList.add(movie);
        }

        logger.info("[{}]", movieList);
        return movieList;
    }
}
 