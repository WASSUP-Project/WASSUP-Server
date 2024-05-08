package net.skhu.wassup.app.certification;

import static net.skhu.wassup.global.error.ErrorCode.DUPLICATE_GROUP_CODE;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.skhu.wassup.app.group.domain.GroupRepository;
import net.skhu.wassup.global.error.exception.CustomException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupUniqueCodeService {

    private final GroupRepository groupRepository;

    public String getGroupUniqueCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int random = secureRandom.nextInt(2);

            if (random == 0) {
                char randomChar = (char) (secureRandom.nextInt(26) + 'A');
                codeBuilder.append(randomChar);
                continue;
            }
            int randomNumber = secureRandom.nextInt(10);
            codeBuilder.append(randomNumber);
        }

        String code = codeBuilder.toString();

        if (isDuplicateCode(code)) {
            throw new CustomException(DUPLICATE_GROUP_CODE);
        }

        log.info("그룹 고유 코드 생성: Code={}", code);
        return code;
    }

    private boolean isDuplicateCode(String code) {
        return groupRepository.existsGroupByUniqueCode(code);
    }

}
