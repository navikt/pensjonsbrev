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
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.navn

@TemplateModelHelpers
object InnhentingOpplysningerFraBruker : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_048
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhente opplysninger",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Du må sende oss flere opplysninger",
                English to "Collection of detailed information",
            )
        }

        outline {

            paragraph {
                textExpr(
                    Bokmal to felles.avsenderEnhet.navn + " har mottatt en <fritekst: blankett/brev/henvendelse> fra deg <fritekst: dato>. For å kunne behandle henvendelsen mangler vi følgende opplysninger:<fritekst:",
                    English to felles.avsenderEnhet.navn + " received a <free text: form/letter/request> from you on <free text: date>. In order to process your request, we need the following information from you:<free text:",
                )
                list {
                    item {
                        text(
                            Bokmal to "Opplysning 1",
                            English to "Details 1",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Opplysning 2>",
                            English to "Details 2>",
                        )
                    }
                }
            }


            paragraph {
                text(
                    Bokmal to "Vi ber deg derfor om å sende oss overnevnte opplysninger innen <fritekst: dato> til adresse:",
                    English to "Please send us the above information by <free text: date>, to the following address:",
                )
            }

            includePhrase(Alderspensjon.Returadresse)

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}

