ALTER TABLE `user` DROP COLUMN name;
ALTER TABLE `refresh_token` RENAME COLUMN id TO pk;

CREATE TABLE identification (
    pk BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_number VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL UNIQUE,
    middle_name VARCHAR(255) NOT NULL
);

ALTER TABLE `user`
    ADD COLUMN identification_fk BIGINT UNIQUE;

ALTER TABLE `user`
    ADD CONSTRAINT fk_user_identification
        FOREIGN KEY (identification_fk)
        REFERENCES identification(pk)
        ON DELETE CASCADE;
