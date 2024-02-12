package ru.iedt.authorization.models.session;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateTokenModel {

    private final UUID session_account_id;
    private final String session_update_token;
    private final LocalDateTime session_update_token_start;

    public UpdateTokenModel(UUID session_account_id, String session_update_token, LocalDateTime session_update_token_start) {
        this.session_account_id = session_account_id;
        this.session_update_token = session_update_token;
        this.session_update_token_start = session_update_token_start;
    }

    public UUID getSessionAccountId() {
        return session_account_id;
    }

    public String getSessionUpdateToken() {
        return session_update_token;
    }

    public LocalDateTime getSessionUpdateTokenStart() {
        return session_update_token_start;
    }

    public static UpdateTokenModel from(Row row) {
        return new UpdateTokenModel(row.getUUID("session_account_id"), row.getString("session_update_token"), row.getLocalDateTime("session_update_token_start"));
    }
}
