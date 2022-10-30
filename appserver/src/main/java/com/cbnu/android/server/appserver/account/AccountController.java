package com.cbnu.android.server.appserver.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("CommunityServer/check_auto_login.jsp")
    public String checkAutoLogin(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getParameter("login_user_idx"));
        return Integer.toString(accountService.autoLogin(request.getParameter("login_user_idx")));
    }
    @PostMapping("CommunityServer/join_user.jsp")
    public String joinUser(HttpServletRequest request, HttpServletResponse response) {
        if (accountService.findByAccount(request.getParameter("user_id")) == true || accountService.findByNickName(request.getParameter("user_nick_name")) == true) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "fail";
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        UserDTO userDTO = UserDTO.builder().user_id(request.getParameter("user_id"))
                .user_pw(request.getParameter("user_pw"))
                .user_nick_name(request.getParameter("user_nick_name")).build();
        accountService.save(userDTO);
        return "Sccuess";

    }

    @PostMapping("/CommunityServer/login_user.jsp")
    public String loginUser(HttpServletRequest request, HttpServletResponse response) {
        UserDTO userDTO = UserDTO.builder()
                .user_id(request.getParameter("user_id"))
                .user_pw(request.getParameter("user_pw"))
                .check_auto_login(Integer.parseInt(request.getParameter("user_autologin"))).build();
        System.out.println(userDTO.toString());
        String result = accountService.login(userDTO);
        if (result == "FAIL") {
            return "Fail";
        }
        System.out.println("success");
        return result;
    }
}
