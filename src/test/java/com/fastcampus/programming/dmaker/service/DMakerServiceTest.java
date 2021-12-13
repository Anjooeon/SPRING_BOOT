package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDtailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/* @SpringBootTest
    빈등록을 하지 않고도 Spring의 모든 Bean이 뜬다.
    테스트를 간편하게 해준다.
    
    @ExtendWith(MockitoExtension.class)
    격리성(예로들어 테스트 데이터만 있으면 실제 운영에서의 예외 상황을 발견하지 못함)을 높여주기위해
    Mockito를 사용하는데 외부라이브러리를 가져오기 위해 @ExtendWith를 사용.
    Mockito는 Spring Boot에 기본으로 내장되어 있음
*/
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    //@Autowired
    @InjectMocks
    private DMakerService dMakerService;
    private final Developer defaultDeveloper = Developer.builder()
                        .developerLevel(DeveloperLevel.SENIOR)
                        .developerSkillType(DeveloperSkillType.FRONT_END)
                        .experienceYears(12)
                        .statusCode(StatusCode.EMPLOYED)
                        .name("name")
                        .age(12)
                        .build();

    private final CreateDeveloper.Request defaultRequest = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(12)
                .memberId("memberId")
                .name("name")
                .age(32)
                .build();


    @Test
    public void testSomthing(){
        /*dMakerService.createDeveloper(CreateDeveloper.Request.builder()
                        .developerLevel(DeveloperLevel.SENIOR)
                        .developerSkillType(DeveloperSkillType.FRONT_END)
                        .experienceYears(12)
                        .memberId("memberId")
                        .age(32)
                .build());
        List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
        System.out.println("=====================================");
        System.out.println(allEmployedDevelopers);
        System.out.println("=====================================");*/
        //assertEquals("hello world", result); //test데이터와 예상결과를 비교

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        DeveloperDtailDto developerDtailDto = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.SENIOR, developerDtailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDtailDto.getDeveloperSkillType());
        assertEquals(12, developerDtailDto.getExperienceYears());
    }
    @Test
    void createDeveloperTest_success(){
        //given

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captor
                =ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultRequest);

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer saveDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, saveDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, saveDeveloper.getDeveloperSkillType());
        assertEquals(12, saveDeveloper.getExperienceYears());

    }
    @Test
    void createDeveloperTest_failed_with_duplicated(){
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        //when
        //CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultRequest);

        //then
        DMakerException dMakerException= assertThrows(DMakerException.class, () -> dMakerService.createDeveloper(defaultRequest));
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}