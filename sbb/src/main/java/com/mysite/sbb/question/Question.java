package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.ManyToOne;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;

import com.mysite.sbb.answer.Answer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 일반적으로 엔티티에는 Setter 메서드를 구현하지 않기를 권한다. 왜냐하면 엔티티는 데이터베이스와 바로 연결되어 있으므로 데이터를 자유롭게 변경할 수 있는 Setter 메서드를 허용하는 것이 안전하지 않다고 판단
// 엔티티를 생성할 경우에는 롬북의 @Builder 어노테이션을 통한 빌드패턴을 사용하고, 데이터를 변경할 땐 그에 해당하는 메서드를 엔티티에 추가하여 데이터를 변경
// 점프투스프링에선 복잡도를 낮추고자 엔티티에 @Setter 메서드 추가하여 진행
@Entity

// 질문(Question) 엔티티 속성 :
// id(질문 고유번호), subject(질문 제목), content(질문 내용), create_date(작성 날짜)

public class Question {
    @Id   // 기본 키(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue 속성 값을 따로 세팅하지 않아도 1씩 자동 증가하여 저장
    // strategy 고유번호 생성하는 옵션
    // GenerationType.IDENTITY 해당 컬럼만의 독립적인 시퀀스를 생성하여 번호를 증가시킬 때 사용
    private Integer id;

    @Column(length = 200)   // 컬럼의 길이 정의
    private String subject;

    @Column(columnDefinition = "TEXT")   // 컬럼의 속성 정의("TEXT"는 글자 수를 제한할 수 없는 경우)
    private String content;

    private LocalDateTime createDate;   // createDate 처럼 대소문자 형태의 이름은 실제 테이블 컬럼명에선 create_date 처럼 모두 소문자로 변경됨

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    // 답변 엔티티와 연결된 속성이라는 것을 명시적으로 @OneToMany (실제 데이터베이스에서 ForeignKey 관계 생성)
    // 하나의 질문에 답변 여러개가 달릴 수 있다. 따라서 질문은 One, 답변은 Many
    // mappedBy는 참조 엔티티의 속성명을 의미. 즉 Answer 엔티티에서 Question 엔티티를 참조한 속성명 question을 mappedBy에 전달해야함
    // cascade = CascadeType.REMOVE 질문을 삭제하면 그에 달린 답변들도 모두 삭제하기 위해
    // Answer 엔티티 객체로 구성된 answerList 속성을 추가함
    // 질문 객체(예:question)에서 답변을 참조하려면 question.getAnswerList()

    @ManyToOne
    private SiteUser author;  //  Question과 Answer 엔티티에 "글쓴이" 항목

    private LocalDateTime modifyDate;  // 질문이나 답변 수정 일시
    
}
