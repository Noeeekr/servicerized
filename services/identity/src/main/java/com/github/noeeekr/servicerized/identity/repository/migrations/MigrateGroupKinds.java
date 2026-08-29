package com.github.noeeekr.servicerized.identity.repository.migrations;

import com.github.noeeekr.servicerized.identity.repository.models.GroupKind;
import com.github.noeeekr.servicerized.identity.repository.models.GroupKinds;
import liquibase.change.custom.CustomSqlChange;
import liquibase.change.custom.CustomSqlRollback;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

public class MigrateGroupKinds implements CustomSqlChange, CustomSqlRollback {
    private static final String TABLE_NAME = "identity." + GroupKind.TABLE_NAME;

    @Override
    public void setUp() throws SetupException {
        return;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        return;
    };


    @Override
    public String getConfirmationMessage() {
        return String.format("Pre-populated identity schema with '%s' system rows.",
                GroupKind.TABLE_NAME);
    }

    @Override
    public SqlStatement[] generateStatements(Database database) throws CustomChangeException {
        String fields = String.format("(%s, %s)", GroupKind.COLUMN_NAME_GROUP_KIND_ID,
            GroupKind.COLUMN_NAME_GROUP_KIND_NAME);
        String values = String.format("(%d, '%s'), (%d, '%s')", GroupKinds.AccessGroup.getId(),
            GroupKinds.AccessGroup.getName(), GroupKinds.FriendGroup.getId(),
            GroupKinds.FriendGroup.getName());
        String statement = String.format("INSERT INTO %s %s VALUES %s", TABLE_NAME, fields, values);
        return new SqlStatement[] {new RawSqlStatement(statement)};
    }

    @Override
    public SqlStatement[] generateRollbackStatements(Database database)
            throws CustomChangeException, RollbackImpossibleException {
        String where = String.format("(k.%s = %d) OR (k.%s = %d)",
                GroupKind.COLUMN_NAME_GROUP_KIND_ID, GroupKinds.AccessGroup.getId(),
                GroupKind.COLUMN_NAME_GROUP_KIND_ID, GroupKinds.FriendGroup.getId());
        String statement = String.format("DELETE FROM %s k WHERE %s", TABLE_NAME, where);
        return new SqlStatement[] {new RawSqlStatement(statement)};
    }
}
