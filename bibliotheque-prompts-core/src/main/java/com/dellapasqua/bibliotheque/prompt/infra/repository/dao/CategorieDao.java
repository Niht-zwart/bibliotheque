package com.dellapasqua.bibliotheque.prompt.infra.repository.dao;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Categorie;
import com.dellapasqua.bibliotheque.prompt.infra.mapper.CategorieMapper;
import com.dellapasqua.bibliotheque.prompt.infra.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieDao {

    private final CategorieRepository categorieRepository;

    public List<Categorie> findAllCategorie() {
        return categorieRepository.findAll().stream().map(CategorieMapper.INSTANCE::toDomain).toList();
    }

    public Categorie findById(String categorie) {
        return categorieRepository.findById(categorie).map(CategorieMapper.INSTANCE::toDomain).orElse(null);
    }
}
