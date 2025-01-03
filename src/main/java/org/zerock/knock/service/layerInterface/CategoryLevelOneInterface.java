package org.zerock.knock.service.layerInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

@Async
public interface CategoryLevelOneInterface {

    Logger logger = LoggerFactory.getLogger(CategoryLevelOneInterface.class);

    void create();
    void read();
    void update();
    void delete();

    class CategoryOneMaker implements Runnable {

        private final CATEGORY_LEVEL_ONE_INDEX categoryLevelOneIndex;

        public CategoryOneMaker(CATEGORY_LEVEL_ONE_INDEX categoryLevelOneIndex) {
            this.categoryLevelOneIndex = categoryLevelOneIndex;
        }


        @Override
        public void run() {

        }

    }

    class CategoryTwoMaker implements Runnable {

        private final CATEGORY_LEVEL_TWO_INDEX categoryLevelTwoIndex;

        public CategoryTwoMaker(CATEGORY_LEVEL_TWO_INDEX categoryLevelTwoIndex) {
            this.categoryLevelTwoIndex = categoryLevelTwoIndex;
        }


        @Override
        public void run() {

        }

    }
}
