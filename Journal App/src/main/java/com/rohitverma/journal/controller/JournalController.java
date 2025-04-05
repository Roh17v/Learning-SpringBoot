package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journals")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllJournals()
    {
        return journalEntryService.getAllEntries();
    }

    @PostMapping
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry journal)
    {
        try {
        journalEntryService.saveEntry(journal);
        return new ResponseEntity<>(journal, HttpStatus.CREATED);
        }
        catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable("id") ObjectId id)
    {
         Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(id);
         if(journalEntry.isPresent())
         {
             return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
         }

        return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public JournalEntry updateJournalById(@PathVariable("id") ObjectId id, @RequestBody JournalEntry journal)
    {
        JournalEntry oldEntry = journalEntryService.getEntryById(id).orElse(null);
        if(oldEntry != null) {
            oldEntry.setTitle(journal.getTitle() != null && !journal.getTitle().equals("") ? journal.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(journal.getContent() != null && !journal.getContent().equals("") ? journal.getContent() : oldEntry.getContent());
        }
        journalEntryService.saveEntry(oldEntry);
        return oldEntry;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable("id") ObjectId id)
    {
        journalEntryService.deleteEntryById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
