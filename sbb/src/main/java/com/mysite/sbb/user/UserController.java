package com.mysite.sbb.user;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
                    return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        // 사용자ID 또는 이메일 주소가 동일할 경우
        // DataIntegrityViolationException 예외가 발생하므로
        // "이미 등록된 사용자입니다."라는 오류를 화면에 표시
        // 다른 오류의 경우에는 해당 오류의 메시지(e.getMessage())를 출력

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
    // 스프링 시큐리티에 로그인 URL을 /user/login으로 설정했으므로
    // User 컨트롤러에 해당 매핑을 추가
    // login_form.html 템플릿을 렌더링하는 GET 방식의 login 메서드를 추가
    // 실제 로그인을 진행하는 @PostMapping 방식의 메서드는 스프링 시큐리티가 대신 처리하므로 직접 구현할 필요 X


}
// /user/signup URL이 GET으로 요청되면 회원 가입을 위한 템플릿을 렌더링하고
// POST로 요청되면 회원가입을 진행
// 회원 가입시 비밀번호1과 비밀번호2가 동일한지를 검증
// 만약 2개의 값이 일치하지 않을 경우에는 bindingResult.rejectValue를 사용하여 오류가 발생
// bindingResult.rejectValue의 각 파라미터는 bindingResult.rejectValue(필드명, 오류코드, 에러메시지)를 의미하며 여기서 오류코드는 일단 "passwordInCorrect"로 정의

