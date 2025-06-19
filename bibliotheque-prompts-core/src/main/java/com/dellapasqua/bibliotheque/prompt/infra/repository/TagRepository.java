package com.dellapasqua.bibliotheque.prompt.infra.repository;

import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TagRepository extends MongoRepository<TagEntity, String> {

    List<TagEntity> findAllByCategorie(String type);

    Boolean existsByNomAndCategorie(String nom, String categorie);

    List<TagEntity> findByNomLikeAndCategorie(String nom, String categorie);
}
