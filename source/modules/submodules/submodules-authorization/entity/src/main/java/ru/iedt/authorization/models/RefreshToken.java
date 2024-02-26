package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    private final UUID session_account_id;
    private final String session_refresh_tokens;
    private final LocalDateTime session_refresh_tokens_start;

    public RefreshToken(UUID session_account_id, String session_refresh_tokens, LocalDateTime session_refresh_tokens_start) {
        this.session_account_id = session_account_id;
        this.session_refresh_tokens = session_refresh_tokens;
        this.session_refresh_tokens_start = session_refresh_tokens_start;
    }

    public UUID getSessionAccountId() {
        return session_account_id;
    }

    public String getSessionUpdateToken() {
        return session_refresh_tokens;
    }

    public LocalDateTime getSessionUpdateTokenStart() {
        return session_refresh_tokens_start;
    }

    public static RefreshToken from(Row row) {
        return new RefreshToken(row.getUUID("session_account_id"), row.getString("session_refresh_tokens"), row.getLocalDateTime("session_refresh_tokens_start"));
    }
}
