package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BibliothequeMapper {

    BibliothequeMapper INSTANCE = Mappers.getMapper(BibliothequeMapper.class);

}
