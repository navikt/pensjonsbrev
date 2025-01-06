package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OrienteringOmForlengetSaksbehandlingstid : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_05_041
    override val kode = Pesysbrevkoder.Redigerbar.PE_ORIENTERING_OM_FORLENGET_SAKSBEHANDLINGSTID
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om forlenget saksbehandlingstid",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Orientering om forlenget saksbehandlingstid",
                English to "Information about application processing delay",
            )
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to "Vi har ".expr() + fritekst("saksdato") + " mottatt din søknad om ".expr() +
                            fritekst("sak pensjon") + ". " + "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. " +
                            "Forsinkelsen skyldes ".expr() + fritekst("årsak til forsinkelse") + ".".expr(),
                    English to "We have received your application for ".expr() + fritekst("sak pensjon") +
                            " on the ".expr() + fritekst("dato") + ". " + "Due to delays in ".expr() +
                            fritekst("årsak til forsinkelse") + ", the processing of your case will take longer than we anticipated.".expr()
                )
            }
            title1 {
                text(
                    Bokmal to "Ny svartid",
                    English to "New estimated date for completion",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Vi antar at kravet ditt kan bli ferdigbehandlet innen ".expr() +
                            fritekst("antall dager/uker/måneder") + ".".expr(),
                    English to "Without further delays, we assume the processing of your case to be completed within ".expr() +
                            fritekst("antall dager/uker/måneder") + ".".expr()
                )
            }
            title1 {
                text(
                    Bokmal to "Meld fra om endringer",
                    English to "Please report changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi ber om at du holder oss orientert om forhold som kan betydning for avgjørelsen av søknaden din. " +
                            "Du kan melde fra om endringer på vår nettside ${Constants.NAV_URL}.",
                    English to "Please report to us if there are any circumstances that may affect your application. " +
                            "You can report changes on our website ${Constants.NAV_URL}."
                )
            }
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}