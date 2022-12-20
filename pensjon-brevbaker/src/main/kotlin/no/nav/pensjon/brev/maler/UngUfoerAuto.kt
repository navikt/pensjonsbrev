package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.InnvilgetTilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.ektefelle
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.fellesbarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.gjenlevende
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.kravVirkningFraOgMed
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.minsteytelseVedVirkSats
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.saerkullsbarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.totaltUfoerePerMnd
import no.nav.pensjon.brev.maler.fraser.UngUfoer
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

// BrevTypeKode: PE_BA_04_505
@TemplateModelHelpers
object UngUfoerAuto : VedtaksbrevTemplate<UngUfoerAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UNG_UFOER_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = UngUfoerAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
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

            includePhrase(Vedtak.Overskrift)
            includePhrase(UngUfoer.UngUfoer20aar(kravVirkningFraOgMed))

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = totaltUfoerePerMnd,
                    ufoeretrygd = true.expr(),
                    ektefelle = ektefelle.utbetalt_safe.ifNull(false),
                    gjenlevende = gjenlevende.utbetalt_safe.ifNull(false),
                    fellesbarn = fellesbarn.utbetalt_safe.ifNull(false),
                    saerkullsbarn = saerkullsbarn.utbetalt_safe.ifNull(false),
                )
            )

            includePhrase(
                Barnetillegg.BarnetilleggIkkeUtbetalt(
                    saerkullInnvilget = saerkullsbarn.notNull(),
                    saerkullUtbetalt = saerkullsbarn.utbetalt_safe.ifNull(false),
                    harFlereSaerkullsbarn = saerkullsbarn.gjelderFlereBarn_safe.ifNull(false),
                    inntektstakSaerkullsbarn = saerkullsbarn.inntektstak_safe.ifNull(Kroner(0)),
                    fellesInnvilget = fellesbarn.notNull(),
                    fellesUtbetalt = fellesbarn.utbetalt_safe.ifNull(false),
                    harFlereFellesBarn = fellesbarn.gjelderFlereBarn_safe.ifNull(false),
                    inntektstakFellesbarn = fellesbarn.inntektstak_safe.ifNull(Kroner(0)),
                )
            )


            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(UngUfoer.EndringMinsteYtelseUngUfoerVed20aar(minsteytelseVedVirkSats))
            includePhrase(Ufoeretrygd.HjemmelSivilstand)

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)
            includePhrase(Ufoeretrygd.VirkningFraOgMed(kravVirkningFraOgMed))

            includePhrase(Felles.MeldEndringerPesys_001)
            includePhrase(Felles.RettTilKlagePesys_001)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)

        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}