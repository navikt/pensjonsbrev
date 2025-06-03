package no.nav.pensjon.brev.maler.alder.omregning

import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonPerManed
import no.nav.pensjon.brev.api.model.maler.alderApi.PersongrunnlagAvdod
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class OmregningAlderUfore2016Felles(
    val virkFom: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>,
    val totalPensjon: Expression<Kroner>,
    val beregningsperioder: Expression<List<AlderspensjonPerManed>>,
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>,
    val avdodNavn: Expression<String>,
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

        showIf(beregningsperioder.size().greaterThan(1) and totalPensjon.greaterThan(0)) {
            paragraph {
                text(
                    Bokmal to "Du kan lese mer om andre beregningsperioder i vedlegget.",
                    Nynorsk to "Du kan lese meir om andre berekningsperiodar i vedlegget.",
                    English to "There is more information about other calculation periods in the attachment."
                )
            }
        }

        showIf(gjenlevendetilleggKap19Innvilget and avdodNavn.notNull()) {
            paragraph {
                textExpr(
                    Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr() + avdodNavn,
                    Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr() + avdodNavn,
                    English to "You receive a survivor’s supplement in the retirement pension because you have pension rights after ".expr() + avdodNavn

                )
            }
            paragraph {
                text(
                    Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. " +
                            "Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, " +
                            "og alderspensjon du har tjent opp selv.",
                    Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. " +
                            "Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, " +
                            "og alderspensjon du har tent opp sjølv.",
                    English to "The retirement pension is based on your own pension earnings. " +
                            "The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, " +
                            "and retirement pension you have earned yourself."
                )
            }
        }



    }
}