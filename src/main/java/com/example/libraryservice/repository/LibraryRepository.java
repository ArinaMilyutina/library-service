package com.example.libraryservice.repository;

import com.example.libraryservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    Optional<Library> findByBookIDAndActualReturnDate(Long bookId, LocalDateTime actualReturnDate);

}

