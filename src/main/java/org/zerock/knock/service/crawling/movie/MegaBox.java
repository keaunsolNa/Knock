package org.zerock.knock.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.StringDateConvertLongTimeStamp;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.LayerClass.Movie;
import org.zerock.knock.service.crawling.CrawlingInterface;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class MegaBox implements CrawlingInterface {

    private final Movie movieService;

    @Autowired
    public MegaBox(Movie movieService) {
        this.movieService = movieService;
    }

    @Override
    public void addNewBrands() {

        logger.info("MEGA_BOX START");

        // Webdriver initialize
        String url = "https://www.megabox.co.kr/movie/comingsoon";
        String cssQuery = "ol#movieList > li";

        StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
        ElementExtractor extractor = new ElementExtractor(url, cssQuery);

        extractor.setUpDriver();
        extractor.preparePage("org.zerock.knock.component.util.NextBtnWithCssSelector", "nextBtn", ".btn-more");
        extractor.run();

        Elements elements = extractor.getElements();

        Set<MOVIE_DTO> dtos = new HashSet<>();

        for (Element element : elements)
        {
            Elements titleElements = element.select("div.tit-area > p.tit");
            MOVIE_DTO dto = new MOVIE_DTO();

            if (!titleElements.isEmpty())
            {
                String title = Objects.requireNonNull(titleElements.first()).text();
                dto.setMovieNm(title);
                logger.info("제목 {}", title);
            }

            Elements dateElements = element.select("div.rate-date > span.date");
            if (!dateElements.isEmpty())
            {
                String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
                logger.info("개봉일 {}", date);
                long time = SDCLTS.Converter(date);
                dto.setOpeningTime(time);
            }
            logger.info("NEXT");

            dtos.add(dto);

        }

        movieService.createMovie(dtos);
        logger.info("EXTRACTOR END");

    }
}
