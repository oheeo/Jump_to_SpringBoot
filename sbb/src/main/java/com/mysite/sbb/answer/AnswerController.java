package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

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
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

}
