package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private int account_lock_count;
    private final int account_lock_time;
    private final boolean is_deprecated;
    private final boolean is_system;

    public UserAccount(
        UUID account_id,
        String account_name,
        String account_mail,
        String account_password_verifier,
        String account_salt,
        LocalDateTime account_last_password_update,
        int account_password_reset_interval,
        int account_lock_count,
        int account_lock_time,
        boolean is_system,
        boolean is_deprecated
    ) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.account_mail = account_mail;
        this.account_password_verifier = account_password_verifier;
        this.account_salt = account_salt;
        this.account_last_password_update = account_last_password_update;
        this.account_password_reset_interval = account_password_reset_interval;
        this.account_lock_count = account_lock_count;
        this.account_lock_time = account_lock_time;
        this.is_deprecated = is_deprecated;
        this.is_system = is_system;
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

    public int getAccountLockCount() {
        return account_lock_count;
    }

    public int getAccountLockTime() {
        return account_lock_time;
    }

    public boolean isDeprecated() {
        return is_deprecated;
    }

    public boolean isSystem() {
        return is_system;
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
            row.getInteger("account_lock_count"),
            row.getInteger("account_lock_time"),
            row.getBoolean("account_is_system"),
            row.getBoolean("account_is_deprecated")
        );
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
            .add("account_lock_count=" + account_lock_count)
            .add("is_deprecated=" + is_deprecated)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (account_password_reset_interval != that.account_password_reset_interval) return false;
        if (account_lock_count != that.account_lock_count) return false;
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
        result = 31 * result + account_lock_count;
        result = 31 * result + (is_deprecated ? 1 : 0);
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

    public UserAccount setAccountLockCount(int account_lock_count) {
        this.account_lock_count = account_password_reset_interval;
        return this;
    }
}
