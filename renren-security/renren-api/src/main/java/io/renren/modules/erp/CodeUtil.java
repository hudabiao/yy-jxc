package io.renren.modules.erp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * @author Administrator
 */
@Component
public class CodeUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String getCode() {
        NumberFormat numberFormat = new DecimalFormat("000000");
        RedisAtomicLong entityIdCounter = new RedisAtomicLong("GOODS:BARCODE:OFFSET", Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long increment = entityIdCounter.getAndIncrement();
        return numberFormat.format(increment);
    }
}
