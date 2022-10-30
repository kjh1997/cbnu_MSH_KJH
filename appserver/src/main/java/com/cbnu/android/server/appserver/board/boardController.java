package com.cbnu.android.server.appserver.board;

import com.cbnu.android.server.appserver.content.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class boardController {

    private final BoardService boardService;

    @GetMapping("CommunityServer/get_board_list.jsp")
    public List<BoardDTO> getBoardList() {
        List<Board> boardList = boardService.getBoardList();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boardList) {
            BoardDTO boardDTO = BoardDTO.builder().board_idx(board.getId()).board_name(board.getName()).build();
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }


}
@Data
@Builder
@AllArgsConstructor
class BoardDTO{
    private int board_idx;
    private String board_name;
}
