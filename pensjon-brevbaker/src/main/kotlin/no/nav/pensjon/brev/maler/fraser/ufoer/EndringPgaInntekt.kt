package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.ufoerApi.Fellesbarn
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

// TBU2249
data class EndringIBrukersUfoeretrygd(
    val harBarnetilleggFellesbarn: Expression<Boolean>,
    val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
    val harBeloepEndringBarnetillegg: Expression<Boolean>,
    val harBeloepEndringUfoeretrygd: Expression<Boolean>,
    val gjelderFlereBarn: Expression<Boolean>,
    val sivilstand: Expression<Sivilstand>,
    val virkningsDatoFom: Expression<LocalDate>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Vi har mottatt nye opplysninger om inntekten ",
                Nynorsk to "Vi har fått nye opplysningar om inntekta ",
                English to "We have received new information about "
            )
            showIf(harBeloepEndringUfoeretrygd and not(harBeloepEndringBarnetillegg)) {
                textExpr(
                    Bokmal to "din. Utbetalingen av uføretrygden din er derfor endret fra ".expr() + virkningsDatoFom.format() + ".",
                    Nynorsk to "di. Utbetalinga av uføretrygda di er derfor endra frå ".expr() + virkningsDatoFom.format() + ".",
                    English to "your income. Therefore, the payment of your disability benefit has been changed from ".expr() + virkningsDatoFom.format() + "."
                )
            }
            showIf(
                harBarnetilleggSaerkullsbarn or (harBarnetilleggSaerkullsbarn and harBeloepEndringUfoeretrygd) and not(
                    harBarnetilleggFellesbarn
                )
            ) {
                textExpr(
                    Bokmal to "din. Utbetalingen av ".expr() + ifElse(
                        harBeloepEndringUfoeretrygd,
                        ifTrue = "uføretrygden din og ",
                        ifFalse = ""
                    ) + "barnetillegget ditt er derfor endret fra ".expr() + virkningsDatoFom.format() + ".",
                    Nynorsk to "di. Utbetalinga av ".expr() + ifElse(
                        harBeloepEndringUfoeretrygd,
                        ifTrue = "uføretrygda di og ",
                        ifFalse = ""
                    ) + "barnetillegget ditt er derfor endra frå ".expr() + virkningsDatoFom.format() + ".",
                    English to "your income. The payment of ".expr() + ifElse(
                        harBeloepEndringUfoeretrygd,
                        ifTrue = "your disability benefit and ",
                        ifFalse = ""
                    ) + "your child benefit is therefore changed from ".expr() + virkningsDatoFom.format() + "."
                )
            }
        }
    }
}