package com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class Categorie {

    private String nom;
    private boolean versionning;
    private boolean parametres;
    private Map<String, String> metadatas;
}
