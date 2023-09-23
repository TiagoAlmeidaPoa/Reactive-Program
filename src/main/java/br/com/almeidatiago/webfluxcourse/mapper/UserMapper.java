package br.com.almeidatiago.webfluxcourse.mapper;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = IGNORE,
    nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(final UserRequest request);

    UserResponse toResponse(final UserEntity entity);
}
