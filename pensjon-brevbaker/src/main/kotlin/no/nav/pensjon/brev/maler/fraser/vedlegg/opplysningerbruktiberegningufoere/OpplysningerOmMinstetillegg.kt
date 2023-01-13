package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.erKonvertert
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull

data class OpplysningerOmMinstetillegg(
    val minsteytelseGjeldendeSats: Expression<Double?>,
    val ungUfoerGjeldende_erUnder20Aar: Expression<Boolean?>,
    val ufoeretrygdGjeldende: Expression<OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende>,
    val inntektFoerUfoereGjeldende: Expression<OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende>,
    val inntektsgrenseErUnderTak: Expression<Boolean>,
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harMinsteytelseSats = minsteytelseGjeldendeSats.ifNull(0.0).greaterThan(0.0)
        showIf(harMinsteytelseSats) {
            includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.RettTilMYOverskrift)
            ifNotNull(ungUfoerGjeldende_erUnder20Aar) { erUnder20Aar ->
                showIf(erUnder20Aar) {
                    includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTInfoMYUngUforUnder20)
                }.orShow {
                    includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTInfoMYUngUfor)
                }
            }.orShow {
                showIf(ufoeretrygdGjeldende.erKonvertert) {
                    includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTInfoMY2)
                }.orShow {
                    includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTInfoMY)
                }
            }
        }

        ifNotNull(minsteytelseGjeldendeSats) {
            showIf(harMinsteytelseSats) {
                includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTDinMY(it))
            }
        }

        showIf(inntektFoerUfoereGjeldende.erSannsynligEndret) {
            includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTMinsteIFU)
        }

        showIf(
            harMinsteytelseSats
                    and inntektFoerUfoereGjeldende.erSannsynligEndret
                    and inntektsgrenseErUnderTak
        ) {

            includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.SlikFastsettesKompGradOverskrift)
            includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTKompGrad)

            showIf(ufoeretrygdGjeldende.erKonvertert) {
                includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTKompGradGjsnttKonvUT)
            }.orShow {
                includePhrase(VedleggOpplysningerBruktIBeregningUTFraser.VedleggBeregnUTKompGradGjsntt)
            }
        }
    }
}