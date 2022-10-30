package com.cbnu.android.server.appserver.account;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserDTO {
    private String user_id;
    private String user_pw;
    private String user_nick_name;
    private int check_auto_login;
}
