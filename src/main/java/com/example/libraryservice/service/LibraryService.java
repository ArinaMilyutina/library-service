package com.example.libraryservice.service;

import com.example.libraryservice.dto.LibraryRequest;
import com.example.libraryservice.dto.LibraryResponse;
import com.example.libraryservice.dto.ResponseMsg;
import com.example.libraryservice.entity.Library;
import com.example.libraryservice.exception.BookAlreadyCheckedOutException;
import com.example.libraryservice.exception.NotFoundException;
import com.example.libraryservice.feignclient.book.BookClient;
import com.example.libraryservice.feignclient.book.BookRequest;
import com.example.libraryservice.feignclient.book.Status;
import com.example.libraryservice.feignclient.user.UserClient;
import com.example.libraryservice.feignclient.user.UserRequest;
import com.example.libraryservice.mapper.LibraryMapper;
import com.example.libraryservice.repository.LibraryRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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


    public LibraryResponse takeTheBook(Long userId, Long bookId, LibraryRequest libraryRequest) throws FeignException.NotFound {
        UserRequest userRequest = userClient.findById(userId);
        BookRequest bookRequest = bookClient.findById(bookId);
        if (bookRequest.getStatus().contains(Status.UNAVAILABLE)) {
            throw new BookAlreadyCheckedOutException(BOOK_ALREADY_CHECKED_OUT);
        }
        Library libraryEntry = LibraryMapper.INSTANCE.LibraryDtoToLibrary(libraryRequest);
        libraryEntry.setUserID(userRequest.getUserId());
        libraryEntry.setBookID(bookRequest.getBookId());
        libraryEntry.setBorrowDate(LocalDateTime.now());
        libraryEntry.setExpectedReturnDate(libraryRequest.getExpectedReturnDate());
        bookClient.updateBookStatus(bookId);
        libraryRepository.save(libraryEntry);
        return LibraryResponse.builder()
                .username(userRequest.getUsername())
                .title(bookRequest.getTitle())
                .borrowDate(libraryEntry.getBorrowDate())
                .expectedReturnDate(libraryEntry.getExpectedReturnDate())
                .build();
    }

    public ResponseMsg returnTheBook(Long bookId) throws NotFoundException {
        Optional<Library> optionalLibrary = libraryRepository.findByBookIDAndActualReturnDate(bookId, null);
        if(optionalLibrary.isEmpty()){
            throw new NoSuchElementException( "Book not found!");
        }
        Library library = optionalLibrary.get();
        library.setActualReturnDate(LocalDateTime.now());
        if (library.getActualReturnDate().isAfter(library.getExpectedReturnDate())) {
            return ResponseMsg.builder()
                    .msg("The book is overdue, you need to pay a fine!")
                    .build();
        }
        libraryRepository.save(library);
        bookClient.updateBookStatus(bookId);
        return ResponseMsg.builder()
                .msg("The book was successfully returned!")
                .build();
    }

}
