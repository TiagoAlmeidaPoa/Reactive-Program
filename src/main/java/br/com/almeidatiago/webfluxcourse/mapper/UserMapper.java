package br.com.almeidatiago.webfluxcourse.mapper;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring"
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(final UserRequest request);
}
