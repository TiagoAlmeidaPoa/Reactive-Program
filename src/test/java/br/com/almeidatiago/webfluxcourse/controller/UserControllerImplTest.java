package br.com.almeidatiago.webfluxcourse.controller;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.mapper.UserMapper;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.model.response.UserResponse;
import br.com.almeidatiago.webfluxcourse.service.UserService;
import br.com.almeidatiago.webfluxcourse.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String NAME = "Tiago";
    public static final String ID = "1234";
    public static final String EMAIL = "tiago@gmail.com";
    public static final String PASSWORD = "123";
    public static final String BASE_URI = "/users";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;
    @MockBean
    private UserMapper mapper;

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final var request = new UserRequest(NAME, EMAIL, PASSWORD);
        when(service.save(any(UserRequest.class))).thenReturn(just(UserEntity.builder().build()));

        webTestClient.post().uri(BASE_URI)
            .contentType(APPLICATION_JSON)
            .body(fromValue(request))
            .exchange()
            .expectStatus().isCreated();

        verify(service, times(1)).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Test endpoint save with bad request")
    void testSaveWithBadRequest() {
        final var request = new UserRequest(NAME.concat(" "), EMAIL, PASSWORD);

        webTestClient.post().uri(BASE_URI)
            .contentType(APPLICATION_JSON)
            .body(fromValue(request))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.path").isEqualTo("/users")
            .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
            .jsonPath("$.error").isEqualTo("Validation Error")
            .jsonPath("$.message").isEqualTo("Error validating attributes")
            .jsonPath("$.errors[0].fieldName").isEqualTo("name")
            .jsonPath("$.errors[0].message").isEqualTo("the field cannot contain blank spaces at the beginning or end");
    }

    @Test
    @DisplayName("Test findById endpoint with success")
    void testFindByIdWithSuccess() {
        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findById(anyString())).thenReturn(just(UserEntity.builder().build()));
        when(mapper.toResponse(any(UserEntity.class))).thenReturn(userResponse);

        webTestClient.get().uri(BASE_URI + "/" + ID)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(ID)
            .jsonPath("$.name").isEqualTo(NAME)
            .jsonPath("$.email").isEqualTo(EMAIL)
            .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).findById(anyString());
        verify(mapper).toResponse(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test findById endpoint with success")
    void testFindByIdWithNotFound() {

        when(service.findById(anyString())).thenThrow(new ObjectNotFoundException(
            format("Object not found. Id: %s, Type: %s", ID, UserEntity.class.getSimpleName())
        ));

        webTestClient.get().uri(BASE_URI + "/" + ID)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Not Found")
            .jsonPath("$.message").isEqualTo(format("Object not found. Id: %s, Type: %s", ID, UserEntity.class.getSimpleName()))
            .jsonPath("$.status").isEqualTo(404);

        verify(service).findById(anyString());
    }

    @Test
    @DisplayName("Test findAll endpoint with success")
    void testFindAllWithSuccess() {
        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.findAll()).thenReturn(Flux.just(UserEntity.builder().build()));
        when(mapper.toResponse(any(UserEntity.class))).thenReturn(userResponse);

        webTestClient.get().uri(BASE_URI)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo(ID)
            .jsonPath("$.[0].name").isEqualTo(NAME)
            .jsonPath("$.[0].email").isEqualTo(EMAIL)
            .jsonPath("$.[0].password").isEqualTo(PASSWORD);

        verify(service, times(1)).findAll();
        verify(mapper).toResponse(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test update endpoint with success")
    void testUpdateWithSuccess() {
        final var request = new UserRequest(NAME, EMAIL, PASSWORD);
        final var userResponse = new UserResponse(ID, NAME, EMAIL, PASSWORD);

        when(service.update(anyString(), any(UserRequest.class))).thenReturn(just(UserEntity.builder().build()));
        when(mapper.toResponse(any(UserEntity.class))).thenReturn(userResponse);

        webTestClient.patch().uri(BASE_URI + "/" + ID)
            .contentType(APPLICATION_JSON)
            .body(fromValue(request))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(ID)
            .jsonPath("$.name").isEqualTo(NAME)
            .jsonPath("$.email").isEqualTo(EMAIL)
            .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service, times(1)).update(anyString(), any(UserRequest.class));
        verify(mapper).toResponse(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test delete endpoint with success")
    void testDeleteWithSuccess() {
        when(service.delete(anyString())).thenReturn(just(UserEntity.builder().build()));

        webTestClient.delete().uri(BASE_URI + "/" + ID)
            .exchange()
            .expectStatus().isOk();

        verify(service).delete(anyString());
    }

    @Test
    @DisplayName("Test delete endpoint with NotFound exception")
    void testDeleteWithNotFound() {
        when(service.delete(anyString())).thenThrow(new ObjectNotFoundException(
            format("Object not found. Id: %s, Type: %s", ID, UserEntity.class.getSimpleName())
        ));

        webTestClient.delete().uri(BASE_URI + "/" + ID)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").isEqualTo("Not Found")
            .jsonPath("$.message").isEqualTo(format("Object not found. Id: %s, Type: %s", ID, UserEntity.class.getSimpleName()))
            .jsonPath("$.status").isEqualTo(404);

        verify(service).delete(anyString());
    }
}