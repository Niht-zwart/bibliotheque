package com.dellapasqua.bibliotheque.prompt.infra.repository;

import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.CategorieEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategorieRepository extends MongoRepository<CategorieEntity, String> {
}
