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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    public static final String ID = "123";
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
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

    @Test
    void testFindById() {

        when(repository.findById(anyString())).thenReturn(Mono.just(UserEntity.builder().build()));

        Mono<UserEntity> result = service.findById(ID);

        StepVerifier.create(result)
            .expectNextMatches(user -> user.getClass() == UserEntity.class)
            .expectComplete()
            .verify();

        Mockito.verify(repository, times(1)).findById(anyString());

    }

    @Test
    void testFindAll() {

        when(repository.findAll()).thenReturn(Flux.just(UserEntity.builder().build()));

        Flux<UserEntity> result = service.findAll();

        StepVerifier.create(result)
            .expectNextMatches(user -> user.getClass() == UserEntity.class)
            .expectComplete()
            .verify();

        Mockito.verify(repository, times(1)).findAll();

    }

    @Test
    void testUpdate() {
        UserRequest request = new UserRequest("Tiago Almeida","tiago@gmail.com","123");
        UserEntity entity = UserEntity.builder().build();

        when(mapper.toEntity(any(UserRequest.class),any(UserEntity.class))).thenReturn(entity);
        when(repository.findById(anyString())).thenReturn(Mono.just(entity));
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(entity));

        Mono<UserEntity> result = service.update(ID, request);

        StepVerifier.create(result)
            .expectNextMatches(Objects::nonNull)
            .expectComplete()
            .verify();

        Mockito.verify(repository, times(1)).save(any(UserEntity.class));

    }

    @Test
    void testDelete() {
        UserEntity entity = UserEntity.builder().build();
        when(repository.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<UserEntity> result = service.delete(ID);

        StepVerifier.create(result)
            .expectNextMatches(Objects::nonNull)
            .expectComplete()
            .verify();

        Mockito.verify(repository, times(1)).findAndRemove(anyString());
    }
}