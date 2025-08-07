package no.nav.pensjon.brev.maler.alder.omregning.opptjening.fraser

import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldende
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldendeSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldendeSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.Opptjening
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class BeskrivelseVedTilvekst(
    val opptjening: Expression<Opptjening>,
    val sisteGyldigeOpptjeningsAar: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(opptjening.equalTo(Opptjening.TILVEKST)) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Skatteoppgjøret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye opptjeningen er lagt til alderspensjonen din.",
                    Language.Nynorsk to "Skatteoppgjeret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye oppteninga er lagt til alderspensjonen din.",
                    Language.English to "The final tax settlement for ".expr() + sisteGyldigeOpptjeningsAar.format() + " has been completed and the new pension earnings have been added to your retirement pension.",
                )
            }
        }
    }
}

data class BeskrivelseVedKorrigering(
    val opptjening: Expression<Opptjening>,
    val antallAarEndretOpptjening: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(opptjening.equalTo(Opptjening.KORRIGERING)) {
            showIf(antallAarEndretOpptjening.equalTo(0)) {
                paragraph {
                    text(
                        Language.Bokmal to "Pensjonsopptjeningen er endret.",
                        Language.Nynorsk to "Pensjonsopptjeningen er endret.",
                        Language.English to "The pension earnings have been changed."
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen din er endret for: ",
                    Language.Nynorsk to "Pensjonsoppteninga di er endra for: ",
                    Language.English to "Your pension earnings have been changed for the following income year(-s): "
                )
            }
            // todo list years..
        }
    }
}

data class AvsnittEndringPensjon(
    val belopEndring: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(belopEndring.equalTo("ENDR_OKT")) {
            paragraph {
                text(
                    Language.Bokmal to "Dette fører til at pensjonen din øker.",
                    Language.Nynorsk to "Dette fører til at pensjonen din aukar.",
                    Language.English to "This leads to an increase in your retirement pension."
                )
            }
        }
        showIf(belopEndring.equalTo("ENDR_RED")) {
            paragraph {
                text(
                    Language.Bokmal to "Dette fører til at pensjonen din blir redusert.",
                    Language.Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                    Language.English to "This leads to a reduction in your retirement pension."
                )
            }
        }
    }
}

data class AvsnittUtbetalingPerMaaned(
    val uforeKombinertMedAlder: Expression<Boolean>,
    val beregnetPensjonPerMaanedGjeldende: Expression<BeregnetPensjonPerMaanedGjeldende>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(uforeKombinertMedAlder.not()) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon fra folketrygden.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon frå folketrygda.",
                    Language.English to "You will receive NOK ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " as retirement pension through the National Insurance Scheme.",
                )
            }
        }
        showIf(uforeKombinertMedAlder) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon i tillegg til uføretrygden din.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon ved sida av uføretrygda di.",
                    Language.English to "You will receive NOK ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". You will receive retirement pension in addition to you disability benefit.",
                )
            }
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på ${Constants.UTBETALINGER_URL}.",
                Language.Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på ${Constants.UTBETALINGER_URL}.",
                Language.English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at ${Constants.UTBETALINGER_URL}."
            )
        }
    }
}
