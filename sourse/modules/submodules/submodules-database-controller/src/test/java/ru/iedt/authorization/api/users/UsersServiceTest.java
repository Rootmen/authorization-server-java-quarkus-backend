package ru.iedt.database.request.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.iedt.authorization.api.users.UsersModel;
import ru.iedt.authorization.api.users.dto.UserAccount;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class UsersServiceTest {
    @Inject
    UsersModel usersModel;

    @Inject
    PgPool client;

    @BeforeAll
    void setup() throws IOException {
        String init = readFromInputStream(UsersServiceTest.class.getResourceAsStream("/init.sql"));
        client.query(init).execute().await().indefinitely();
    }

    @Test
    void testLoadAllUsers() {
        int userCount = 4;
        System.out.println("Старт теста testLoadAllUsers");
        System.out.println("Проверка загрузки информации со всех аккаунтов");
        List<UserAccount> userAccounts =
                usersModel.getAllUserAccount(client).collect().asList().await().indefinitely();
        Assertions.assertEquals(userAccounts.size(), userCount, "UserAccount загрузил не верное количество записей");
        System.out.printf("Загружено %s из %s записей\n", userAccounts.size(), userCount);
        System.out.println("Тест пройден");
    }

    @Test
    void testLoadUsersFromUUID() {
        System.out.println("Старт теста testLoadUsersFromUUID");
        System.out.println("Проверка поиска информации по UUID");
        UUID uuidTest = UUID.fromString("39f311a4-837e-4fff-b6bf-55cf2a7b04ad");
        UserAccount userAccountUUIDTest =
                usersModel.getUserAccount(uuidTest, client).await().indefinitely();
        Assertions.assertEquals(userAccountUUIDTest.getAccountId(), uuidTest, "Поиск по UUID не сработал");
        System.out.printf("UUID = %s искался %s\n", userAccountUUIDTest.getAccountId(), uuidTest);
        System.out.println("Тест пройден");
    }

    @Test
    void testLoadUsersFromName() {
        System.out.println("Старт теста testLoadUsersFromName");
        System.out.println("Проверка поиска информации по username");
        UserAccount userAccountUUIDTest =
                usersModel.getUserAccount("TEST", client).await().indefinitely();
        Assertions.assertEquals(userAccountUUIDTest.getAccountName(), "TEST", "Поиск по UUID не сработал");
        System.out.printf("username = %s искался %s\n", userAccountUUIDTest.getAccountName(), "TEST");
        System.out.println("Тест пройден");
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
