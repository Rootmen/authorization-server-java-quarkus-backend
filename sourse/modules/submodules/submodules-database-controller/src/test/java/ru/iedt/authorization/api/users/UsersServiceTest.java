package ru.iedt.authorization.api.users;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import ru.iedt.authorization.api.users.dto.UserAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class UsersServiceTest {
    @Inject
    UsersModel usersModel;

    @Inject
    PgPool client;

    void setup() throws IOException {
        String init = readFromInputStream(UsersServiceTest.class.getResourceAsStream("/init.sql"));
        client.query(init).execute().await().indefinitely();
    }

    @RepeatedTest(55)
    void testAddUserAccount() throws IOException {
        this.setup();
        System.out.println("Старт теста testAddUserAccount");
        System.out.println("Проверка добавления нового пользователя");
        String username = getRandomString(10);
        String userMail = getRandomString(10);
        String passwordVerifier = getRandomString(25);
        String salt = getRandomString(5);

        UserAccount userAccountInsert = usersModel
                .addUserAccount(username, userMail, passwordVerifier, salt, client)
                .await()
                .indefinitely();
        
        UserAccount userAccountName =
                usersModel.getUserAccount(username, client).await().indefinitely();

        UserAccount userAccountUUID = usersModel
                .getUserAccount(userAccountInsert.getAccountId(), client)
                .await()
                .indefinitely();

        Assertions.assertEquals(userAccountInsert, userAccountName, "Вставка данных и дальнейший поиск не удался");
        Assertions.assertEquals(userAccountInsert, userAccountUUID, "Вставка данных и дальнейший поиск не удался");
        System.out.printf("Найден объект = %s\n", userAccountInsert);
        System.out.printf("Поиск по имени = %s\n", userAccountName);
        System.out.printf("Поиск по UUID = %s\n", userAccountUUID);
        System.out.println("Тест пройден");
    }

    @Test
    void testAddUserAccount2() throws IOException {
        this.setup();
        System.out.println("Старт теста testAddUserAccount");
        System.out.println("Проверка добавления нового пользователя");
        String username = getRandomString(10);
        String userMail = getRandomString(10);
        String passwordVerifier = getRandomString(25);
        String salt = getRandomString(5);

        UserAccount userAccountInsert = usersModel
                .addUserAccount(username, userMail, passwordVerifier, salt, client)
                .await()
                .indefinitely();

        List<UserAccount> userAccountNameAll =
                usersModel.getAllUserAccount(client).collect().asList().await().indefinitely();
        UserAccount userAccountName =
                usersModel.getUserAccount(username, client).await().indefinitely();

        UserAccount userAccountUUID = usersModel
                .getUserAccount(userAccountInsert.getAccountId(), client)
                .await()
                .indefinitely();
        userAccountNameAll =
                usersModel.getAllUserAccount(client).collect().asList().await().indefinitely();
        userAccountNameAll =
                usersModel.getAllUserAccount(client).collect().asList().await().indefinitely();

        Assertions.assertEquals(userAccountInsert, userAccountName, "Вставка данных и дальнейший поиск не удался");
        Assertions.assertEquals(userAccountInsert, userAccountUUID, "Вставка данных и дальнейший поиск не удался");
        System.out.printf("Найден объект = %s\n", userAccountInsert);
        System.out.printf("Поиск по имени = %s\n", userAccountName);
        System.out.printf("Поиск по UUID = %s\n", userAccountUUID);
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

    String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789”;";

    public String getRandomString(int size) {
        StringBuilder result = new StringBuilder(size);
        for (int g = 0; g < size; g++) {
            int ch = (int)(AlphaNumericStr.length() * Math.random());
            result.append(AlphaNumericStr.charAt(ch));
        }
        return result.toString();
    }
}
