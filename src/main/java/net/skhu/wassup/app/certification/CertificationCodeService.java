package net.skhu.wassup.app.certification;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CertificationCodeService {

    @Cacheable(cacheNames = "certificationCode", value = "certificationCode", key = "#id")
    public String getCertificationCode(String id) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        log.info("인증 코드 생성: ID={}, Code={}", id, code);
        return code;
    }

    @CacheEvict(cacheNames = "certificationCode", value = "certificationCode", key = "#id")
    public void deleteCertificationCode(String id) {
        log.info("인증 코드 삭제: ID={}", id);
    }

}
