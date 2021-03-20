package kotlinbars
/*
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.data.hibernate.operations.HibernateJpaOperations
import io.micronaut.data.operations.async.AsyncRepositoryOperations
import org.hibernate.SessionFactory
import org.testcontainers.containers.PostgreSQLContainer
import javax.annotation.PreDestroy
import javax.inject.Singleton

@Factory
@Requires(classes = [PostgreSQLConnectionBuilder::class], env = ["test"])
class TestSuspendingConnectionFactory : AutoCloseable {

    class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.1")

    private val container by lazy {
        val underlying = PostgresContainer().withInitScript("init.sql")
        underlying.start()
        underlying
    }

    @Singleton
    fun connection(): SuspendingConnection {
        return PostgreSQLConnectionBuilder.createConnectionPool {
            host = container.host
            port = container.firstMappedPort
            database = container.databaseName
            username = container.username
            password = container.password
        }.asSuspending
    }

    @PreDestroy
    override fun close() {
        container.stop()
    }

}

@Factory
class TestAsyncRepositoryOperations {

    @Singleton
    fun sessionFactory(): SessionFactory {
        println("asdf")

        /*
        this.sessionFactory = sessionFactory;
        this.transactionOperations = transactionOperations;
        this.executorService = executorService;
         */

    }

}

 */