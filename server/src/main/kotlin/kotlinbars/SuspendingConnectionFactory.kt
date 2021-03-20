package kotlinbars

/*
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import io.micronaut.configuration.jasync.JasyncPoolConfiguration
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import javax.annotation.PreDestroy
import javax.inject.Singleton

@Factory
@Requires(classes = [PostgreSQLConnectionBuilder::class], notEnv = ["test"])
class SuspendingConnectionFactory(jasyncPoolConfiguration: JasyncPoolConfiguration) : AutoCloseable {

    private val connectionPool = PostgreSQLConnectionBuilder.createConnectionPool(jasyncPoolConfiguration.jasyncOptions)

    @Singleton
    fun connection(): SuspendingConnection {
        return connectionPool.asSuspending
    }

    @PreDestroy
    override fun close() {
        if (connectionPool.isConnected()) connectionPool.disconnect()
    }
}

 */