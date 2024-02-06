package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.SakaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<SakaUser, UUID> {

    Optional<SakaUser> findByEmail(String email);

    List<SakaUser> findByEmailIn(List<String> emailList);
}
