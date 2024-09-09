package space.jamestang.core.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.ktorm.jackson.KtormModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun ObjectMapper.config(
    localDatePattern: String,
    localTimePattern: String,
    localDateTimePattern: String
): ObjectMapper {
    registerModule(KtormModule())

    registerModule(JavaTimeModule().apply {
        setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"))
        addDeserializer(LocalDate::class.java, LocalDateDeserializer(DateTimeFormatter.ofPattern(localDatePattern)))
        addSerializer(LocalDate::class.java, LocalDateSerializer(DateTimeFormatter.ofPattern(localDatePattern)))
        addDeserializer(LocalTime::class.java, LocalTimeDeserializer(DateTimeFormatter.ofPattern(localTimePattern)))
        addSerializer(LocalTime::class.java, LocalTimeSerializer(DateTimeFormatter.ofPattern(localTimePattern)))
        addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(localDateTimePattern))
        )
        addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimePattern))
        )
    })
    configure(SerializationFeature.INDENT_OUTPUT, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    setDefaultLeniency(true)
    setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
        indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
        indentObjectsWith(DefaultIndenter("  ", "\n"))
    })
    return this
}

fun customJacksonConverter(): ObjectMapper{

    val mapper = jacksonObjectMapper()
    mapper.apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        setSerializationInclusion(JsonInclude.Include.ALWAYS)
        setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            indentObjectsWith(DefaultIndenter("  ", "\n"))
        })
        config("yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm:ss")
    }

    return mapper
}

fun Application.configureSerialization(){
    install(ContentNegotiation) {
        clearIgnoredTypes()
        register(ContentType.Application.Json, JacksonConverter(customJacksonConverter()))
    }
}


