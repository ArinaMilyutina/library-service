package com.example.libraryservice.repository;

import com.example.libraryservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library,Long> {
    List<Library> findByReturnDateIsNull();
}

