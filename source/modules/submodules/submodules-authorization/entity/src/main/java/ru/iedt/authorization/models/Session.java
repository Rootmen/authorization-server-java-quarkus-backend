package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Session {

    private final String session_id;
    private final BigInteger session_key;
    private final LocalDateTime session_start;
    private final UUID account_id;
    private final UUID app_id;
    private final BigInteger session_server_private_key;
    private final BigInteger session_server_public_key;
    private final BigInteger session_account_public_key;
    private final BigInteger session_scrambler;
    private final BigInteger session_authorization_key;
    private final String session_signature;

    public String getSessionId() {
        return session_id;
    }

    public BigInteger getSessionKey() {
        return session_key;
    }

    public LocalDateTime getSessionStart() {
        return session_start;
    }

    public UUID getAccountId() {
        return account_id;
    }

    public UUID getAppId() {
        return app_id;
    }

    public BigInteger getSessionServerPrivateKey() {
        return session_server_private_key;
    }

    public BigInteger getSessionServerPublicKey() {
        return session_server_public_key;
    }

    public BigInteger getSessionAccountPublicKey() {
        return session_account_public_key;
    }

    public BigInteger getSessionScrambler() {
        return session_scrambler;
    }

    public BigInteger getSessionAuthorizationKey() {
        return session_authorization_key;
    }

    public String getSessionSignature() {
        return session_signature;
    }

    public Session(
        String session_id,
        BigInteger session_key,
        LocalDateTime session_start,
        UUID account_id,
        UUID app_id,
        BigInteger session_server_private_key,
        BigInteger session_server_public_key,
        BigInteger session_account_public_key,
        BigInteger session_scrambler,
        BigInteger session_authorization_key,
        String session_signature
    ) {
        this.session_id = session_id;
        this.session_key = session_key;
        this.session_start = session_start;
        this.account_id = account_id;
        this.app_id = app_id;
        this.session_server_private_key = session_server_private_key;
        this.session_server_public_key = session_server_public_key;
        this.session_account_public_key = session_account_public_key;
        this.session_scrambler = session_scrambler;
        this.session_authorization_key = session_authorization_key;
        this.session_signature = session_signature;
    }

    public static Session from(Row row) {
        return new Session(
            row.getString("session_id"),
            new BigInteger(row.getString("session_key"), 16),
            row.getLocalDateTime("session_start"),
            row.getUUID("account_id"),
            row.getUUID("app_id"),
            new BigInteger(row.getString("session_server_private_key"), 16),
            new BigInteger(row.getString("session_server_public_key"), 16),
            new BigInteger(row.getString("session_account_public_key"), 16),
            new BigInteger(row.getString("session_scrambler"), 16),
            new BigInteger(row.getString("session_authorization_key"), 16),
            row.getString("session_signature")
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Session.class.getSimpleName() + "[", "]")
            .add("session_id='" + session_id + "'")
            .add("session_key=" + session_key)
            .add("session_start=" + session_start)
            .add("account_id=" + account_id)
            .add("app_id=" + app_id)
            .add("session_server_private_key=" + session_server_private_key)
            .add("session_server_public_key=" + session_server_public_key)
            .add("session_account_public_key=" + session_account_public_key)
            .add("session_scrambler=" + session_scrambler)
            .add("session_authorization_key=" + session_authorization_key)
            .add("session_signature='" + session_signature + "'")
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!Objects.equals(session_id, session.session_id)) return false;
        if (!Objects.equals(session_key, session.session_key)) return false;
        if (!Objects.equals(session_start, session.session_start)) return false;
        if (!Objects.equals(account_id, session.account_id)) return false;
        if (!Objects.equals(app_id, session.app_id)) return false;
        if (!Objects.equals(session_server_private_key, session.session_server_private_key)) return false;
        if (!Objects.equals(session_server_public_key, session.session_server_public_key)) return false;
        if (!Objects.equals(session_account_public_key, session.session_account_public_key)) return false;
        if (!Objects.equals(session_scrambler, session.session_scrambler)) return false;
        if (!Objects.equals(session_authorization_key, session.session_authorization_key)) return false;
        return Objects.equals(session_signature, session.session_signature);
    }

    @Override
    public int hashCode() {
        int result = session_id != null ? session_id.hashCode() : 0;
        result = 31 * result + (session_key != null ? session_key.hashCode() : 0);
        result = 31 * result + (session_start != null ? session_start.hashCode() : 0);
        result = 31 * result + (account_id != null ? account_id.hashCode() : 0);
        result = 31 * result + (app_id != null ? app_id.hashCode() : 0);
        result = 31 * result + (session_server_private_key != null ? session_server_private_key.hashCode() : 0);
        result = 31 * result + (session_server_public_key != null ? session_server_public_key.hashCode() : 0);
        result = 31 * result + (session_account_public_key != null ? session_account_public_key.hashCode() : 0);
        result = 31 * result + (session_scrambler != null ? session_scrambler.hashCode() : 0);
        result = 31 * result + (session_authorization_key != null ? session_authorization_key.hashCode() : 0);
        result = 31 * result + (session_signature != null ? session_signature.hashCode() : 0);
        return result;
    }
}
