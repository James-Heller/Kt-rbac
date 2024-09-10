package space.jamestang.core.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.ktorm.support.mysql.MySqlDialect
import space.jamestang.core.Config

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