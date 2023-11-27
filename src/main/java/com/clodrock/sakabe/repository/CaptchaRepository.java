package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<Captcha, Long> {
    Optional<Captcha> findByIdAndCode(Long id, String answer);
}
