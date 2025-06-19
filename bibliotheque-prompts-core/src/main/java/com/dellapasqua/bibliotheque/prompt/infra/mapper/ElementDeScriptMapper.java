package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.ElementDeScript;
import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Tag;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.ElementDeScriptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CategorieMapper.class, TagMapper.class})
public interface ElementDeScriptMapper {

    ElementDeScriptMapper INSTANCE = Mappers.getMapper(ElementDeScriptMapper.class);

    @Mapping(target = "tags", source = ".", qualifiedByName = "toTags")
    ElementDeScript toDomain(ElementDeScriptEntity elementDeScriptEntity);

    ElementDeScriptEntity toEntity(ElementDeScript elementDeScript);

    @Named("toTags")
    default List<Tag> toTags(ElementDeScriptEntity entity) {
        return entity.getTags().stream()
                     .map(x -> new Tag(x, entity.getCategorie()))
                     .toList();
    }
}
