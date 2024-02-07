package ru.iedt.authorization.models.session.output;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public class SessionAuthorizationInfoModel {

    private final String session_id;
    private final UUID session_account_id;
    private final String session_server_public_key;
    private final String salt;

    public SessionAuthorizationInfoModel(String session_id, UUID session_account_id, String session_server_public_key, String salt) {
        this.session_id = session_id;
        this.session_account_id = session_account_id;
        this.session_server_public_key = session_server_public_key;
        this.salt = salt;
    }
}
