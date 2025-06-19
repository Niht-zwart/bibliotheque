package com.dellapasqua.bibliotheque.prompt.infra.mapper;

import com.dellapasqua.bibliotheque.prompt.domain.bibliotheque.model.ElementDeScript;
import com.dellapasqua.bibliotheque.prompt.infra.rest.CreateElementScriptRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TagMapper.class})
public interface ControllerMapper {

    ControllerMapper INSTANCE = Mappers.getMapper(ControllerMapper.class);

    ElementDeScript toDomain(CreateElementScriptRequest request);
}
