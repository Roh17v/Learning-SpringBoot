package com.rohitverma.journal.service;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean saveEntry(JournalEntry entry, String username) {
        User user = userService.findUserByUsername(username);
        journalEntryRepository.save(entry);
        user.getJournalEntries().add(entry);
        userService.saveUser(user);
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

    @Transactional
    public boolean deleteJournalEntry(ObjectId id, String username) {
        User user = userService.findUserByUsername(username);
        JournalEntry entry = getEntryById(id).orElse(null);
        if(entry != null && user.getJournalEntries().contains(entry)) {
            deleteEntryById(id);
            user.getJournalEntries().remove(entry);
            userService.saveUser(user);
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
