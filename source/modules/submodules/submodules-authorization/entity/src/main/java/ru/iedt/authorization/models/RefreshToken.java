package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    private final UUID account_id;
    private final String token_refresh;
    private final LocalDateTime token_refresh_create_time;

    public RefreshToken(UUID account_id, String token_refresh, LocalDateTime token_refresh_create_time) {
        this.account_id = account_id;
        this.token_refresh = token_refresh;
        this.token_refresh_create_time = token_refresh_create_time;
    }

    public UUID getAccountId() {
        return account_id;
    }

    public String getTokenRefresh() {
        return token_refresh;
    }

    public LocalDateTime getTokenCreateTime() {
        return token_refresh_create_time;
    }

    public static RefreshToken from(Row row) {
        return new RefreshToken(row.getUUID("account_id"), row.getString("token_refresh"), row.getLocalDateTime("token_refresh_create_time"));
    }
}
