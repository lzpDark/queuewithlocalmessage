package com.github.lzpdark.queuewithlocalmessage.model;

import lombok.Data;

import java.util.Date;

/**
 * @author lzp
 */
@Data
public class LocalMessage {
    private Integer id;
    private String keyId;
    private String message;
    private Integer status;
    private Integer errorCount;
    private Date created_at;

    public LocalMessage(String keyId, String message) {
        this.keyId = keyId;
        this.message = message;
    }
}
