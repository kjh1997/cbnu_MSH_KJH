package com.cbnu.android.server.appserver.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
     Account findById(int id);
     boolean existsByAccount(String user_id);

     boolean existsByNickname(String user_nick_name);


     Account findByAccount(String account);
}
