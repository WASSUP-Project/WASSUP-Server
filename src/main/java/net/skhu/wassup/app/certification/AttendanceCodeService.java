package net.skhu.wassup.app.certification;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttendanceCodeService {

    private final Map<String, Long> attendanceCode = new HashMap<>();

    public String createCode(Long groupId) {
        String code = generateUniqueCode();
        attendanceCode.put(code, groupId);
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
