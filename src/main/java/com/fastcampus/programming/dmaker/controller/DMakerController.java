package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDtailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public List<DeveloperDto> getAllDevelopers() {
        //GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDtailDto getAllDeveloperDetail(@PathVariable String memberId) {
        //GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")   //validation이 작동하게 하려면 request에 valid라는 어노테이션이 달려야함
    public CreateDeveloper.Response createDevelopers(@Valid @RequestBody CreateDeveloper.Request request) {
        //GET /developers HTTP/1.1
        //log.info("GET /create-developers HTTP/1.1");
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);

        //return Collections.singletonList("Olaf"); //단일 객체를 가지고 있는 리스트를 리턴할때는 이렇게 하는게 더 좋다.
    }

    //PUT은 모든 데이터수정 , PACH는 특정 데이터를 수정
    @PutMapping("/developer/{memberId}")
    public DeveloperDtailDto editDeveloper(
            @PathVariable String memberId,
           @Valid  @RequestBody EditDeveloper.Request reqest
            ) {
        //GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return dMakerService.editDeveloper(memberId, reqest);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDtailDto deleteDeveloper(@PathVariable String memberId){
        return dMakerService.deleteDeveloper(memberId);
    }
}
