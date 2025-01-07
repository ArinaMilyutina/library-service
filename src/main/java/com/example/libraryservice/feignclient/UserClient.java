package com.example.libraryservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "library2")
public interface UserClient {


}