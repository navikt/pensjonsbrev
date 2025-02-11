package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.dinPensjonsutbetaling
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalderAuto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
                Nynorsk to "",
                English to ""
            )
        }

        outline {
            title2 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + uttaksgrad.format()+ " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
                    Nynorsk to "",
                    English to ""
                )
            }

            title2 {
                text(
                    Bokmal to "Slik har vi beregnet",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller normert pensjonsalder, må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året.",
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() + " ville du fått ".expr() + dinPensjonsutbetaling.format() + " kroner årlig i pensjon.",
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Beregningen er uavhengig av din faktisk sivilstand.",
                    Nynorsk to "",
                    English to ""
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}
