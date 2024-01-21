-- Создание схемы
CREATE SCHEMA IF NOT EXISTS dnauthorization;

-- Таблица dnauthorization.users_account
CREATE TABLE IF NOT EXISTS dnauthorization.users_account
(
    account_id                      uuid PRIMARY KEY default gen_random_uuid(),
    account_name                    text                                       NOT NULL UNIQUE,
    account_mail                    text                                       NOT NULL UNIQUE,
    account_password_verifier       text                                       NOT NULL,
    account_salt                    text                                       NOT NULL,
    account_last_password_update    timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    account_password_reset_interval integer          DEFAULT 90                NOT NULL
);


