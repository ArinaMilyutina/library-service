package com.example.libraryservice.feignclient.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "library2")
public interface UserClient {
    @GetMapping("/external/user/find-by-id/{id}")
    UserRequest findById(@PathVariable Long id);
}