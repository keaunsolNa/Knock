package org.zerock.knock.dto.dto.category;

import lombok.Data;
import org.zerock.knock.dto.dto.user.USER_DTO;

import java.util.Set;

@Data
public class CATEGORY_LEVEL_TWO_DTO {

    private String id;
    private String nm;
    private CATEGORY_LEVEL_ONE_DTO parentCategory;
    private Set<USER_DTO> favoriteUsers;

}
