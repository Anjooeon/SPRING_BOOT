package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
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
    public void createDeveloper(CreateDeveloper.Request request){
        /*transactional annotaion으로 인해 해당 부분이 필요없어짐*/
       /* EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();*/

            /*business logic start*/
            Developer developer = Developer.builder()
                    .developerLevel(DeveloperLevel.JUNGNIOR)
                    .developerSkillType(DeveloperSkillType.FRONT_END)
                    .experienceYears(2)
                    .name("Olaf")
                    .age(5)
                    .build();

            developerRepository.save(developer);
           // developerRepository.delete(developer);
            /*business logic end*/

          /*  transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }*/

    }
}
