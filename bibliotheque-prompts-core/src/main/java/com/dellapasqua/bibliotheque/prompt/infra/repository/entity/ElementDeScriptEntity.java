package com.dellapasqua.bibliotheque.prompt.infra.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document("bibliotheque")
public class ElementDeScriptEntity {

    @Id
    private String id;
    private String categorie;
    private String nom;
    private String description;
    private String contenu;
    private List<String> parametres;
    private List<String> tags;
    private LocalDate date;
    private List<ElementDeScriptEntity> historiques;
}
