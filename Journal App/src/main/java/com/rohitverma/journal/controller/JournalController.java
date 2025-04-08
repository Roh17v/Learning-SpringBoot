package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.service.JournalEntryService;
import com.rohitverma.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journals")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<?> getAllJournalsForUser(@PathVariable String username)
    {
        Optional<User> user = userService.findUserByUsername(username);
        if(!user.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<JournalEntry> entries = user.get().getJournalEntries();

        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry journal, @PathVariable String username)
    {
        try {
            Optional<User> user = userService.findUserByUsername(username);
            if(!user.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if(journalEntryService.saveEntry(journal, username))
            {
                return new ResponseEntity<>(journal, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable("id") ObjectId id)
    {
         Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(id);
         if(journalEntry.isPresent())
         {
             return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
         }

        return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable("id") ObjectId id, @RequestBody JournalEntry journal)
    {
        if(journalEntryService.updateJournalEntry(id, journal)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{username}/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable("id") ObjectId id, @PathVariable("username") String username)
    {
        JournalEntry journal = journalEntryService.getEntryById(id).orElse(null);
        Optional<User> user = userService.findUserByUsername(username);
        if(journal != null && user.isPresent() && user.get().getJournalEntries().contains(journal)) {
            user.get().getJournalEntries().remove(journal);
            journalEntryService.deleteEntryById(journal.getId());
            userService.saveUser(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
