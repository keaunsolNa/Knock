package org.zerock.knock.dto.dto.category;

import lombok.Data;
import org.zerock.knock.dto.dto.user.USER_DTO;

@Data
public class CATEGORY_LEVEL_TWO_DTO {

    private String id;
    private String nm;
    private String parentNm;
    private Iterable<USER_DTO> favoriteUsers;

}
