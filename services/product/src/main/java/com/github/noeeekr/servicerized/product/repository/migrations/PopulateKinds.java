package com.github.noeeekr.servicerized.product.repository.migrations;

import java.util.List;
import com.github.noeeekr.servicerized.product.repository.models.entities.KindEntity;
import liquibase.change.custom.CustomSqlChange;
import liquibase.change.custom.CustomSqlRollback;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawSqlStatement;

public class PopulateKinds implements CustomSqlChange, CustomSqlRollback {
    public ValidationErrors validate(Database database) {
        return null;
    }

    public void setFileOpener(ResourceAccessor acessor) {}

    public void setUp() {}

    public String getConfirmationMessage() {
        return String.format("Populated '%s' entity with '%d' default system rows. ",
                KindEntity.class.getCanonicalName(),
                // Uses the last id as a count
                KindEntity.Default.getVirtualServiceKind().getKindId());
    }


    public SqlStatement[] generateStatements(Database database) {
        StringBuilder valueStatement = new StringBuilder();
        KindEntity[] entities = {KindEntity.Default.getVirtualServiceKind()};
        List.of(entities).forEach((entity) -> {
            valueStatement.append(String.format("(%d, %s)", entity.getKindId(), entity.getKindName()));
        });

        String fieldStatement =
                String.format("(%s, %s)", KindEntity.METADATA.DATABASE_COLUMN_NAME_KIND_ID,
                        KindEntity.METADATA.DATABASE_COLUMN_NAME_KIND_NAME);
        String statement = String.format("INSERT INTO %s %s VALUES %s",
                KindEntity.METADATA.TABLE_NAME, fieldStatement, valueStatement.toString());
        return new SqlStatement[] {new RawSqlStatement(statement)};
    }

    public SqlStatement[] generateRollbackStatements(Database database) {
        // Unecessary, migration rollback deletes the table & no actual need to unpopulate this in
        // isolation.
        return new SqlStatement[0];
    }
}
