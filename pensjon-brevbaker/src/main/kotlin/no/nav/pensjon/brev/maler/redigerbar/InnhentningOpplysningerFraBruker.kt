package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDtoSelectors.BrevDataSelectors.avsenderEnhetAdresselinje1
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDtoSelectors.BrevDataSelectors.avsenderEnhetAdresselinje2
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDtoSelectors.BrevDataSelectors.avsenderEnhetLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDtoSelectors.BrevDataSelectors.avsenderEnhetNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.InnhentingAvInformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Alderspensjon
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnhentningOpplysningerFraBruker : RedigerbarTemplate<InnhentningOpplysningerFraBrukerDto> {

    override val kode = Brevkode.Redigerbar.PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnhentningOpplysningerFraBrukerDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhente opplysninger",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // TODO viktig eller annet?
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
                    Bokmal to pesysData.avsenderEnhetNavn + " har mottatt en <fritekst: blankett/brev/henvendelse> fra deg <fritekst: dato>. For å kunne behandle henvendelsen mangler vi følgende opplysninger:",
                    English to pesysData.avsenderEnhetNavn + " received a <free text: form/letter/request> from you on <free text: date>. In order to process your request, we need the following information from you:",
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
                            Bokmal to "Opplysning 2",
                            English to "Details 2",
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

            includePhrase(InnhentingAvInformasjon.Returadresse(
                avsenderEnhetNavn = pesysData.avsenderEnhetNavn,
                avsenderEnhetAdresselinje1 = pesysData.avsenderEnhetAdresselinje1,
                avsenderEnhetAdresselinje2 = pesysData.avsenderEnhetAdresselinje2,
                avsenderEnhetLand = pesysData.avsenderEnhetLand,
            ))

            includePhrase(Alderspensjon.HarDuSpoersmaal)
        }
    }
    override val kategori: TemplateDescription.Brevkategori
        get() = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
}

