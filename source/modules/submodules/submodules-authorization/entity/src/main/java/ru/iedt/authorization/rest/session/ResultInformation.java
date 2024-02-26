package ru.iedt.authorization.rest.session;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Session Authorization Information", description = "Информация о сессии")
public record ResultInformation(
    @Schema(title = "Идентификатор сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank String session_id,
    @Schema(title = "Идентификатор аккаунта", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank UUID session_account_id,
    @Schema(title = "Публичный ключ сервера", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank String session_server_public_key,
    @Schema(title = "Дополнение к публичному ключу", required = true, pattern = "[0-9a-z]+") @NotBlank String salt,
    @Schema(title = "X часть ключа сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank String x_cord,
    @Schema(title = "Y часть ключа сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank String y_cord
) {}
