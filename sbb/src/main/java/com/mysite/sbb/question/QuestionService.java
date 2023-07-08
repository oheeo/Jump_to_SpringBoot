package com.mysite.sbb.question;

import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }
    // 질문 목록 페이징
    // 정수 타입의 페이지번호를 입력받아 해당 페이지의 질문 목록을 리턴
    // PageRequest.of(page, 10)에서 page는 조회할 페이지의 번호, 10은 한 페이지에 보여줄 게시물의 갯수
    // PageRequest.of : 질문 목록 최신순으로
    // Sort.Order.desc("createDate") : (작성일시(createDate)를 역순(Desc)으로) 조회

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
