package net.skhu.wassup.global.message;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailMessageSender implements MessageSender {

    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String title, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject("[Wassup 인증 메일]");
        mailMessage.setText("인증 번호 : " + message);

        try {
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new IllegalArgumentException("이메일 전송에 실패했습니다.");
        }

        mailMessage.getText();
    }
}
