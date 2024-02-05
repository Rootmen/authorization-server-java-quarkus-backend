package ru.iedt.authorization.api.users.dto;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class UserAccountModel {
    private final UUID account_id;
    private String account_mail;
    private String account_name;
    private String account_password_verifier;
    private String account_salt;
    private final LocalDateTime account_last_password_update;
    private int account_password_reset_interval;
    private final boolean is_deprecated;

    public UserAccountModel(
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

    public static UserAccountModel from(Row row) {
        return new UserAccountModel(
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
        return new StringJoiner(", ", UserAccountModel.class.getSimpleName() + "[", "]")
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

        UserAccountModel that = (UserAccountModel) o;

        if (account_password_reset_interval != that.account_password_reset_interval) return false;
        if (is_deprecated != that.is_deprecated) return false;
        if (!Objects.equals(account_id, that.account_id)) return false;
        if (!Objects.equals(account_mail, that.account_mail)) return false;
        if (!Objects.equals(account_name, that.account_name)) return false;
        if (!Objects.equals(account_password_verifier, that.account_password_verifier)) return false;
        if (!Objects.equals(account_salt, that.account_salt)) return false;
        return Objects.equals(account_last_password_update, that.account_last_password_update);
    }

    @Override
    public int hashCode() {
        int result = account_id != null ? account_id.hashCode() : 0;
        result = 31 * result + (account_mail != null ? account_mail.hashCode() : 0);
        result = 31 * result + (account_name != null ? account_name.hashCode() : 0);
        result = 31 * result + (account_password_verifier != null ? account_password_verifier.hashCode() : 0);
        result = 31 * result + (account_salt != null ? account_salt.hashCode() : 0);
        result = 31 * result + (account_last_password_update != null ? account_last_password_update.hashCode() : 0);
        result = 31 * result + account_password_reset_interval;
        result = 31 * result + (is_deprecated ? 1 : 0);
        return result;
    }

    public UserAccountModel setAccountName(String account_name) {
        this.account_name = account_name;
        return this;
    }

    public UserAccountModel setAccountMail(String account_mail) {
        this.account_mail = account_mail;
        return this;
    }

    public UserAccountModel setAccountPasswordVerifier(String account_password_verifier) {
        this.account_password_verifier = account_password_verifier;
        return this;
    }

    public UserAccountModel setAccountSalt(String account_salt) {
        this.account_salt = account_salt;
        return this;
    }

    public UserAccountModel setAccountPasswordResetInterval(int account_password_reset_interval) {
        this.account_password_reset_interval = account_password_reset_interval;
        return this;
    }
}
