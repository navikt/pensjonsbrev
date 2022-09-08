package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.aarFoerVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.antallFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.brukersSivilstandUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.forventetInntektAvkoret
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.fyller67IVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggSaerkullsBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.ufoeregrad
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.utbetalingsgrad
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.virkningFraOgMed
import no.nav.pensjon.brev.maler.fraser.UfoeretrygdEndretPgaInntekt
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import java.time.LocalDate
import java.time.Month

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
    val forventetInntektAvkortet: Kroner,
    val forventetInntektAvkoret: Kroner,
    val aarFoerVirkningsAar: Year,
    val ufoeregrad: Double,
    val utbetalingsgrad: Double,
    val fyller67IVirkningsAar: Boolean,
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
                UfoeretrygdEndretPgaInntekt.Tittel(
                    harEndretUfoeretrygd = harEndretUfoeretrygd,
                    harFlereBarnetillegg = harFlereBarnetillegg,
                    harEndretBarnetillegg = harEndretBarnetillegg,
                )
            )
        }

        outline {
            showIf(virkningFraOgMed.month.equalTo(Month.JANUARY) and virkningFraOgMed.day.equalTo(1)) {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningReduksjon(
                        forventetInntektAvkoret = forventetInntektAvkoret,
                        virkningFraOgMedAar = virkningFraOgMed.year,
                        aarFoerVirkningsAar = aarFoerVirkningsAar,
                        ufoeregrad = ufoeregrad,
                        utbetalingsgrad = utbetalingsgrad,
                        fyller67IVirkningsAar = fyller67IVirkningsAar,
                    )
                )
            }.orShow {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningInntektsendring(
                        harFlereFellesBarn = harFlereFellesBarn,
                        harEndretBarnetillegg = harEndretBarnetillegg,
                        harEndretUfoeretrygd = harEndretUfoeretrygd,
                        harFlereBarnetillegg = harFlereBarnetillegg,
                        harInnvilgetBarnetilleggFellesBarn = harInnvilgetBarnetilleggFellesBarn,
                        brukersSivilstandUfoeretrygd = brukersSivilstandUfoeretrygd,
                        virkningFraOgMed = virkningFraOgMed,
                    )
                )
            }
        }
    }
}