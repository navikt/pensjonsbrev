package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Alderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
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
object InnhentingDokumentasjonFraBruker : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_047
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_DOKUMENTASJON_FRA_BRUKER
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhente dokumentasjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Du må sende oss flere opplysninger",
                English to "Collection of documentation",
            )
        }
        outline {

            paragraph {
                textExpr(
                    Bokmal to "Vi har mottatt en ".expr() + fritekst("blankett/brev/henvendelse") + " fra deg " + fritekst("dato") + ". Du har opplyst at " +
                            fritekst("opplysning") + ".",
                    English to "We have received a ".expr() + fritekst("blankett/brev/henvendelse") + " from you dated " + fritekst("dato") + ". You have told us that " +
                            fritekst("opplysning") + ".",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "For at opplysningene skal bli tatt i betraktning, må opplysningene dokumenteres. Vi ber deg derfor om å sende oss dokumentasjon innen ".expr() +
                            fritekst("dato") + " til følgende adresse:",
                    English to "In order for these details to be taken into consideration, we need documentary support for this information. We would therefore ask you to send us that documentary evidence by ".expr() +
                            fritekst("dato") + " at the following address:",
                )
            }

            includePhrase(Alderspensjon.Returadresse)

            paragraph {
                text(
                    Bokmal to "Som dokumentasjon for inntekten, både person- og kapitalinntekten, kan du for eksempel sende kopi av ligningen, kopier av lønnsslipper fra de tre siste månedene, bekreftelse fra arbeidsgiveren, kopier av lønns- og trekkoppgaver, bekreftelse fra regnskapsfører eller årsoppgaver fra banken.",
                    English to "Valid documentation of income can be copy of tax assessment, copies of payslips, or the last three months copies of pay and tax deduction statements, confirmation from your employer, confirmation by your accountant or annual statements from your bank. Both personal and investment income should be documented.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom vi ikke har mottatt ny dokumentasjon innen den angitte fristen i dette brevet, kan vi ikke legge disse opplysningene til grunn for videre saksbehandling.",
                    English to "Undocumented statements from yourself will not be taken into account. Please provide the required documentation by the deadline given in this letter.",
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}