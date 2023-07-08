package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
    // 검색어(kw)를 입력받아 쿼리의 조인문과 where문을 생성하여 리턴하는 메서드
    // q - Root, 즉 기준을 의미하는 Question 엔티티의 객체 (질문 제목과 내용을 검색하기 위해 필요)
    // u1 - Question 엔티티와 SiteUser 엔티티를 아우터 조인(JoinType.LEFT)하여 만든 SiteUser 엔티티의 객체. Question 엔티티와 SiteUser 엔티티는 author 속성으로 연결되어 있기 때문에 q.join("author")와 같이 조인해야 한다. (질문 작성자를 검색하기 위해 필요)
    // a - Question 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체. Question 엔티티와 Answer 엔티티는 answerList 속성으로 연결되어 있기 때문에 q.join("answerList")와 같이 조인해야 한다. (답변 내용을 검색하기 위해 필요)
    // u2 - 바로 위에서 작성한 a 객체와 다시 한번 SiteUser 엔티티와 아우터 조인하여 만든 SiteUser 엔티티의 객체 (답변 작성자를 검색하기 위해서 필요)
    // 검색어(kw)가 포함되어 있는지를 like로 검색하기 위해 제목, 내용, 질문 작성자, 답변 내용, 답변 작성자 각각에 cb.like를 사용하고 최종적으로 cb.or로 OR 검색되게

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
//        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
        // QuestionRepository에서 추가한 findAllByKeyword 메서드를 사용하기 위해 수정함
        // Specification을 사용할때와 동일하게 동작
    // 질문 목록 페이징
    // 정수 타입의 페이지번호를 입력받아 해당 페이지의 질문 목록을 리턴
    // PageRequest.of(page, 10)에서 page는 조회할 페이지의 번호, 10은 한 페이지에 보여줄 게시물의 갯수
    // PageRequest.of : 질문 목록 최신순으로
    // Sort.Order.desc("createDate") : (작성일시(createDate)를 역순(Desc)으로) 조회
    // 검색어를 의미하는 매개변수 kw를 getList에 추가하고 kw 값으로 Specification 객체를 생성하여 findAll 메서드 호출시 전달

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);  // 작성자 정보를 저장
        // create 메서드에 SiteUser 매개변수를 추가하여 Question 데이터를 생성
        this.questionRepository.save(q);
    }
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    // 질문 데이터를 수정할수 있는 modify 메서드

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    // 질문 데이터를 삭제하는 delete 메서드

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
    // Question 엔티티에 사용자를 추천인으로 저장하는 vote 메서드

}
