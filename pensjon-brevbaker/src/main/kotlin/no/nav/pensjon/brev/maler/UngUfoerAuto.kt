package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate

data class UngUfoerAutoDto(
    val kravVirkningFraOgMed: LocalDate,
    val totaltUfoerePerMnd: Int,
    val utbetalt: Set<Ufoeretrygd.Tillegg>,
    val innvilget: Set<Ufoeretrygd.Tillegg>,
    val antallFellesbarn: Int,
    val antallSaerkullsbarn: Int,
    val inntektstakFellesbarn: Int,
    val inntektstakSaerkullsbarn: Int,
    val minsteytelseVedVirkSats: Double,
)

object UngUfoerAuto : StaticTemplate {
    override val template = createTemplate(
        name = "PE_BA_04_505",
        base = PensjonLatex,
        letterDataType = UngUfoerAutoDto::class,
        lang = languages(Bokmal, Nynorsk),
        title = newText(
            Bokmal to "NAV har regnet om uføretrygden din",
            Nynorsk to "NAV har endra uføretrygda di",
        ),
        letterMetadata = LetterMetadata(
            "Vedtak – ung ufør ved 20 år",
            isSensitiv = true,
        )
    ) {
        outline {
            val virkningsDato = argument().select(UngUfoerAutoDto::kravVirkningFraOgMed).map(::KravVirkningFraOgMed)

            includePhrase(Vedtak.overskrift)
            includePhrase(virkningsDato, Ufoeretrygd.ungUfoer20aar_001)
            includePhrase(argument().map { Ufoeretrygd.BeloepPerMaaned(Kroner(it.totaltUfoerePerMnd), it.utbetalt) }, Ufoeretrygd.beloep)
            includePhrase(
                argument().map {
                    Ufoeretrygd.BarnetilleggIkkeUtbetaltDto(
                        antallFellesbarn = it.antallFellesbarn,
                        antallSaerkullsbarn = it.antallSaerkullsbarn,
                        inntektstakFellesbarn = Kroner(it.inntektstakFellesbarn),
                        inntektstakSaerkullsbarn = Kroner(it.inntektstakSaerkullsbarn),
                        utbetalt = it.utbetalt,
                        innvilget = it.innvilget
                    )
                },
                Ufoeretrygd.barnetileggIkkeUtbetalt
            )

            includePhrase(Ufoeretrygd.vedtakBegrunnelseOverskrift)
            includePhrase(argument().map { GrunnbeloepSats(it.minsteytelseVedVirkSats) }, Ufoeretrygd.ungUfoerHoeyereVed20aar)
            includePhrase(Ufoeretrygd.hjemmelSivilstand)

            includePhrase(Ufoeretrygd.virkningFomOverskrift)
            includePhrase(virkningsDato, Ufoeretrygd.virkningFraOgMed)

            includePhrase(Felles.meldEndringerPesys_001)
            includePhrase(Felles.rettTilKlagePesys_001)
            includePhrase(Felles.rettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.sjekkUtbetalingene)

        }
    }
}