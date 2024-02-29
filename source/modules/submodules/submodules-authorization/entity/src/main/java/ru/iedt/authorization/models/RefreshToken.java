package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    //account_id, token_refresh, token_refresh_create_time, token_ip, token_signature
    private final UUID account_id;
    private final String session_id;
    private final String token_refresh;
    private final LocalDateTime token_refresh_create_time;
    private final String token_ip;
    private final String token_signature;

    public RefreshToken(UUID account_id, String session_id, String token_refresh, LocalDateTime token_refresh_create_time, String token_ip, String token_signature) {
        this.account_id = account_id;
        this.session_id = session_id;
        this.token_refresh = token_refresh;
        this.token_refresh_create_time = token_refresh_create_time;
        this.token_ip = token_ip;
        this.token_signature = token_signature;
    }

    public UUID getAccountId() {
        return account_id;
    }

    public String getSessionId() {
        return session_id;
    }

    public String getTokenRefresh() {
        return token_refresh;
    }

    public LocalDateTime getTokenCreateTime() {
        return token_refresh_create_time;
    }

    public String getTokenIp() {
        return token_ip;
    }

    public String getTokenSignature() {
        return token_signature;
    }

    public static RefreshToken from(Row row) {
        return new RefreshToken(row.getUUID("account_id"), row.getString("session_id"), row.getString("token_refresh"), row.getLocalDateTime("token_refresh_create_time"), row.getString("token_ip"), row.getString("token_signature"));
    }
}
