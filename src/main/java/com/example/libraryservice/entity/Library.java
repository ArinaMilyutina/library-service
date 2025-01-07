package com.example.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "library")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JoinColumn(name = "book_id", nullable = false)
    private Long bookID;
    @JoinColumn(name = "user_id", nullable = false)
    private Long userID;

    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
}
