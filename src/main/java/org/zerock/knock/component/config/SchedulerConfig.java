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

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KOFIC.class);
    private final KOFIC kofic;

    @Value("${schedule.kofic.use}")
    private boolean useScheduleKOFIC;

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
