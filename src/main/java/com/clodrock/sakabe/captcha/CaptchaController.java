package com.clodrock.sakabe.captcha;

import cn.apiclub.captcha.Captcha;
import com.clodrock.sakabe.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/generate")
    public ResponseEntity<CaptchaResponse> generateCaptcha() {
        return ResponseEntity.ok().body(captchaService.generateCaptcha());
    }

    @PostMapping("/check")
    public ResponseEntity<CheckCaptchaResponse> generateCaptcha(@RequestBody CheckCaptchaRequest request) {
        return ResponseEntity.ok().body(captchaService.checkCaptcha(request));
    }

    @Service
    @RequiredArgsConstructor
    public static class CaptchaService {
        private final CaptchaRepository captchaRepository;

        public CaptchaResponse generateCaptcha() {
            CaptchaGenerator captchaGenerator = new CaptchaGenerator();
            Captcha generated = captchaGenerator.createCaptcha(200, 50);

            com.clodrock.sakabe.entity.Captcha saved = captchaRepository.save(com.clodrock.sakabe.entity.Captcha.builder()
                    .code(generated.getAnswer()).build());

            String captcha = CaptchaUtils.encodeBase64(generated);
            return new CaptchaResponse(saved.getId(), captcha);
        }

        public CheckCaptchaResponse checkCaptcha(CheckCaptchaRequest request) {
            Optional<com.clodrock.sakabe.entity.Captcha> captcha = captchaRepository.findByIdAndCode(request.id(), request.answer());
            return new CheckCaptchaResponse(captcha.isPresent());
        }
    }
}
