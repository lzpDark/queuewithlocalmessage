package com.github.lzpdark.queuewithlocalmessage.model;

import lombok.Data;

import java.util.Date;

/**
 * @author lzp
 */
@Data
public class Incident {
    private Integer id;
    private String title;
    private String content;
    private String fromUser;
    private String toUser;
    private Date created_at;
    private Date updated_at;

    public Incident(String title, String content, String fromUser, String toUser) {
        this.title = title;
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
