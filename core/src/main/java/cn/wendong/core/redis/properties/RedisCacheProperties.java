package cn.wendong.core.redis.properties;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author MB yangtdo@qq.com
 * @date 2018-12-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.shiro.redis")
public class RedisCacheProperties {

    public static final long MILLIS_PER_SECOND = 1000;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_DAY = 24 * MILLIS_PER_HOUR;


    private String keyPrefix = "shiro:cache:";

    private String sessionPrefix = "shiro:session:";

    /**
     * Time unit：millis
     */
    private Long sessionTimeOut = 30 * MILLIS_PER_MINUTE;

    /**
     * Time unit：millis
     */
    private Long sessionCacheExpire = MILLIS_DAY;

    /**
     * Time unit：millis
     */
    private Long valueCacheExpire = -1L;

    private boolean isSerializeTransient = true;

    private Map<String, String> filterChain;

    private List<Class<?>> classList;

    public Map<String, String> getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(Map<String, String> filterChain) {
        this.filterChain = filterChain;
    }
 
}
