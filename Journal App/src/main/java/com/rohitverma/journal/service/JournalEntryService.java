package com.rohitverma.journal.service;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }


    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }

}
