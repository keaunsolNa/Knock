package org.zerock.knock.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.ConvertDTOAndIndex;
import org.zerock.knock.component.util.ConvertImage;
import org.zerock.knock.component.util.StringDateConvertLongTimeStamp;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.LayerClass.Movie;
import org.zerock.knock.service.crawling.common.AbstractCrawlingService;

import java.util.Objects;
import java.util.Set;

/**
 *
 */
@Service
public class MegaBox extends AbstractCrawlingService {

    private final StringDateConvertLongTimeStamp SDCLTS = new StringDateConvertLongTimeStamp();
    private final ConvertImage convertImage = new ConvertImage();
    private final ConvertDTOAndIndex movieDtoToIndex;
    @Value("${api.megabox.url}")
    private String urlPath;

    @Value("${api.megabox.cssquery}")
    private String cssQuery;

    protected MegaBox(Movie movieService, ConvertDTOAndIndex movieDtoToIndex) {
        super(movieService);
        this.movieDtoToIndex = movieDtoToIndex;
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
    protected String[] prepareCss() {
        String[] cssQuery = new String[2];
        cssQuery[0] = "nextBtn";
        cssQuery[1] = ".btn-more";
        return cssQuery;
    }


    @Override
    @Async
    protected void processElement(Element element, Set<MOVIE_DTO> dtos) {
        MOVIE_DTO dto = new MOVIE_DTO();

        logger.info("{} START", getClass().getSimpleName());

        Elements titleElements = element.select("div.tit-area > p.tit");

        String tile = Objects.requireNonNull(titleElements.first()).text();
        KOFIC_INDEX kofic = movieService.similaritySearch(tile);

        if (kofic != null)
        {
            dto = movieDtoToIndex.koficIndexToMovieIndex(kofic);
        }

        else
        {
            Elements dateElements = element.select("div.rate-date > span.date");
            if (!dateElements.isEmpty()) {
                String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
                dto.setOpeningTime(SDCLTS.Converter(date));
            }

        }
        encodingBase64(element, dto);
        setReservationLink(element, dto);
        setPlot(element, dto);

        dtos.add(dto);
        logger.info("{} END", getClass().getSimpleName());
    }

    private void setReservationLink(Element element, MOVIE_DTO dto) {
        Elements codeElement = element.select("div.movie-score");

        if(!codeElement.isEmpty()) {

            String dataNo = Objects.requireNonNull(element.select("a.movieBtn").first()).attr("data-no");
            String reservationLink = "https://www.megabox.co.kr/movie-detail?rpstMovieNo=" + dataNo;

            String[] reservationLinks;
            if (dto.getReservationLink() == null)
            {
                reservationLinks = new String[3];
            }
            else
            {
                reservationLinks = dto.getReservationLink();
            }

            reservationLinks[0] = reservationLink;
            dto.setReservationLink(reservationLinks);
        }
    }

    private void setPlot (Element element, MOVIE_DTO dto) {
        Elements codeElement = element.select("div.summary");

        if(!codeElement.isEmpty()) {

            String summeryText = codeElement.text();
            dto.setPlot(summeryText);

        }
    }

    private void encodingBase64(Element element, MOVIE_DTO dto) {

        Elements imgElement = element.select("div.movie-list-info img");
        if(!imgElement.isEmpty()) {

            String srcPath = Objects.requireNonNull(imgElement.first()).attr("src");
            try {
                dto.setPosterBase64(convertImage.convertImageToBase64(srcPath));
            } catch (Exception e) {
                logger.info("[{}]", "Exception in ConvertImageTo Base64");
            }

        }
    }

}
