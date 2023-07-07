package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username);
}
// 사용자를 조회하는 기능이 필요하므로 findByusername 메서드를 User 리포지터리에 추가