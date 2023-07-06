package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
// @RequiredArgsConstructor : questionRepository 속성을 포함하는 생성자를 생성
// final이 붙은 속성을 포함하는 생성자를 자동으로 생성
// 따라서 스프링 의존성 주입 규칙에 의해 questionRepository 객체가 자동으로 주입
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
//    @ResponseBody  // 템플릿을 사용하기에 @ResponseBody 필요 없음
    public String list(Model model) {
    // Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할 (Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다)
        List<Question> questionList = this.questionService.getList();

        model.addAttribute("questionList", questionList);
        return "question_list";  // list 메서드에서 question_list.html 템플릿 파일의 이름인 "question_list"를 리턴
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";  // 질문 저장후 질문목록으로 이동
    }

}
