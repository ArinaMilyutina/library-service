package com.example.libraryservice.controller;

import com.example.libraryservice.dto.LibraryDto;
import com.example.libraryservice.entity.Library;
import com.example.libraryservice.exception.NotFoundException;
import com.example.libraryservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping("/admin/take-book")
    public ResponseEntity<Library> takeTheBook(@RequestParam Long userId, @RequestParam Long bookId, @Valid @RequestBody LibraryDto libraryDto) throws NotFoundException {
        Library libraryEntry = libraryService.takeTheBook(userId, bookId, libraryDto);
        return ResponseEntity.ok(libraryEntry);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/admin/return-book/{bookId}")
    public ResponseEntity<Library> returnTheBook(@PathVariable Long bookId) {
        Library library = libraryService.returnTheBook(bookId);
        return ResponseEntity.ok(library);
    }


 /*   @GetMapping("/available-books")
    public ResponseEntity<List<Book>> getAvailableBooks() throws NotFoundException {
        List<Book> availableBooks = libraryService.getAvailableBooks();
        return ResponseEntity.ok(availableBooks);
    }*/
}