package org.zerock.knock.service.LayerClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.repository.Category.CategoryLevelOneRepository;
import org.zerock.knock.repository.Category.CategoryLevelTwoRepository;

@Service
public class CategoryInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CategoryInitializer.class);
    private final CategoryLevelOneRepository categoryLevelOneRepository;
    private final CategoryLevelTwoRepository categoryLevelTwoRepository;


    public CategoryInitializer(CategoryLevelOneRepository categoryLevelOneRepository, CategoryLevelTwoRepository categoryLevelTwoRepository) {
        this.categoryLevelOneRepository = categoryLevelOneRepository;
        this.categoryLevelTwoRepository = categoryLevelTwoRepository;
    }

    public Iterable<CATEGORY_LEVEL_ONE_INDEX> initializeCategoryLevelOne() {

        logger.info("{} START", getClass().getSimpleName());

        String[] nameValue = new String[]{"Movie", "Opera"};
        insertCategoryLevelOne(nameValue);

        logger.info("{} END", getClass().getSimpleName());

        return categoryLevelOneRepository.findAll();

    }
    public void insertCategoryLevelOne (String[] categoryLevelOneNames ) {

        logger.info("{} START", getClass().getSimpleName());
        for (String key : categoryLevelOneNames)
        {
            Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwoIndices = categoryLevelTwoRepository.findAllByParentNm(key);

            CATEGORY_LEVEL_ONE_INDEX categoryLevelOneIndex = categoryLevelOneRepository.findByNm(key).orElse(new CATEGORY_LEVEL_ONE_INDEX());
            categoryLevelOneIndex.setNm(key);
            categoryLevelOneIndex.setChildCategory(categoryLevelTwoIndices);
            categoryLevelOneRepository.save(categoryLevelOneIndex);

        }

        logger.info("{} END", getClass().getSimpleName());
    }
}
