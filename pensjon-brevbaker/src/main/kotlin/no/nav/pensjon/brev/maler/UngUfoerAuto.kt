package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

// BrevTypeKode: PE_BA_04_505
object UngUfoerAuto : StaticTemplate {
    override val template = createTemplate(
        name = "UP_FULLTT_BELOPENDR",
        base = PensjonLatex,
        letterDataType = UngUfoerAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            "Vedtak – ung ufør ved 20 år",
            isSensitiv = true,
        )
    ) {
        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har endra uføretrygda di",
            )
        }

        outline {
            val virkningsDato = argument().select(UngUfoerAutoDto::kravVirkningFraOgMed).map(::KravVirkningFraOgMed)

            includePhrase(Vedtak.overskrift)
            includePhrase(Ufoeretrygd.ungUfoer20aar_001, virkningsDato)

            includePhrase(
                Ufoeretrygd.beloep,
                argument().map {
                    Ufoeretrygd.BeloepPerMaaned(
                        perMaaned = Kroner(it.totaltUfoerePerMnd),
                        ektefelle = it.ektefelle?.utbetalt ?: false,
                        gjenlevende = it.gjenlevende?.utbetalt ?: false,
                        fellesbarn = it.fellesbarn?.utbetalt ?: false,
                        saerkullsbarn = it.saerkullsbarn?.utbetalt ?: false
                    )
                }
            )

            includePhrase(
                Ufoeretrygd.barnetileggIkkeUtbetalt,
                argument().map {
                    Ufoeretrygd.BarnetilleggIkkeUtbetaltDto(
                        saerkullsbarn = it.saerkullsbarn,
                        fellesbarn = it.fellesbarn,
                    )
                }
            )

            includePhrase(Ufoeretrygd.vedtakBegrunnelseOverskrift)
            includePhrase(Ufoeretrygd.ungUfoerHoeyereVed20aar, argument().map { GrunnbeloepSats(it.minsteytelseVedVirkSats) })
            includePhrase(Ufoeretrygd.hjemmelSivilstand)

            includePhrase(Ufoeretrygd.virkningFomOverskrift)
            includePhrase(Ufoeretrygd.virkningFraOgMed, virkningsDato)

            includePhrase(Felles.meldEndringerPesys_001)
            includePhrase(Felles.rettTilKlagePesys_001)
            includePhrase(Felles.rettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.sjekkUtbetalingene)

        }

        // TODO: Inkluder vedlegg "Dette er din månedlige uføretrygd før skatt"
        // TODO: Inkluder vedlegg "Orientering om rettigheter og plikter"
    }
}