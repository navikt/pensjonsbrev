package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.kravVirkningFraOgMed
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.minsteytelseVedVirkSats
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.GrunnbeloepSats
import no.nav.pensjon.brev.maler.fraser.common.KravVirkningFraOgMed
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevTypeKode: PE_BA_04_505
@TemplateModelHelpers
object UngUfoerAuto : VedtaksbrevTemplate<UngUfoerAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UNG_UFOER_AUTO

    override val template = createTemplate(
        name = kode.name,
        base = PensjonLatex,
        letterDataType = UngUfoerAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – ung ufør ved 20 år",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
        )
    ) {
        title {
            text(
                Bokmal to "NAV har regnet om uføretrygden din",
                Nynorsk to "NAV har endra uføretrygda di",
            )
        }

        outline {
            val virkningsDato = kravVirkningFraOgMed.select(::KravVirkningFraOgMed)

            includePhrase(Vedtak.overskrift)
            includePhrase(Ufoeretrygd.ungUfoer20aar_001, virkningsDato)

            includePhrase(
                Ufoeretrygd.beloep,
                argument().map {
                    Ufoeretrygd.BeloepPerMaaned(
                        perMaaned = it.totaltUfoerePerMnd,
                        ektefelle = it.ektefelle?.utbetalt ?: false,
                        gjenlevende = it.gjenlevende?.utbetalt ?: false,
                        fellesbarn = it.fellesbarn?.utbetalt ?: false,
                        saerkullsbarn = it.saerkullsbarn?.utbetalt ?: false
                    )
                }
            )

            includePhrase(
                Ufoeretrygd.barnetilleggIkkeUtbetalt,
                argument().map {
                    Ufoeretrygd.BarnetilleggIkkeUtbetaltDto(
                        saerkullsbarn = it.saerkullsbarn,
                        fellesbarn = it.fellesbarn,
                    )
                }
            )

            includePhrase(Vedtak.begrunnelseOverskrift)
            includePhrase(Ufoeretrygd.ungUfoerHoeyereVed20aar, minsteytelseVedVirkSats.select(::GrunnbeloepSats))
            includePhrase(Ufoeretrygd.hjemmelSivilstand)

            includePhrase(Ufoeretrygd.virkningFomOverskrift)
            includePhrase(Ufoeretrygd.virkningFraOgMed, virkningsDato)

            includePhrase(Felles.meldEndringerPesys_001)
            includePhrase(Felles.rettTilKlagePesys_001)
            includePhrase(Felles.rettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.sjekkUtbetalingene)

        }

        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(orienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}