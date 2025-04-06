package com.rohitverma.journal.repository;

import com.rohitverma.journal.model.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);

    String username(@NonNull String username);
}
