package space.jamestang.core

import io.ktor.server.application.*

object Config {

    lateinit var jwt_audience: String
    lateinit var jwt_domain: String
    lateinit var jwt_realm: String
    lateinit var jwt_secret: String
    lateinit var jwt_expire: String

    lateinit var db_url: String
    lateinit var db_user: String
    lateinit var db_password: String


    lateinit var redis_host: String
    lateinit var redis_port: String
    lateinit var redis_expire: String
    lateinit var redis_db: String


}

fun Application.initConfigurations() {
    Config.javaClass.declaredFields.forEach {
        if (it.name == "INSTANCE") return@forEach
        log.info("Setting ${it.name} from environment")
        val key = it.name.split("_")
        val name = key.joinToString(".")
        it.set(Config, environment.config.property(name).getString())
    }
}