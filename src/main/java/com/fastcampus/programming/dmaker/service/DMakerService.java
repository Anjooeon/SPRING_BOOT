package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERENC_YEAR_NOT_MATCHED;

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
    private final EntityManager em;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request){
        validateCreateDeveloperRequest(request);
        /*transactional annotaion으로 인해 해당 부분이 필요없어짐*/
       /* EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();*/

            /*business logic start*/
            Developer developer = Developer.builder()
                    .developerLevel(request.getDeveloperLevel())
                    .developerSkillType(request.getDeveloperSkillType())
                    .experienceYears(request.getExperienceYears())
                    .name(request.getName())
                    .age(request.getAge())
                    .memberId(request.getMemberId())
                    .build();

            developerRepository.save(developer);

            return CreateDeveloper.Response.fromEntity(developer);
           // developerRepository.delete(developer);
            /*business logic end*/

          /*  transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }*/

    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request){
        //business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();
        if(developerLevel == DeveloperLevel.SENIOR
            && experienceYears < 10){
            throw  new DMakerException(LEVEL_EXPERENC_YEAR_NOT_MATCHED); //static import 활용
        }

        if(developerLevel == DeveloperLevel.JUNGNIOR
            && experienceYears < 4 || experienceYears > 10){
            throw  new DMakerException(LEVEL_EXPERENC_YEAR_NOT_MATCHED);
        }

        if(developerLevel == DeveloperLevel.JUNIOR
                && experienceYears > 4 ){
            throw  new DMakerException(LEVEL_EXPERENC_YEAR_NOT_MATCHED);
        }

       developerRepository.findByMemberId(request.getMemberId())
               .ifPresent((developer -> {
                   throw new DMakerException(DUPLICATED_MEMBER_ID);
               }));


    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }
}
