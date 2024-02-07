package ru.iedt.authorization.models.session.output;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

public class SessionAuthorizationConfirmModel {

    private final String session_id;
    private final UUID session_account_id;
    private final String out_confirm;
    private final ArrayList<String> user_role;
    private final String token;
    private final String update_token;

    public SessionAuthorizationConfirmModel(String session_id, UUID session_account_id, String out_confirm, ArrayList<String> user_role, String token, String update_token) {
        this.session_id = session_id;
        this.session_account_id = session_account_id;
        this.out_confirm = out_confirm;
        this.user_role = user_role;
        this.token = token;
        this.update_token = update_token;
    }

}
