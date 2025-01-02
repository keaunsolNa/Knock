package org.zerock.knock;

import org.zerock.knock.service.crawling.movie.MegaBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zerock.knock.service.crawling.musical.InterPark;


@EnableScheduling
@EnableAsync
@SpringBootApplication
public class KnockApplication {

    private static final Logger logger = LoggerFactory.getLogger(KnockApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(KnockApplication.class, args);
        MegaBox megaBox = new MegaBox();
        megaBox.addNewBrands();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {

            logger.info(">>>> SpringBoot Initialize <<<< ");
/*
            String[] beanNames = ctx.getBeanDefinitionNames();
            for (String beanName : beanNames)
            {
                logger.info("BEAN NAME : " + beanName);
            }*/
        };
    }
}
