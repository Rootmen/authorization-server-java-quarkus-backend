package ru.iedt.authorization.rest.session;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Session Authorization Confirm", description = "Токен доступа и подтверждение сервера")
public record ResultConfirm(
        @Schema(title = "Идентификатор сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank(message = "session_id is empty") String session_id,
        @Schema(title = "Идентификатор аккаунта", required = true) @NotBlank(message = "session_account_id is empty") UUID session_account_id,
        @Schema(title = "Подтверждение сервера", required = true) @NotBlank(message = "out_confirm is empty") String out_confirm,
        @Schema(title = "Права пользователей", required = true) @NotBlank(message = "user_role is empty") ArrayList<String> user_role,
        @Schema(title = "Токен доступа", required = true) @NotBlank(message = "token is empty") String token,
        @Schema(title = "Токен обновления", required = true) @NotBlank(message = "refresh_token is empty") String refresh_token
) {
}
