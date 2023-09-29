package br.com.almeidatiago.webfluxcourse.service;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.mapper.UserMapper;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void save() {
        UserRequest request = new UserRequest("Tiago Almeida","tiago@gmail.com","123");
        UserEntity entity = UserEntity.builder().build();

        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(entity));
        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);

        Mono<UserEntity> result = service.save(request);

        StepVerifier.create(result)
            .expectNextMatches(Objects::nonNull)
            .expectComplete()
            .verify();

        Mockito.verify(repository, times(1)).save(any(UserEntity.class));

    }
}