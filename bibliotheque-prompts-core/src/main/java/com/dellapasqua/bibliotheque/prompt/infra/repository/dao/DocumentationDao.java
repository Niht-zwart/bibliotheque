package com.dellapasqua.bibliotheque.prompt.infra.repository.dao;

import com.dellapasqua.bibliotheque.prompt.domain.documentation.Document;
import com.dellapasqua.bibliotheque.prompt.infra.mapper.DocumentMapper;
import com.dellapasqua.bibliotheque.prompt.infra.repository.CategorieDocumentRepository;
import com.dellapasqua.bibliotheque.prompt.infra.repository.DocumentRepository;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.DocumentCategorieEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentationDao {

    private final DocumentRepository documentRepository;
    private final CategorieDocumentRepository categorieDocumentRepository;

    public List<String> findAllCategories() {
        return categorieDocumentRepository.findAll().stream().map(DocumentCategorieEntity::getNom).toList();
    }

    public void enregistrerCategorie(String categorie) {
        categorieDocumentRepository.save(new DocumentCategorieEntity(categorie));
    }

    public List<Document> findAllDocumentByCategorie(String categorie) {
        return documentRepository.findAllByCategorie(categorie).stream().map(DocumentMapper.INSTANCE::toDomain).toList();
    }

    public void enregistrerDocument(Document document) {
        documentRepository.save(DocumentMapper.INSTANCE.toEntity(document));
    }
}
