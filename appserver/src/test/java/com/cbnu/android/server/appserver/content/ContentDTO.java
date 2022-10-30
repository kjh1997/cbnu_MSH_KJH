package com.cbnu.android.server.appserver.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static org.junit.jupiter.api.Assertions.*;

@Data
@Builder
@AllArgsConstructor
class ContentDTO {
    private String content_subject;
    private String content_nick_name;
    private String content_write_date;
    private String content_text;
    private String content_image;
    private int content_writer_idx;
    private int content_board_idx;

}