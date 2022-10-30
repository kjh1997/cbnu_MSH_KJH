package com.cbnu.android.server.appserver.board;

import com.cbnu.android.server.appserver.account.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository  extends JpaRepository<Board, Long> {
}
