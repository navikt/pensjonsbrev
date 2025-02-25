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
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl
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
            FellesDeserializer<LetterMarkup.Sakspart, LetterMarkupImpl.SakspartImpl>(LetterMarkupImpl.SakspartImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.Signatur::class.java, object :
            FellesDeserializer<LetterMarkup.Signatur, LetterMarkupImpl.SignaturImpl>(LetterMarkupImpl.SignaturImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList, ParagraphContentImpl.ItemListImpl>(ParagraphContentImpl.ItemListImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.ItemList.Item::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.ItemList.Item, ParagraphContentImpl.ItemListImpl.ItemImpl>(ParagraphContentImpl.ItemListImpl.ItemImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Literal::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Literal, ParagraphContentImpl.TextImpl.LiteralImpl>(ParagraphContentImpl.TextImpl.LiteralImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.Variable::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.Variable, ParagraphContentImpl.TextImpl.VariableImpl>(ParagraphContentImpl.TextImpl.VariableImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.ParagraphContent.Text.NewLine::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Text.NewLine, ParagraphContentImpl.TextImpl.NewLineImpl>(ParagraphContentImpl.TextImpl.NewLineImpl::class.java) {}
        )

        addDeserializer(LetterMarkup.Attachment::class.java, object :
            FellesDeserializer<LetterMarkup.Attachment, LetterMarkupImpl.AttachmentImpl>(LetterMarkupImpl.AttachmentImpl::class.java) {}
        )


        addDeserializer(LetterMarkup.ParagraphContent.Table::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table, ParagraphContentImpl.TableImpl>(ParagraphContentImpl.TableImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Row::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Row, ParagraphContentImpl.TableImpl.RowImpl>(ParagraphContentImpl.TableImpl.RowImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Cell::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Cell, ParagraphContentImpl.TableImpl.CellImpl>(ParagraphContentImpl.TableImpl.CellImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.Header::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.Header, ParagraphContentImpl.TableImpl.HeaderImpl>(ParagraphContentImpl.TableImpl.HeaderImpl::class.java) {}
        )
        addDeserializer(LetterMarkup.ParagraphContent.Table.ColumnSpec::class.java, object :
            FellesDeserializer<LetterMarkup.ParagraphContent.Table.ColumnSpec, ParagraphContentImpl.TableImpl.ColumnSpecImpl>(ParagraphContentImpl.TableImpl.ColumnSpecImpl::class.java) {}
        )

        addDeserializer(LetterMarkup::class.java, object :
            FellesDeserializer<LetterMarkup, LetterMarkupImpl>(LetterMarkupImpl::class.java) {}
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