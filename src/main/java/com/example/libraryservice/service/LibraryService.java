package com.example.libraryservice.service;

import com.example.libraryservice.dto.LibraryDto;
import com.example.libraryservice.entity.Library;
import com.example.libraryservice.exception.BookAlreadyCheckedOutException;
import com.example.libraryservice.feignclient.BookClient;
import com.example.libraryservice.feignclient.BookRequest;
import com.example.libraryservice.feignclient.UserClient;
import com.example.libraryservice.feignclient.UserRequest;
import com.example.libraryservice.mapper.LibraryMapper;
import com.example.libraryservice.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class LibraryService {
    private static final String BOOK_ALREADY_CHECKED_OUT = "The book has already been taken by another user.";
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private BookClient bookClient;


    public Library takeTheBook(String username, String ISBN, LibraryDto libraryDto) {
        UserRequest userRequest = userClient.findByUsername(username);
        BookRequest bookRequest = bookClient.findByISBN(ISBN);
        Optional<Library> optionalLibrary = checkingTheBook(bookRequest.getBookId());
        if (optionalLibrary.isPresent()) {
            throw new BookAlreadyCheckedOutException(BOOK_ALREADY_CHECKED_OUT);
        }
        Library libraryEntry = LibraryMapper.INSTANCE.LibraryDtoToLibrary(libraryDto);
        libraryEntry.setUserID(userRequest.getUserId());
        libraryEntry.setBookID(bookRequest.getBookId());
        libraryEntry.setBorrowDate(LocalDateTime.now());
        libraryEntry.setReturnDate(libraryEntry.getReturnDate());
        bookClient.updateBookStatus(ISBN);
        return libraryRepository.save(libraryEntry);
    }

    public Optional<Library> checkingTheBook(Long id) {
        return libraryRepository.findByBookID(id);
    }
}
