package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.mysite.sbb.question.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

// 답변(Answer) 엔티티 속성 :
// id(답변 고유번호), question(어떤 질문인지), content(답변 내용), create_date(작성 날짜)

public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;
    // question 속성은 답변 엔티티에서 질문 엔티티를 참조함 (어떤 질문에 대한 답변인지)
    // 질문 엔티티와 연결된 속성이라는 것을 명시적으로 @ManyToOne (실제 데이터베이스에서 ForeignKey 관계 생성)
    // 답변은 하나의 질문에 여러개가 달릴 수 있다. 따라서 답변은 Many, 질문은 One
    // 부모는 Question, 자식은 Answer
    // 답변 객체(예:answer)를 통해 질문 객체의 제목을 알고 싶다면 answer.getQuestion().getSubject()
}
