package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.utbetalt_safe
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
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.gjeldendeBeregnetUTPerMaaned
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDtoSelectors.tidligereUfoeretrygdPerioder
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.ordinaerUTBeloepBrutto_safe
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdPerMaanedSelectors.totalUTBeloepNetto_safe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedtak.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
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
            includePhrase(Ufoeretrygd.UngUfoer20aar_001(kravVirkningFraOgMed))

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = totaltUfoerePerMnd,
                    ektefelle = ektefelle.utbetalt_safe.ifNull(false),
                    gjenlevende = gjenlevende.utbetalt_safe.ifNull(false),
                    fellesbarn = fellesbarn.utbetalt_safe.ifNull(false),
                    saerkullsbarn = saerkullsbarn.utbetalt_safe.ifNull(false),
                )
            )

            includePhrase(Ufoeretrygd.BarnetilleggIkkeUtbetalt(saerkullsbarn = saerkullsbarn, fellesbarn = fellesbarn))


            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(Ufoeretrygd.EndringMinsteYtelseUngUfoerVed20aar(minsteytelseVedVirkSats))
            includePhrase(Ufoeretrygd.HjemmelSivilstand)

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)
            includePhrase(Ufoeretrygd.VirkningFraOgMed(kravVirkningFraOgMed))

            includePhrase(Felles.MeldEndringerPesys_001)
            includePhrase(Felles.RettTilKlagePesys_001)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)

        }

        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt,
            with(maanedligUfoeretrygdFoerSkatt) {
                tidligereUfoeretrygdPerioder.isNotEmpty() or
                        gjeldendeBeregnetUTPerMaaned.ordinaerUTBeloepBrutto_safe
                            .notEqualTo(gjeldendeBeregnetUTPerMaaned.totalUTBeloepNetto_safe)
            }
        )
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}