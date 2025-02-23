//package org.knock.knock_back.service.crawling.movie;
//
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
//import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
//import org.knock.knock_back.dto.document.movie.MOVIE_INDEX;
//import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
//import org.knock.knock_back.service.crawling.common.AbstractCrawlingService;
//import org.knock.knock_back.service.layerClass.Movie;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class CGV extends AbstractCrawlingService {
//
//    private final ConvertDTOAndIndex movieDtoToIndex;
//
//    @Value("${api.cgv.url}")
//    private String urlPath;
//
//    @Value("${api.cgv.cssQuery1}")
//    private String cssQuery;
//
//    @Value("${api.cgv.cssQuery2}")
//    private String cssQuery2;
//
//    @Value("${api.cgv.cssQuery3}")
//    private String cssQuery3;
//
//    @Override
//    protected String getUrlPath() { return urlPath; }
//
//    @Override
//    protected String getCssQuery() { return cssQuery; }
//
//    @Override
//    protected String[] prepareCss() {
//        String[] cssQuery = new String[2];
//        cssQuery[0] = cssQuery2;
//        cssQuery[1] = cssQuery3;
//        return cssQuery;
//    }
//
//    protected CGV(Movie movieService, ConvertDTOAndIndex movieDtoToIndex) {
//        super(movieService);
//        this.movieDtoToIndex = movieDtoToIndex;
//    }
//
//    @Override
//    protected void processElement(Element element, Set<MOVIE_DTO> dtos) {
//        MOVIE_DTO dto = new MOVIE_DTO();
//
//        Elements titleElements = element.select("div.tit_area strong.tit");
//        String title = Objects.requireNonNull(titleElements.first()).text();
//
//        Optional<MOVIE_INDEX> optionalIndex = movieService.checkMovie(title);
//        if (optionalIndex.isPresent()) {
//            logger.info("{} Already Exists Movie ", title);
//            dtos.add(movieDtoToIndex.MovieIndexToDTO(optionalIndex.get()));
//            return;
//        }
//
//        KOFIC_INDEX kofic = movieService.similaritySearch(title);
//        if (kofic != null) {
//            dto = movieDtoToIndex.koficIndexToMovieDTO(kofic);
//        }
//
//        Elements dateElements = element.select("span.rel-date");
//        if (!dateElements.isEmpty()) {
//            String date = Objects.requireNonNull(dateElements.first()).text().replace("개봉 ", "");
//            dto.setOpeningTime(date);
//        }
//
//        setReservationLink(element, dto);
//
//        encodingBase64(element, dto);
//
//        dtos.add(dto);
//    }
//
//    private void setReservationLink(Element element, MOVIE_DTO dto) {
//        Elements reservationElement = element.select("div.box-image a");
//        if (!reservationElement.isEmpty()) {
//            String movieCode = Objects.requireNonNull(reservationElement.first()).attr("href");
//            String reservationLink = "https://m.cgv.co.kr" + movieCode;
//
//            String[] reservationLinks;
//            if (dto.getReservationLink() == null) {
//                reservationLinks = new String[3];
//            } else {
//                reservationLinks = dto.getReservationLink();
//            }
//
//            reservationLinks[1] = reservationLink;
//            dto.setReservationLink(reservationLinks);
//        }
//    }
//
//    /**
//     * 영화 포스터를 가져와 Base64로 변환
//     */
//    private void encodingBase64(Element element, MOVIE_DTO dto) {
//        Elements imgElement = element.select("span.imgbox img");
//        if (!imgElement.isEmpty()) {
//            String srcPath = Objects.requireNonNull(imgElement.first()).attr("src");
//            dto.setPosterBase64(srcPath);
//        }
//    }
//}
