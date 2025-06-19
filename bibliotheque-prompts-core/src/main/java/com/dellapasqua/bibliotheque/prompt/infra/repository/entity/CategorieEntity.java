package com.dellapasqua.bibliotheque.prompt.infra.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document("categorie")
public class CategorieEntity {

    @Id
    private String nom;
    private boolean versionning;
    private boolean parametres;
    private Map<String, String> metadatas;
}
