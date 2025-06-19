package com.dellapasqua.bibliotheque.prompt.domain.documentation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Document {

    private String titre;
    private String description;
    private String url;
    private String categorie;

}
