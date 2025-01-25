package org.zerock.knock.component.util.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zerock.knock.dto.Enum.CategoryLevelOne;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.document.user.USER_INDEX;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.dto.dto.user.USER_DTO;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author nks
 * @apiNote MOVIE DTO <-> INDEX
 */
@Component
public class ConvertDTOAndIndex {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final StringDateConvertLongTimeStamp stringDateConvertLongTimeStamp;

    public ConvertDTOAndIndex(StringDateConvertLongTimeStamp stringDateConvertLongTimeStamp) {
        this.stringDateConvertLongTimeStamp = stringDateConvertLongTimeStamp;
    }

    /**
     * MOVIE DTO -> INDEX
     * @param dtos 변환할 MOVIE_DTO 객체
     * @return SET<MOVIE_INDEX> 반환할 MOVIE_INDEX 객체
     */
    public Set<MOVIE_INDEX> MovieDtoToIndex(Iterable<MOVIE_DTO> dtos) {

        Set<MOVIE_INDEX> result = new HashSet<>();
        for (MOVIE_DTO dto : dtos) {

            MOVIE_INDEX index =
                    new MOVIE_INDEX(
                            dto.getMovieId()
                            , dto.getMovieNm()
                            , stringDateConvertLongTimeStamp.Converter(dto.getOpeningTime())
                            , dto.getKOFICCode()
                            , dto.getReservationLink()
                            , dto.getPosterBase64()
                            , dto.getDirectors()
                            , dto.getActors()
                            , dto.getCompanyNm()
                            , CategoryLevelOne.MOVIE
                            , CLTDtoToCLTIndex(dto.getCategoryLevelTwo())
                            , dto.getRunningTime()
                            , dto.getPlot()
                            , dto.getFavorites()
                    );
            result.add(index);
        }

        return result;
    }

    /**
     * MOVIE INDEX -> DTO
     *
     * @param indexs 변환할 MOVIE_INDEX 객체
     * @return SET<MOVIE_DTO> 반환할 MOVIE_DTO 객체
     */
    public Set<MOVIE_DTO> MovieIndexToDTO(Iterable<MOVIE_INDEX> indexs) {

        logger.info("[{}] ", indexs);
        Set<MOVIE_DTO> result = new LinkedHashSet<>();
        for (MOVIE_INDEX index : indexs) {
            result.add(MovieIndexToDTO(index));
        }

        return result;
    }

    /**
     * MOVIE INDEX -> DTO
     *
     * @param index 변환할 MOVIE_INDEX 객체
     * @return MOVIE_DTO 반환할 MOVIE_DTO 객체
     */
    public MOVIE_DTO MovieIndexToDTO(MOVIE_INDEX index)
    {

        MOVIE_DTO dto = new MOVIE_DTO();
        dto.setMovieId(index.getMovieId());
        dto.setMovieNm(index.getMovieNm());
        dto.setOpeningTime(stringDateConvertLongTimeStamp.Converter(index.getOpeningTime()));
        dto.setKOFICCode(index.getKOFICCode());
        dto.setReservationLink(index.getReservationLink());
        dto.setPosterBase64(index.getPosterBase64());
        dto.setDirectors(index.getDirectors());
        dto.setActors(index.getActors());
        dto.setCompanyNm(index.getCompanyNm());
        dto.setCategoryLevelOne(index.getCategoryLevelOne());
        dto.setCategoryLevelTwo(CLTIndexToCLTDTO(index.getCategoryLevelTwo()));
        dto.setRunningTime(index.getRunningTime());
        dto.setPlot(index.getPlot());
        dto.setFavorites(index.getFavorites());

        return dto;
    }


    /**
     * KOFIC INDEX -> MOVIE DTO
     *
     * @param index 변환할 KOFIC 객체
     * @return SET<MOVIE_INDEX> 반환할 MOVIE_INDEX 객체
     */
    public MOVIE_DTO koficIndexToMovieDTO(KOFIC_INDEX index) {

        MOVIE_DTO dto = new MOVIE_DTO();

        dto.setMovieId(index.getMovieId());
        dto.setMovieNm(index.getMovieNm());
        dto.setOpeningTime(stringDateConvertLongTimeStamp.Converter(index.getOpeningTime()));
        dto.setKOFICCode(index.getKOFICCode());
        dto.setDirectors(index.getDirectors());
        dto.setActors(index.getActors());
        dto.setCompanyNm(index.getCompanyNm());
        dto.setCategoryLevelTwo(CLTIndexToCLTDTO(index.getCategoryLevelTwo()));
        dto.setRunningTime(null == index.getRunningTime() ? 0 : index.getRunningTime());

        return dto;
    }

    /**
     * CATEGORY_LEVEL_TWO INDEX -> CATEGORY_LEVEL_TWO DTO
     *
     * @param index 변환할 CATEGORY_LEVEL_TWO 객체
     * @return CATEGORY_LEVEL_TWO 반환할 CATEGORY_LEVEL_TWO DTO 객체
     */
    public Set<CATEGORY_LEVEL_TWO_DTO> CLTIndexToCLTDTO(Iterable<CATEGORY_LEVEL_TWO_INDEX> index) {

        Set<CATEGORY_LEVEL_TWO_DTO> result = new HashSet<>();

        for (CATEGORY_LEVEL_TWO_INDEX innerIndex : index) {

            CATEGORY_LEVEL_TWO_DTO dto = new CATEGORY_LEVEL_TWO_DTO();
            dto.setId(innerIndex.getId());
            dto.setNm(innerIndex.getNm());
            dto.setFavoriteUsers(innerIndex.getFavoriteUsers());
            dto.setParentNm(innerIndex.getParentNm());

            result.add(dto);
        }


        return result;
    }

    /**
     * CATEGORY_LEVEL_TWO DTO -> CATEGORY_LEVEL_TWO INDEX
     *
     * @param dto 변환할 CATEGORY_LEVEL_TWO 객체
     * @return Set<CATEGORY_LEVEL_TWO> 반환할 CATEGORY_LEVEL_TWO INDEX 객체
     */
    public Set<CATEGORY_LEVEL_TWO_INDEX> CLTDtoToCLTIndex(Iterable<CATEGORY_LEVEL_TWO_DTO> dto) {

        Set<CATEGORY_LEVEL_TWO_INDEX> result = new HashSet<>();

        for (CATEGORY_LEVEL_TWO_DTO innerDto : dto) {

            CATEGORY_LEVEL_TWO_INDEX index =
                    new CATEGORY_LEVEL_TWO_INDEX
                            (
                                    innerDto.getId()
                                    , innerDto.getNm()
                                    , innerDto.getParentNm()
                                    , innerDto.getFavoriteUsers()
                            );
            result.add(index);
        }


        return result;
    }

    /**
     * USER INDEX -> USER DTO
     *
     * @param index 변환할 USER_INDEX 객체
     * @return USER_DTO 반환할 USER_DTO 객체
     */
    public Set<USER_DTO> userIndexToUserDto (Iterable<USER_INDEX> index) {

        Set<USER_DTO> set = new HashSet<>();

        USER_DTO userDto = new USER_DTO();

        set.add(userDto);
        return set;
    }

    /**
     * USER DTO -> USER INDEX
     *
     * @param dto 변환할 USER_DTO 객체
     * @return Set<USER_DTO> 반환할 USER_DTO 객체
     */
    public Set<USER_INDEX> userDtoToUserIndex (Iterable<USER_DTO> dto) {

        Set<USER_INDEX> set = new HashSet<>();

        USER_INDEX userIndex = new USER_INDEX();

        set.add(userIndex);
        return set;
    }
}
