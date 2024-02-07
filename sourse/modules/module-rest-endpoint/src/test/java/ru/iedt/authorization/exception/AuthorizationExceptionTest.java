package ru.iedt.authorization.exception;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AuthorizationExceptionTest {

    @Test
    public void testExceptions() {

        try {
            throw new AuthorizationException("Ошибка", "Тестирование", new RuntimeException());
        } catch (AuthorizationException ignored) {

        }
    }
}
