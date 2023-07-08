package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model, @PathVariable("id") Integer id,
            @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
            // 현재 로그인한 사용자에 대한 정보를 알기 위해서는 스프링 시큐리티가 제공하는 Principal 객체를 사용
            // createAnswer 메서드에 Principal 객체를 매개변수로 지정
            // principal.getName()을 호출하면 현재 로그인한 사용자의 사용자ID를 알 수 있다.
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // principal 객체를 통해 사용자명을 얻은 후, 사용자명을 통해 SiteUser 객체를 얻어서
        // 답변을 등록하는 AnswerService의 create 메서드에 전달하여 답변을 저장
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        Answer answer = this.answerService.create(question,
                answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
        // 답변을 작성 후에 해당 답변으로 이동하도록 앵커 태그를 추가
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
    // URL의 답변 아이디를 통해 조회한 답변 데이터의 "내용"을
    // AnswerForm 객체에 대입하여 answer_form.html 템플릿에서 사용할수 있도록 함
    // answer_form.html은 답변을 수정하기 위한 템플릿으로 신규로 작성
    // 답변 수정시 기존의 내용이 필요하므로 AnswerForm 객체에 조회한 값을 저장해야 함

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
        // 답변을 수정한 후에 해당 답변으로 이동하도록 앵커 태그를 추가
    }
    // 폼을 통해 요청되는 POST방식의 /answer/modify/답변ID 형태의 URL을 처리하기 위해
    // POST 방식의 답변 수정을 처리하는 answerModify 메서드를 추가
    // 답변 수정을 완료한 후에는 질문 상세 페이지로 돌아가기 위해 answer.getQuestion.getId()로 질문의 아이디를 가져옴

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
    // 답변 삭제 버튼을 누르면 요청되는 GET방식의 /answer/delete/답변ID 형태의 URL을 처리하기 위해
    // 답변을 삭제하는 answerDelete 메서드
    // 답변을 삭제한 후에는 해당 답변이 있던 질문상세 화면으로 리다이렉트

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
        // 답변을 추천한 후에 해당 답변으로 이동하도록 앵커 태그를 추가
    }
    // 답변 추천 버튼을 눌렀을때 호출되는 URL을 처리하기 위해
    // 추천은 로그인한 사람만 가능해야 하므로 @PreAuthorize("isAuthenticated()") 애너테이션이 적용
    // AnswerService의 vote 메서드를 호출하여 추천인을 저장
    // 오류가 없다면 질문 상세화면으로 리다이렉트

}
