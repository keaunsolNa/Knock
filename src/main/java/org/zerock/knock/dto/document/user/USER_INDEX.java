package org.zerock.knock.dto.document.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@ToString
@Document(indexName = "user-index")
public class USER_INDEX {
}
