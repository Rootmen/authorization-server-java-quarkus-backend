package ru.iedt.authorization.validation.session;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Session Confirm", description = "Данные для подтверждения сессии")
public record Confirm(
        @NotBlank(message = "session_id is empty") @Schema(title = "id сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") String session_id,
        @NotBlank(message = "confirm is empty") @Schema(title = "Подтверждение сессии от клиента", required = true, pattern = "[1-9a-f][0-9a-f]+") String confirm
) {
}