package com.dellapasqua.bibliotheque.prompt.domain.documentation;

import com.dellapasqua.bibliotheque.prompt.infra.repository.dao.DocumentationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentationService {

    private final DocumentationDao documentationDao;

    public List<String> findAllCategories() {
        return documentationDao.findAllCategories();
    }

    public void enregistrerCategorie(String categorie) {
        documentationDao.enregistrerCategorie(categorie);
    }

    public List<Document> findAllDocumentByCategorie(String categorie) {
        return documentationDao.findAllDocumentByCategorie(categorie);
    }

    public void enregistrerDocument(Document document) {
        documentationDao.enregistrerDocument(document);
    }
}
