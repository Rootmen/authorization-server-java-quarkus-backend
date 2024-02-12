package ru.iedt.authorization.models.session.output;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SessionAuthorizationInfo", description = "Информация о сессии")
public class SessionAuthorizationInfoModel {

    @NotBlank
    @Schema(title = "Идентификатор сессии", required = true, pattern = "[1-9a-f][0-9a-f]+")
    public final String session_id;

    @NotBlank
    @Schema(title = "Идентификатор аккаунта", required = true, pattern = "[1-9a-f][0-9a-f]+")
    public final UUID session_account_id;

    @NotBlank
    @Schema(title = "Публичный ключ сервера", required = true, pattern = "[1-9a-f][0-9a-f]+")
    public final String session_server_public_key;

    @NotBlank
    @Schema(title = "Дополнение к публичному ключу", required = true, pattern = "[0-9a-z]+")
    public final String salt;

    @NotBlank
    @Schema(title = "X часть ключа сессии", required = true, pattern = "[1-9a-f][0-9a-f]+")
    public final String x_cord;

    @NotBlank
    @Schema(title = "Y часть ключа сессии", required = true, pattern = "[1-9a-f][0-9a-f]+")
    public final String y_cord;

    public SessionAuthorizationInfoModel(String session_id, UUID session_account_id, String session_server_public_key, String salt, String x_cord, String y_cord) {
        this.session_id = session_id;
        this.session_account_id = session_account_id;
        this.session_server_public_key = session_server_public_key;
        this.salt = salt;
        this.x_cord = x_cord;
        this.y_cord = y_cord;
    }
}
