package ru.iedt.authorization.models;

import io.vertx.mutiny.sqlclient.Row;
import java.util.StringJoiner;

public class UserAccountBlock {

    private final boolean is_block;
    private final int time_to_unlock;

    public UserAccountBlock(boolean is_block, int time_to_unlock) {
        this.is_block = is_block;
        this.time_to_unlock = time_to_unlock;
    }

    public boolean isIsBlock() {
        return is_block;
    }

    public int getTimeToUnlock() {
        return time_to_unlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccountBlock that = (UserAccountBlock) o;

        if (is_block != that.is_block) return false;
        return time_to_unlock == that.time_to_unlock;
    }

    @Override
    public int hashCode() {
        int result = (is_block ? 1 : 0);
        result = 31 * result + time_to_unlock;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserAccountBlock.class.getSimpleName() + "[", "]").add("is_block=" + is_block).add("time_to_unlock=" + time_to_unlock).toString();
    }

    public static UserAccountBlock from(Row row) {
        return new UserAccountBlock(row.getBoolean("is_block"), row.getInteger("time_to_unlock"));
    }
}
