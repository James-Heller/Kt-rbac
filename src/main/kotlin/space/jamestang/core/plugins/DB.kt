package space.jamestang.core.plugins

import com.zaxxer.hikari.*
import io.ktor.server.application.*
import io.ktor.util.*
import org.ktorm.database.*
import org.ktorm.logging.*
import org.ktorm.support.mysql.*
import space.jamestang.core.*

object DB  {
    private lateinit var db: Database
    private lateinit var source: HikariDataSource


    fun initDatabase() {
        val billDataSourceConfig = HikariConfig()
        billDataSourceConfig.jdbcUrl = Config.db_url
        billDataSourceConfig.username = Config.db_user
        billDataSourceConfig.password = Config.db_password
        billDataSourceConfig.maximumPoolSize = 10



        source = HikariDataSource(billDataSourceConfig)
        db = Database.connect(source, MySqlDialect(), logger = Slf4jLoggerAdapter("Database"))

    }

    val Application.mysql get() = db
    val mysql get() = db

    fun closeDatabase() {
        source.close()
    }
}