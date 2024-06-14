package net.skhu.wassup.global.message;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SMSMessageSender implements MessageSender {

    @Value("${coolsms.api.senderNumber}")
    private String senderNumber;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Override
    public void send(String to, String title, String message) {
        Message msg = new Message();
        msg.setFrom(senderNumber);
        msg.setTo(to);
        msg.setText("[" + title + "]\n\n" + message);

        messageService.sendOne(new SingleMessageSendingRequest(msg));
        log.info("SMS Send to : {} Message : {} {}", to, title, message);
    }

}
