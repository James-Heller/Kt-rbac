package space.jamestang.core.plugins

import io.ktor.server.application.*
import org.redisson.api.*
import space.jamestang.core.*

object Redis{

    private lateinit var config: org.redisson.config.Config
    private lateinit var client: RedissonClient

    fun initialize(){
        config = org.redisson.config.Config().apply {
            useSingleServer().apply {
                address = Config.redis_host

                database = Config.redis_db.toInt()
            }
        }

        client = org.redisson.Redisson.create(config)
    }

    val Application.cache get() = client


}