CREATE TABLE if NOT EXISTS `uuid_username` (
    `uuid`      varchar(36)     NOT NULL,
    `username`  varchar(16)     NOT NULL,
    PRIMARY KEY (`uuid`)
) DEFAULT CHARSET = utf8mb4;
CREATE TABLE if NOT EXISTS `online_server` (
   `id`      varchar(64)        NOT NULL,
   `player_count`  INT          NOT NULL,
   `type`     varchar(32)       NOT NULL,
   `map_id`    BIT              NOT NULL,
   `statue`    BIT               NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE = MEMORY DEFAULT CHARSET = utf8mb4;