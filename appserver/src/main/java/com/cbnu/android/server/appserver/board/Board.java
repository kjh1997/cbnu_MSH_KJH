package com.cbnu.android.server.appserver.board;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
