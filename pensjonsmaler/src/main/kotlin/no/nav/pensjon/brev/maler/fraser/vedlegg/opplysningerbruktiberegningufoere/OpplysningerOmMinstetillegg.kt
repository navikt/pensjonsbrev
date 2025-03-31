package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.InntektFoerUfoereGjeldendeSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.UfoeretrygdGjeldendeSelectors.erKonvertert
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.formatTwoDecimals
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

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
            title1 {
                text(
                    Language.Bokmal to "For deg som har rett til minsteytelse",
                    Language.Nynorsk to "For deg som har rett til minsteyting",
                    Language.English to "You have been granted minimum benefit"
                )
            }
            ifNotNull(ungUfoerGjeldende_erUnder20Aar) { erUnder20Aar ->
                showIf(erUnder20Aar) {
                    includePhrase(VedleggBeregnUTInfoMYUngUforUnder20)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMYUngUfor)
                }
            }.orShow {
                showIf(ufoeretrygdGjeldende.erKonvertert) {
                    includePhrase(VedleggBeregnUTInfoMY2)
                }.orShow {
                    includePhrase(VedleggBeregnUTInfoMY)
                }
            }
        }

        ifNotNull(minsteytelseGjeldendeSats) {
            showIf(harMinsteytelseSats) {
                includePhrase(VedleggBeregnUTDinMY(it))
            }
        }

        showIf(inntektFoerUfoereGjeldende.erSannsynligEndret) {
            includePhrase(VedleggBeregnUTMinsteIFU)
        }

        showIf(
            harMinsteytelseSats
                    and inntektFoerUfoereGjeldende.erSannsynligEndret
                    and inntektsgrenseErUnderTak
        ) {

            title1 {
                text(
                    Language.Bokmal to "Slik har vi fastsatt kompensasjonsgraden din",
                    Language.Nynorsk to "Slik har vi fastsett kompensasjonsgraden din",
                    Language.English to "This is your degree of compensation"
                )
            }
            includePhrase(VedleggBeregnUTKompGrad)

            showIf(ufoeretrygdGjeldende.erKonvertert) {
                includePhrase(VedleggBeregnUTKompGradGjsnttKonvUT)
            }.orShow {
                includePhrase(VedleggBeregnUTKompGradGjsntt)
            }
        }
    }

    object VedleggBeregnUTInfoMY : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,28 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
                    Language.Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,28 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
                    Language.English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.28 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
                )
            }
    }

    object VedleggBeregnUTInfoMY2 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,33 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
                    Language.Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane og har uføretrygd rekna ut frå uførepensjon per 31.desember 2014, utgjer minste årlege yting 2,33 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
                    Language.English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.33 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
                )
            }
    }

    object VedleggBeregnUTInfoMYUngUfor : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig, utgjør minste årlige ytelse 2,91 ganger grunnbeløpet.",
                    Language.Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Minste årlege yting er 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda.",
                    Language.English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.91 times the basic amount."
                )
            }
    }

    object VedleggBeregnUTInfoMYUngUforUnder20 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp fra du fyller 20 år. Er du enslig, utgjør minste årlige ytelse 2,91 ganger folketrygdens grunnbeløp fra du fyller 20 år.",
                    Language.Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,66 gonger grunnbeløpet i folketrygda frå du fyller 20 år. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda frå du fyller 20 år.",
                    Language.English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months from the age of 20. If you are single, the minimum benefit is 2.91 times the basic amount from the age of 20."
                )
            }
    }


    data class VedleggBeregnUTDinMY(val sats_minsteytelseGjeldende: Expression<Double>) : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                textExpr(
                    Language.Bokmal to "For deg vil minsteytelse utgjøre ".expr() + sats_minsteytelseGjeldende.formatTwoDecimals() + " ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrer seg, kan det medføre at uføretrygden endres.",
                    Language.Nynorsk to "For deg vil minsteytinga utgjera ".expr() + sats_minsteytelseGjeldende.formatTwoDecimals() + " gonger grunnbeløpet i folketrygda. Er di uføregrad under 100 prosent, vil minsteytinga bli justert ut frå uføregrad. Vi justerer også minsteytinga ut frå trygdetid dersom du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrar seg, kan det føre til at uføretrygda vert endra.",
                    Language.English to "For you, the minimum benefit is equal to ".expr() + sats_minsteytelseGjeldende.formatTwoDecimals() + " times the National Insurance basic amount. If your degree of disability is lower than 100 percent, the minimum benefit will be adjusted for your degree of disability. We will also adjust the minimum benefit if your period of national insurance coverage is less than 40 years. In case of changes in your marital status your disability benefit might change."
                )
            }
    }

    object VedleggBeregnUTMinsteIFU : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Language.Bokmal to "Slik fastsetter vi inntekten din før du ble ufør",
                    Language.Nynorsk to "Slik fastset vi inntekta di før du blei ufør",
                    Language.English to "This is how we establish your income prior to your disability"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Når vi fastsetter inntekten din før du ble ufør tar vi utgangspunkt i den normale inntektssituasjonen din før du ble ufør. Denne inntekten skal likevel ikke settes lavere enn:",
                    Language.Nynorsk to "Når vi fastsett inntekta di før du blei ufør tek vi utgangspunkt i den normale inntektssituasjonen din før du blei ufør. Denne inntekta skal likevel ikkje setjast lågare enn:",
                    Language.English to "When we establish you income prior to your disability, we base our calculations on your normal income prior to your date of disability. However, your income prior to your date of disability will not be set lower than:"
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "3,3 ganger grunnbeløpet dersom du lever sammen med ektefelle/partner/samboer. Samboerforholdet ditt må ha vart i minst 12 av de siste 18 månedene.",
                            Language.Nynorsk to "3,3 gonger grunnbeløpet dersom du lever saman med ektefelle/partnar/sambuar. Sambuarforholdet ditt må ha vart i minst 12 av dei siste 18 månadane.",
                            Language.English to "3.3 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months."
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "3,5 ganger grunnbeløpet dersom du er enslig.",
                            Language.Nynorsk to "3,5 gonger grunnbeløpet dersom du er einsleg.",
                            Language.English to "3.5 times the National Insurance basic amount if you are single."
                        )
                    }
                }
            }
        }
    }

    object VedleggBeregnUTKompGrad : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du har rett til i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.",
                    Language.Nynorsk to "Vi fastset kompensasjonsgrad ved å samanlikna det du har rett til i 100 prosent uføretrygd med di oppjusterte inntekt før du blei ufør. Kompensasjonsgraden vert brukt til å rekna ut kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.",
                    Language.English to "Your degree of compensation is established by comparing what you are entitled to with a degree of disability of 100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit."
                )
            }
    }

    object VedleggBeregnUTKompGradGjsntt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                    Language.Nynorsk to "Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekninga.",
                    Language.English to "If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
                )
            }
    }

    object VedleggBeregnUTKompGradGjsnttKonvUT : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Language.Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                    Language.Nynorsk to "Kompensasjonsgraden skal ved utrekninga ikkje setjast høgare enn 70 prosent. Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekning.",
                    Language.English to "Your degree of compensation will not be set higher than 70 percent. If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
                )
            }
    }
}