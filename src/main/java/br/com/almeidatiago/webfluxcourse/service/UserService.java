package br.com.almeidatiago.webfluxcourse.service;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.mapper.UserMapper;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.model.response.UserResponse;
import br.com.almeidatiago.webfluxcourse.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<UserEntity> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

}
