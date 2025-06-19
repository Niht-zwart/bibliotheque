package com.dellapasqua.bibliotheque.prompt.infra.repository;

import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.DocumentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<DocumentEntity, ObjectId> {

    List<DocumentEntity> findAllByCategorie(String categorie);
}
