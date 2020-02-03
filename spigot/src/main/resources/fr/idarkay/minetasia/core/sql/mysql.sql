CREATE TABLE if NOT EXISTS `uuid_username` (
    `uuid`      varchar(36)     NOT NULL,
    `username`  varchar(16)     NOT NULL,
    PRIMARY KEY (`uuid`)
) DEFAULT CHARSET = utf8mb4;
# CREATE TABLE if NOT EXISTS `online_server` (
#    `id`             varchar(64)     NOT NULL,
#    'create_time'    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
#    `ip`             varchar(64)     NOT NULL,
#    `port`           INT             NOT NULL,
#    `type`           varchar(28)     NOT NULL,
#    `config_name`    varchar(32)     NOT NULL,
#    `player_count`   INT             NOT NULL DEFAULT 0,
#    `statue`         BIT             NOT NULL DEFAULT 0,
#    PRIMARY KEY (`id`)
# ) ENGINE = MEMORY DEFAULT CHARSET = utf8mb4;