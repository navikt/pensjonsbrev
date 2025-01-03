package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
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
                text(
                    Bokmal to "Vi har <Fritekst: sakdato> mottatt din søknad om <Fritekst: sak pensjon>. " +
                    "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. Forsinkelsen skyldes <Fritekst: årsak til forsinkelse>.",
                    English to "We have received your application for <Fritekst: sak pensjon> on the <Fritekst: dato>. " +
                    "Due to delays in <Fritekst: årsak til forsinkelse> the processing of your case will take longer than we anticipated.",
                )
            }
            title1 {
                text(
                    Bokmal to "Ny svartid",
                    English to "New estimated date for completion",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi antar at kravet ditt kan bli ferdigbehandlet innen <Fritekst: antall dager/uker/måneder>.",
                    English to "Without further delays we assume the processing of your case to be complete within <Fritekst: antall dager/uker/måneder>"
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
            paragraph {
                text(
                    Bokmal to "Ta gjerne kontakt med oss på telefon ${Constants.navKontaktsenterPensjon} hvis du ønsker mer informasjon. " +
                            "Husk at du kan finne mer informasjon om regelverket på ${Constants.NAV_URL}.",
                    English to "Please contact Nav at tel. +47 ${Constants.navKontaktsenterPensjon} if you would like more information. " +
                            "Remember that you can find more information about the regulations at ${Constants.NAV_URL}."
                )
            }
        }
    }
}