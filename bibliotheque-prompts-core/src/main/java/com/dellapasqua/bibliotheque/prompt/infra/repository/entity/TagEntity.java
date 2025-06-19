package com.dellapasqua.bibliotheque.prompt.infra.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("tags")
public class TagEntity {

    private String nom;
    private String categorie;
}
