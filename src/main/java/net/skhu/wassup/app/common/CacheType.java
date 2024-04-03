package net.skhu.wassup.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {

    AUTH_CODE("certificationCode", 60 * 60 * 24, 1000);

    private final String cacheName;

    private final int expireAfterWrite;

    private final int maximumSize;

}