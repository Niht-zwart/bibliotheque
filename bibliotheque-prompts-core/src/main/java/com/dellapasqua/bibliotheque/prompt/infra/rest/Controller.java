package com.dellapasqua.bibliotheque.prompt.infra.rest;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.BibliothequeService;
import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.BibliothequeTag;
import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Categorie;
import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.ElementDeScript;
import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Tag;
import com.dellapasqua.bibliotheque.prompt.infra.mapper.ControllerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bibliotheques")
@RequiredArgsConstructor
public class Controller {

    private final BibliothequeService bibliothequeService;

    @GetMapping
    public Map<String, List<BibliothequeTag>> getListTags() {
        return bibliothequeService.getBibliotheque();
    }

    @GetMapping("/categories")
    public Categorie findCategorieByNom(@RequestParam String nom) {
        return bibliothequeService.findCategorieByNom(nom);
    }

    @GetMapping("/{categorie}/tag/{tag}")
    public List<ElementDeScript> findAllByTag(@PathVariable String categorie,
                                              @PathVariable String tag) {
        return bibliothequeService.findByTagAndType(new Tag(tag, categorie));
    }

    @GetMapping("/{id}")
    public ElementDeScript findById(@PathVariable String id) {
        return bibliothequeService.findById(id);
    }

    @PostMapping
    public void create(@RequestBody CreateElementScriptRequest createRequest) {
        bibliothequeService.enregistrer(ControllerMapper.INSTANCE.toDomain(createRequest));
    }

    @GetMapping("/{categorie}/tags")
    public List<Tag> findTags(@PathVariable String categorie,
                              @RequestParam String nom) {
        return bibliothequeService.findTagsByNomLikeAndCategorie(nom, categorie);
    }
}
