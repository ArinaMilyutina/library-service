package com.example.libraryservice.service;

import com.example.libraryservice.dto.LibraryRequest;
import com.example.libraryservice.dto.LibraryResponse;
import com.example.libraryservice.dto.ResponseMsg;
import com.example.libraryservice.entity.Library;
import com.example.libraryservice.exception.BookAlreadyCheckedOutException;
import com.example.libraryservice.feignclient.book.BookClient;
import com.example.libraryservice.feignclient.book.BookRequest;
import com.example.libraryservice.feignclient.book.Status;
import com.example.libraryservice.feignclient.user.UserClient;
import com.example.libraryservice.feignclient.user.UserRequest;
import com.example.libraryservice.repository.LibraryRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
    private static final String TITLE = "The Little Prince";
    private static final String USERNAME = "Arina20";
    private static final Long ID = 1L;


    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private LibraryService libraryService;


    private LibraryRequest libraryRequest;
    private UserRequest userRequest;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        libraryRequest = LibraryRequest.builder().expectedReturnDate(LocalDateTime.now().plusDays(7)).build();
        userRequest = new UserRequest(ID, USERNAME);
        bookRequest = new BookRequest(ID, TITLE, Collections.singleton(Status.AVAILABLE));
    }

    @Test
    void takeTheBook_availableBook() throws FeignException.NotFound {
        when(userClient.findById(ID)).thenReturn(userRequest);
        when(bookClient.findById(ID)).thenReturn(bookRequest);
        LibraryResponse libraryResponse = libraryService.takeTheBook(ID, ID, libraryRequest);
        assertNotNull(libraryResponse);
        assertEquals(userRequest.getUsername(), libraryResponse.getUsername());
        assertEquals(bookRequest.getTitle(), libraryResponse.getTitle());
        verify(libraryRepository, times(1)).save(any(Library.class));
        verify(bookClient, times(1)).updateBookStatus(ID);
    }


    @Test
    void takeTheBook_bookAlreadyCheckedOut() throws FeignException.NotFound {
        bookRequest.setStatus(Collections.singleton(Status.UNAVAILABLE));
        when(userClient.findById(ID)).thenReturn(userRequest);
        when(bookClient.findById(ID)).thenReturn(bookRequest);
        assertThrows(BookAlreadyCheckedOutException.class, () -> libraryService.takeTheBook(ID, ID, libraryRequest));
        verify(libraryRepository, never()).save(any(Library.class));
        verify(bookClient, never()).updateBookStatus(ID);
    }


    @Test
    void returnTheBook_bookReturnedOnTime() {
        Library library = new Library();
        library.setBookID(ID);
        library.setExpectedReturnDate(LocalDateTime.now().plusDays(7));
        when(libraryRepository.findByBookIDAndActualReturnDate(ID, null)).thenReturn(Optional.of(library));
        ResponseMsg responseMsg = libraryService.returnTheBook(ID);
        assertEquals("The book was successfully returned!", responseMsg.getMsg());
        verify(libraryRepository, times(1)).save(any(Library.class));
        verify(bookClient, times(1)).updateBookStatus(ID);
    }

    @Test
    void returnTheBook_bookReturnedOverdue() {
        Library library = new Library();
        library.setBookID(ID);
        library.setExpectedReturnDate(LocalDateTime.now().minusDays(1));
        library.setActualReturnDate(LocalDateTime.now());
        when(libraryRepository.findByBookIDAndActualReturnDate(ID, null)).thenReturn(Optional.of(library));
        ResponseMsg responseMsg = libraryService.returnTheBook(ID);
        assertEquals("The book is overdue, you need to pay a fine!", responseMsg.getMsg());
        verify(libraryRepository, times(1)).save(any(Library.class));
        verify(bookClient, never()).updateBookStatus(ID);
    }

    @Test
    void returnTheBook_bookNotFound() {
        when(libraryRepository.findByBookIDAndActualReturnDate(ID, null)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> libraryService.returnTheBook(ID));
        verify(libraryRepository, never()).save(any(Library.class));
        verify(bookClient, never()).updateBookStatus(ID);
    }
}
