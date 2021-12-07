package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController //DmakerController를 RestController Type에 등록해서
@RequiredArgsConstructor
public class DMakerController {
    /*
    DMakerController(Bean) DMakerService(Bean) DeveloperRepository(Bean)
    ==============================Spring Application=====================
    */

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        //GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return Arrays.asList("snow", "Elsa", "Olaf");
    }

    @PostMapping("/create-developers")   //validation이 작동하게 하려면 request에 valid라는 어노테이션이 달려야함
    public List<String> createDevelopers(@Valid @RequestBody CreateDeveloper.Request request) {
        //GET /developers HTTP/1.1
        //log.info("GET /create-developers HTTP/1.1");
        log.info("request : {}", request);

        dMakerService.createDeveloper(request);

        return Collections.singletonList("Olaf"); //단일 객체를 가지고 있는 리스트를 리턴할때는 이렇게 하는게 더 좋다.
    }
}
