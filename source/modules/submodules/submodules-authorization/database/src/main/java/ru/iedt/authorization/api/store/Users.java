package ru.iedt.authorization.api.store;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@RegisterForReflection
@DefinitionStore
public class Users extends QueryStoreDefinition {

    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getResource("/query/USERS.xml")).toURI();
    }

    @Override
    public String getStoreName() {
        return "USERS";
    }
}
