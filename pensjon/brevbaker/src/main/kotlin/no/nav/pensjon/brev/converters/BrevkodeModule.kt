package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import kotlin.jvm.java

object BrevkodeModule : SimpleModule() {
    private fun readResolve(): Any = BrevkodeModule

    init {
        addDeserializer(Brevkode.Automatisk::class.java, BrevkodeDeserializerAutomatisk)
        addDeserializer(Brevkode.Redigerbart::class.java, BrevkodeDeserializerRedigerbart)
        addSerializer(AlltidValgbartVedleggKode::class.java, AlltidValgbartVedleggKodeSerializer)
        addAbstractTypeMapping<AlltidValgbartVedleggKode, AlltidValgbartVedleggBrevkode>()
    }

    private object BrevkodeDeserializerAutomatisk : JsonDeserializer<Brevkode.Automatisk>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Brevkode.Automatisk =
            AutomatiskBrevkode(ctxt.readValue(parser, String::class.java))
    }

    private object BrevkodeDeserializerRedigerbart : JsonDeserializer<Brevkode.Redigerbart>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Brevkode.Redigerbart =
            RedigerbarBrevkode(ctxt.readValue(parser, String::class.java))
    }

    private object AlltidValgbartVedleggKodeSerializer : JsonSerializer<AlltidValgbartVedleggKode>() {
        override fun serialize(value: AlltidValgbartVedleggKode, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartObject()
            gen.writeStringField("kode", value.kode)
            gen.writeStringField("visningstekst", value.visningstekst)
            gen.writeEndObject()
        }
    }
}