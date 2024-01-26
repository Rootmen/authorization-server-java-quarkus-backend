package ru.iedt.authorization.api.users.dto;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.UUID;

public class UserAccount {
    private final UUID account_id;
    private String account_mail;
    private String account_name;
    private String account_password_verifier;
    private String account_salt;
    private final LocalDateTime account_last_password_update;
    private int account_password_reset_interval;
    private final boolean is_deprecated;

    public UserAccount(
            UUID account_id,
            String account_name,
            String account_mail,
            String account_password_verifier,
            String account_salt,
            LocalDateTime account_last_password_update,
            int account_password_reset_interval,
            boolean is_deprecated) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.account_mail = account_mail;
        this.account_password_verifier = account_password_verifier;
        this.account_salt = account_salt;
        this.account_last_password_update = account_last_password_update;
        this.account_password_reset_interval = account_password_reset_interval;
        this.is_deprecated = is_deprecated;
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

    public boolean isDeprecated() {
        return is_deprecated;
    }

    public static UserAccount from(Row row) {
        return new UserAccount(
                row.getUUID("account_id"),
                row.getString("account_name"),
                row.getString("account_mail"),
                row.getString("account_password_verifier"),
                row.getString("account_salt"),
                row.getLocalDateTime("account_last_password_update"),
                row.getInteger("account_password_reset_interval"),
                row.getBoolean("account_is_deprecated"));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (account_password_reset_interval != that.account_password_reset_interval) return false;
        if (!account_id.equals(that.account_id)) return false;
        if (!account_mail.equals(that.account_mail)) return false;
        if (!account_name.equals(that.account_name)) return false;
        if (!account_password_verifier.equals(that.account_password_verifier)) return false;
        if (!account_salt.equals(that.account_salt)) return false;
        return account_last_password_update.equals(that.account_last_password_update);
    }

    @Override
    public int hashCode() {
        int result = account_id.hashCode();
        result = 31 * result + account_mail.hashCode();
        result = 31 * result + account_name.hashCode();
        result = 31 * result + account_password_verifier.hashCode();
        result = 31 * result + account_salt.hashCode();
        result = 31 * result + account_last_password_update.hashCode();
        result = 31 * result + account_password_reset_interval;
        return result;
    }

    public UserAccount setAccountName(String account_name) {
        this.account_name = account_name;
        return this;
    }

    public UserAccount setAccountMail(String account_mail) {
        this.account_mail = account_mail;
        return this;
    }

    public UserAccount setAccountPasswordVerifier(String account_password_verifier) {
        this.account_password_verifier = account_password_verifier;
        return this;
    }

    public UserAccount setAccountSalt(String account_salt) {
        this.account_salt = account_salt;
        return this;
    }

    public UserAccount setAccountPasswordResetInterval(int account_password_reset_interval) {
        this.account_password_reset_interval = account_password_reset_interval;
        return this;
    }
}
