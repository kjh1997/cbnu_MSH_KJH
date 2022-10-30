package com.cbnu.android.server.appserver.content;

import com.cbnu.android.server.appserver.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int boardidx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account writeridx;
    @JsonIgnore
    private String subject;
    @JsonIgnore
    private LocalDateTime writeDate;
    @JsonIgnore
    private String text;
    @JsonIgnore
    private String image;

    public Content() {
        LocalDateTime now = LocalDateTime.now();
        this.writeDate = now;
    }

}
