package com.dellapasqua.bibliotheque.prompt.infra.rest;

import com.dellapasqua.bibliotheque.prompt.domain.documentation.Document;
import com.dellapasqua.bibliotheque.prompt.domain.documentation.DocumentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentation")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentationService documentationService;

    @GetMapping("/categories")
    public List<String> findAllCategories() {
        return documentationService.findAllCategories();
    }

    @PostMapping("/categories")
    public void enregistrerCategorie(@RequestParam String categorie) {
        documentationService.enregistrerCategorie(categorie);
    }

    @GetMapping("/documents")
    public List<Document> findAllDocuments(@RequestParam String categorie) {
        return documentationService.findAllDocumentByCategorie(categorie);
    }

    @PostMapping("/documents")
    public void enregistrerDocument(@RequestBody Document document) {
        documentationService.enregistrerDocument(document);
    }
}
