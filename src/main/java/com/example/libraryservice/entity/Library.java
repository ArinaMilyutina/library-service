package com.example.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "library",schema = "library_service_schema")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "book_id")
    private Long bookID;
    @Column(name = "user_id")
    private Long userID;
    private LocalDateTime borrowDate;
    private LocalDateTime expectedReturnDate;

}
