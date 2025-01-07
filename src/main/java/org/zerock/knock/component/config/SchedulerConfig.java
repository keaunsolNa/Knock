package org.zerock.knock.component.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.knock.service.crawling.movie.KOFIC;

/**
 * @author nks
 * @apiNote Scheduler 로 제어되는 설정들
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KOFIC.class);
    private final KOFIC kofic;

    @Value("${schedule.kofic.use}")
    private boolean useScheduleKOFIC;

    /**
     * 주기적으로 KOFIC 에서 영화 정보를 받아온다.
     * @apiNote cronTab = 1시간에 1번, 정시
     */
    @Async
    @Scheduled(cron = "${schedule.kofic.cron}")
    public void koficJob() {

        try
        {
            if (useScheduleKOFIC)
            {
                kofic.requestAPI();
            }
        }
        catch (Exception e)
        {
            logger.debug("[{}] 예기치 않은 종료 : ", e.getMessage());
        }
    }
}
