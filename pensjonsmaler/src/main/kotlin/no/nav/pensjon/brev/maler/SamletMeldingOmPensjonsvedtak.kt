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
        )
    ) {
        title {
            text(
            Bokmal to "Samlet melding om pensjonsvedtak",
                English to "TODO"
            )
        }
        outline {
            title1 {
                text(
                    Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                    English to "TODO"
                )
            }
            paragraph {
                text(
                    Bokmal to "I forbindelse med din søknad om ",
                    English to "TODO"
                )
                showIf(sakstype.equalTo(Sakstype.UFOREP)) {
                    text(
                        Bokmal to "uføretrygd",
                        English to "TODO"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.ALDER)) {
                    text(
                        Bokmal to "alderspensjon",
                        English to "TODO"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.GJENLEV)) {
                    text (
                        Bokmal to "etterlattepensjon",
                        English to "TODO"
                    )
                }
                text(
                    Bokmal to " fra EUs og EØS medlemsland legger vi ved",
                    English to "TODO"
                )
                list {
                    item {
                        text(
                            Bokmal to "P1 – Samlet melding om pensjonsvedtak",
                            English to "TODO"
                        )
                    }
                    item {
                        text(
                            Bokmal to "Informasjon om skjemaet P1 og hvordan det brukes",
                            English to "TODO"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "P1 gir deg oversikt over pensjonsvedtak fattet av trygdemyndigheter som har behandlet din søknad om ",
                    English to "TODO"
                )
                showIf(sakstype.equalTo(Sakstype.UFOREP)) {
                    text(
                        Bokmal to "uføretrygd",
                        English to "TODO"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.ALDER)) {
                    text(
                        Bokmal to "alderspensjon",
                        English to "TODO"
                    )
                }.orShowIf(sakstype.equalTo(Sakstype.GJENLEV)) {
                    text (
                        Bokmal to "etterlattepensjon",
                        English to "TODO"
                    )
                }
                text(
                    Bokmal to ".",
                    English to "TODO"
                )
            }
        }

    }
}