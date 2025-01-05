package org.zerock.knock.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.ConvertImage;
import org.zerock.knock.component.util.StringDateConvertLongTimeStamp;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.LayerClass.Movie;
import org.zerock.knock.service.crawling.common.AbstractCrawlingService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

@Service
public class MegaBox extends AbstractCrawlingService {

    private final Movie movieService;
    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private final ConvertImage convertImage = new ConvertImage();

    @Value("${api.megabox.url}")
    private String urlPath;

    @Value("${api.megabox.cssquery}")
    private String cssQuery;

    @Autowired
    public MegaBox(Movie movieService) {
        this.movieService = movieService;
    }

    @Override
    protected String getUrlPath() {
        return urlPath;
    }

    @Override
    protected String getCssQuery() {
        return cssQuery;
    }

    @Override
    protected void preparePage(WebDriver driver) {

        try {

            Class<?> clazz = Class.forName("org.zerock.knock.component.util.NextBtnWithCssSelector");
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();

            Method method = clazz.getMethod("nextBtn", WebDriver.class, String.class);
            method.invoke(instance, driver, ".btn-more");

        } catch (Exception e) {
            logger.error("Error in preparePage: {}", e.getMessage(), e);
        }
    }

    @Override
    protected void processElement(Element element, Set<MOVIE_DTO> dtos) {
        MOVIE_DTO dto = new MOVIE_DTO();

        logger.info("{} START", getClass().getSimpleName());

        Elements titleElements = element.select("div.tit-area > p.tit");
        if (!titleElements.isEmpty()) {
            dto.setMovieNm(Objects.requireNonNull(titleElements.first()).text());
        }

        Elements dateElements = element.select("div.rate-date > span.date");
        if (!dateElements.isEmpty()) {
            String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
            dto.setOpeningTime(SDCLTS.Converter(date));
        }

        Elements imgElement = element.select("div.movie-list-info img");
        if(!imgElement.isEmpty()) {

            String srcPath = Objects.requireNonNull(imgElement.first()).attr("src");
            try {
                dto.setPosterBase64(convertImage.convertImageToBase64(srcPath));
            } catch (Exception e) {
                logger.info("[{}]", "Exception in ConvertImageTo Base64");
            }

        }

        Elements codeElement = element.select("div.case a.bokdBtn[data-no]");
        if(!codeElement.isEmpty()) {
            String code = Objects.requireNonNull(codeElement.first()).text();
        }
        dtos.add(dto);

        logger.info("{} END", getClass().getSimpleName());
    }

    @Override
    protected void saveData(Set<MOVIE_DTO> dtos) {

        movieService.createMovie(dtos);
    }
}
