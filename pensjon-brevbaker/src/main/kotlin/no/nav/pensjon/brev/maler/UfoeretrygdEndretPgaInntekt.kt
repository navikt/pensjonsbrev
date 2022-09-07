package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.antallFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.brukersSivilstandUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggSaerkullsBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.virkningFraOgMed
import no.nav.pensjon.brev.maler.fraser.UfoeretrygdEndretPgaInntektTitle
import no.nav.pensjon.brev.maler.fraser.common.Felles.SivilstandEPSUbestemtForm
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class UfoeretrygdEndretPgaInntektDto(
    val beloepGammelUfoeretrygd: Kroner,
    val beloepNyUfoeretrygd: Kroner,
    val beloepGammelBarnetillegSaerkullsbarn: Kroner,
    val beloepNyBarnetillegSaerkullsbarn: Kroner,
    val beloepGammelBarnetillegFellesBarn: Kroner,
    val beloepNyBarnetillegFellesBarn: Kroner,
    val harInnvilgetBarnetilleggFellesBarn: Boolean,
    val harInnvilgetBarnetilleggSaerkullsBarn: Boolean,
    val brukersSivilstandUfoeretrygd: Sivilstand,
    val virkningFraOgMed: LocalDate,
    val antallFellesBarn: Int,
)

@TemplateModelHelpers
object UfoeretrygdEndretPgaInntekt : VedtaksbrevTemplate<UfoeretrygdEndretPgaInntektDto> {
    override val kode = Brevkode.Vedtak.UFOER_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        kode.name,
        letterDataType = UfoeretrygdEndretPgaInntektDto::class,
        languages(Bokmal),
        letterMetadata = LetterMetadata(
            "Vedtak – endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK
        )
    ) {
        val harFlereBarnetillegg = harInnvilgetBarnetilleggFellesBarn and harInnvilgetBarnetilleggSaerkullsBarn
        val harEndretBarnetillegg =
            (beloepGammelBarnetillegSaerkullsbarn.notEqualTo(beloepNyBarnetillegSaerkullsbarn)
                    or beloepGammelBarnetillegFellesBarn.notEqualTo(beloepNyBarnetillegFellesBarn))
        val harEndretUfoeretrygd = beloepGammelUfoeretrygd.notEqualTo(beloepNyUfoeretrygd)
        val harFlereFellesBarn = antallFellesBarn.greaterThan(1)

        title {
            includePhrase(
                UfoeretrygdEndretPgaInntektTitle(
                    harEndretUfoeretrygd = harEndretUfoeretrygd,
                    harFlereBarnetillegg = harFlereBarnetillegg,
                    harEndretBarnetillegg = harEndretBarnetillegg,
                )
            )
        }

        outline {
            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger om inntekten ",
                )
                showIf(not(harInnvilgetBarnetilleggFellesBarn)) {
                    text(
                        Bokmal to "din.",
                    )
                }.orShow {
                    text(Bokmal to "til deg eller din ")
                    includePhrase(SivilstandEPSUbestemtForm(brukersSivilstandUfoeretrygd))
                    text(Bokmal to ". Inntekten til din ")
                    includePhrase(SivilstandEPSUbestemtForm(brukersSivilstandUfoeretrygd))
                    text(
                        Bokmal to "har kun betydning for størrelsen på barnetillegget "
                    )
                    showIf(harFlereBarnetillegg) {
                        textExpr(
                            Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet")
                                    + " som bor med begge foreldre.",
                        )
                    }.orShow {
                        text(Bokmal to "ditt."
                        )
                    }
                }
                // Utbetalingen av uføretrygden din og barnetillegget ditt barnetilleggene dine er derfor endret fra <PE_VedtaksData_VirkningFOM>.
                text(
                    Bokmal to " Utbetalingen av "
                )

                showIf(harEndretUfoeretrygd) {
                    text(
                        Bokmal to "ufoeretrygden din"
                    )
                    showIf(harEndretBarnetillegg) {
                        text(
                            Bokmal to " og "
                        )
                    }
                }
                showIf(harEndretBarnetillegg){
                    showIf(harFlereBarnetillegg){
                        text(
                            Bokmal to "barnetilleggene dine"
                        )
                    }.orShow {
                        text(
                            Bokmal to "barnetillegget ditt"
                        )
                    }
                }
                textExpr(
                    Bokmal to " er derfor endret fra".expr() + virkningFraOgMed.format()
                )
                textExpr(
                    Bokmal to "Vi har mottatt nye opplysninger om inntekten din til deg eller din <PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT>.".expr() +
                            " Inntekten til din <PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT> har kun betydning for størrelsen på barnetillegget for <PE_UT_Barnet_Barna_Felles>".expr() +
                            " som bor med begge sine foreldreditt. Utbetalingen av uføretrygden din og barnetillegget ditt barnetilleggene dine er derfor endret fra <PE_VedtaksData_VirkningFOM>.".expr()
                )
            }
        }
    }
}