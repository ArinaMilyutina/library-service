package com.example.libraryservice.service;

import com.example.libraryservice.dto.LibraryDto;
import com.example.libraryservice.entity.Library;
import com.example.libraryservice.exception.BookAlreadyCheckedOutException;
import com.example.libraryservice.feignclient.*;
import com.example.libraryservice.mapper.LibraryMapper;
import com.example.libraryservice.repository.LibraryRepository;
import feign.FeignException;
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


    public Library takeTheBook(Long userId, Long bookId, LibraryDto libraryDto) throws FeignException.NotFound {
        UserRequest userRequest = userClient.findById(userId);
        BookRequest bookRequest = bookClient.findById(bookId);
        if (bookRequest.getStatus().contains(Status.UNAVAILABLE)) {
            throw new BookAlreadyCheckedOutException(BOOK_ALREADY_CHECKED_OUT);
        }
        Library libraryEntry = LibraryMapper.INSTANCE.LibraryDtoToLibrary(libraryDto);
        libraryEntry.setUserID(userRequest.getUserId());
        libraryEntry.setBookID(bookRequest.getBookId());
        libraryEntry.setBorrowDate(LocalDateTime.now());
        libraryEntry.setExpectedReturnDate(libraryDto.getExpectedReturnDate());
        bookClient.updateBookStatus(bookId);
        return libraryRepository.save(libraryEntry);
    }

    public Library returnTheBook(Long bookId) {
        Optional<Library> optionalLibrary = libraryRepository.findByBookID(bookId);
        Library library = optionalLibrary.get();
        library.setActualReturnDate(LocalDateTime.now());
        if (library.getActualReturnDate().isAfter(library.getExpectedReturnDate())) {

        }
        libraryRepository.save(library);
        bookClient.updateBookStatus(bookId);

        return library;
    }

}
