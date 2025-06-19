package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.Categorie;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.CategorieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategorieMapper {

    CategorieMapper INSTANCE = Mappers.getMapper(CategorieMapper.class);

    Categorie toDomain(CategorieEntity entity);
}
