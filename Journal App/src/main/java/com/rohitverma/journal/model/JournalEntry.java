package com.rohitverma.journal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Date;

@Document(collection = "JournalEntries")
@Getter
@Setter
public class JournalEntry {

    @Id
    private ObjectId id;

    private String title;

    private String content;

    private Date date = new Date();
}
