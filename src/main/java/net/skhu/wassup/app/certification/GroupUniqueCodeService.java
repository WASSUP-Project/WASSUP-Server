package net.skhu.wassup.app.certification;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import net.skhu.wassup.app.group.domain.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupUniqueCodeService {

    private static final Logger logger = LoggerFactory.getLogger(GroupUniqueCodeService.class);

    private final GroupRepository groupRepository;

    public String getGroupUniqueCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int random = secureRandom.nextInt(2);
            if (random == 0) {
                char randomChar = (char) (secureRandom.nextInt(26) + 'A');
                codeBuilder.append(randomChar);
            } else {
                int randomNumber = secureRandom.nextInt(10);
                codeBuilder.append(randomNumber);
            }
        }
        String code = codeBuilder.toString();
        isDuplicateCode(code);
        logger.info("그룹 고유 코드 생성: Code={}", code);
        return code;
    }

    private void isDuplicateCode(String code) {
        if (groupRepository.existsGroupByUniqueCode(code)) {
            logger.error("그룹 고유 코드 중복: Code={}", code);
        }
    }

}
