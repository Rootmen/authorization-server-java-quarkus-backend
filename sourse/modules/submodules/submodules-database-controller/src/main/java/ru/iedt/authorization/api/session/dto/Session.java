package ru.iedt.authorization.api.session.dto;

import io.vertx.mutiny.sqlclient.Row;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;


public class Session {


    private final BigInteger session_id;
    private final BigInteger session_key;
    private final LocalDateTime session_start;
    private final UUID session_account_id;
    private final BigInteger session_server_private_key;
    private final BigInteger session_server_public_key;
    private final BigInteger session_account_public_key;
    private final BigInteger session_scrambler;
    private final BigInteger session_authorization_key;

    public BigInteger getSessionId() {
        return session_id;
    }

    public BigInteger getSessionKey() {
        return session_key;
    }

    public LocalDateTime getSessionStart() {
        return session_start;
    }

    public UUID getSessionAccountId() {
        return session_account_id;
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

    public Session(BigInteger session_id, BigInteger session_key, LocalDateTime session_start, UUID session_account_id, BigInteger session_server_private_key, BigInteger session_server_public_key, BigInteger session_account_public_key, BigInteger session_scrambler, BigInteger session_authorization_key) {
        this.session_id = session_id;
        this.session_key = session_key;
        this.session_start = session_start;
        this.session_account_id = session_account_id;
        this.session_server_private_key = session_server_private_key;
        this.session_server_public_key = session_server_public_key;
        this.session_account_public_key = session_account_public_key;
        this.session_scrambler = session_scrambler;
        this.session_authorization_key = session_authorization_key;
    }


    public static Session from(Row row) {
        return new Session(
                new BigInteger(row.getString("session_id"), 16),
                new BigInteger(row.getString("session_key"), 16),
                row.getLocalDateTime("session_start"),
                row.getUUID("session_account_id"),
                new BigInteger(row.getString("session_server_private_key"), 16),
                new BigInteger(row.getString("session_server_public_key"), 16),
                new BigInteger(row.getString("session_account_public_key"), 16),
                new BigInteger(row.getString("session_scrambler"), 16),
                new BigInteger(row.getString("session_authorization_key"), 16)
        );
    }
}
