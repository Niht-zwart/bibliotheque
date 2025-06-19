package com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class ElementDeScript {

    private String id;
    private String nom;
    private String description;
    private String contenu;
    private List<Tag> tags;
    @Setter
    private LocalDate date;
    private List<String> parametres;
    @With
    private List<ElementDeScript> historiques;
    private String categorie;
}
