DROP SCHEMA DNAUTHORIZATION CASCADE;
-- СОЗДАНИЕ СХЕМЫ
CREATE SCHEMA IF NOT EXISTS DNAUTHORIZATION;

-- ТАБЛИЦА DNAUTHORIZATION.USERS_ACCOUNT
-- Создание схемы
CREATE SCHEMA IF NOT EXISTS dnauthorization;

-- Таблица dnauthorization.users_account
CREATE TABLE IF NOT EXISTS DNAUTHORIZATION.USERS_ACCOUNT
(
    ACCOUNT_ID                      UUID      DEFAULT GEN_RANDOM_UUID() PRIMARY KEY,
    ACCOUNT_NAME                    TEXT                                NOT NULL UNIQUE,
    ACCOUNT_MAIL                    TEXT                                NOT NULL UNIQUE,
    ACCOUNT_PASSWORD_VERIFIER       TEXT                                NOT NULL,
    ACCOUNT_SALT                    TEXT                                NOT NULL,
    ACCOUNT_LAST_PASSWORD_UPDATE    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACCOUNT_PASSWORD_RESET_INTERVAL INTEGER   DEFAULT 0                 NOT NULL
);

-- Добавление комментариев к таблице dnauthorization.users_account и её столбцам
COMMENT ON TABLE dnauthorization.users_account IS 'Таблица для хранения информации о учетных записях';
COMMENT ON COLUMN dnauthorization.users_account.account_id IS 'Уникальный идентификатор учетной записи';
COMMENT ON COLUMN dnauthorization.users_account.account_name IS 'Имя учетной записи пользователя';
COMMENT ON COLUMN dnauthorization.users_account.account_mail IS 'Электронная почта учетной записи пользователя';
COMMENT ON COLUMN dnauthorization.users_account.account_password_verifier IS 'Верификатор пароля учетной записи';
COMMENT ON COLUMN dnauthorization.users_account.account_salt IS 'Случайная строка, используемая для хеширования пароля';
COMMENT ON COLUMN dnauthorization.users_account.account_last_password_update IS 'Дата и время последнего обновления пароля';
COMMENT ON COLUMN dnauthorization.users_account.account_password_reset_interval IS 'Интервал сброса пароля в днях';

-- Таблица dnauthorization.users_info
CREATE TABLE IF NOT EXISTS DNAUTHORIZATION.USERS_INFO
(
    ACCOUNT_ID           UUID    NOT NULL UNIQUE REFERENCES DNAUTHORIZATION.USERS_ACCOUNT (ACCOUNT_ID),
    USER_SURNAME         TEXT    NOT NULL,
    USER_NAME            TEXT    NOT NULL,
    USER_PATRONYMIC      TEXT    NOT NULL,
    USER_DATE_OF_BIRTH   DATE    NOT NULL,
    USER_PERSONAL_NUMBER INTEGER NOT NULL,
    USER_STRUCTURE       INTEGER NOT NULL,
    USER_CURRENT_POST    INTEGER NOT NULL,
    USER_PHONE           TEXT    NOT NULL,
    USER_OFFICE          TEXT    NOT NULL
);

-- Добавление комментариев к таблице dnauthorization.users_info и её столбцам
COMMENT ON TABLE dnauthorization.users_info IS 'Таблица для хранения дополнительной информации о пользователях';
COMMENT ON COLUMN dnauthorization.users_info.account_id IS 'Идентификатор учетной записи пользователя (внешний ключ)';
COMMENT ON COLUMN dnauthorization.users_info.user_surname IS 'Фамилия пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_name IS 'Имя пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_patronymic IS 'Отчество пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_date_of_birth IS 'Дата рождения пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_personal_number IS 'Персональный номер пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_structure IS 'Идентификатор структуры или подразделения, к которому относится пользователь';
COMMENT ON COLUMN dnauthorization.users_info.user_current_post IS 'Идентификатор текущей должности пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_phone IS 'Номер телефона пользователя';
COMMENT ON COLUMN dnauthorization.users_info.user_office IS 'Офис, в котором работает пользователь';

-- Таблица dnauthorization.active_session
CREATE TABLE IF NOT EXISTS DNAUTHORIZATION.ACTIVE_SESSION
(
    SESSION_ID                 TEXT      NOT NULL UNIQUE,
    SESSION_KEY                TEXT      NOT NULL,
    SESSION_START              TIMESTAMP NOT NULL,
    SESSION_ACCOUNT_ID         UUID REFERENCES DNAUTHORIZATION.USERS_ACCOUNT (ACCOUNT_ID),
    SESSION_SERVER_PRIVATE_KEY TEXT,
    SESSION_SERVER_PUBLIC_KEY  TEXT,
    SESSION_ACCOUNT_PUBLIC_KEY TEXT,
    SESSION_SCRAMBLER          TEXT,
    SESSION_AUTHORIZATION_KEY  TEXT
);

-- Добавление комментариев к таблице dnauthorization.active_session и её столбцам
comment on table dnauthorization.active_session is 'таблица для отслеживания активных сеансов пользователей';
comment on column dnauthorization.active_session.session_id is 'уникальный идентификатор сеанса';
comment on column dnauthorization.active_session.session_key is 'ключ сеанса для аутентификации';
comment on column dnauthorization.active_session.session_start is 'дата и время начала сеанса';
comment on column dnauthorization.active_session.session_account_id is 'идентификатор учетной записи, связанной с сеансом (внешний ключ)';
comment on column dnauthorization.active_session.session_server_private_key is 'закрытый ключ сервера, связанный с сеансом';
comment on column dnauthorization.active_session.session_server_public_key is 'публичный ключ сервера, связанный с сеансом';
comment on column dnauthorization.active_session.session_account_public_key is 'публичный ключ учетной записи, связанный с сеансом';
comment on column dnauthorization.active_session.session_scrambler is 'значение для шифрования и смешивания данных сеанса';
comment on column dnauthorization.active_session.session_authorization_key is 'ключ авторизации для доступа к ресурсам сеанса';



INSERT INTO DNAUTHORIZATION.USERS_ACCOUNT(ACCOUNT_NAME, ACCOUNT_MAIL, ACCOUNT_PASSWORD_VERIFIER, ACCOUNT_SALT)
VALUES ('TEST', 'TEST', 'TEST', 'TEST');
INSERT INTO DNAUTHORIZATION.USERS_ACCOUNT(ACCOUNT_NAME, ACCOUNT_MAIL, ACCOUNT_PASSWORD_VERIFIER, ACCOUNT_SALT)
VALUES ('TEST2', 'TEST2', 'TEST2', 'TEST');
INSERT INTO DNAUTHORIZATION.USERS_ACCOUNT(ACCOUNT_NAME, ACCOUNT_MAIL, ACCOUNT_PASSWORD_VERIFIER, ACCOUNT_SALT)
VALUES ('TEST3', 'TEST3', 'TEST3', 'TEST3');
INSERT INTO DNAUTHORIZATION.USERS_ACCOUNT(ACCOUNT_ID, ACCOUNT_NAME, ACCOUNT_MAIL, ACCOUNT_PASSWORD_VERIFIER,
                                          ACCOUNT_SALT)
VALUES ('39f311a4-837e-4fff-b6bf-55cf2a7b04ad'::uuid, 'TEST4', 'TEST4', 'TEST4', 'TEST4');