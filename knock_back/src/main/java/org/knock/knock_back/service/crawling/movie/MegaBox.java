package org.knock.knock_back.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
import org.knock.knock_back.dto.document.movie.MOVIE_INDEX;
import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
import org.knock.knock_back.service.layerClass.Movie;
import org.knock.knock_back.service.crawling.common.AbstractCrawlingService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
@Service
public class MegaBox extends AbstractCrawlingService {

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

        Elements titleElements = element.select("div.tit-area > p.tit");

        String tile = Objects.requireNonNull(titleElements.first()).text();

        Optional<MOVIE_INDEX> optionalIndex = movieService.checkMovie(tile);

        if (optionalIndex.isPresent())
        {
            logger.info("{} Already Exists Movie ", tile);
            dtos.add(movieDtoToIndex.MovieIndexToDTO(optionalIndex.get()));
            return;
        }

        KOFIC_INDEX kofic = movieService.similaritySearch(tile);

        if (kofic != null)
        {
            dto = movieDtoToIndex.koficIndexToMovieDTO(kofic);

            // TODO : 일 데이터 없을 경우 예외처리 추가
            if (dto.getOpeningTime().equals("개봉 예정"))
            {
                Elements dateElements = element.select("div.rate-date > span.date");
                if (!dateElements.isEmpty()) {
                    String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
                    dto.setOpeningTime(date);
                }
            }
        }

        else
        {
            Elements dateElements = element.select("div.rate-date > span.date");
            if (!dateElements.isEmpty()) {
                String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉일 ", "");
                dto.setOpeningTime(date);
            }

        }
        encodingBase64(element, dto);
        setReservationLink(element, dto);
        setPlot(element, dto);

        dtos.add(dto);
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
            dto.setPosterBase64(srcPath);
        }
    }

}
