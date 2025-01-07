package com.example.libraryservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "book-service")
public class BookClient {
}
