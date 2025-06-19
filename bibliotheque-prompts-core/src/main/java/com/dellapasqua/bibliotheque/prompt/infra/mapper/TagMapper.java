package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Tag;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.TagEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    List<TagEntity> toEntities(List<Tag> tags);

    default TagEntity toEntity(Tag tag) {
        TagEntity entity = new TagEntity();
        entity.setNom(StringUtils.upperCase(tag.nom()));
        entity.setCategorie(tag.categorie());
        return entity;
    }

    default Tag toDomain(TagEntity tag) {
        return new Tag(StringUtils.capitalize(tag.getNom().toLowerCase()), tag.getCategorie());
    }

    default List<String> toString(List<Tag> tags) {
        return tags.stream().map(Tag::nom).map(StringUtils::capitalize).toList();
    }

    default String toString(Tag tag) {
        return tag.nom();
    }
}
