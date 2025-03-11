package org.knock.knock_back.controller.performingArts;

import org.knock.knock_back.dto.dto.performingArts.KOPIS_DTO;
import org.knock.knock_back.service.layerClass.PerformingArtsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author nks
 * @apiNote 공연예술 요청을 받는 Controller
 */
@RestController
@RequestMapping("/api/performingArts")
public class PerformingArtsController  {

    private final PerformingArtsService performingArtsService;


    public PerformingArtsController(PerformingArtsService performingArtsService) {
        this.performingArtsService = performingArtsService;
    }

    /**
     * 요청 시 KOPIS 인덱스에 저장된 모든 객체 정보를 가져와 반환한다. 
     * @return ResponseEntity<Iterable<KOPIS_DTO>> : 저장된 모든 공연예술 정보
     */
    @GetMapping()
    public ResponseEntity<Iterable<KOPIS_DTO>> getPerformingArts() {
        return ResponseEntity.ok(performingArtsService.readPerformingArts());
    }

    /**
     * 요청 시 KOPIS 인덱스에 저장된 대상 객체 정보를 가져와 반환한다.
     * @return ResponseEntity<Iterable<KOPIS_DTO>> : 저장된 모든 공연예술 정보
     */
    @GetMapping("/getDetail")
    public ResponseEntity<KOPIS_DTO> getDetail(@RequestParam String performingArtsId) {
        return ResponseEntity.ok(performingArtsService.readPerformingArtsDetail(performingArtsId));
    }

    /**
     * 요청 시 현재 상영 예정작 공연예술에 있는 모든 LEVEL_TWO CATEGORY 정보를 가져와 반환
     * @return ResponseEntity<Iterable<CATEGORY_LEVEL_TWO_DTO>> : 현재 상영 예정 리스트에 있는 공연예술들
     */
    @GetMapping("/getCategory")
    public ResponseEntity<Map<String, Object>> getCategory() {

        return ResponseEntity.ok(performingArtsService.getCategory());
    }

    /**
     * 요청 시 현재 공연예술을 구독한 다른 사람들이 공통으로 구독하고 있는 공연예술을 리스트로 만들어 반환한다.
     * @param performingArtsId : 확인하고 싶은 공연예술의 id
     * @retrun ResponseEntity<Iterable<KOPIS_INDEX>> : 대상 공연예술을 구독한 사람들이 공통적으로 구독한 공연예술들
     */
    @GetMapping("/recommend")
    public ResponseEntity<Iterable<KOPIS_DTO>> getRecommend(@RequestParam String performingArtsId) {

        return ResponseEntity.ok(performingArtsService.getRecommend(performingArtsId));
    }
}
