package com.example.libraryservice.repository;

import com.example.libraryservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    List<Library> findByReturnDateIsNull();

    Optional<Library> findByBookID(Long bookId);
}

