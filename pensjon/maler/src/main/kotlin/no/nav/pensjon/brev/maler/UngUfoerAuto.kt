package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.utbetalt
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.InnvilgetTilleggSelectors.utbetalt
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
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.*
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*

// BrevTypeKode: PE_BA_04_505
@TemplateModelHelpers
object UngUfoerAuto : AutobrevTemplate<UngUfoerAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_UNG_UFOER_20_AAR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Nav har regnet om uføretrygden din" },
                nynorsk { + "Nav har endra uføretrygda di" },
            )
        }

        outline {

            includePhrase(Vedtak.Overskrift)
            includePhrase(UngUfoer.UngUfoer20aar(kravVirkningFraOgMed))

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = totaltUfoerePerMnd,
                    ufoeretrygd = true.expr(),
                    ektefelle = ektefelle.safe { utbetalt }.ifNull(false),
                    gjenlevende = gjenlevende.safe { utbetalt }.ifNull(false),
                    fellesbarn = fellesbarn.safe { utbetalt }.ifNull(false),
                    saerkullsbarn = saerkullsbarn.safe { utbetalt }.ifNull(false),
                )
            )

            includePhrase(
                Barnetillegg.BarnetilleggIkkeUtbetalt(
                    saerkullInnvilget = saerkullsbarn.notNull(),
                    saerkullUtbetalt = saerkullsbarn.safe { utbetalt }.ifNull(false),
                    harFlereSaerkullsbarn = saerkullsbarn.safe { gjelderFlereBarn }.ifNull(false),
                    inntektstakSaerkullsbarn = saerkullsbarn.safe { inntektstak }.ifNull(Kroner(0)),
                    fellesInnvilget = fellesbarn.notNull(),
                    fellesUtbetalt = fellesbarn.safe { utbetalt }.ifNull(false),
                    harFlereFellesBarn = fellesbarn.safe { gjelderFlereBarn }.ifNull(false),
                    inntektstakFellesbarn = fellesbarn.safe { inntektstak }.ifNull(Kroner(0)),
                )
            )


            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(UngUfoer.EndringMinsteYtelseUngUfoerVed20aar(minsteytelseVedVirkSats))
            includePhrase(Ufoeretrygd.HjemmelSivilstand)

            includePhrase(Ufoeretrygd.VirkningFomOverskrift)
            includePhrase(Ufoeretrygd.VirkningFraOgMed(kravVirkningFraOgMed))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}