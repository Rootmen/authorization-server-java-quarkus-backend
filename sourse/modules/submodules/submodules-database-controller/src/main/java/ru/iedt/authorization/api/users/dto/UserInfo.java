package ru.iedt.authorization.api.users.dto;

import io.vertx.mutiny.sqlclient.Row;
import java.time.LocalDate;
import java.util.StringJoiner;
import java.util.UUID;

public class UserInfo {
    private final UUID account_id;
    private String user_surname;
    private String user_name;
    private String user_patronymic;
    private LocalDate user_date_of_birth;
    private int user_personal_number;
    private int user_structure;
    private UUID user_current_post;
    private String user_phone;
    private String user_office;

    public UserInfo(
            UUID account_id,
            String user_surname,
            String user_name,
            String user_patronymic,
            LocalDate user_date_of_birth,
            int user_personal_number,
            int user_structure,
            UUID user_current_post,
            String user_phone,
            String user_office) {
        this.account_id = account_id;
        this.user_surname = user_surname;
        this.user_name = user_name;
        this.user_patronymic = user_patronymic;
        this.user_date_of_birth = user_date_of_birth;
        this.user_personal_number = user_personal_number;
        this.user_structure = user_structure;
        this.user_current_post = user_current_post;
        this.user_phone = user_phone;
        this.user_office = user_office;
    }

    public UUID getAccountId() {
        return account_id;
    }

    public String getUserSurname() {
        return user_surname;
    }

    public String getUserName() {
        return user_name;
    }

    public String getUserPatronymic() {
        return user_patronymic;
    }

    public LocalDate getUserDateBirth() {
        return user_date_of_birth;
    }

    public int getUserPersonalNumber() {
        return user_personal_number;
    }

    public int getUserStructure() {
        return user_structure;
    }

    public UUID getUserCurrentPost() {
        return user_current_post;
    }

    public String getUserPhone() {
        return user_phone;
    }

    public String getUserOffice() {
        return user_office;
    }

    public static UserInfo from(Row row) {
        return new UserInfo(
                row.getUUID("account_id"),
                row.getString("user_surname"),
                row.getString("user_name"),
                row.getString("user_patronymic"),
                row.getLocalDate("user_date_of_birth"),
                row.getInteger("user_personal_number"),
                row.getInteger("user_structure"),
                row.getUUID("user_current_post"),
                row.getString("user_phone"),
                row.getString("user_office"));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserInfo.class.getSimpleName() + "[", "]")
                .add("account_id=" + account_id)
                .add("user_surname='" + user_surname + "'")
                .add("user_name='" + user_name + "'")
                .add("user_patronymic='" + user_patronymic + "'")
                .add("user_date_of_birth=" + user_date_of_birth)
                .add("user_personal_number=" + user_personal_number)
                .add("user_structure=" + user_structure)
                .add("user_current_post=" + user_current_post)
                .add("user_phone='" + user_phone + "'")
                .add("user_office='" + user_office + "'")
                .toString();
    }

    public void setUserSurname(String user_surname) {
        this.user_surname = user_surname;
    }

    public void setUsername(String user_name) {
        this.user_name = user_name;
    }

    public void setUserPatronymic(String user_patronymic) {
        this.user_patronymic = user_patronymic;
    }

    public void setUserDateOfBirth(LocalDate user_date_of_birth) {
        this.user_date_of_birth = user_date_of_birth;
    }

    public void setUserPersonalNumber(int user_personal_number) {
        this.user_personal_number = user_personal_number;
    }

    public void setUserStructure(int user_structure) {
        this.user_structure = user_structure;
    }

    public void setUserCurrentPost(UUID user_current_post) {
        this.user_current_post = user_current_post;
    }

    public void setUserPhone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUserOffice(String user_office) {
        this.user_office = user_office;
    }
}
