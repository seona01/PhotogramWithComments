package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 상속받으면 @없어도 IoC 자동 등록
public interface UserRepository extends JpaRepository<User, Integer> {

    // JPA query creation : method name 사용
    User findByUsername(String username);   // 찾은 후 user object return
}
