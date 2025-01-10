package com.example.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryRequest {
    @Future(message = "Return date must be in the future")
    @NotNull(message = "Expected return date cannot be blank")
    private LocalDateTime expectedReturnDate;
}
