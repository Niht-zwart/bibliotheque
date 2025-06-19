package com.dellapasqua.bibliotheque.prompt.infra.repository.dao;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.*;
import com.dellapasqua.bibliotheque.prompt.infra.mapper.ElementDeScriptMapper;
import com.dellapasqua.bibliotheque.prompt.infra.repository.ElementDeScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElementDeScriptDao {

    private final ElementDeScriptRepository elementDeScriptRepository;

    public List<BibliothequeTag> countByTagAndCategorie(Categorie categorie) {
        return elementDeScriptRepository.countPromptsByTag(categorie.getNom());
    }

    public List<ElementDeScript> findByTagsContainingAndCategorie(Tag tag) {
        return elementDeScriptRepository.findByTagsContainingAndCategorie(tag.nom(), tag.categorie())
                                        .stream()
                                        .map(ElementDeScriptMapper.INSTANCE::toDomain)
                                        .toList();
    }

    public ElementDeScript findById(String id) {
        return elementDeScriptRepository.findById(id)
                                        .map(ElementDeScriptMapper.INSTANCE::toDomain)
                                        .orElse(null);
    }

    public void enregistrer(ElementDeScript elementDeScript) {
        elementDeScriptRepository.save(ElementDeScriptMapper.INSTANCE.toEntity(elementDeScript));
    }
}
