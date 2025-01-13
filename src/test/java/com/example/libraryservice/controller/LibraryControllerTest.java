package com.example.libraryservice.controller;

import com.example.libraryservice.dto.LibraryRequest;
import com.example.libraryservice.dto.LibraryResponse;
import com.example.libraryservice.dto.ResponseMsg;
import com.example.libraryservice.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {
    private static final String TITLE = "The Little Prince";
    private static final String USERNAME = "Arina20";
    private static final String URL_TAKE_THE_BOOK = "/library/admin/take-book";
    private static final String URL_RETURN_THE_BOOK = "/library/admin/return-book/{bookId}";
    private static final Long ID = 1L;

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void takeTheBook() throws Exception {
        LibraryRequest libraryRequest = LibraryRequest.builder()
                .expectedReturnDate(LocalDateTime.now().plusDays(7))
                .build();
        LibraryResponse libraryResponse = LibraryResponse.builder()
                .username(USERNAME)
                .title(TITLE)
                .borrowDate(LocalDateTime.now())
                .expectedReturnDate(libraryRequest.getExpectedReturnDate())
                .build();
        when(libraryService.takeTheBook(ID, ID, libraryRequest)).thenReturn(libraryResponse);
        mockMvc.perform(post(URL_TAKE_THE_BOOK)
                        .param("userId", ID.toString())
                        .param("bookId", ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(libraryResponse.getUsername()))
                .andExpect(jsonPath("$.title").value(libraryResponse.getTitle()));
        verify(libraryService, times(1)).takeTheBook(ID, ID, libraryRequest);

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void returnTheBook_existingBook_shouldReturnResponseMsg() throws Exception {
        ResponseMsg responseMsg = ResponseMsg.builder().msg("The book was successfully returned!").build();
        when(libraryService.returnTheBook(ID)).thenReturn(responseMsg);
        mockMvc.perform(get(URL_RETURN_THE_BOOK, ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.msg").value(responseMsg.getMsg()));

        verify(libraryService, times(1)).returnTheBook(ID);

    }
}
