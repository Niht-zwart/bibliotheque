package com.dellapasqua.bibliotheque.prompt.infra.repository.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("document")
public class DocumentEntity {

    private String titre;
    private String description;
    private String url;
    private String categorie;
}
