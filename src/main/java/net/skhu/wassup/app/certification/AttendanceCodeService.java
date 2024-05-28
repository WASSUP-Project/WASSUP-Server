package net.skhu.wassup.app.certification;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttendanceCodeService {

    private final ExpiringMap<String, Long> attendanceCode = ExpiringMap.builder()
            .variableExpiration()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expirationListener((key, value) -> log.info("출석 코드 만료 : {}", key))
            .build();

    private long getExpirationTime() {
        long currentTimeMillis = System.currentTimeMillis();
        long midnightMillis = currentTimeMillis + (24 * 60 * 60 * 1000)
                - (currentTimeMillis % (24 * 60 * 60 * 1000));

        return midnightMillis - currentTimeMillis;
    }

    public String createCode(Long groupId) {
        String code = generateUniqueCode();
        long expirationTime = getExpirationTime();
        attendanceCode.put(code, groupId, expirationTime, TimeUnit.MILLISECONDS);

        return code;
    }

    public Long findGroupIdByCode(String code) {
        return attendanceCode.get(code);
    }

    public String generateUniqueCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int random = secureRandom.nextInt(3);

            if (random == 0) {
                char randomChar = (char) (secureRandom.nextInt(26) + 'A');
                codeBuilder.append(randomChar);
                continue;
            }
            if (random == 1) {
                char randomChar = (char) (secureRandom.nextInt(26) + 'a');
                codeBuilder.append(randomChar);
                continue;
            }
            int randomNumber = secureRandom.nextInt(10);
            codeBuilder.append(randomNumber);
        }

        return codeBuilder.toString();
    }

}
