package ru.iedt.authorization.rest.session;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "App Information", description = "Описание приложения")
public record ResultAppInformation(
        @Schema(title = "Идентификатор приложения", required = true, pattern = "[1-9a-f][0-9a-f]+") @NotBlank(message = "app_id is empty") UUID app_id,
        @Schema(title = "Название приложения", required = true) @NotBlank(message = "session_account_id is empty") String app_name,
        @Schema(title = "Адрес приложения", required = true) @NotBlank(message = "out_confirm is empty") String redirect_url,
        @Schema(title = "Иконка приложения", required = true) @NotBlank(message = "app_image is empty") String app_image
) {
}