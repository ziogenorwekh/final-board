package store.shportfolio.board.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomCacheManager {

    private final CacheManager cacheManager;

    @Autowired
    public CustomCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void save(String email, String code) {
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache != null) {
            cache.evict(email); // 기존 캐시 제거
            cache.put(email, code); // 새 값 저장
            log.info("Saved code for email: {}, code: {}", email, code);
        } else {
            log.error("Cache 'verificationCodes' not found");
        }
    }

    public String getCodeWithEmail(String email) {
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache != null) {
            String code = cache.get(email, String.class);
            log.info("Retrieved code for email: {}, code: {}", email, code);
            return code;
        } else {
            log.error("Cache 'verificationCodes' not found");
            return null;
        }
    }

    public String getVerifiedEmailWithSession(String sessionId) {
        log.info("getVerifiedEmailWithSession sessionId: {}", sessionId);
        Cache cache = cacheManager.getCache("verificationCodes");
        return cache.get(sessionId, String.class);
    }

    public void deleteCode(String email) {
        Cache cache = cacheManager.getCache("verificationCodes");
        if (cache != null) {
            cache.evict(email);
            log.info("Deleted code for email: {}", email);
        } else {
            log.error("Cache 'verificationCodes' not found");
        }
    }
}
