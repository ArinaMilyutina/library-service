package com.example.libraryservice.controller;

import com.example.libraryservice.dto.LibraryRequest;
import com.example.libraryservice.dto.LibraryResponse;
import com.example.libraryservice.dto.ResponseMsg;
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
    public ResponseEntity<LibraryResponse> takeTheBook(@RequestParam Long userId, @RequestParam Long bookId, @Valid @RequestBody LibraryRequest libraryRequest) throws NotFoundException {
        return ResponseEntity.ok(libraryService.takeTheBook(userId, bookId, libraryRequest));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/admin/return-book/{bookId}")
    public ResponseEntity<ResponseMsg> returnTheBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(libraryService.returnTheBook(bookId));
    }

}