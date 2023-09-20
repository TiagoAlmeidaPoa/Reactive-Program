package br.com.almeidatiago.webfluxcourse.mapper;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.NullValueCheckStrategy.*;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = IGNORE,
    nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(final UserRequest request);
}
