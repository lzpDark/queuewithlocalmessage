
CREATE TABLE incident (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    from_user VARCHAR(100),
    to_user VARCHAR(100),
    content VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'incident业务表' charset = utf8mb4;

CREATE TABLE local_message (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    key_id VARCHAR(50),
    message TEXT,
    status SMALLINT DEFAULT 0 NOT NULL COMMENT '消息状态',
    error_count INT UNSIGNED DEFAULT 0 NOT NULL COMMENT '错误次数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (key_id)
) COMMENT '记录本地消息' charset = utf8mb4;
