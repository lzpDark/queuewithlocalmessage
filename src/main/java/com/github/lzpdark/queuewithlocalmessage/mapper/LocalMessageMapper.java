package com.github.lzpdark.queuewithlocalmessage.mapper;

import com.github.lzpdark.queuewithlocalmessage.model.LocalMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author lzp
 */
@Mapper
public interface LocalMessageMapper {
    @Insert("insert into local_message (key_id, message) values (#{keyId}, #{message})")
    @Options(keyProperty = "id", useGeneratedKeys = true)
    int addLocalMessage(LocalMessage localMessage);

    @Delete("delete from local_message where key_id = #{key}")
    void delete(String key);
}
