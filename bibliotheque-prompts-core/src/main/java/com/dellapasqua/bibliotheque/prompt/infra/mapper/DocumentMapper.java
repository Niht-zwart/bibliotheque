package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import com.dellapasqua.bibliotheque.prompt.domain.documentation.Document;
import com.dellapasqua.bibliotheque.prompt.infra.repository.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    Document toDomain(DocumentEntity entity);

    DocumentEntity toEntity(Document document);
}
