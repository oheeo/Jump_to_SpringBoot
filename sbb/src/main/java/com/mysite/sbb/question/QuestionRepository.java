package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mysite.sbb.question.Question;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    Page<Question> findAll(Pageable pageable);
    // 질문 목록 페이징
    // Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
    // 검색 기능
    // Specification 과 Pageable 객체를 입력으로 Question 엔티티를 조회하는 findAll 메서드를 선언

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
    // @Query 애너테이션이 적용된 findAllByKeyword 메서드 추가
    // @Query를 작성할 때에는 반드시 테이블 기준이 아닌 엔티티 기준으로 작성
    // 즉, site_user와 같은 테이블명 대신 SiteUser처럼 엔티티명을 사용,
    // 조인문에서 보듯이 q.author_id=u1.id와 같은 컬럼명 대신 q.author=u1처럼 엔티티의 속성명을 사용
    // @Query에 파라미터로 전달할 kw 문자열은 메서드의 매개변수에 @Param("kw")처럼 @Param 애너테이션을 사용
    // 검색어를 의미하는 kw 문자열은 @Query 안에서 :kw로 참조

}