package org.knock.knock_back.service.crawling.movie;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
import org.knock.knock_back.dto.document.movie.MOVIE_INDEX;
import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
import org.knock.knock_back.service.crawling.common.AbstractCrawlingService;
import org.knock.knock_back.service.layerClass.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class LOTTE extends AbstractCrawlingService {

    private final ConvertDTOAndIndex movieDtoToIndex;

    @Value("${api.lotte.url}")
    private String urlPath;

    @Value("${api.lotte.cssquery}")
    private String cssQuery;

    @Override
    protected String getUrlPath() { return urlPath; }

    @Override
    protected String getCssQuery() { return cssQuery; }

    @Override
    protected String[] prepareCss() {
        String[] cssQuery = new String[2];
        cssQuery[0] = "scrollDownUntilElementLoaded";
        cssQuery[1] = ".screen_add_box";
        return cssQuery;
    }

    protected LOTTE(Movie movieService, ConvertDTOAndIndex movieDtoToIndex) {
        super(movieService);
        this.movieDtoToIndex = movieDtoToIndex;
    }

    @Override
    protected void processElement(Element element, Set<MOVIE_DTO> dtos) {

        MOVIE_DTO dto = new MOVIE_DTO();

        Elements titleElements = element.select("div.btm_info strong.tit_info");
        String title;
        try
        {
             title = Objects.requireNonNull(titleElements.first()).text();
        }
        catch (NullPointerException e)
        {
            logger.error(e.getMessage());
            return;
        }


        Optional<MOVIE_INDEX> optionalIndex = movieService.checkMovie(title);
        if (optionalIndex.isPresent()) {
            logger.info("{} Already Exists Movie ", title);
            dtos.add(movieDtoToIndex.MovieIndexToDTO(optionalIndex.get()));
            return;
        }

        KOFIC_INDEX kofic = movieService.similaritySearch(title);
        if (kofic != null) {
            dto = movieDtoToIndex.koficIndexToMovieDTO(kofic);
        }

        Elements dateElements = element.select("span.remain_info");

        if (!dateElements.isEmpty()) {
            String releaseDate = convertReleaseDate(dateElements);
            dto.setOpeningTime(releaseDate);
        }

        logger.info("[{}] Movie : {}", title, dto);
        setReservationLink(element, dto);
        encodingBase64(element, dto);

        dtos.add(dto);
    }

    private void setReservationLink(Element element, MOVIE_DTO dto) {
        Elements reservationElement = element.select("div.over_box a[href]");
        if (!reservationElement.isEmpty()) {
            String reservationLink = reservationElement.attr("href");
            String[] reservationLinks;
            if (dto.getReservationLink() == null) {
                reservationLinks = new String[3];
            } else {
                reservationLinks = dto.getReservationLink();
            }

            reservationLinks[2] = reservationLink;
            dto.setReservationLink(reservationLinks);
        }
    }

    /**
     * 영화 포스터를 가져와 Base64로 변환
     */
    private void encodingBase64(Element element, MOVIE_DTO dto) {
        Elements imgElement = element.select("div.poster_info  img");
        if (!imgElement.isEmpty()) {
            String srcPath = Objects.requireNonNull(imgElement.first()).attr("src");
            dto.setPosterBase64(srcPath);
        }
    }

    /**
     * "D-XX" 형식의 날짜를 실제 개봉일(YYYY.MM.DD)로 변환
     */
    private String convertReleaseDate(Elements dateElements) {
        if (!dateElements.isEmpty()) {
            String remainText = dateElements.text().trim();

            // "D-XX" 형식인지 확인
            if (remainText.matches("D-\\d+")) {
                int daysRemaining = Integer.parseInt(remainText.replace("D-", ""));
                LocalDate releaseDate = LocalDate.now().plusDays(daysRemaining);
                return releaseDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            }
        }
        return "미정";  // 개봉일 정보가 없을 경우
    }

}
