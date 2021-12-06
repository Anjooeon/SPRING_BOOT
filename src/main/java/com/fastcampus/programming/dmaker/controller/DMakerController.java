package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<String> getAllDevelopers(){
        //GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return Arrays.asList("snow","Elsa","Olaf");
    }

    @GetMapping("/create-developers")
    public List<String> createDevelopers(){
        //GET /developers HTTP/1.1
        log.info("GET /create-developers HTTP/1.1");

        dMakerService.createDeveloper();

        return Collections.singletonList("Olaf"); //단일 객체를 가지고 있는 리스트를 리턴할때는 이렇게 하는게 더 좋다.
    }
}
