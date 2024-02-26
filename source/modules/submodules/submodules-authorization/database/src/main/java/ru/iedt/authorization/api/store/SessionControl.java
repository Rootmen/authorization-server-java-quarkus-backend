package ru.iedt.authorization.api.store;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

@RegisterForReflection
@DefinitionStore
public class SessionControl extends QueryStoreDefinition {

    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getResource("/query/SESSION_CONTROL.xml")).toURI();
    }

    @Override
    public String getStoreName() {
        return "SESSION_CONTROL";
    }
}
