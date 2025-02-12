package no.nav.pensjon.brev.skribenten.routes

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode

object BrevkodeModule : SimpleModule() {
    private fun readResolve(): Any = BrevkodeModule

    init {
        addDeserializer(Brevkode.Automatisk::class.java, BrevkodeDeserializerAutomatisk)
        addDeserializer(Brevkode.Redigerbart::class.java, BrevkodeDeserializerRedigerbart)
    }

    private object BrevkodeDeserializerAutomatisk : JsonDeserializer<Brevkode.Automatisk>() {
        override fun deserialize(
            parser: JsonParser,
            ctxt: DeserializationContext,
        ): Brevkode.Automatisk =
            AutomatiskBrevkode(ctxt.readValue(parser, String::class.java))
    }

    private object BrevkodeDeserializerRedigerbart : JsonDeserializer<Brevkode.Redigerbart>() {
        override fun deserialize(
            parser: JsonParser,
            ctxt: DeserializationContext,
        ): Brevkode.Redigerbart =
            RedigerbarBrevkode(ctxt.readValue(parser, String::class.java))
    }
}
