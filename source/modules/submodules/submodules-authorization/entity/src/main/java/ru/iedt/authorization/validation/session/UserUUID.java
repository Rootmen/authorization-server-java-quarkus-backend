package ru.iedt.authorization.validation.session;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "User UUID", description = "Данные о соответствии UUID пользователя и имени")
public record UserUUID(@NotBlank(message = "session_id is empty") @Schema(title = "UUID пользователя", required = true) UUID uuid, @NotBlank(message = "session_id is empty") @Schema(title = "Имя пользователя", required = true) String username) {}
