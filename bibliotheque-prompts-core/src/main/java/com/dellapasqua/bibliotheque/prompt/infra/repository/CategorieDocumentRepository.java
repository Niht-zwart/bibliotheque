package com.dellapasqua.bibliotheque.prompt.infra.repository;

import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.DocumentCategorieEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategorieDocumentRepository extends MongoRepository<DocumentCategorieEntity, String> {
}
