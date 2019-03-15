package fi.jubic.snoozy.example.repo;

import fi.jubic.snoozy.example.Configuration;
import fi.jubic.snoozy.example.User;
import org.jooq.Record;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class UserRepo {
    private static final fi.jubic.snoozy.example.db.tables.User USER
            = fi.jubic.snoozy.example.db.tables.User.USER;

    private final org.jooq.Configuration configuration;

    @Inject
    public UserRepo(Configuration configuration) {
        this.configuration = configuration.getJooqConfiguration()
                .getConfiguration();
    }

    public Optional<User> getUser(String username) {
        return map(
                DSL.using(configuration)
                .select()
                .from(USER)
                .where(USER.NAME.eq(username))
                .fetchOne()
        );
    }

    private static Optional<User> map(Record record) {
        if (record == null || record.getValue(USER.ID) == null) {
            return Optional.empty();
        }

        return Optional.of(
                User.builder()
                    .setId(record.getValue(USER.ID))
                    .setName(record.getValue(USER.NAME))
                    .setRole(record.getValue(USER.ROLE))
                    .setHash(record.getValue(USER.HASH))
                    .setSalt(record.getValue(USER.SALT))
                    .build()
        );
    }
}
