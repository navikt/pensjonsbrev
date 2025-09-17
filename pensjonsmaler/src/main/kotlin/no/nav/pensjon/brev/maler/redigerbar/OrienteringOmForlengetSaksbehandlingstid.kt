package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OrienteringOmForlengetSaksbehandlingstid : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_05_041
    override val kode = Pesysbrevkoder.Redigerbar.PE_ORIENTERING_OM_FORLENGET_SAKSBEHANDLINGSTID
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
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
                bokmal { + "Orientering om forlenget saksbehandlingstid" },
                english { + "Information about application processing delay" },
            )
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                val ytelse = fritekst("ytelse")
                val aarsak = fritekst("årsak til forsinkelse")

                text(
                    bokmal { + "Vi har " + dato + " mottatt din søknad om " + ytelse + ". "
                            + "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. "
                            + "Forsinkelsen skyldes " + aarsak + "." },
                    english { + "We have received your application for " + ytelse +
                            " on the " + dato + ". " + "Due to delays in " +
                            aarsak + ", the processing of your case will take longer than we anticipated." }
                )
            }
            title1 {
                text(
                    bokmal { + "Ny svartid" },
                    english { + "New estimated date for completion" },
                )
            }
            paragraph {
                val frist = fritekst("antall dager/uker/måneder")
                text(
                    bokmal { + "Vi antar at kravet ditt kan bli ferdigbehandlet innen " + frist + "." },
                    english { + "Without further delays, we assume the processing of your case to be completed within " + frist + "." }
                )
            }
            title1 {
                text(
                    bokmal { + "Meld fra om endringer" },
                    english { + "Please report changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi ber om at du holder oss orientert om forhold som kan betydning for avgjørelsen av søknaden din. " +
                            "Du kan melde fra om endringer på vår nettside ${Constants.NAV_URL}." },
                    english { + "Please report to us if there are any circumstances that may affect your application. " +
                            "You can report changes on our website ${Constants.NAV_URL}." }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}