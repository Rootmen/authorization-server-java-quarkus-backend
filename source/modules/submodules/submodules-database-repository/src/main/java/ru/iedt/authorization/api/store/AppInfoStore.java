package ru.iedt.authorization.api.store;

import io.quarkus.runtime.annotations.RegisterForReflection;
import ru.iedt.database.request.store.DefinitionStore;
import ru.iedt.database.request.store.QueryStoreDefinition;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RegisterForReflection
@DefinitionStore
public class AppInfoStore extends QueryStoreDefinition {
    @Override
    public URI getStorePath() throws URISyntaxException {
        return Objects.requireNonNull(this.getClass().getResource("/query/APP_LIST.xml"))
                .toURI();
    }

    @Override
    public String getStoreName() {
        return "APP_LIST";
    }
}