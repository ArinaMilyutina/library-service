package com.example.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryDto {
    @Future(message = "Return date must be in the future")
    @NotNull(message = "Expected return date cannot be blank")
    private LocalDateTime expectedReturnDate;
    private String username;
    private String ISBN;
}