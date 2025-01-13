package org.zerock.knock.component.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.document.user.USER_INDEX;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_ONE_DTO;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.dto.dto.user.USER_DTO;
import org.zerock.knock.repository.Category.CategoryLevelOneRepository;

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

    private final CategoryLevelOneRepository categoryLevelOneRepository;
    private final StringDateConvertLongTimeStamp stringDateConvertLongTimeStamp;

    public ConvertDTOAndIndex(CategoryLevelOneRepository categoryLevelOneRepository, StringDateConvertLongTimeStamp stringDateConvertLongTimeStamp, ConvertImage convertImage) {
        this.categoryLevelOneRepository = categoryLevelOneRepository;
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

            MOVIE_INDEX index = new MOVIE_INDEX();
            index.setMovieId(dto.getMovieId());
            index.setMovieNm(dto.getMovieNm());
            index.setOpeningTime(stringDateConvertLongTimeStamp.Converter(dto.getOpeningTime()));
            index.setKOFICCode(dto.getKOFICCode());
            index.setReservationLink(dto.getReservationLink());
            index.setPosterBase64(dto.getPosterBase64());
            index.setDirectors(dto.getDirectors());
            index.setActors(dto.getActors());
            index.setCategoryLevelOne(categoryLevelOneRepository.findByNm("Movie").orElse(new CATEGORY_LEVEL_ONE_INDEX()));
            index.setCategoryLevelTwo(CLTDtoToCLTIndex(dto.getCategoryLevelTwo()));
            index.setRunningTime(dto.getRunningTime());
            index.setPlot(dto.getPlot());
            index.setFavorites(userDtoToUserIndex(dto.getFavorites()));

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
        dto.setCategoryLevelTwo(CLTIndexToCLTDTO(index.getCategoryLevelTwo()));
        dto.setRunningTime(index.getRunningTime());
        dto.setPlot(index.getPlot());
        dto.setFavorites(userIndexToUserDto(index.getFavorites()));

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
     * CATEGORY_LEVEL_ONE INDEX -> CATEGORY_LEVEL_ONE DTO
     *
     * @param index 변환할 CATEGORY_LEVEL_ONE 객체
     * @return CATEGORY_LEVEL_ONE 반환할 CATEGORY_LEVEL_ONE_DTO 객체
     */
    public CATEGORY_LEVEL_ONE_DTO CLTOIndexToCLTODTO(CATEGORY_LEVEL_ONE_INDEX index) {

        CATEGORY_LEVEL_ONE_DTO dto = new CATEGORY_LEVEL_ONE_DTO();

        dto.setId(index.getId());
        dto.setNm(index.getNm());
        dto.setChildCategory(CLTIndexToCLTDTO(index.getChildCategory()));
        dto.setFavoriteUsers(userIndexToUserDto(index.getFavoriteUsers()));

        return dto;
    }

    /**
     * CATEGORY_LEVEL_ONE DTO -> CATEGORY_LEVEL_ONE INDEX
     *
     * @param dto 변환할 CATEGORY_LEVEL_ONE 객체
     * @return CATEGORY_LEVEL_ONE 반환할 CATEGORY_LEVEL_ONE_DTO 객체
     */
    public CATEGORY_LEVEL_ONE_INDEX CLTODTOToCLTOIndex(CATEGORY_LEVEL_ONE_DTO dto) {

        CATEGORY_LEVEL_ONE_INDEX index = new CATEGORY_LEVEL_ONE_INDEX();

        index.setId(dto.getId());
        index.setNm(dto.getNm());
        index.setChildCategory(CLTDtoToCLTIndex(dto.getChildCategory()));
        index.setFavoriteUsers(userDtoToUserIndex(dto.getFavoriteUsers()));

        return index;
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
            dto.setFavoriteUsers(userIndexToUserDto(innerIndex.getFavoriteUsers()));
            dto.setParentNm(innerIndex.getParentNm());

            result.add(dto);
        }


        return result;
    }

    /**
     * CATEGORY_LEVEL_TWO DTO -> CATEGORY_LEVEL_TWO INDEX
     *
     * @param dto 변환할 CATEGORY_LEVEL_TWO 객체
     * @return CATEGORY_LEVEL_TWO 반환할 CATEGORY_LEVEL_TWO INDEX 객체
     */
    public Set<CATEGORY_LEVEL_TWO_INDEX> CLTDtoToCLTIndex(Iterable<CATEGORY_LEVEL_TWO_DTO> dto) {

        Set<CATEGORY_LEVEL_TWO_INDEX> result = new HashSet<>();

        for (CATEGORY_LEVEL_TWO_DTO innerDto : dto) {

            CATEGORY_LEVEL_TWO_INDEX index = new CATEGORY_LEVEL_TWO_INDEX();
            index.setId(innerDto.getId());
            index.setNm(innerDto.getNm());
            index.setFavoriteUsers(userDtoToUserIndex(innerDto.getFavoriteUsers()));
            index.setParentNm(innerDto.getParentNm());

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
