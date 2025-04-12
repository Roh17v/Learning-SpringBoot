package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.model.User;
import com.rohitverma.journal.service.JournalEntryService;
import com.rohitverma.journal.service.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journals")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalsForUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        List<JournalEntry> entries = user.getJournalEntries();

        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry journal)
    {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUsername(auth.getName());
            if(journalEntryService.saveEntry(journal, user.getUsername()))
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
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByUsername(authentication.getName());

         boolean belongsToUser = user.getJournalEntries().stream().anyMatch(journalEntry -> journalEntry.getId().equals(id));

         if(belongsToUser) {
             JournalEntry journal = journalEntryService.getEntryById(id).orElse(null);
            return new ResponseEntity<>(journal, HttpStatus.OK);
         }

         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable("id") ObjectId id, @RequestBody JournalEntry journal)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());

        boolean belongsToUser = user.getJournalEntries().stream().anyMatch(journalEntry -> journalEntry.getId().equals(id));
        if(belongsToUser) {
            if(journalEntryService.updateJournalEntry(id, journal)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("There was some error updating the journal!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable("id") ObjectId id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        JournalEntry journal = journalEntryService.getEntryById(id).orElse(null);
        if(journal != null && user.getJournalEntries().contains(journal)) {
            user.getJournalEntries().remove(journal);
            journalEntryService.deleteEntryById(journal.getId());
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
