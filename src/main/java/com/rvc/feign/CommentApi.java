package com.rvc.feign;


import com.rvc.pojo.dto.CommentStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "comment-service",
        url = "localhost:9200",
        path = "/communication/comment")
public interface CommentApi {
    @PostMapping("status")
    void status(CommentStatusDto commentStatusDto);
}
