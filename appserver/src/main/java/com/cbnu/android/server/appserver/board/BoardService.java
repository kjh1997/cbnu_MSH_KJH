package com.cbnu.android.server.appserver.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;


    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }
}
