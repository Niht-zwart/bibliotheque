package com.dellapasqua.bibliotheque.prompt.infra.rest;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateElementScriptRequest {
    private String id;
    private String categorie;
    private String nom;
    private String description;
    private String contenu;
    private List<Tag> tags;
    private LocalDate date;
    private List<String> parametres;
    private List<CreateElementScriptRequest> historiques;
}
