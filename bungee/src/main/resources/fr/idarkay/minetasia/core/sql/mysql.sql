CREATE TABLE if NOT EXISTS `online_player` (
    `uuid`      varchar(36)     NOT NULL,
    `username`  varchar(16)     NOT NULL,
    `proxy`     varchar(36)     NOT NULL,
    `server`    varchar(64)     NOT NULL,
    PRIMARY KEY (`uuid`)
) ENGINE = MEMORY DEFAULT CHARSET = utf8mb4;