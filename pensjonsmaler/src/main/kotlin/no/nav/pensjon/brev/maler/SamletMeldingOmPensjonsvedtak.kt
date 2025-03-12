package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDtoSelectors.sakstype
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType

@TemplateModelHelpers
object SamletMeldingOmPensjonsvedtak : AutobrevTemplate<SamletMeldingOmPensjonsvedtakDto> {
    override val kode = Pesysbrevkoder.AutoBrev.P1_SAMLET_MELDING_OM_PENSJONSVEDTAK // 000090

    override val template = createTemplate(
        name = kode.name,
        letterDataType = SamletMeldingOmPensjonsvedtakDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Samlet melding om pensjonsvedtak",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
        pdfVedlegg = listOf(
            PDFVedlegg(
                PDFVedleggType.P1, mapOf(
                    "holder" to mapOf(
                        "fornavn" to "Peder",
                        "etternavn" to "Ås",
                        "etternavn_foedsel" to "Holm",
                        "gateadresse" to "Fyrstikkalleen 1",
                        "landkode" to "DK",
                        "postkode" to "1000",
                        "by" to "Helsfyr"
                    )
                )
            )
        )
    ) {
        title {
            text(
                Bokmal to "Samlet melding om pensjonsvedtak",
                English to "Summary of Pension Decisions"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                    English to "P1 – Summary of Pension Decisions"
                )
            }
            paragraph {
                text(
                    Bokmal to "I forbindelse med din søknad om ",
                    English to "Your application for "
                )
                showIf(sakstype.equalTo(Sakstype.UFOREP)) {
                    text(
                        Bokmal to "uføretrygd",
                        English to "invalidity pension"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.ALDER)) {
                    text(
                        Bokmal to "alderspensjon",
                        English to "old age pension"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.GJENLEV)) {
                    text(
                        Bokmal to "etterlattepensjon",
                        English to "survivors pension"
                    )
                }
                text(
                    Bokmal to " fra EUs og EØS medlemsland legger vi ved",
                    English to " from EU/EEA member countries, we enclose:"
                )
                list {
                    item {
                        text(
                            Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                            English to "P1 – Summary of Pension Decisions"
                        )
                    }
                    item {
                        text(
                            Bokmal to "Informasjon om skjemaet P1 og hvordan det brukes",
                            English to "Information about the P1 form and its use"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "P1 gir deg oversikt over pensjonsvedtak fattet av trygdemyndigheter som har behandlet din søknad om ",
                    English to "The P1 form provides an overview of the decisions taken in your case by the various institutions in the EU/EEA member countries."
                )
                showIf(sakstype.equalTo(Sakstype.UFOREP)) {
                    text(
                        Bokmal to "uføretrygd",
                        English to ""
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.ALDER)) {
                    text(
                        Bokmal to "alderspensjon",
                        English to ""
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.GJENLEV)) {
                    text(
                        Bokmal to "etterlattepensjon",
                        English to ""
                    )
                }
                text(
                    Bokmal to ".",
                    English to ""
                )
            }
        }

    }
}