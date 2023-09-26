package br.com.almeidatiago.webfluxcourse.service;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.mapper.UserMapper;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.model.response.UserResponse;
import br.com.almeidatiago.webfluxcourse.repository.UserRepository;
import br.com.almeidatiago.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<UserEntity> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    public Mono<UserEntity> findById(final String id) {
        return repository.findById(id)
            .switchIfEmpty(Mono.error(
                new ObjectNotFoundException(
                    format("Object not found. Id: %s, Type: %s", id, UserEntity.class.getSimpleName())
                )
            ));
    }

    public Flux<UserEntity> findAll() {
        return repository.findAll();
    }

    public Mono<UserEntity> update(final String id, final UserRequest request){
        return findById(id)
            .map(entity -> mapper.toEntity(request, entity))
            .flatMap(repository::save);
    }

}
