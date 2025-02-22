package org.knock.knock_back.service.layerClass;

import lombok.RequiredArgsConstructor;
import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
import org.knock.knock_back.dto.Enum.AlarmTiming;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.document.movie.MOVIE_INDEX;
import org.knock.knock_back.dto.document.user.SSO_USER_INDEX;
import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
import org.knock.knock_back.dto.dto.user.SSO_USER_DTO;
import org.knock.knock_back.repository.movie.MovieRepository;
import org.knock.knock_back.repository.user.SSOUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author nks
 * @apiNote user 정보 변경에 활용되는 service
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ConvertDTOAndIndex convertDTOAndIndex;
    private final MovieRepository movieRepository;
    private final SSOUserRepository ssoUserRepository;

    /**
     * 토큰으로 부터 유저 정보 획득하여 반환한다
     * @return userDto
     */
    public SSO_USER_DTO getUserInfo()
    {

        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return convertDTOAndIndex.userIndexToUserDTO(user);
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return null;
        }
    }

    /**
     * 전체 구독 목록을 가져온다.
     * @return set : 카테고리 별 구독 목록 id
     */
    public Map<String, Iterable<?>> getUserCategorySubscribeList()
    {
        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<CategoryLevelOne, LinkedList<String>> map = user.getSubscribeList();

            Map<String, Iterable<?>> userSubscribeList = new HashMap<>();

            for (CategoryLevelOne category : map.keySet())
            {
                LinkedList<String> list = map.get(category);
                Set<?> set =  new HashSet<>(list);

                userSubscribeList.put(category.name(), set);
            }

            return userSubscribeList;
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return null;
        }

    }

    /**
     * 카테고리 별 구독 목록을 가져온다.
     * @param categoryLevelOne : 확인할 대상의 종류
     * @return set : 카테고리 별 구독 목록 id
     */
    public Iterable<?> getUserSubscribeList(CategoryLevelOne categoryLevelOne)
    {
        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LinkedList<String> list = user.getSubscribeList().get(categoryLevelOne);

            return makeSet(categoryLevelOne, list);
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return null;
        }

    }

    /**
     * 구독한다
     * @param categoryLevelOne : 구독할 대상의 종류
     * @param id : 변경할 대상의 id
     * @return boolean : 대상 영화 구독 성공 여부
     */
    public Integer subscribe(CategoryLevelOne categoryLevelOne, String id)
    {
        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            user.getSubscribeList().get(categoryLevelOne).add(id);
            ssoUserRepository.save(user);

            return CategoryLevelOneUpdate(categoryLevelOne, id, user.getId(), true);
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return -1;
        }
    }

    /**
     * 구독 해지한다
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param id : 변경할 대상의 id
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    public Integer subscribeCancel(CategoryLevelOne categoryLevelOne, String id)
    {
        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.getSubscribeList().get(categoryLevelOne).remove(id);
            ssoUserRepository.save(user);

            return CategoryLevelOneUpdate(categoryLevelOne, id, user.getId(), false);
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return -1;
        }
    }

    /**
     * 구독 확인한다.
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param id : 변경할 대상의 id
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    public Boolean subscribeCheck(CategoryLevelOne categoryLevelOne, String id)
    {
        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getSubscribeList().get(categoryLevelOne).contains(id);
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * 유저의 선호 카테고리를 변경한다
     * @param categoryName : 변경할 대상의 category
     * @return boolean : 대상 카테고리 변경 성공 여부
     */
    public Boolean changeUserCategory(String categoryName)
    {
        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.updateFavoriteLevelOne(CategoryLevelOne.valueOf(categoryName));
            ssoUserRepository.save(user);

            return true;
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * 유저의 알람 정보를 변경한다
     * @param categoryLevelOne : 변경할 대상
     * @param alarmValue : 변경할 대상의 알람 정보
     * @return boolean : 대상 알람 정보 변경 성공 여부
     */
    public Boolean changeUserAlarm(CategoryLevelOne categoryLevelOne, String alarmValue)
    {
        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            int idx = categoryLevelOne.equals(CategoryLevelOne.MOVIE) ? 0
                    : categoryLevelOne.equals(CategoryLevelOne.MUSICAL) ? 1
                    : categoryLevelOne.equals(CategoryLevelOne.OPERA) ? 2 : 3;

            AlarmTiming[] alarmTimings = user.getAlarmTimings();
            alarmTimings[idx] = AlarmTiming.valueOf(alarmValue);
            user.updateAlarmTimings(alarmTimings);

            ssoUserRepository.save(user);

            return true;
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * 유저의 nickName 변경한다.
     * @param nickName : 변경할 대상의 닉네임
     * @return boolean : 대상 닉네임 변경 성공 여부
     */
    public Boolean changeUserName(String nickName)
    {
        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.updateNickName(nickName);
            ssoUserRepository.save(user);

            return true;
        }

        catch (Exception e)
        {
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * MOVIE, OPERA 등의 카테고리 index 변경
     * @param target : 변경할 category
     * @param targetId : 변경할 category ID
     * @param userId : 변경할 user ID
     * @param flag : 구독 / 구독 취소
     * @return Integer : 변경 후 대상 구독 수
     */
    private Integer CategoryLevelOneUpdate (CategoryLevelOne target, String targetId, String userId, boolean flag)
    {

        switch (target)
        {
            case CategoryLevelOne.MOVIE ->
            {

                MOVIE_INDEX movieIndex = movieRepository.findById(targetId).orElseThrow();

                if (movieIndex.getFavorites() == null || movieIndex.getFavorites().isEmpty())
                {
                    movieIndex.setFavorites(new HashSet<>());
                }

                if (flag) movieIndex.getFavorites().add(userId);
                else movieIndex.getFavorites().remove(userId);

                movieRepository.save(movieIndex);

                return movieIndex.getFavorites().size();
            }

            // TODO 다른 것들
            case CategoryLevelOne.OPERA ->
            {

            }
        }

        return -1;
    }

    /**
     * 대상 카테고리의 id 목록을 받아 대상 Iterable<DTO> 형태로 반환
     * @param target : 변경할 category
     * @param list : 변경할 category ID
     * @return Set<?> : 생성된 DTO 목록
     */
    private Set<?> makeSet (CategoryLevelOne target, LinkedList<String> list)
    {
        switch (target)
        {

            case CategoryLevelOne.MOVIE ->
            {

                Set<MOVIE_DTO> set = new HashSet<>();
                for (String id : list)
                {
                    MOVIE_INDEX movieIndex = movieRepository.findById(id).orElseThrow();
                    set.add(convertDTOAndIndex.MovieIndexToDTO(movieIndex));
                }

                return set;
            }

            // TODO 다른 것들
            case CategoryLevelOne.MUSICAL ->
            {

                return null;
            }
        }

        return null;
    }
}
