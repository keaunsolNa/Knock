package org.knock.knock_back.dto.dto.category;

import lombok.Data;
import org.knock.knock_back.dto.dto.user.USER_DTO;

@Data
public class CATEGORY_LEVEL_ONE_DTO {

    private String id;
    private String nm;
    private Iterable<CATEGORY_LEVEL_TWO_DTO> childCategory;
    private Iterable<USER_DTO> favoriteUsers;
}
