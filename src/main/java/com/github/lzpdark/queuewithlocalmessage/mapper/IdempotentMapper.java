package com.github.lzpdark.queuewithlocalmessage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * @author lzp
 */
@Mapper
public interface IdempotentMapper {
    @Insert("insert ignore into deduplicate_tbl (k) values (#{key})")
    int addRecord(String key);

    @Delete("delete from deduplicate_tbl where update_at < #{date}")
    int deleteRecordsBefore(Date date);
}
