package no.nav.pensjon.brev.pdfbygger.kafka

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nav.pensjon.brev.pdfbygger.getProperty
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


fun Application.kafkaModule() {

    val configPrefix = "pdfBygger.kafka"
    val properties = mapOf(
        "bootstrap.servers" to getProperty("${configPrefix}.bootstrap.servers"),
        "security.protocol" to getProperty("${configPrefix}.security.protocol"),
        "ssl.keystore.type" to getProperty("${configPrefix}.ssl.keystore.type"),
        "ssl.keystore.location" to getProperty("${configPrefix}.ssl.keystore.location"),
        "ssl.keystore.password" to getProperty("${configPrefix}.ssl.keystore.password"),
        "ssl.key.password" to getProperty("${configPrefix}.ssl.key.password"),
        "ssl.truststore.type" to getProperty("${configPrefix}.ssl.truststore.type"),
        "ssl.truststore.location" to getProperty("${configPrefix}.ssl.truststore.location"),
        "ssl.truststore.password" to getProperty("${configPrefix}.ssl.truststore.password"),
        "max.poll.records" to getProperty("${configPrefix}.ssl.truststore.password"),
    )


// create a producer with String Serializer for key and value
    val consumer = KafkaConsumer<String, String>(properties, StringDeserializer(), StringDeserializer())

    val topic = getProperty("${configPrefix}.topic")
    consumer.subscribe(listOf(topic))
    launch(Dispatchers.IO) {
        while (true){
            val message = consumer.poll(60.seconds.toJavaDuration())
            message.forEach {
                it.value()
            }
        }
    }
}