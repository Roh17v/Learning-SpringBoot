package com.rohitverma.journal.controller;

import com.rohitverma.journal.model.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journals")
public class JournalController {
    private Map<Long, JournalEntry> journalList = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAllJournals()
    {
        return  new ArrayList<>(journalList.values());
    }

    @PostMapping
    public boolean addJournal(@RequestBody JournalEntry journal)
    {
        journalList.put(journal.getId(), journal);
        return true;
    }

    @GetMapping("/{id}")
    public JournalEntry getJournalById(@PathVariable("id") long id)
    {
        return journalList.get(id);
    }
}
