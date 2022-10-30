package com.cbnu.android.server.appserver.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AccountService {
    private final UserRepository userRepositroy;

    public Boolean findByAccount(String user_id) {
        if (userRepositroy.existsByAccount(user_id) == false) {
            return false;
        } else {
            return true;
        }
    }

    public boolean findByNickName(String user_nick_name) {
        if (userRepositroy.existsByNickname(user_nick_name) == false) {
            return false;
        } else {
            return true;
        }
    }

    public void save(UserDTO userDTO) {
        Account user = new Account();
        user.setAccount(userDTO.getUser_id());
        user.setPassword(userDTO.getUser_pw());
        user.setNickname(userDTO.getUser_nick_name());
        userRepositroy.save(user);
    }

    public String login(UserDTO userDTO) {
        Account user = userRepositroy.findByAccount(userDTO.getUser_id());
         if (user == null) {
            System.out.println("null");
            return "FAIL";
        }
        if (user.getAccount().equals(userDTO.getUser_id()) == false) {
            System.out.println("?");
            return "FAIL";
        }
        if (user.getPassword().equals(userDTO.getUser_pw())== false) {
            System.out.println("??");
            return "FAIL";
        }
        user.setUserAutoLogin(userDTO.getCheck_auto_login());
        userRepositroy.save(user);
        return Long.toString(user.getId());
    }

    public int autoLogin(String login_user_idx) {

        return userRepositroy.findById(Integer.parseInt(login_user_idx)).getUserAutoLogin();
    }
}
