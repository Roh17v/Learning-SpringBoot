package com.rohitverma.journal.repository;

import com.rohitverma.journal.model.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);

    String username(@NonNull String username);
}
