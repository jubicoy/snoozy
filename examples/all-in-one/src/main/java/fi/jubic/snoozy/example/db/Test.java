/*
 * This file is generated by jOOQ.
 */
package fi.jubic.snoozy.example.db;


import fi.jubic.snoozy.example.db.tables.Todo;
import fi.jubic.snoozy.example.db.tables.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Test extends SchemaImpl {

    private static final long serialVersionUID = 673470400;

    /**
     * The reference instance of <code>test</code>
     */
    public static final Test TEST = new Test();

    /**
     * The table <code>test.todo</code>.
     */
    public final Todo TODO = fi.jubic.snoozy.example.db.tables.Todo.TODO;

    /**
     * The table <code>test.user</code>.
     */
    public final User USER = fi.jubic.snoozy.example.db.tables.User.USER;

    /**
     * No further instances allowed
     */
    private Test() {
        super("test", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Todo.TODO,
            User.USER);
    }
}
