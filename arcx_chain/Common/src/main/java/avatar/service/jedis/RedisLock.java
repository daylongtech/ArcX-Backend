package avatar.service.jedis;

import avatar.util.GameData;
import avatar.util.redis.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

public class RedisLock {

    private RedisTemplate redisTemplate;
    /**

     */
    private static final int DEFAULT_ACQUIRY_RETRY_MILLIS = 100;
    /**

     */
    private static final String LOCK_SUFFIX = "_redis_lock";
    /**

     */
    private String lockKey;
    /**

     */
//    private int expireMsecs = 60 * 1000;
    private int expireMsecs = 1000;
    /**

     */
    private int timeoutMsecs = 10 * 1000;
    /**

     */
    private volatile boolean locked = false;

    /**

     * @return
     */
    public static RedisTemplate loadCache(){
        Redis redis = (Redis) GameData.getCache();
        RedisTemplate template = redis.getRedisTemplate();
        return template;
    }

    /**

     * @param redisTemplate

     */
    public RedisLock(RedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey + LOCK_SUFFIX;
    }

    /**

     * @param redisTemplate


     */
    public RedisLock(RedisTemplate redisTemplate, String lockKey, int timeoutMsecs) {
        this(redisTemplate, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    /**

     * @param redisTemplate



     */
    public RedisLock(RedisTemplate redisTemplate, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redisTemplate, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    public String getLockKey() {
        return lockKey;
    }

    /**

     * @param key
     * @return
     */
    private String get(final String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        return obj != null ? obj.toString() : null;
    }

    /**

     * @param key
     * @param value
     * @return
     */
    private boolean setNX(final String key, final String value) {
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    /**

     * @param key
     * @param value
     * @return
     */
    private String getSet(final String key, final String value) {
        Object obj = redisTemplate.opsForValue().getAndSet(key,value);
        return obj != null ? (String) obj : null;
    }

    /**


     * @throws InterruptedException
     */
    public boolean lock() throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); 
            if (this.setNX(lockKey, expiresStr)) {
                locked = true;
                return true;
            }
            
            String currentValue = this.get(lockKey);
            
            if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
                
                String oldValue = this.getSet(lockKey, expiresStr);
                
                if (oldValue != null && oldValue.equals(currentValue)) {
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RETRY_MILLIS;
            
            Thread.sleep(DEFAULT_ACQUIRY_RETRY_MILLIS);
        }
        return false;
    }
    /**

     */
    public void unlock() {
        if (locked) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
    }

}