package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.JournalEntry;
import com.rohitverma.journal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public JournalEntry addJournal(@RequestBody JournalEntry journal)
    {
        journalEntryService.saveEntry(journal);
        return journal;
    }

    @GetMapping("/{id}")
    public JournalEntry getJournalById(@PathVariable("id") ObjectId id)
    {
        return journalEntryService.getEntryById(id).orElse(null);
    }
//
//    @PutMapping("{id}")
//    public JournalEntry updateJournalById(@PathVariable("id") long id, @RequestBody JournalEntry journal)
//    {
//
//    }
//
    @DeleteMapping("{id}")
    public void deleteJournalById(@PathVariable("id") ObjectId id)
    {
        journalEntryService.deleteEntryById(id);
    }
}
