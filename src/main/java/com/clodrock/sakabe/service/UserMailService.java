package com.clodrock.sakabe.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserMailService {
    private final MailService mailService;

    public boolean sendActivationMail(String email, String activationCode) {
        String content = "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background-color: #f9f9f9;\n" +
                "            color: #333;\n" +
                "            text-align: center;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            color: #3498db;\n" +
                "        }\n" +
                "\n" +
                "        strong {\n" +
                "            color: #e74c3c;\n" +
                "        }\n" +
                "\n" +
                "        a {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #2ecc71;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Saka'ya Hoşgeldiniz,</p>\n\n" +
                "    <p>Hesabınızı aktive etmek için aşağıdaki onay kodunu kullanabilirsiniz:</p>\n" +
                "    <p style=\"font-size: 24px; color: #e74c3c;\"><strong>"+ activationCode +"</strong></p>\n" +
                "</body>\n" +
                "</html>";
        String subject = "Saka Kullanıcı Aktivasyon";

        try {
            mailService.sendMail(email, subject, content);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
