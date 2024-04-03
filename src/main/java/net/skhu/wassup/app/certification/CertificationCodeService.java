package net.skhu.wassup.app.certification;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CertificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(CertificationCodeService.class);

    @Cacheable(cacheNames = "certificationCode", value = "certificationCode", key = "#id")
    public String getCertificationCode(String id) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        logger.info("인증 코드 생성: ID={}, Code={}", id, code);
        return code;
    }

    @CacheEvict(cacheNames = "certificationCode", value = "certificationCode", key = "#id")
    public void deleteCertificationCode(String id) {
        logger.info("인증 코드 삭제: ID={}", id);
    }

}
