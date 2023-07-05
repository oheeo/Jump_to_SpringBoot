package com.mysite.sbb.question;

import com.mysite.sbb.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // QuestionRepository 는 리포지터리로 만들기 위해 JpaRepository 인터페이스를 상속
    // JpaRepository 를 상속할 때는 제네릭스 타입으로 <Question, Integer> 처럼 리포지터리의 대상이 되는 엔티티의 타입(Question)과 해당 엔티티의 PK의 속성 타입(Integer)을 지정
    // Question 엔티티의 PK(Primary Key) 속성인 id의 타입은 Integer
    Question findBySubject(String subject);
    // test 코드에서 findBySubject 메서드를 사용하기 위해

    Question findBySubjectAndContent(String subject, String content);
    // test 코드에서 findBySubjectAndContent 메서드를 사용하기 위해

    List<Question> findBySubjectLike(String subject);
    // test 코드에서 findBySubjectLike 메서드를 사용하기 위해
}