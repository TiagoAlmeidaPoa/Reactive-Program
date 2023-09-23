package br.com.almeidatiago.webfluxcourse.repository;

import br.com.almeidatiago.webfluxcourse.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<UserEntity> save(final UserEntity user) {
        return mongoTemplate.save(user);
    }

    public Mono<UserEntity> findById(String id) {
        return mongoTemplate.findById(id, UserEntity.class);
    }
}
