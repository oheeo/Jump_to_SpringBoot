package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.QuestionService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {
    // @SpringBootTest 애너테이션은 SbbApplicationTests 클래스가 스프링부트 테스트 클래스임을 의미

//   @Autowired
//   private QuestionRepository questionRepository;
    // @Autowired 애너테이션은 questionRepository 객체를 스프링이 자동 생성해줌
    // 객체를 주입하는 방식에는 @Autowired 외에 Setter 또는 생성자를 사용하는 방법
    // 순환참조 문제와 같은 이유로 @Autowired 보다는 생성자를 통한 객체 주입방식이 권장
    // 하지만 테스트 코드의 경우 생성자를 통한 객체의 주입이 불가능하므로
    // 테스트 코드 작성시에만 @Autowired 를 사용하고 실제 코드 작성시에는 생성자를 통한 객체 주입방식

//    @Autowired
//    private AnswerRepository answerRepository;
    // 답변 데이터 처리를 위해서 답변 리포지터리가 필요하므로 AnswerRepository 객체를 @Autowired로 주입

    @Autowired
    private QuestionService questionService;

    //    @Transactional
    @Test
    // @Test 애너테이션은 testJpa 메서드가 테스트 메서드임을 의미
    void testJpa() {
        // 질문(Question) 데이터 저장
//        Question q1 = new Question();
//        q1.setSubject("sbb가 무엇인가요?");  // 질문 제목 등록
//        q1.setContent("sbb에 대해서 알고 싶습니다.");  // 질문 내용 등록
//        q1.setCreateDate(LocalDateTime.now());  // 질문 등록 시간
//        this.questionRepository.save(q1);  // 첫번째 질문 저장
//
//        Question q2 = new Question();
//        q2.setSubject("스프링부트 모델 질문입니다.");
//        q2.setContent("id는 자동으로 생성되나요?");
//        q2.setCreateDate(LocalDateTime.now());
//        this.questionRepository.save(q2);  // 두번째 질문 저장


        // 질문(Question) findAll
//        List<Question> all = this.questionRepository.findAll();
//        // findAll() : question 테이블에 저장된 모든 데이터 조회
//        assertEquals(2, all.size());
//        // 2건(q1, q2)의 데이터를 저장했기 때문에
//        // 데이터의 사이즈는 2인지 확인하기 위해 JUnit의 assertEquals(기대값, 실제값)
//        // 기대값과 실제값이 동일한지를 조사 (동일하지 않으면 테스트 실패)
//
//        Question q = all.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
//        // 저장한 첫번째 데이터의 제목이 "sbb가 무엇인가요?"와 일치하는지 테스트

        // 질문(Question) findById (Question 엔티티의 Id값으로 데이터 조회)
//        Optional<Question> oq = this.questionRepository.findById(1);
//        if(oq.isPresent()) {
//            Question q = oq.get();
//            assertEquals("sbb가 무엇인가요?", q.getSubject());
//            // findById의 리턴 타입은 Question이 아닌 Optional이므로 null 처리를 유연하게 처리하기 위해
//            // isPresent로 null이 아닌지 확인 후 get으로 실제 Question 객체 값을 얻어야 함

        // 질문(Question) findBySubject (Question 엔티티의 subject 값으로 데이터 조회)
        // Question 리포지터리는 findBySubject와 같은 메서드를 기본적으로 제공하지 않기에
        // findBySubject 메서드를 사용하려면 QuestionRepository 인터페이스를 변경해야 함
//        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//        assertEquals(1, q.getId());

        // 질문(Question) findBySubjectAndContent (제목과 내용 함께 조회)
//        Question q = this.questionRepository.findBySubjectAndContent(
//                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//        assertEquals(1, q.getId());

        // 질문(Question) findBySubjectLike (제목에 특정 문자열이 포함되어 있는 데이터 조회)
//        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
//        Question q = qList.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
        // sbb% : "sbb"로 시작하는 문자열
        // %sbb : "sbb"로 끝나는 문자열
        // %sbb% : "sbb"를 포함하는 문자열


        // 질문(Question) 데이터 수정
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());   // assertTrue(값)은 값이 true인지를 테스트
//        Question q = oq.get();
//        q.setSubject("수정된 제목");
//        this.questionRepository.save(q);   // 변경된 Question 데이터를 저장


        // 질문(Question) 데이터 삭제
//        assertEquals(2, this.questionRepository.count());
//        // count() 해당 리포지터리의 총 데이터건수를 리턴
//        Optional<Question> oq = this.questionRepository.findById(1);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//        this.questionRepository.delete(q);
//        assertEquals(1, this.questionRepository.count());
//        // 삭제하기 전에는 데이터 건수가 2, 삭제한 후에는 데이터 건수가 1인지를 테스트


        // 답변(Answer) 데이터 생성 후 저장
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        Answer a = new Answer();
//        a.setContent("네 자동으로 생성됩니다.");
//        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
//        a.setCreateDate(LocalDateTime.now());
//        this.answerRepository.save(a);
//        // id가 2인 질문 데이터를 가져온 다음
//        // Answer 엔티티의 question 속성에 방금 가져온 질문 데이터를 대입해(a.setQuestion(q)) 답변 데이터를 생성

        // 답변(Answer) 데이터 조회
//        Optional<Answer> oa = this.answerRepository.findById(1);
//        // id 값이 1인 답변 조회
//        assertTrue(oa.isPresent());
//        Answer a = oa.get();
//        assertEquals(2, a.getQuestion().getId());
//        // 그 답변의 질문 id가 2인지 테스트

        // 답변에 연결된 질문 찾기 vs 질문에 달린 답변 찾기
        // a.getQuestion() : 앞에서 구성한 Answer 엔티티의 question 속성을 이용하면 "답변에 연결된 질문"을 조회할 수 있다.
        // answerList : "질문에 달린 답변"을 조회하려면 질문 엔티티에 정의한 answerList
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
        // 질문 객체로부터 답변 리스트를 구하는 테스트코드이다.
        // id가 2인 질문에 답변을 한 개 등록했으므로 위와 같이 검증할 수 있다.
        // @Transactional 애너테이션을 사용하면 메서드가 종료될 때까지 DB 세션이 유지


        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}