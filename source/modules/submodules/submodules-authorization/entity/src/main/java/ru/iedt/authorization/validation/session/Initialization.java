package ru.iedt.authorization.validation.session;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Session Initialization", description = "Данные для создания сессии")
public record Initialization(
    @NotBlank(message = "x_cord is empty") @Schema(title = "x координата для эллиптической кривой", required = true, pattern = "[1-9a-f][0-9a-f]+") String x_cord,
    @NotBlank(message = "y_cord is empty") @Schema(title = "у координата для эллиптической кривой", required = true, pattern = "[1-9a-f][0-9a-f]+") String y_cord,
    @NotBlank(message = "account_public_key is empty") @Schema(title = "Публичный ключ для протокола SRP-6A", required = true, pattern = "[1-9a-f][0-9a-f]+") String account_public_key,
    @NotBlank(message = "app_id is empty") @Schema(title = "uuid приложения для которого совещается авторизация", required = true, pattern = "[1-9a-f][0-9a-f]+") UUID app_id
) {}
