package org.knock.knock_back.service.crawling.common;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
import org.knock.knock_back.dto.dto.crawling.CrawlingConfig;
import org.knock.knock_back.dto.dto.crawling.CrawlingProperties;
import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
import org.knock.knock_back.service.layerClass.Movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CrawlingService extends AbstractCrawlingService {

    private final ConvertDTOAndIndex movieDtoToIndex;
    private final Map<String, CrawlingConfig> sourceConfigMap;
    Logger logger = LoggerFactory.getLogger(CrawlingService.class);

    protected CrawlingService(Movie movieService, ConvertDTOAndIndex movieDtoToIndex,
                              CrawlingProperties crawlingProperties) {
        super(movieService);
        this.movieDtoToIndex = movieDtoToIndex;
        this.sourceConfigMap = crawlingProperties.getSources();
    }

    private CrawlingConfig currentConfig;

    public void startCrawling(String sourceName) {
        this.currentConfig = sourceConfigMap.get(sourceName.toUpperCase());
        if (this.currentConfig == null) {
            throw new IllegalArgumentException("Invalid source name: " + sourceName);
        }
        addNewIndex();
    }

    @Override
    protected String getUrlPath() {
        return currentConfig.getUrl();
    }

    @Override
    protected String getCssQuery() {
        return currentConfig.getCssQuery1();
    }

    @Override
    protected String[] prepareCss() {
        return new String[]{ currentConfig.getCssQuery2(), currentConfig.getCssQuery3() };
    }

    @Override
    protected void processElement(Element element, Set<MOVIE_DTO> dtos) {

        MOVIE_DTO dto = new MOVIE_DTO();

        Elements titleElements = element.select(currentConfig.getTitleQuery());

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

        if (movieService.checkMovie(title).isPresent()) {
            logger.info("{} Already Exists Movie ", title);
            dto = movieDtoToIndex.MovieIndexToDTO(movieService.checkMovie(title).get());
            setReservationLink(element, dto);
            dtos.add(dto);
            return;
        }

        KOFIC_INDEX kofic = movieService.similaritySearch(title);
        if (kofic != null) {
            dto = movieDtoToIndex.koficIndexToMovieDTO(kofic);
        }

        Elements dateElements = element.select(currentConfig.getDateQuery());

        if (!dateElements.isEmpty()) {

            String date;
            if (currentConfig.getName().equals("LOTTE"))
            {
                date = convertReleaseDate(dateElements);
            }
            else
            {
                date = Objects.requireNonNull(dateElements.first()).text().replace(currentConfig.getDateExtract(), "");
                date = date.trim();
                logger.info("{} Date Extract : {}", date, date);
            }
            dto.setOpeningTime(date);
        }

        setReservationLink(element, dto);
        encodeBase64(element, dto);

        if (currentConfig.getName().equals("MEGABOX")) setPlot(element, dto);

        dtos.add(dto);
    }

    private void setReservationLink(Element element, MOVIE_DTO dto) {
        Elements reservationElement = element.select(currentConfig.getReservationQuery());

        if (!reservationElement.isEmpty()) {
            String reservationLink = reservationElement.attr(currentConfig.getReservationExtract());

            if (currentConfig.getName().equals("CGV"))
            {
                Pattern pattern = Pattern.compile("fnQuickReserve\\('(\\d+)'");
                Matcher matcher = pattern.matcher(reservationLink);
                if (matcher.find()) {
                    reservationLink = matcher.group(1);
                } else {
                    logger.warn("No Movie ID Found in OnClick: {}", reservationLink);
                    return;
                }

            }
            if (!currentConfig.getName().equals("LOTTE")) reservationLink = currentConfig.getReservationPrefix() + reservationLink;

            String[] reservationLinks;
            int idx = currentConfig.getName().equals("MEGABOX") ? 0 : currentConfig.getName().equals("CGV") ? 1 : 2;
            if (dto.getReservationLink() == null) {
                reservationLinks = new String[3];
            } else {
                reservationLinks = dto.getReservationLink();
            }
            reservationLinks[idx] = reservationLink;
            dto.setReservationLink(reservationLinks);
        }
    }

    private void encodeBase64(Element element, MOVIE_DTO dto) {
        Elements imgElement = element.select(currentConfig.getPosterQuery());
        if (!imgElement.isEmpty()) {
            String srcPath = Objects.requireNonNull(imgElement.first()).attr(currentConfig.getPosterExtract());
            dto.setPosterBase64(srcPath);
        }
    }

    private void setPlot (Element element, MOVIE_DTO dto) {
        Elements codeElement = element.select(currentConfig.getPlotQuery());

        if(!codeElement.isEmpty()) {

            String summeryText = codeElement.text();
            dto.setPlot(summeryText);

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
