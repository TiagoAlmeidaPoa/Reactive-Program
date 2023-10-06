package br.com.almeidatiago.webfluxcourse.controller;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import br.com.almeidatiago.webfluxcourse.mapper.UserMapper;
import br.com.almeidatiago.webfluxcourse.model.request.UserRequest;
import br.com.almeidatiago.webfluxcourse.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithSuccess() {
        final var request = new UserRequest("Tiago", "tiago@gmail.com", "123");
        when(service.save(any(UserRequest.class))).thenReturn(just(UserEntity.builder().build()));

        webTestClient.post().uri("/users")
            .contentType(APPLICATION_JSON)
            .body(fromValue(request))
            .exchange()
            .expectStatus().isCreated();

        verify(service, times(1)).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Test endpoint save with success")
    void testSaveWithBadRequest() {
        final var request = new UserRequest(" Tiago", "tiago@gmail.com", "123");

        webTestClient.post().uri("/users")
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
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}