package com.example.libraryservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "library2")
public interface UserClient {
    @GetMapping("/user/find-by-id/{id}")
    UserRequest findById(@PathVariable Long id);
}