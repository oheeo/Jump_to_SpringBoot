package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
// @RequiredArgsConstructor : questionRepository 속성을 포함하는 생성자를 생성
// final이 붙은 속성을 포함하는 생성자를 자동으로 생성
// 따라서 스프링 의존성 주입 규칙에 의해 questionRepository 객체가 자동으로 주입
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
//    @ResponseBody  // 템플릿을 사용하기에 @ResponseBody 필요 없음
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        // Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할 (Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다)
        // @RequestParam(value="page", defaultValue="0") int page : http://localhost:8080/question/list?page=0 처럼 GET 방식으로 요청된 URL에서 page값을 가져오기 위해
        // URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값으로 0
        // 검색어에 해당하는 kw 파라미터를 추가했고 디폴트값으로 빈 문자열을 설정
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        // 화면에서 입력한 검색어를 화면에 유지하기 위해 model.addAttribute("kw", kw)로 kw 값을 저장
        // 화면에서 kw 값이 파라미터로 들어오면 해당 값으로 질문 목록이 검색되어 조회될 것
        return "question_list";  // list 메서드에서 question_list.html 템플릿 파일의 이름인 "question_list"를 리턴
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    // 로그인이 필요한 메서드들에 @PreAuthorize("isAuthenticated()") 애너테이션을 적용해서
    // 로그아웃 상태에서 호출되면 로그인 페이지로 이동 (예, 로그아웃 상태로 답글 달 때)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";  // 질문 저장후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    // 현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우, "수정권한이 없습니다."
    // 수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에 값을 담아서 템플릿으로 전달
    // POST 형식의 /question/modify/{id} 요청을 처리하기 위해 questionModify 메서드를 추가
    // questionForm의 데이터를 검증하고 로그인한 사용자와 수정하려는 질문의 작성자가 동일한지도 검증
    // 통과되면 QuestionService에서 작성한 modify 메서드를 호출하여 질문 데이터를 수정 후 질문 상세 화면 다시 호출

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
    // URL로 전달받은 id값을 사용하여 Question 데이터를 조회한 후
    // 로그인한 사용자와 질문 작성자가 동일할 경우 delete 메서드로 질문을 삭제
    // 질문 데이터 삭제후에는 질문 목록 화면으로 돌아갈 수 있도록 루트 페이지로 리다이렉트

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
    // 추천 버튼을 눌렀을때 호출되는 URL을 처리하기 위해
    // 추천은 로그인한 사람만 가능해야 하므로 @PreAuthorize("isAuthenticated()") 애너테이션이 적용
    // vote 메서드를 호출하여 추천인을 저장
    // 오류가 없다면 질문 상세화면으로 리다이렉트

}
