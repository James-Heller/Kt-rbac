package space.jamestang.core.plugins

import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*
import space.jamestang.core.util.*

fun Application.configureExceptionHandler(){
    install(ExceptionHandler)
}

class ExceptionHandler {

    companion object Plugin: BaseApplicationPlugin<ApplicationCallPipeline, Configuration, ExceptionHandler> {
        override val key: AttributeKey<ExceptionHandler>
            get() = AttributeKey("ExceptionHandler")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): ExceptionHandler {

            val handler = ExceptionHandler()
            pipeline.intercept(ApplicationCallPipeline.Plugins){
                try {
                    proceed()
                }catch (e: Exception){

                    when(e){
                        is TransactionException -> {
                            call.respond(Resp.error(4396, e.message?: "Transaction Exception"))
                        }
                        is ParseException -> {
                            call.respond(Resp.error(8848, e.message?: "Parse Exception"))
                        }
                        else -> {
                            e.printStackTrace()
                            call.respond(Resp.error(4396, e.message?: "Unknown Exception"))
                        }
                    }
                }
            }

            return handler
        }

    }
}

class TransactionException(msg: String): Exception(msg)
class ParseException(msg: String): RuntimeException(msg)