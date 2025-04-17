package com.github.lzpdark.queuewithlocalmessage.mapper;

import com.github.lzpdark.queuewithlocalmessage.model.Incident;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

/**
 * @author lzp
 */
@Mapper
public interface IncidentMapper {

    @Insert("""
        insert into incident (title, from_user, to_user, content)
        values (#{title}, #{fromUser}, #{toUser}, #{content})
    """)
    @Options(keyProperty = "id", useGeneratedKeys = true)
    int addIncident(Incident incident);

}
