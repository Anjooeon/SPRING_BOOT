package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.service.DMakerService;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.test.web.servlet.ResultMatcher.*;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@WebMvcTest
Controller 관련 Bean들을 올려서 테스트 진행
*/
@WebMvcTest(DMakerController.class)
class DMakerControllerTest {
    /*
    MockMvc : method를 직접 호출하지 않고 url로 데이터를 바인딩하는 것처럼
    가상으로 바인딩해줌
    */
    @Autowired
    private MockMvc mockMvc;
    /*
     @MockBean : DMakerController는 DMakerService를 의존하므로 가짜 빈으로 등록해주고 테스트
    */
    @MockBean
    private DMakerService dMakerService;
    /*json으로 데이터를 주고받기 위해서*/
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    @Test
    void getAllDevelopers() throws  Exception{
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                        .developerSkillType(DeveloperSkillType.BACK_END)
                        .developerLevel(DeveloperLevel.JUNIOR)
                        .memberId("memberId1").build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.SENIOR)
                .memberId("memberId2").build();

        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto,seniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect((ResultMatcher) jsonPath("$.[0].develpoerSkillType",
                        is(DeveloperSkillType.BACK_END.name()))
                ).andExpect(
                        (ResultMatcher) jsonPath("$.[0].developerLevel",
                                is(DeveloperLevel.JUNIOR.name()))
                );

    }
}