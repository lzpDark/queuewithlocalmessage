package com.github.lzpdark.queuewithlocalmessage.mapper;

import com.github.lzpdark.queuewithlocalmessage.model.LocalMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lzp
 */
@Mapper
public interface LocalMessageMapper {
    @Insert("insert into local_message (key_id, message) values (#{keyId}, #{message})")
    @Options(keyProperty = "id", useGeneratedKeys = true)
    int addLocalMessage(LocalMessage localMessage);

    @Select("select * from local_message where status = 0 order by id limit 100")
    @Results({
            @Result(property = "keyId", column = "key_id"),
            @Result(property = "errorCount", column = "error_count"),
            @Result(property = "status", column = "status"),
            @Result(property = "created_at", column = "created_at"),
    })
    List<LocalMessage> selectToSendMessages();

    @Delete("delete from local_message where key_id = #{keyId}")
    void delete(String keyId);

    @Update("update local_message set status = 1 where key_id = #{keyId}")
    int failed(String keyId);

    @Update("update local_message set error_count = error_count + 1 where key_id = #{keyId}")
    int incrementErrorCount(String keyId);
}
