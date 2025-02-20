package org.knock.knock_back.controller.user;

import lombok.RequiredArgsConstructor;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.dto.user.SSO_USER_DTO;
import org.knock.knock_back.service.layerClass.UserService;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    /**
     * 토큰으로 부터 유저 정보 획득하여 반환한다
     * @return userDto
     */
    @GetMapping (value = "/getUserInfo")
    @ResponseBody
    public ResponseEntity<SSO_USER_DTO> getUserInfo()
    {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    /**
     * 전체 구독 목록을 가져온다.
     * @return set : 카테고리 별 구독 목록 id
     */
    @GetMapping (value = "/getSubscribeList")
    public ResponseEntity<Map<String, Iterable<?>>> getUserCategorySubscribeList()
    {
        return ResponseEntity.ok(userService.getUserCategorySubscribeList());
    }

    /**
     * 카테고리 별 구독 목록을 가져온다.
     * @param categoryLevelOne : 확인할 대상의 종류
     * @return set : 카테고리 별 구독 목록 id
     */
    @GetMapping (value = "/{category}/getSubscribeList")
    public ResponseEntity<Iterable<?>> getUserSubscribeList(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne)
    {
        return ResponseEntity.ok(userService.getUserSubscribeList(categoryLevelOne));
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
        return ResponseEntity.ok(userService.subscribe(categoryLevelOne, valueMap.get("value")));
    }

    /**
     * 구독 해지한다
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param valueMap : 변경할 대상의 id, "value : id" 형식
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    @PostMapping("{category}/cancelSub")
    public ResponseEntity<Integer> subscribeCancel (@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                    @RequestBody Map<String, String> valueMap)
    {
        return ResponseEntity.ok(userService.subscribeCancel(categoryLevelOne, valueMap.get("value")));
    }

    /**
     * 구독 확인한다.
     * @param categoryLevelOne : 구독 해지할 대상의 종류
     * @param valueMap : 변경할 대상의 id, "value : id" 형식
     * @return boolean : 대상 영화 구독 해지 성공 여부
     */
    @PostMapping("{category}/isSubscribe")
    public ResponseEntity<Boolean> subscribeCheck(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                  @RequestBody Map<String, String> valueMap)
    {
        return ResponseEntity.ok(userService.subscribeCheck(categoryLevelOne, valueMap.get("value")));
    }

    /**
     * 유저의 선호 카테고리를 변경한다
     * @param valueMap : 변경할 대상의 category, "value : id" 형식
     * @return boolean : 대상 카테고리 변경 성공 여부
     */
    @PostMapping (value = "/changeCategory")
    public ResponseEntity<Boolean> changeUserCategory(@RequestBody Map<String, String> valueMap)
    {
        return ResponseEntity.ok(userService.changeUserCategory(valueMap.get("value")));
    }

    /**
     * 유저의 알람 정보를 변경한다
     * @param categoryLevelOne : 변경할 대상
     * @param valueMap : 변경할 대상의 알람 정보, "value : id" 형식
     * @return boolean : 대상 알람 정보 변경 성공 여부
     */
    @PostMapping (value = "/{category}/changeAlarm")
    public ResponseEntity<Boolean> changeUserAlarm(@PathVariable(name = "category") CategoryLevelOne categoryLevelOne,
                                                   @RequestBody Map<String, String> valueMap)
    {
        return ResponseEntity.ok(userService.changeUserAlarm(categoryLevelOne, valueMap.get("value")));
    }

    /**
     * 유저의 nickName 변경한다.
     * @param valueMap : 변경할 대상의 닉네임, "value : id" 형식
     * @return boolean : 대상 닉네임 변경 성공 여부
     */
    @PostMapping (value = "/changeName")
    public ResponseEntity<Boolean> changeUserName(@RequestBody Map<String, String> valueMap)
    {
        return ResponseEntity.ok(userService.changeUserName(valueMap.get("value")));
    }
}
