package com.rvc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "demo-service",
        url = "localhost:8080",
        path = "testApi")
public interface TestApi {

    @GetMapping("test")
    String testApi();
}
