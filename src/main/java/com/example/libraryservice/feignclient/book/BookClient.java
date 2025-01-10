package com.example.libraryservice.feignclient.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "books-service")
public interface BookClient {

    @GetMapping("/external/book/find-by-id/{id}")
    BookRequest findById(@PathVariable("id") Long id);

    @GetMapping("/external/book/status/{id}")
    void updateBookStatus(@PathVariable("id") Long id);
}
