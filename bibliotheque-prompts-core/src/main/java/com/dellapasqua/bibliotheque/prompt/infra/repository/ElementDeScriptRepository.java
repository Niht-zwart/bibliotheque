package com.dellapasqua.bibliotheque.prompt.infra.repository;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.BibliothequeTag;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.ElementDeScriptEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ElementDeScriptRepository extends MongoRepository<ElementDeScriptEntity, String> {

    @Aggregation(pipeline = {
            "{ $match: { categorie: ?0 } }",
            "{ $unwind: '$tags' }",
            "{ $group: { _id: '$tags', nombres: { $sum: 1 } } }",
            "{ $project: { tag: '$_id', nombres: 1, _id: 0 } }"
    })
    List<BibliothequeTag> countPromptsByTag(String categorie);

    List<ElementDeScriptEntity> findByTagsContainingAndCategorie(String tag, String categorie);
}
