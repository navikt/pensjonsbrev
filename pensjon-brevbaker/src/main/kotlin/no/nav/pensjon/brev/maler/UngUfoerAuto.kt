package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.gjelderFlereBarn_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.inntektstak_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.BarnetilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDtoSelectors.InnvilgetTilleggSelectors.utbetalt_safe
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
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*

// BrevTypeKode: PE_BA_04_505
@TemplateModelHelpers
object UngUfoerAuto : AutobrevTemplate<UngUfoerAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_UNG_UFOER_20_AAR_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = UngUfoerAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
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

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}