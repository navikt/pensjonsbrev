package no.nav.pensjon.brev.maler.alder.aldersovergang

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class Aldersovergang2016Felles(
    val virkFom: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>,
    val totalPensjon: Expression<Kroner>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision"
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format(),
                Nynorsk to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format(),
                English to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format()

            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner kvar månad før skatt frå "+ virkFom.format() + " i alderspensjon fra folketrygden.",
                Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner hver måned før skatt fra "+ virkFom.format() + " i alderspensjon frå folketrygda.",
                English to "You will receive NOK ".expr() + totalPensjon.format() + " every month before tax from "+ virkFom.format() + "  as retirement pension from the National Insurance Scheme"

            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                        "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger.", //TODO url
                Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                        "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger.",
                English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                        "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon."
            )
        }



    }
}