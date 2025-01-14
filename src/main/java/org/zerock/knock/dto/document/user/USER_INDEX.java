package org.zerock.knock.dto.document.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@ToString
@Document(indexName = "user-index")
public class USER_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;


    private String userNm;
}
