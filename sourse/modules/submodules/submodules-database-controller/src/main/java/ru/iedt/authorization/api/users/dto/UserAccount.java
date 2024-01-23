package ru.iedt.authorization.api.users.dto;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

public class UserAccount {
    private final UUID account_id;

    private final String account_mail;

    private final String account_name;
    private final String account_password_verifier;
    private final String account_salt;
    private final LocalDateTime account_last_password_update;
    private final int account_password_reset_interval;

    public UserAccount(
            UUID account_id,
            String account_name,
            String account_mail,
            String account_password_verifier,
            String account_salt,
            LocalDateTime account_last_password_update,
            int account_password_reset_interval) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.account_mail = account_mail;
        this.account_password_verifier = account_password_verifier;
        this.account_salt = account_salt;
        this.account_last_password_update = account_last_password_update;
        this.account_password_reset_interval = account_password_reset_interval;
    }

    public UUID getAccountId() {
        return account_id;
    }

    public String getAccountName() {
        return account_name;
    }

    public String getAccountMail() {
        return account_mail;
    }

    public String getAccountPasswordVerifier() {
        return account_password_verifier;
    }

    public String getAccountSalt() {
        return account_salt;
    }

    public LocalDateTime getAccountLastPasswordUpdate() {
        return account_last_password_update;
    }

    public int getAccountPasswordResetInterval() {
        return account_password_reset_interval;
    }

    public static UserAccount from(Row row) {
        return new UserAccount(
                row.getUUID("account_id"),
                row.getString("account_name"),
                row.getString("account_mail"),
                row.getString("account_password_verifier"),
                row.getString("account_salt"),
                row.getLocalDateTime("account_last_password_update"),
                row.getInteger("account_password_reset_interval"));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserAccount.class.getSimpleName() + "[", "]")
                .add("account_id=" + account_id)
                .add("account_mail='" + account_mail + "'")
                .add("account_name='" + account_name + "'")
                .add("account_password_verifier='" + account_password_verifier + "'")
                .add("account_salt='" + account_salt + "'")
                .add("account_last_password_update=" + account_last_password_update)
                .add("account_password_reset_interval=" + account_password_reset_interval)
                .toString();
    }
}
