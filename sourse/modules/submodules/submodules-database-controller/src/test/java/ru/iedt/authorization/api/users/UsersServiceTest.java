package ru.iedt.authorization.api.users;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import ru.iedt.authorization.api.users.dto.UserAccountModel;
import ru.iedt.authorization.api.users.dto.UserInfoModel;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTest
public class UsersServiceTest {

    @Inject
    UserAccountRepository userAccountModel;

    @Inject
    UserInfoRepository userInfoModel;

    @Inject
    PgPool client;

    @BeforeAll
    void setup() throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(UsersServiceTest.class.getResourceAsStream("/init.sql"))))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        client.query(resultStringBuilder.toString()).execute().await().indefinitely();
    }

    @Order(1)
    @RepeatedTest(250)
    void testAddUserAccount() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newVirtualThreadPerTaskExecutor();
        ArrayList<FutureTask<Void>> arrayList = new ArrayList<>();
        Callable<Void> task = () -> {
            String username = getRandomString(10);
            String userMail = getRandomString(10);
            String passwordVerifier = getRandomString(25);
            String salt = getRandomString(5);
            UserAccountModel userAccountInsert = userAccountModel.addUserAccount(username, userMail, passwordVerifier, salt, client).await().indefinitely();
            Assertions.assertNotEquals(userAccountInsert, null, "Вставка вернула null значение");
            UserAccountModel userAccountName = userAccountModel.getUserAccount(username, client).await().indefinitely();
            UserAccountModel userAccountUUID = userAccountModel.getUserAccount(userAccountInsert.getAccountId(), client).await().indefinitely();
            Assertions.assertEquals(userAccountInsert, userAccountName, "Вставка данных и дальнейший поиск не удался");
            Assertions.assertEquals(userAccountInsert, userAccountUUID, "Вставка данных и дальнейший поиск не удался");
            return null;
        };
        for (int g = 0; g < 100; g++) {
            FutureTask<Void> future = new FutureTask<>(task);
            arrayList.add(future);
        }
        for (int g = 0; g < 100; g++) {
            executor.execute(arrayList.get(g));
        }
        for (int g = 0; g < 100; g++) {
            arrayList.get(g).get();
        }
    }

    @Order(2)
    @RepeatedTest(150)
    void testUpdateUserAccount() {
        String accountName = getRandomString(10);
        String userMail = getRandomString(10);
        UserAccountModel userAccountFirst = userAccountModel.getAllUserAccount(this.client).collect().first().await().indefinitely();
        userAccountFirst.setAccountName(accountName).setAccountMail(userMail);
        userAccountModel.updateUserAccount(userAccountFirst, client).await().indefinitely();
        UserAccountModel userAccountUpdate = userAccountModel.getUserAccount(userAccountFirst.getAccountId(), client).await().indefinitely();
        Assertions.assertEquals(userAccountUpdate, userAccountFirst, "Обновление данных не удалось");
        String mailUpdate = userAccountUpdate.getAccountMail();
        String mailFirst = userAccountFirst.getAccountMail();
        String accountNameUpdate = userAccountUpdate.getAccountName();
        String accountNameFirst = userAccountFirst.getAccountName();
        Assertions.assertEquals(mailUpdate, mailFirst, "Обновление данных не удалось");
        Assertions.assertEquals(mailUpdate, userMail, "Обновление данных не удалось");
        Assertions.assertEquals(accountNameUpdate, accountNameFirst, "Обновление данных не удалось");
        Assertions.assertEquals(accountNameUpdate, accountName, "Обновление данных не удалось");
    }

    @Order(3)
    @Test
    void addUserInfo() {
        Random r = new Random();
        ArrayList<UserInfoModel> arrayList = new ArrayList<>();
        List<UserInfoModel> usersAccount = userAccountModel
            .getAllUserAccount(this.client)
            .onItem()
            .transformToUni(userAccount -> {
                String userSurname = getRandomString(5);
                String userName = getRandomString(5);
                String userPatronymic = getRandomString(5);
                LocalDate userDateOfBirth = LocalDate.of(r.nextInt(1970, 2024), r.nextInt(1, 12), r.nextInt(1, 29));
                int userPersonalNumber = r.nextInt(2111100000);
                UUID userCurrentPost = UUID.randomUUID();
                int userStructure = r.nextInt(15);
                String userPhone = getRandomString(5);
                String userOffice = getRandomString(5);
                return userInfoModel.addUserInfo(userAccount.getAccountId(), userSurname, userName, userPatronymic, userDateOfBirth, userPersonalNumber, userStructure, userCurrentPost, userPhone, userOffice, client);
            })
            .merge()
            .collect()
            .asList()
            .await()
            .indefinitely();
        for (UserInfoModel userAccount : usersAccount) {
            UserInfoModel result = userInfoModel.getUserInfo(userAccount.getAccountId(), client).await().indefinitely();
            Assertions.assertEquals(userAccount, result, "Вставка данных не удалась");
        }
    }

    static String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789”;";

    public static String getRandomString(int size) {
        StringBuilder result = new StringBuilder(size);
        for (int g = 0; g < size; g++) {
            int ch = (int) (AlphaNumericStr.length() * Math.random());
            result.append(AlphaNumericStr.charAt(ch));
        }
        return result.toString();
    }
}
