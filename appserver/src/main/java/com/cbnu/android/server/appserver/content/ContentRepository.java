package com.cbnu.android.server.appserver.content;

import com.cbnu.android.server.appserver.account.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository  extends JpaRepository<Content, Integer> {
    Content findById(int id);


}
