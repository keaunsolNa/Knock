package org.zerock.knock.component.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.repository.Category.CategoryLevelOneRepository;
import org.zerock.knock.repository.Category.CategoryLevelTwoRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CategoryInitializer.class);
    private final CategoryLevelOneRepository categoryLevelOneRepository;
    private final CategoryLevelTwoRepository categoryLevelTwoRepository;


    public CategoryInitializer(CategoryLevelOneRepository categoryLevelOneRepository, CategoryLevelTwoRepository categoryLevelTwoRepository) {
        this.categoryLevelOneRepository = categoryLevelOneRepository;
        this.categoryLevelTwoRepository = categoryLevelTwoRepository;
    }

    public void initializeCategoryLevelTwo() {

        logger.info("CATEGORY_TWO_START");
        Set<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoIndices = new HashSet<>();
        String[] movieValue = new String[] { "휴먼", "드라마", "로맨스", "코미디", "액션", "호러", "스릴러", "다큐멘터리", "SF", "슬랩스틱", "느와르",
                                             "전쟁", "애니메이션", "미스터리", "애로", "추리", "뮤지컬", "오페라", "하이틴", "범죄", "시대물", "기타" };

        String[] operaValue = new String[] { "간주곡", "그랜드 오페라", "베리스모", "사보이 오페라", "서정 비극", "오페라 부파", "오페라 세리아", "오페라 코미크", "징슈필" };

        String[] levelOneValue = new String[]{ "Movie", "Opera" };

        String[][] values = new String[2][];

        values[0] = movieValue;
        values[1] = operaValue;

        categoryLevelOneRepository.deleteAll();
        categoryLevelTwoRepository.deleteAll();

        for (int i = 0; i < 2; i++)
        {

            CATEGORY_LEVEL_ONE_INDEX categoryLevelOneIndex = new CATEGORY_LEVEL_ONE_INDEX();
            categoryLevelOneIndex.setNm(levelOneValue[i]);

            for (String key : values[i])
            {
                CATEGORY_LEVEL_TWO_INDEX categoryLevelTwoIndex = new CATEGORY_LEVEL_TWO_INDEX();
                categoryLevelTwoIndex.setNm(key);
                categoryLevelTwoIndex.setParentNm(levelOneValue[i]);
                categoryLevelTwoIndex.setFavoriteUsers(null);

                categoryLevelTwoIndices.add(categoryLevelTwoIndex);
            }

            categoryLevelOneRepository.save(categoryLevelOneIndex);
        }

        categoryLevelTwoRepository.saveAll(categoryLevelTwoIndices);
        logger.info("CATEGORY_TWO_END");
    }

    public void initializeCategoryLevelOne () {

        Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoIndices = categoryLevelTwoRepository.findAll();
        String[] nameValue = new String[]{"Movie", "Opera"};

        logger.info("CATEGORY_ONE_START");
        for (String key : nameValue)
        {
            Set<CATEGORY_LEVEL_TWO_INDEX> set = new HashSet<>();
            CATEGORY_LEVEL_ONE_INDEX parent = categoryLevelOneRepository.findByNm(key);

            logger.info("PARENT " + parent.getNm() + " : ");

            for (CATEGORY_LEVEL_TWO_INDEX cate : categoryLevelTwoIndices)
            {
                if (cate.getParentNm().equals(key))
                {
                    logger.info("CHILD : " + cate.getNm());
                    set.add(cate);
                }
            }

            CATEGORY_LEVEL_ONE_INDEX categoryLevelOneIndex = categoryLevelOneRepository.findByNm(key);

            categoryLevelOneIndex.setChildCategory(set);
            categoryLevelOneRepository.save(categoryLevelOneIndex);

            logger.info("END LOOP");
        }
    }
}
