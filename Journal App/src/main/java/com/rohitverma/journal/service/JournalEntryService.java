package com.rohitverma.journal.service;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public boolean saveEntry(JournalEntry entry, String username) {
        Optional<User> user = userService.findUserByUsername(username);
        if(!user.isPresent()) return false;
        journalEntryRepository.save(entry);
        user.get().getJournalEntries().add(entry);
        userService.saveUser(user.get());
        return true;
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public boolean updateJournalEntry(ObjectId id, JournalEntry journal) {
        JournalEntry oldEntry = getEntryById(id).orElse(null);
        if(oldEntry != null) {
            oldEntry.setTitle(journal.getTitle() != null && !journal.getTitle().isEmpty() ? journal.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(journal.getContent() != null && !journal.getContent().isEmpty() ? journal.getContent() : oldEntry.getContent());
            saveEntry(oldEntry);
            return true;
        }
        return false;
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
