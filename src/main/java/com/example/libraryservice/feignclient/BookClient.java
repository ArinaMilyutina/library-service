package com.example.libraryservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "books-service")
public interface BookClient {

    @GetMapping("/book/find-by-isbn/{ISBN}")
    BookRequest findByISBN(@PathVariable("ISBN") String ISBN);

    @GetMapping("/book/status/{ISBN}")
    void updateBookStatus(@PathVariable("ISBN") String ISBN);
}
