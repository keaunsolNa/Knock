package org.zerock.knock.service.crawling.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.LayerClass.Movie;
import org.zerock.knock.service.crawling.CrawlingInterface;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCrawlingService implements CrawlingInterface {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Movie movieService;

    protected AbstractCrawlingService(Movie movieService) {
        this.movieService = movieService;
    }

    protected abstract String getUrlPath();
    protected abstract String getCssQuery();
    protected abstract String[] prepareCss();
    protected abstract void processElement(Element element, Set<MOVIE_DTO> dtos);

    @Override
    public void addNewBrands() {
        logger.info("{} START", getClass().getSimpleName());

        ElementExtractor extractor = new ElementExtractor(getUrlPath(), getCssQuery());
        extractor.setUpDriver();
        extractor.preparePage(extractor.getDriver(), prepareCss());
        extractor.run();

        Elements elements = extractor.getElements();
        Set<MOVIE_DTO> dtos = new HashSet<>();
        for (Element element : elements) {
            processElement(element, dtos);
        }

        saveData(dtos);
        logger.info("{} END", getClass().getSimpleName());
    }

    protected void saveData(Set<MOVIE_DTO> dtos) {

        movieService.createMovie(dtos);
    }
}
