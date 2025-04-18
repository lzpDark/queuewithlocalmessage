package com.github.lzpdark.queuewithlocalmessage.consumer;

import com.github.lzpdark.queuewithlocalmessage.mapper.IdempotentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 删除过期的幂等处理记录
 * 只需要删除，比较简单
 * 也可以通过数据库的脚本定期执行来处理
 * @author lzp
 */
@Component
@Slf4j
public class IdempotencyRecordCleaner {

    @Autowired
    IdempotentMapper idempotentMapper;

    private static final long fixedRate = 1_800_000;

    // 30min: 30 * 60 * 1000
    @Scheduled(fixedRate = 1_800_000)
    public void clean() {
        // 计算删除的上限时间
        long millis = System.currentTimeMillis() - fixedRate;
        // 删掉幂等记录
        log.info("delete {} idempotent records.",
                idempotentMapper.deleteRecordsBefore(new Date(millis)));
    }
}
