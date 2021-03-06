package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.*;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.entity.RetiredDeveloper;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableJpaAuditing
//@Transactional/*business logic의 처음과 끝에 같은 로직이 들어가는 AOP개념이 적용된다.*/
public class DMakerService {
    // @Autowired
    //@Injection
    //위의 두 방식은 test하기 어려워서 옛날에는 많이 사용했지만 요새는 생성자로 선언한다.
    //따라서 위에 @RequiredArgsConstructor로 자동으로 생성자를 만들어준다.
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;
    private final EntityManager em;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request){
        validateCreateDeveloperRequest(request);
        /*transactional annotaion으로 인해 해당 부분이 필요없어짐*/
       /* EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();*/

            /*business logic start*/

            return CreateDeveloper.Response.fromEntity(
                    developerRepository.save(createDeveloperFromRequest(request))
            );
           // developerRepository.delete(developer);
            /*business logic end*/

          /*  transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }*/

    }
    private Developer createDeveloperFromRequest(CreateDeveloper.Request request){
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .age(request.getAge())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .build();
    }
    private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request){

        //business validation
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());
       developerRepository.findByMemberId(request.getMemberId())
               .ifPresent((developer -> {
                   throw new DMakerException(DUPLICATED_MEMBER_ID);
               }));
    }
    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }
    private Developer getDeveloperByMemberId(String memberId){
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELPOER));

    }

    @Transactional(readOnly = true)
    public DeveloperDtailDto getDeveloperDetail(String memberId) {
        return DeveloperDtailDto.fromEntity(getDeveloperByMemberId(memberId));
    }
    @Transactional
    public DeveloperDtailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        request.getDeveloperLevel().validateExperienceYears(request.getExperienceYears());

        return DeveloperDtailDto.fromEntity(getUpdatedDeveloperFromRequest(request, getDeveloperByMemberId(memberId)));
    }

    private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    @Transactional
    public DeveloperDtailDto deleteDeveloper(String memberId){
        //1.EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELPOER));
        developer.setStatusCode(StatusCode.RETIRED);

        //원자성 테스트(automic)
        //developerRepository.save(developer);
        //if(developer != null) throw new DMakerException(NO_DEVELPOER);

        //2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDtailDto.fromEntity(developer);
    }

}
