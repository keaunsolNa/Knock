package org.knock.knock_back.controller.user;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author nks
 * @apiNote user 정보 변경에 활용되는 페이지
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ConvertDTOAndIndex convertDTOAndIndex;
    private final MovieRepository movieRepository;
    private final SSOUserRepository ssoUserRepository;

    /**
     * 토큰으로 부터 유저 정보 획득하여 반환한다
     * @return userDto
     */
    @GetMapping (value = "/getUserInfo")
    @ResponseBody
    public ResponseEntity<SSO_USER_DTO> getAccessToken() {

        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SSO_USER_DTO userDTO = convertDTOAndIndex.userIndexToUserDTO(user);

            return ResponseEntity.ok(userDTO);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(null);
        }

    }

    /**
     * 전체 구독 목록을 가져온다.
     * @return set : 카테고리 별 구독 목록 id
     */
    @GetMapping (value = "/getSubscribeList")
    public ResponseEntity<Map<String, Iterable<?>>> getUserSubscribeList() {

        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<CategoryLevelOne, LinkedList<String>> map = user.getSubscribeList();

            Map<String, Iterable<?>> userSubscribeList = new HashMap<>();

            for (CategoryLevelOne category : map.keySet())
            {
                LinkedList<String> list = map.get(category);
                Set<?> set =  makeSet(category, list);

                userSubscribeList.put(category.name(), set);
            }

            return ResponseEntity.ok(userSubscribeList);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(null);
        }

    }

    /**
     * 카테고리 별 구독 목록을 가져온다.
     * @param categoryLevelOne : 확인할 대상의 종류
     * @return set : 카테고리 별 구독 목록 id
     */
    @GetMapping (value = "/{category}/getSubscribeList")
    public ResponseEntity<Iterable<?>> getUserSubscribeList(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne) {

        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LinkedList<String> list = user.getSubscribeList().get(categoryLevelOne);
            Set<?> set = makeSet(categoryLevelOne, list);

            return ResponseEntity.ok(set);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(null);
        }

    }

    /**
     * 구독한다
     * @param categoryLevelOne : 구독할 대상의 종류
     * @param valueMap : 변경할 대상의 id, "value : id" 형식
     * @return boolean : 대상 영화 구독 성공 여부
     */
    @PostMapping("/{category}/sub")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Integer> subscribe(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                             @RequestBody Map<String, String> valueMap)
    {

        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            user.getSubscribeList().get(categoryLevelOne).add(valueMap.get("value"));
            ssoUserRepository.save(user);

            Integer count = CategoryLevelOneUpdate(categoryLevelOne, valueMap.get("value"), user.getId(), true);

            return ResponseEntity.ok(count);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(-1);
        }
    }

    /**
     * 구독 해지한다
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param valueMap : 변경할 대상의 id, "value : id" 형식
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    @PostMapping("{category}/cancelSub")
    public ResponseEntity<Integer> subscribeCancel (@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                    @RequestBody Map<String, String> valueMap) {

        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.getSubscribeList().get(categoryLevelOne).remove(valueMap.get("value"));
            ssoUserRepository.save(user);

            Integer count = CategoryLevelOneUpdate(categoryLevelOne, valueMap.get("value"), user.getId(), false);

            return ResponseEntity.ok(count);
        }

        catch (Exception e)
        {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(-1);
        }
    }

    /**
     * 구독 확인한다.
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param valueMap : 변경할 대상의 id, "value : id" 형식
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    @PostMapping("{category}/isSubscribe")
    public ResponseEntity<Boolean> subscribeCheck(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                  @RequestBody Map<String, String> valueMap) {

        try
        {
            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user.getSubscribeList().get(categoryLevelOne).contains(valueMap.get("value")))
            {
                return ResponseEntity.ok(true);
            }
            else
            {
                return ResponseEntity.ok(false);
            }
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(false);
        }

    }

    /**
     * 유저의 선호 카테고리를 변경한다
     * @param valueMap : 변경할 대상의 category, "value : id" 형식
     * @return boolean : 대상 카테고리 변경 성공 여부
     */
    @PostMapping (value = "/changeCategory")
    public ResponseEntity<Boolean> changeUserCategory(@RequestBody Map<String, String> valueMap) {

        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.updateFavoriteLevelOne(CategoryLevelOne.valueOf(valueMap.get("value")));
            ssoUserRepository.save(user);

            return ResponseEntity.ok(true);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * 유저의 알람 정보를 변경한다
     * @param categoryLevelOne : 변경할 대상
     * @param valueMap : 변경할 대상의 알람 정보, "value : id" 형식
     * @return boolean : 대상 알람 정보 변경 성공 여부
     */
    @PostMapping (value = "/{category}/changeAlarm")
    public ResponseEntity<Boolean> changeUserAlarm(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                   @RequestBody Map<String, String> valueMap) {

        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            int idx = categoryLevelOne.equals(CategoryLevelOne.MOVIE) ? 0
                    : categoryLevelOne.equals(CategoryLevelOne.MUSICAL) ? 1
                    : categoryLevelOne.equals(CategoryLevelOne.OPERA) ? 2 : 3;

            AlarmTiming[] alarmTimings = user.getAlarmTimings();
            alarmTimings[idx] = AlarmTiming.valueOf(valueMap.get("value"));
            user.updateAlarmTimings(alarmTimings);

            ssoUserRepository.save(user);

            return ResponseEntity.ok(true);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * 유저의 nickName 변경한다.
     * @param valueMap : 변경할 대상의 닉네임, "value : id" 형식
     * @return boolean : 대상 닉네임 변경 성공 여부
     */
    @PostMapping (value = "/changeName")
    public ResponseEntity<Boolean> changeUserName(@RequestBody Map<String, String> valueMap) {

        try
        {

            SSO_USER_INDEX user = (SSO_USER_INDEX) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.updateNickName(valueMap.get("value"));
            ssoUserRepository.save(user);

            return ResponseEntity.ok(true);
        }

        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(false);
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
