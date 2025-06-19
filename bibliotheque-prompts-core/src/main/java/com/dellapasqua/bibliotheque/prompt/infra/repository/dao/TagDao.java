package com.dellapasqua.bibliotheque.prompt.infra.repository.dao;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Tag;
import com.dellapasqua.bibliotheque.prompt.infra.mapper.TagMapper;
import com.dellapasqua.bibliotheque.prompt.infra.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagDao {

    private final TagRepository tagRepository;


    public Boolean existsByNomAndCategorie(Tag tag) {
        return tagRepository.existsByNomAndCategorie(StringUtils.upperCase(tag.nom()), tag.categorie());
    }

    public void save(Tag tag) {
        tagRepository.save(TagMapper.INSTANCE.toEntity(tag));
    }

    public List<Tag> findTagsByNomLikeAndCategorie(String nom, String categorie) {
        return tagRepository.findByNomLikeAndCategorie(StringUtils.upperCase(nom), categorie).stream().map(TagMapper.INSTANCE::toDomain).toList();
    }

    public List<Tag> findAllByCategorie(String categorie) {
        return tagRepository.findAllByCategorie(categorie)
                            .stream()
                            .map(TagMapper.INSTANCE::toDomain)
                            .toList();
    }
}
