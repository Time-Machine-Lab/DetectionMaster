package com.rvc.feign;


import com.rvc.pojo.CommentStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


//@FeignClient(value = "comment-service",
//        url = "localhost:9200",
//        path = "/communication/comment")
@FeignClient("rvc-communication-service")
public interface CommentApi {
//    @PostMapping("/status")
    @PostMapping("/communication/comment/status")
    void status(CommentStatusDto commentStatusDto);
}
