CREATE TABLE incident
(
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(100),
    from_user  VARCHAR(100),
    to_user    VARCHAR(100),
    content    VARCHAR(255),
    created_at DATETIME  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT 'incident业务表' charset = utf8mb4;

CREATE TABLE local_message
(
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    key_id      VARCHAR(50),
    message     TEXT,
    status      SMALLINT     DEFAULT 0 NOT NULL COMMENT '消息状态: 0待处理 1有问题',
    error_count INT UNSIGNED DEFAULT 0 NOT NULL COMMENT '错误次数',
    created_at  DATETIME               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (key_id)
) COMMENT '记录本地消息' charset = utf8mb4;

CREATE TABLE deduplicate_tbl
(
    id        BIGINT AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    k         VARCHAR(100)                        NOT NULL COMMENT '去重key(长度请根据自己的幂等检查函数调整)',
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY(k)
) COMMENT '幂等去重' charset = utf8mb4;
