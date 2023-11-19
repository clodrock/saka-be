package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.SakaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SakaUser, Integer> {

    Optional<SakaUser> findByEmail(String email);

    List<SakaUser> findByEmailIn(List<String> emailList);
}
