package ru.iedt.authorization.validation.session;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Token Update", description = "Данные для пересоздания токена")
public record TokenUpdate(
    @NotBlank(message = "session_id is empty") @Schema(title = "id сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") String session_id,
    @NotBlank(message = "app_id is empty") @Schema(title = "uuid приложения для которого совещается авторизация", required = true, pattern = "[1-9a-f][0-9a-f]+") UUID app_id,
    @NotBlank(message = "app_id is empty") @Schema(title = "uuid приложения для которого совещается авторизация", required = true, pattern = "[1-9a-f][0-9a-f]+") String tokenUpdate
) {}
