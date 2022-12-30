package no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningUT

import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.harFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.FellesbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.avkortningsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.erRedusertMotinntekt
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.fribeloepEllerInntektErPeriodisert
import no.nav.pensjon.brev.api.model.vedlegg.SaerkullsbarnSelectors.justeringsbeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or

barnetilleggGjeldende.fellesbarn_safe, barnetilleggGjeldende.saerkullsbarn_safe,
) { fellesTillegg, saerkullTillegg ->
    val harAnvendtTrygdetidUnder40 = trygdetidsdetaljerGjeldende.anvendtTT.lessThan(40)

    // TODO -HH dette kan sjekkes med saerkullTillegg.notNull(), men over i ifnotnull sjekker vi at du har både barnetillegg felles barn og saerkullsbarn
    val harSaerkullsbarn = saerkullTillegg.innvilgetBarnetillegg
    val harFellesbarn = fellesTillegg.innvilgetBarnetillegg

    val harTilleggForFlereFellesbarn = fellesTillegg.harFlereBarn
    val harTilleggForFlereSaerkullsbarn = fellesTillegg.harFlereBarn


    showIf(fellesTillegg.erRedusertMotinntekt or saerkullTillegg.erRedusertMotinntekt) {
        includePhrase(SlikBeregnBTOverskrift)
        includePhrase(VedleggBeregnUTInnlednBT)
    }

    showIf(harFellesbarn and not(harSaerkullsbarn)) {
        includePhrase(FastsetterStoerelsenPaaBTFellesbarn(harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40))
    }

    showIf(harSaerkullsbarn and not(harFellesbarn)) {
        includePhrase(FastsetterStoerelsenPaaBTSaerkullsbarn(harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40))
    }

    showIf(harFellesbarn and harSaerkullsbarn) {
        includePhrase(
            FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
                harAnvendtTrygdetidUnder40 = harAnvendtTrygdetidUnder40,
                harTilleggForFlereFellesbarn = harTilleggForFlereFellesbarn,
                harTilleggForFlereSaerkullsbarn = harTilleggForFlereSaerkullsbarn,
                sivilstand = sivilstand
            )
        )
    }



    showIf(fellesTillegg.erRedusertMotinntekt or saerkullTillegg.erRedusertMotinntekt) {
        includePhrase(
            PeriodisertInntektInnledning(
                justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar,
                justeringsbeloepAar_barnetilleggSBGjeldende = saerkullTillegg.justeringsbeloepAar,
                sivilstand = sivilstand
            )
        )

        showIf(fellesTillegg.erRedusertMotinntekt and not(saerkullTillegg.erRedusertMotinntekt)) {
            includePhrase(
                PeriodisertInntektFellesbarnA(
                    fellesTillegg.avkortningsbeloepAar,
                    fellesTillegg.fribeloepEllerInntektErPeriodisert,
                    fellesTillegg.justeringsbeloepAar,
                    sivilstand = sivilstand
                )
            )
        }

        showIf(saerkullTillegg.erRedusertMotinntekt and not(fellesTillegg.erRedusertMotinntekt)) {
            includePhrase(
                PeriodisertInntekSaerkullsbarnA(
                    saerkullTillegg.avkortningsbeloepAar,
                    saerkullTillegg.fribeloepEllerInntektErPeriodisert,
                    saerkullTillegg.justeringsbeloepAar
                )
            )
        }

        showIf(fellesTillegg.erRedusertMotinntekt) {
            includePhrase(
                PeriodisertInntektFellesbarnB(
                    avkortningsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.avkortningsbeloepAar,
                    fribeloepEllerInntektErPeriodisert_barnetilleggFBGjeldende = fellesTillegg.fribeloepEllerInntektErPeriodisert,
                    harTilleggForFlereFellesbarn = harTilleggForFlereFellesbarn,
                    justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                )
            )
            includePhrase(
                PeriodisertInntektFellesbarnC(
                    justeringsbeloepAar_barnetilleggFBGjeldende = fellesTillegg.justeringsbeloepAar
                )
            )
        }
