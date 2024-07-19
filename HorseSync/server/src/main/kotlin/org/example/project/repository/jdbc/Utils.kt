package biblio.app.repository.jdbc

import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


fun <T> executeDatabaseTransaction(
    dataSource: DataSource,
    action: (Connection) -> T,
): T {
    return dataSource.connection.use { connection ->
        try {
            connection.autoCommit = false
            val result = action(connection)
            connection.commit()
            result
        } catch (e: SQLException) {
            connection.rollback()
            throw e
        } finally {
            if (!connection.isClosed) connection.autoCommit = true
        }
    }
}
