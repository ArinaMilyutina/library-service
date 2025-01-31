package com.example.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponse {
    private String username;
    private String title;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime borrowDate;
}
