package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere


import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

/* IF (KravArsakType <> SOKNAD_BT
AND brevkode <> PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200, (PE_UT_04_102 AND KravArsakType <> TILST_DOD))
OR brevkode = PE_UT_06_300
THEN INCLUDE */

data class EtteroppgjoerAvUfoeretrygdOgBarnetillegg(
    val borIUtlandet: Expression<Boolean>,
    val harBarnetilleggInnvilget: Expression<Boolean>,
    val harFellesbarn: Expression<Boolean>,
    val sivilstand: Expression<Sivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // TBU066V, TBU067V
        title1 {
            textExpr(
                Language.Bokmal to "Etteroppgjør av uføretrygd ".expr() +
                        ifElse(harBarnetilleggInnvilget, ifTrue = "og barnetillegg", ifFalse = "") + "".expr(),

                Language.Nynorsk to "Etteroppgjer av uføretrygd ".expr() +
                        ifElse(harBarnetilleggInnvilget, ifTrue = "og barnetillegg", ifFalse = "") + "".expr(),
                Language.English to "Final settlement of disability benefit ".expr() +
                        ifElse(harBarnetilleggInnvilget, ifTrue = "and child supplement", ifFalse = "") + "".expr(),
            )
        }
        paragraph {
            text(
                Language.Bokmal to "Hvert år når likningen er klar mottar vi opplysninger om inntekten ",
                Language.Nynorsk to "Kvart år når likninga er klar får vi opplysningar om inntekta ",
                Language.English to "Once the tax assessment for the year in question is complete, we will receive information about "
            )
            showIf(not(harFellesbarn)) {
                text(
                    Language.Bokmal to "din fra Skatteetaten.",
                    Language.Nynorsk to "di frå Skatteetaten.",
                    Language.English to "your income from the Tax Administration."
                )
            }
            showIf(harFellesbarn) {
                textExpr(
                    Language.Bokmal to "til deg og din ".expr() + sivilstand.ubestemtForm() + " fra Skatteetaten.".expr(),
                    Language.Nynorsk to "til deg og ".expr() + sivilstand.bestemtForm() + " di frå Skatteetaten.".expr(),
                    Language.English to "your and your ".expr() + sivilstand.ubestemtForm() + "'s income from the Tax Administration.".expr()
                )
            }
            textExpr(
                Language.Bokmal to " Vi bruker likningsopplysningene til å beregne riktig utbetaling av uføretrygd".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = " og barnetillegg",
                            ifFalse = ""
                        ) + " for året likningen gjelder for. Har du fått for mye eller for lite utbetalt i uføretrygd".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = "og barnetillegg",
                            ifFalse = ""
                        ) + ", vil vi foreta et etteroppgjør.".expr(),
                Language.Nynorsk to " Vi bruker opplysningane om inntekt til å berekne riktig utbetaling av uføretrygd".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = " og barnetillegg",
                            ifFalse = ""
                        ) + " for det året som likninga gjeld for. Har du fått for mykje eller for lite utbetalt i uføretrygd".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = " og barnetillegg",
                            ifFalse = ""
                        ) + ", vil vi gjere eit etteroppgjer.".expr(),
                Language.English to " We will use the tax assessment data to calculate the correct disability benefit payments ".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = "and child supplement ",
                            ifFalse = ""
                        ) + "for the year the tax assessment is due to. If you have received too much or too little in disability payments".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = " and child supplement",
                            ifFalse = ""
                        ) + ", we will conduct a final settlement.".expr()
            )
            showIf(borIUtlandet) {
                text(
                    Language.Bokmal to " Har du meldt inn inntekt fra arbeid i et annet land enn Norge, og vi ikke mottar inntektsopplysninger fra Skatteetaten, gjør vi etteroppgjøret ut fra inntekten din fra utlandet.",
                    Language.Nynorsk to " Har du meldt inn inntekt frå arbeid i eit anna land enn Noreg, og vi ikkje får opplysningar om inntekt frå Skatteetaten, gjer vi eit etteroppgjer ut frå inntekta di frå utlandet.",
                    Language.English to " We will do the final settlement based on your reported income from another country, if you have not received income in Norway."
                )
            }
            text(
                Language.Bokmal to " Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mye, må du betale dette tilbake.",
                Language.Nynorsk to " Har du fått utbetalt for lite, vil vi utbetale dette beløpet til deg. Har du fått utbetalt for mykje, må du betale dette tilbake.",
                Language.English to " If you have received too little, we will pay out the amount to you. If you have received too much, you will have to pay the amount back."
            )
        }

        paragraph {
            textExpr(
                Language.Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden ".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = "og barnetillegget ",
                            ifFalse = ""
                        ) + "blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på ${Constants.NAV_URL}.".expr(),
                Language.Nynorsk to "Det er viktig at du melder frå om inntektsendringar slik at uføretrygda ".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = "og barnetillegget ",
                            ifFalse = ""
                        ) + "blir riktig utbetalt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på ${Constants.NAV_URL}.".expr(),
                Language.English to "It is important that you report changes in income, so that you receive the correct disability benefit payments".expr() +
                        ifElse(
                            harBarnetilleggInnvilget,
                            ifTrue = " and child supplement",
                            ifFalse = ""
                        ) + ". You can easily register change in income under the option “uføretrygd” at ${Constants.NAV_URL}.".expr()
            )
        }
    }
}


