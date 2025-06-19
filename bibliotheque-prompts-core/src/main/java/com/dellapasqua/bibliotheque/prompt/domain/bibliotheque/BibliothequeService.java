package com.dellapasqua.bibliotheque.prompt.domain.bibliotheque;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.*;
import com.dellapasqua.bibliotheque.prompt.infra.repository.dao.CategorieDao;
import com.dellapasqua.bibliotheque.prompt.infra.repository.dao.ElementDeScriptDao;
import com.dellapasqua.bibliotheque.prompt.infra.repository.dao.TagDao;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BibliothequeService {

    private final TagDao tagDao;
    private final ElementDeScriptDao elementDeScriptDao;
    private final CategorieDao categorieDao;

    public Map<String, List<BibliothequeTag>> getBibliotheque() {
        List<Categorie> categories = categorieDao.findAllCategorie();
        Map<String, List<BibliothequeTag>> bibliotheques = new HashMap<>();
        for (Categorie categorie : categories) {
            List<BibliothequeTag> bibliothequeTags = elementDeScriptDao.countByTagAndCategorie(categorie)
                                                                       .stream()
                                                                       .sorted(Comparator.comparing(BibliothequeTag::getNombres))
                                                                       .toList();
            bibliotheques.put(categorie.getNom(), bibliothequeTags);
        }
        return bibliotheques;
    }

    public Categorie findCategorieByNom(String nomCategorie) {
        return categorieDao.findById(nomCategorie);
    }

    public List<ElementDeScript> findByTagAndType(Tag tag) {
        return elementDeScriptDao.findByTagsContainingAndCategorie(tag);
    }

    public ElementDeScript findById(String id) {
        return elementDeScriptDao.findById(id);
    }

    public void enregistrer(ElementDeScript elementDeScript) {
        Categorie categorie = categorieDao.findById(elementDeScript.getCategorie());
        if (Objects.isNull(categorie)) {
            throw new RuntimeException("La catégorie n'existe pas !");
        }
        if (Objects.isNull(elementDeScript.getId())) {
            elementDeScript.setDate(LocalDate.now());
            elementDeScriptDao.enregistrer(elementDeScript);
        } else {
            ElementDeScript toSave = elementDeScript;
            if (categorie.isVersionning()) {
                ElementDeScript oldElementDeScript = elementDeScriptDao.findById(elementDeScript.getId());
                toSave = elementDeScript.withHistoriques(
                        Stream.of(oldElementDeScript.getHistoriques(), List.of(oldElementDeScript.withHistoriques(List.of())))
                              .flatMap(List::stream)
                              .toList()
                );
            }
            toSave.setDate(LocalDate.now());
            elementDeScriptDao.enregistrer(toSave);
        }
        saveTags(elementDeScript);
    }

    public List<Tag> findTagsByNomLikeAndCategorie(String nom, String categorie) {
        if (StringUtils.isBlank(nom)) {
            return tagDao.findAllByCategorie(categorie);
        }
        return tagDao.findTagsByNomLikeAndCategorie(nom, categorie);
    }

    private void saveTags(ElementDeScript elementDeScript) {
        for (Tag tag : elementDeScript.getTags()) {
            if (!Boolean.TRUE.equals(tagDao.existsByNomAndCategorie(tag))) {
                tagDao.save(tag);
            }
        }
    }
}
