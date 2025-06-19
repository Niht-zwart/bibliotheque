package com.dellapasqua.bibliotheque.prompt.infra.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("document-categorie")
@AllArgsConstructor
public class DocumentCategorieEntity {

    @Id
    private String nom;
}
