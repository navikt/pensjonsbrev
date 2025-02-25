package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.AbstractDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.BrukerImpl
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.FellesImpl
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetImpl
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlereImpl
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
        addDeserializer(SignerendeSaksbehandlere::class.java, SignerendeSaksbehandlereDeserializer)
        addDeserializer(Felles::class.java, FellesobjektetDeserializer)

        addDeserializer(LetterMarkup.Sakspart::class.java, object :
            FellesDeserializer<LetterMarkup.Sakspart, LetterMarkup.SakspartImpl>(LetterMarkup.SakspartImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.Signatur::class.java, object :
            FellesDeserializer<LetterMarkup.Signatur, LetterMarkup.SignaturImpl>(LetterMarkup.SignaturImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList, LetterMarkup.ParagraphContent.ItemListImpl>(LetterMarkup.ParagraphContent.ItemListImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList.Item::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList.Item, LetterMarkup.ParagraphContent.ItemListImpl.ItemImpl>(LetterMarkup.ParagraphContent.ItemListImpl.ItemImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Literal::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Literal, LetterMarkup.ParagraphContent.Text.LiteralImpl>(LetterMarkup.ParagraphContent.Text.LiteralImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Variable::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Variable, LetterMarkup.ParagraphContent.Text.VariableImpl>(LetterMarkup.ParagraphContent.Text.VariableImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.NewLine::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.NewLine, LetterMarkup.ParagraphContent.Text.NewLineImpl>(LetterMarkup.ParagraphContent.Text.NewLineImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.Attachment::class.java, object :
            FellesDeserializer<LetterMarkup.Attachment, LetterMarkup.AttachmentImpl>(LetterMarkup.AttachmentImpl::class.java) {}
        )
    }

    private object NavEnhetDeserializer : FellesDeserializer<NAVEnhet, NavEnhetImpl>(NavEnhetImpl::class.java)

    private object TelefonnummerDeserializer :
        FellesDeserializer<Telefonnummer, TelefonnummerImpl>(TelefonnummerImpl::class.java)

    private object FoedselsnummerDeserializer :
        FellesDeserializer<Foedselsnummer, FoedselsnummerImpl>(FoedselsnummerImpl::class.java)

    private object BrukerDeserializer :
        FellesDeserializer<Bruker, BrukerImpl>(BrukerImpl::class.java)

    private object SignerendeSaksbehandlereDeserializer :
        FellesDeserializer<SignerendeSaksbehandlere, SignerendeSaksbehandlereImpl>(SignerendeSaksbehandlereImpl::class.java)

    private object FellesobjektetDeserializer :
        FellesDeserializer<Felles, FellesImpl>(FellesImpl::class.java)

    private abstract class FellesDeserializer<T, V : T>(private val v: Class<V>) : JsonDeserializer<T>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): T =
            parser.codec.treeToValue(parser.codec.readTree<JsonNode>(parser), v)
    }
}