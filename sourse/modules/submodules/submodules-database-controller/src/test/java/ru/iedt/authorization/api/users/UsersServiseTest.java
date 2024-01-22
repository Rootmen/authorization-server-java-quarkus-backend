package ru.iedt.authorization.api.users;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.iedt.authorization.api.users.dto.UserAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserAccountTest {
    @Inject
    UsersModel usersModel;

    @Inject
    PgPool client;

    @BeforeClass
    public void setup() throws IOException {
        String initsq = readFromInputStream(this.getClass().getResourceAsStream("/init.sql"));
        client.query(initsq)
                .execute()
                .await()
                .indefinitely();
    }


    @Test
    void testLoadAllUsers() throws IOException {

        System.out.println(usersModel.getAllUserAccount(client).collect().asList().await().indefinitely());


        UUID uuidTest = UUID.fromString("39f311a4-837e-4fff-b6bf-55cf2a7b04ad");
        UserAccount userAccountUUIDTest = usersModel.getUserAccount(uuidTest, client).await().indefinitely();
        Assertions.assertEquals(userAccountUUIDTest.getAccountId(), uuidTest);
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}