CREATE TABLE if NOT EXISTS `uuid_username` (
    `uuid`      varchar(36)     NOT NULL,
    `username`  varchar(16)     NOT NULL,
    PRIMARY KEY (`uuid`)
) DEFAULT CHARSET = utf8mb4;