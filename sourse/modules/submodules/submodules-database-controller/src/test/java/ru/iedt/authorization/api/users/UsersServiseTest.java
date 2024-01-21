package ru.iedt.authorization.api.users;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserAccountTest {
    @Inject
    UsersModel usersModel;

    @Inject
    PgPool client;

    @Test
    void testLoadAllUsers() {
        client.query("DROP TABLE IF EXISTS fruits")
                .execute()
                .flatMap(r -> client.query("CREATE SCHEMA IF NOT EXISTS dnauthorization;")
                        .execute())
                .flatMap(r -> client.query(
                                "CREATE TABLE IF NOT EXISTS dnauthorization.users_account\n" + "                (\n"
                                        + "                        account_id                      uuid PRIMARY KEY default gen_random_uuid(),\n"
                                        + "                account_name                    text                                       NOT NULL UNIQUE,\n"
                                        + "        account_mail                    text                                       NOT NULL UNIQUE,\n"
                                        + "                account_password_verifier       text                                       NOT NULL,\n"
                                        + "                account_salt                    text                                       NOT NULL,\n"
                                        + "                account_last_password_update    timestamp        DEFAULT CURRENT_TIMESTAMP NOT NULL,\n"
                                        + "                account_password_reset_interval integer          DEFAULT 90                NOT NULL\n"
                                        + ");")
                        .execute())
                .flatMap(r -> client.query("Insert Into dnauthorization.users_account(account_name ,account_mail,account_password_verifier,account_salt) values ('test', 'test', 'test','test') ;")
                        .execute())
                .flatMap(r -> client.query("Insert Into dnauthorization.users_account(account_name ,account_mail,account_password_verifier,account_salt) values ('test2', 'test2', 'test2','test2') ;")
                        .execute())
                .flatMap(r -> client.query("Insert Into dnauthorization.users_account(account_name ,account_mail,account_password_verifier,account_salt) values ('test3', 'test3', 'test3','test3') ;")
                        .execute())
                .await()
                .indefinitely();

        System.out.println(usersModel.getAllUserAccount(client).collect().asList().await().indefinitely());
    }
}