package com.piho.onoff.repositories;

import com.piho.onoff.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    Entry findEntryByEntryId(String entryId);

}
