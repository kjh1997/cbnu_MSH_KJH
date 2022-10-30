package com.cbnu.android.server.appserver.account;
import com.cbnu.android.server.appserver.content.Content;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @JsonIgnore
    private String account;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private int userAutoLogin;
    @JsonIgnore
    private String nickname;
    @JsonIgnore
    @OneToMany(mappedBy = "writeridx")
    private List<Content> content;


    public Account() {
        this.userAutoLogin = 0;
    }




}
