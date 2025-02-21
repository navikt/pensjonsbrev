package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerImpl
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetImpl
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.TelefonnummerImpl

@OptIn(InterneDataklasser::class)
object FellesModule : SimpleModule() {
    private fun readResolve(): Any = FellesModule

    init {
        addDeserializer(NAVEnhet::class.java, NavEnhetDeserializer)
        addDeserializer(Telefonnummer::class.java, TelefonnummerDeserializer)
        addDeserializer(Foedselsnummer::class.java, FoedselsnummerDeserializer)
        addDeserializer(Bruker::class.java, BrukerDeserializer)
    }

    private object NavEnhetDeserializer : FellesDeserializer<NAVEnhet, NavEnhetImpl>(NavEnhetImpl::class.java)

    private object TelefonnummerDeserializer :
        FellesDeserializer<Telefonnummer, TelefonnummerImpl>(TelefonnummerImpl::class.java)

    private object FoedselsnummerDeserializer :
        FellesDeserializer<Foedselsnummer, FoedselsnummerImpl>(FoedselsnummerImpl::class.java)

    private object BrukerDeserializer :
        FellesDeserializer<Bruker, Bruker>(Bruker::class.java)


    private abstract class FellesDeserializer<T, V : T>(private val v: Class<V>) : JsonDeserializer<T>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T =
            parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), v)
    }
}