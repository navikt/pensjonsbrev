package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import kotlin.math.exp


object RettTilMYOverskrift_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "For deg som har rett til minsteytelse",
                Nynorsk to "For deg som har rett til minsteyting",
                English to "You have been granted minimum benefit"
            )
        }
}

object VedleggBeregnUTInfoMY_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,28 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,28 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.28 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
            )
        }
}

object VedleggBeregnUTInfoMY2_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,33 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane og har uføretrygd rekna ut frå uførepensjon per 31.desember 2014, utgjer minste årlege yting 2,33 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.33 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
            )
        }
}

object VedleggBeregnUTInfoMYUngUfor_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig, utgjør minste årlige ytelse 2,91 ganger grunnbeløpet.",
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Minste årlege yting er 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda.",
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.91 times the basic amount."
            )
        }
}

object VedleggBeregnUTInfoMYUngUforUnder20_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp fra du fyller 20 år. Er du enslig, utgjør minste årlige ytelse 2,91 ganger folketrygdens grunnbeløp fra du fyller 20 år.",
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,66 gonger grunnbeløpet i folketrygda frå du fyller 20 år. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda frå du fyller 20 år.",
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months from the age of 20. If you are single, the minimum benefit is 2.91 times the basic amount from the age of 20."
            )
        }
}


data class VedleggBeregnUTDinMY_001(
    val sats_minsteytelseGjeldende: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "For deg vil minsteytelse utgjøre ".expr() + sats_minsteytelseGjeldende.format() + " ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrer seg, kan det medføre at uføretrygden endres.",
                Nynorsk to "For deg vil minsteytinga utgjera ".expr() + sats_minsteytelseGjeldende.format() + " gonger grunnbeløpet i folketrygda. Er di uføregrad under 100 prosent, vil minsteytinga bli justert ut frå uføregrad. Vi justerer også minsteytinga ut frå trygdetid dersom du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrar seg, kan det føre til at uføretrygda vert endra.",
                English to "For you, the minimum benefit is equal to ".expr() + sats_minsteytelseGjeldende.format() + " times the National Insurance basic amount. If your degree of disability is lower than 100 percent, the minimum benefit will be adjusted for your degree of disability. We will also adjust the minimum benefit if your period of national insurance coverage is less than 40 years. In case of changes in your marital status your disability benefit might change."
            )
        }
}

object VedleggBeregnUTMinsteIFU_002 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik fastsetter vi inntekten din før du ble ufør",
                Nynorsk to "Slik fastset vi inntekta di før du blei ufør",
                English to "This is how we establish your income prior to your disability"
            )
        }
        paragraph {
            text(
                Bokmal to "Når vi fastsetter inntekten din før du ble ufør tar vi utgangspunkt i den normale inntektssituasjonen din før du ble ufør. Denne inntekten skal likevel ikke settes lavere enn:",
                Nynorsk to "Når vi fastsett inntekta di før du blei ufør tek vi utgangspunkt i den normale inntektssituasjonen din før du blei ufør. Denne inntekta skal likevel ikkje setjast lågare enn:",
                English to "When we establish you income prior to your disability, we base our calculations on your normal income prior to your date of disability. However, your income prior to your date of disability will not be set lower than:"
            )
            list {
                item {
                    text(
                        Bokmal to "3,3 ganger grunnbeløpet dersom du lever sammen med ektefelle/partner/samboer. Samboerforholdet ditt må ha vart i minst 12 av de siste 18 månedene.",
                        Nynorsk to "3,3 gonger grunnbeløpet dersom du lever saman med ektefelle/partnar/sambuar. Sambuarforholdet ditt må ha vart i minst 12 av dei siste 18 månadane.",
                        English to "3.3 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months."
                    )
                    text(
                        Bokmal to "3,5 ganger grunnbeløpet dersom du er enslig.",
                        Nynorsk to "3,5 gonger grunnbeløpet dersom du er einsleg.",
                        English to "3.5 times the National Insurance basic amount if you are single."
                    )
                }
            }
        }
    }
}

object SlikFastsettesKompGradOverskrift_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Slik har vi fastsatt kompensasjonsgraden din",
                Nynorsk to "Slik har vi fastsett kompensasjonsgraden din",
                English to "This is your degree of compensation"
            )
        }
}

object VedleggBeregnUTKompGrad_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du har rett til i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.",
                Nynorsk to "Vi fastset kompensasjonsgrad ved å samanlikna det du har rett til i 100 prosent uføretrygd med di oppjusterte inntekt før du blei ufør. Kompensasjonsgraden vert brukt til å rekna ut kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.",
                English to "Your degree of compensation is established by comparing what you are entitled to with a degree of disability of 100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit."
            )
        }
}

object VedleggBeregnUTKompGradGjsntt_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                Nynorsk to "Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekninga.",
                English to "If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
            )
        }
}

object VedleggBeregnUTKompGradGjsnttKonvUT_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
                Nynorsk to "Kompensasjonsgraden skal ved utrekninga ikkje setjast høgare enn 70 prosent. Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekning.",
                English to "Your degree of compensation will not be set higher than 70 percent. If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
            )
        }
}

object SlikBeregnBTOverskrift_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                Nynorsk to "Slik reknar vi ut storleiken på barnetillegget",
                English to "How we calculate the amount of child supplement"
            )
        }
}

object VedleggBeregnUTInnlednBT_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt. ",
                Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt. ",
                English to "The amount of child supplement is dependent on your total income. "
            )
            text(
                Bokmal to "Barnetillegget kan bli redusert ut fra:",
                Nynorsk to "Barnetillegget kan bli redusert ut frå:",
                English to "Child supplement can be reduced based on:"
            )
            list {
                item {
                    text(
                        Bokmal to "uføretrygd",
                        Nynorsk to "uføretrygd",
                        English to "disability benefits"
                    )
                }
                item {
                    text(
                        Bokmal to "arbeidsinntekt",
                        Nynorsk to "arbeidsinntekt",
                        English to "income from employment"
                    )
                }
                item {
                    text(
                        Bokmal to "næringsinntekt",
                        Nynorsk to "næringsinntekt ",
                        English to "income from self-employment"
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra utlandet",
                        Nynorsk to "inntekt frå utlandet",
                        English to "income from overseas"
                    )
                }
                item {
                    text(
                        Bokmal to "ytelser/pensjon fra Norge",
                        Nynorsk to "ytingar/pensjon frå Noreg",
                        English to "payments/pensions from Norway"
                    )
                }
                item {
                    text(
                        Bokmal to "pensjon fra utlandet",
                        Nynorsk to "pensjon frå utlandet",
                        English to "pensions from overseas"
                    )
                }
            }
        }
}

object VedleggBeregnUTInfoBTSB_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.",
                English to "We determine the amount of child supplement based on your income. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child."
            )
        }
}

object VedleggBeregnUTredusTTBTSB_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                English to "As your period of national insurance cover is less than 40 years, the exemption amounts are reduced based on the period of national insurance that you have."
            )
        }
}

data class VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001(
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner er 50 prosent av den inntekten som overstiger fribeløpet for barn som ikke bor sammen med begge foreldrene.",
                Nynorsk to avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner er 50 prosent av den inntekta som overstig fribeløpet for barn som ikkje bur saman med begge foreldra.",
                English to "50 percent of the income that exceeds the exemption amount is NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " for a child/children that does/do not live with both parents."
            )
        }
}


data class VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop_001(
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner er 50 prosent av den inntekten som overstiger fribeløpet for barn som ikke bor sammen med begge foreldrene. Dette beløpet bruker vi til å redusere barnetillegget for hele året.",
                Nynorsk to avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner er 50 prosent av den inntekta som overstig fribeløpet for barn som ikkje bur saman med begge foreldra. Dette beløpet bruker vi til å redusera barnetillegget for heile året.",
                English to "50 percent of the income that exceeds the exemption amount is NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " for a child/children that does/do not live with both parents. This amount will be used to reduce this child supplement during the calendar year."
            )
        }
}

data class VedleggBeregnUTPeridisertFriBOgInntektBTSB_001(
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    val harFlereBarn: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "barna",
                    ifFalse = "barnet"
                ) + " som ikke bor sammen med begge foreldrene blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.",
                Nynorsk to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "barna",
                    ifFalse = "barnet"
                ) + " som ikkje bur saman med begge foreldra vert 50 prosent av den inntekta som overstig fribeløpet omrekna til eit årleg beløp som svarar til ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.",
                English to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "the child that does",
                    ifFalse = "the children that do"
                ) + " not live with both parents 50 percent of the income that exceeds the exemption amount is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class VedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001(
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    val harFlereBarn: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "barna",
                    ifFalse = "barnet"
                ) + " som ikke bor sammen med begge foreldrene blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner. Dette beløpet bruker vi til å redusere barnetillegget for hele året.",
                Nynorsk to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "barna",
                    ifFalse = "barnet"
                ) + " som ikkje bur saman med begge foreldra vert 50 prosent av den inntekta som overstig fribeløpet omrekna til eit årleg beløp som svarar til ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner. Dette beløpet bruker vi til å redusera barnetillegget for heile året.",
                English to "For ".expr() + ifElse(
                    harFlereBarn,
                    ifTrue = "the child that does",
                    ifFalse = "the children that do"
                ) + " not live with both parents, 50 percent of the income that exceeds the exemption amount is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ". This amount will be used to reduce this child supplement during the calendar year."
            )
        }
}

data class VedleggBeregnUTJusterBelopOver0BTSB_001(
    val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor lagt til ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor lagt til ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class VedleggBeregnUTJusterBelopOver0BTFB(
    val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor lagt til ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor lagt til ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + "."
            )
        }
}

data class VedleggBeregnUTJusterBelopUnder0BTSB_001(
    val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor trukket fra ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor trekt frå ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class VedleggBeregnUTJusterBelopUnder0BTFB(
    val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor trukket fra ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor trekt frå ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + "."
            )
        }
}

// TODO: Fire ubrukte fraser
object ReduksjonBTSBOverskrift_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                English to "Reduction of child supplement payment for children from a previous relationship before tax"
            )
        }
}

data class ReduksjonBTSBTabell1_001(
    val inntektBruktIAvkortning_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + inntektBruktIAvkortning_barnetilleggSBGjeldende.format() + " kroner.",
                Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ".expr() + inntektBruktIAvkortning_barnetilleggSBGjeldende.format() + " kroner.",
                English to "Total income applied in calculation of reduction in child supplement is NOK ".expr() + inntektBruktIAvkortning_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class ReduksjonBTSBTabell2_001(
    val fribeloep_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + fribeloep_barnetilleggSBGjeldende.format() + " kroner.",
                Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er ".expr() + fribeloep_barnetilleggSBGjeldende.format() + " kroner.",
                English to "Exemption amount applied in calculation of reduction in child supplement is NOK ".expr() + fribeloep_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class ReduksjonBTSBTabell3_001(
    val inntektOverFribeloep_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Inntekt over fribeløpet er ".expr() + inntektOverFribeloep_barnetilleggSBGjeldende.format() + " kroner.",
                Nynorsk to "Inntekt over fribeløpet er ".expr() + inntektOverFribeloep_barnetilleggSBGjeldende.format() + " kroner.",
                English to "Income exceeding the exemption amount is NOK ".expr() + inntektOverFribeloep_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class Reduksjonbtsbtabell19_001(
    val inntektstak_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            textExpr(
                Bokmal to "Grensen for å få utbetalt barnetillegg er ".expr() + inntektstak_barnetilleggSBGjeldende.format() + " kroner.",
                Nynorsk to "Grensa for å få utbetalt barnetillegg er ".expr() + inntektstak_barnetilleggSBGjeldende.format() + " kroner.",
                English to "The income limit for receiving child supplement er NOK ".expr() + inntektstak_barnetilleggSBGjeldende.format() + "."
            )
        }
}

data class VedleggBeregnUTredusBTSBPgaInntekt_001(
    val beloep_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Du vil få utbetalt ".expr() + beloep_barnetilleggSBGjeldende.format() + " kroner i måneden før skatt i barnetillegg.",
                Nynorsk to "Du vil få utbetalt ".expr() + beloep_barnetilleggSBGjeldende.format() + " kroner i månaden før skatt i barnetillegg.",
                English to "You will receive a monthly child supplement payment of NOK ".expr() + beloep_barnetilleggSBGjeldende.format() + " before tax."
            )
        }
}

object VedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",
                Nynorsk to "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",
                English to "You will not receive a child supplement because your income is over the income limit for receiving a child supplement."
            )
        }
}

object VedleggBeregnUTJusterBelopIkkeUtbetalt_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får difor ikkje utbetalt barnetillegg for resten av året",
                English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
            )
        }
}

// TBU607V
data class MaanedligTilleggFellesbarn(
    val beloep_barnetilleggFBGjeldende: Expression<Kroner>,
    val harFlereBarn: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du vil få utbetalt ".expr() + beloep_barnetilleggFBGjeldende.format() + " kroner i måneden før skatt i barnetillegg for ".expr() +
                    ifElse(harFlereBarn, ifTrue = "barna", ifFalse = "barnet") +
                    " som bor med begge sine foreldre".expr(),
                Nynorsk to "Du vil få utbetalt ".expr() + beloep_barnetilleggFBGjeldende.format() + " kroner i månaden før skatt i barnetillegg for ".expr() +
                    ifElse(harFlereBarn, ifTrue = "barna", ifFalse = "barnet") +
                    " som bur saman med begge foreldra sine.".expr(),
                English to "You will receive a monthly child supplement payment of NOK ".expr() + beloep_barnetilleggFBGjeldende.format() + " for the ".expr() +
                    ifElse(harFlereBarn, ifTrue = "children who live", ifFalse = "child who lives") +
                    " together with both parents.".expr()
            )
        }
    }
}

// TBU608V
data class FaaIkkeUtbetaltTilleggFellesbarn(
    val beloep_barnetilleggFBGjeldende: Expression<Kroner>,
    val harFlereBarn: Expression<Boolean>,
    val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            showIf(
                beloep_barnetilleggFBGjeldende.equalTo(0) and justeringsbeloepAar_barnetilleggFBGjeldende.equalTo(0)
            ) {
                textExpr(
                    Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() +
                        ifElse(harFlereBarn, ifTrue = "barna", ifFalse = "barnet") +
                        " som bor med begge sine foreldre fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                    Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() +
                        ifElse(harFlereBarn, ifTrue = "barna", ifFalse = "barnet") +
                        " som bur saman med begge foreldra sine fordi samla inntekt er over grensa for å få utbetalt barnetillegg. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ".expr(),
                    English to "You will not receive a child supplement for the ".expr() +
                        ifElse(harFlereBarn, ifTrue = "children who live", ifFalse = "child who lives") +
                        " together with both parents because your income is over the income limit for receiving a child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                )

            }
        }
        showIf(
            beloep_barnetilleggFBGjeldende.equalTo(0) and justeringsbeloepAar_barnetilleggFBGjeldende.notEqualTo(0)
        ) {
            text(
                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
            )
        }
    }
}

// TBU069V - Fellesbarn
data class FastsetterStoerelsenPaaBTFellesbarn(
    val harAnvendtTrygdetidUnder40 : Expression<Boolean>,
    // val yrkesskadeResult: yrkesskadeResultatListe
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene.",
                Nynorsk to "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra.",
                English to "We determine the amount of child supplement based on the total income of both parents."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.".expr(),
                English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
            )
        }
        showIf(harAnvendtTrygdetidUnder40) {
            text(
                Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                English to "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
            )
        }
        paragraph {
            text(
                Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon.",
                Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon.",
                English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with the highest disability benefit. This applies also if one of the parents receives retirement pension."
            )
        }
    }
}

// TBU069V - Saerkullsbarn
data class FastsetterStoerelsenPaaBTSaerkullsbarn(
    val harAnvendtTrygdetidUnder40 : Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget.",
                Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget.",
                English to "We determine the amount of child supplement based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.".expr(),
                English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
            )
        }
        showIf(harAnvendtTrygdetidUnder40) {
            text(
                Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har. ",
                Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har. ",
                English to "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to the the length of time of you National Insurance membership. "
            )
        }
        paragraph {
            text(
                Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrete adresse som barnet. ",
                Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet.",
                English to "If both parents recieve disbaility benefit, child supplement will be paid to the parent with guardianship of the child. This applies also if one of the parents receives retirement pension. If both parents have guardianship of the child, child supplement is given to the parent who lives at the same registered adresse as the child."
            )
        }
    }
}

// TBU069V - Fellesbarn OG Saerkullsbarn
data class FastsetterStoerelsenPaaBTFellesbarnOgSaerkullsbarn(
    val harAnvendtTrygdetidUnder40: Expression<Boolean>,
    val harTilleggForFlereFellesbarn: Expression<Boolean>,
    val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
    val sivilstand: Expression<Sivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din ".expr(),
                Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og din ".expr(),
                English to "We determine the amount of child supplement based on the total income for you and your ".expr()
            )

            includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))

            textExpr(
                Bokmal to " for ".expr() +
                    ifElse(harTilleggForFlereFellesbarn, ifTrue = "barna", ifFalse = "barnet") + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.".expr(),
                Nynorsk to " for ".expr() +
                    ifElse(harTilleggForFlereFellesbarn, ifTrue = "barna", ifFalse = "barnet") + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.".expr(),
                English to " for the ".expr() +
                    ifElse(harTilleggForFlereFellesbarn, ifTrue = "children who live", ifFalse = "child who lives") + " with both parents. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 4.6 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child.".expr()
            )
        }
        paragraph {
            textExpr(
                Bokmal to "For ".expr() +
                    ifElse(harTilleggForFlereSaerkullsbarn, ifTrue = "barna", ifFalse = "barnet") + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ".expr(),
                Nynorsk to "For ".expr() +
                    ifElse(harTilleggForFlereSaerkullsbarn, ifTrue = "barna", ifFalse = "barnet") + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ".expr(),
                English to "For the ".expr() +
                    ifElse(harTilleggForFlereSaerkullsbarn, ifTrue = "children who do", ifFalse = "child who does") + "not live together with both parents, the amount of child supplement is based on your income. The income of a spouse/partner/cohabitant who is not the child's parent, is not taken into consideration. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child. ".expr(),
            )
        }
        showIf(harAnvendtTrygdetidUnder40) {
            text(
                Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                English to "Since you have less than 40 years National Insurance membership, the exemption amount is ruduced correspondingly in relation to how long you have had National Insurance membership."
            )
        }
    }
}

// TBU605V
data class PeriodisertInntektFellesbarn(
    val avkortningsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    val fribeloepEllerInntektFellesbarn: Expression<Boolean>,
    val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    val sivilstand: Expression<Sivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val oekeJusteringsbeloepFellesbarn = justeringsbeloepAar_barnetilleggFBGjeldende.greaterThan(0)
            text(
                Bokmal to "Når inntekten din eller til din ",
                Nynorsk to "Når inntekta di eller til di ",
                English to "When your or your "
            )

            includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))

            text(
                Bokmal to " endrer seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                Nynorsk to " endrar seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                English to "'s income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount "
            )

            showIf(fribeloepEllerInntektFellesbarn) {
                textExpr(
                    Bokmal to " blir omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                    Nynorsk to " blir rekna om til et årleg beløp som svarer til ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                    English to " is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                )
            }.orShowIf(not(fribeloepEllerInntektFellesbarn)) {
                textExpr(
                    Bokmal to " er ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                    Nynorsk to " er ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + " kroner.".expr(),
                    English to " is NOK ".expr() + avkortningsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                )
            }
            showIf(justeringsbeloepAar_barnetilleggFBGjeldende.equalTo(0)) {
                text(
                    Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                    Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                    English to " This amount will be used to reduce your child supplement during the calendar year."
                )
            }
            showIf(justeringsbeloepAar_barnetilleggFBGjeldende.notEqualTo(0)) {
                textExpr(
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                        ifElse(oekeJusteringsbeloepFellesbarn, ifTrue = "lagt til", ifFalse = "trukket fra") + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                        ifElse(oekeJusteringsbeloepFellesbarn, ifTrue = "lagt til", ifFalse = "trekt frå") + " ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                        ifElse(oekeJusteringsbeloepFellesbarn, ifTrue = "increased", ifFalse = "reduced") + " with NOK ".expr() + justeringsbeloepAar_barnetilleggFBGjeldende.format() + ".".expr()
                )
            }
        }
    }
}

data class PeriodisertInntekSaerkullsbarn(
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    val fribeloepEllerInntektSaerkullsbarn: Expression<Boolean>,
    val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val oekeJusteringsbeloepSaerkullsbarn = justeringsbeloepAar_barnetilleggSBGjeldende.greaterThan(0)
            text(
                Bokmal to "Når inntekten din endrer seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                Nynorsk to "Når inntekta di endrar seg, blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                English to "When your income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount "
            )
            showIf(fribeloepEllerInntektSaerkullsbarn) {
                textExpr(
                    Bokmal to " blir omregnet til et årlig beløp som tilsvarer ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                    Nynorsk to " blir rekna om til et årleg beløp som svarer til ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                    English to " is recalculated to an annual amount of NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                )
            }.orShowIf(not(fribeloepEllerInntektSaerkullsbarn)) {
                textExpr(
                    Bokmal to " er ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                    Nynorsk to " er ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + " kroner.".expr(),
                    English to " is NOK ".expr() + avkortningsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                )
            }
            showIf(justeringsbeloepAar_barnetilleggSBGjeldende.equalTo(0)) {
                text(
                    Bokmal to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                    Nynorsk to " Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året.",
                    English to " This amount will be used to reduce your child supplement during the calendar year."
                )
            }
            showIf(justeringsbeloepAar_barnetilleggSBGjeldende.notEqualTo(0)) {
                textExpr(
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ".expr() +
                        ifElse(oekeJusteringsbeloepSaerkullsbarn, ifTrue = "lagt til", ifFalse = "trukket fra") + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året".expr(),
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ".expr() +
                        ifElse(oekeJusteringsbeloepSaerkullsbarn, ifTrue = "lagt til", ifFalse = "trekt frå") + " ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.".expr(),
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ".expr() +
                        ifElse(oekeJusteringsbeloepSaerkullsbarn, ifTrue = "increased", ifFalse = "reduced") + " with NOK ".expr() + justeringsbeloepAar_barnetilleggSBGjeldende.format() + ".".expr()
                )
            }
        }
    }
}
// TBU613V
data class PeriodisertInntektFellesbarnSaerkullsbarn(
    val avkortningsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    val avkortningsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    val fribeloepEllerInntektFellesbarn: Expression<Boolean>,
    val fribeloepEllerInntektSaerkullsbarn: Expression<Boolean>,
    val harTilleggForFlereFellesbarn: Expression<Boolean>,
    val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
    val justeringsbeloepAar_barnetilleggFBGjeldende: Expression<Kroner>,
    val justeringsbeloepAar_barnetilleggSBGjeldende: Expression<Kroner>,
    val sivilstand: Expression<Sivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Når inntekten din eller til din ",
                Nynorsk to "Når inntekta di eller til di ",
                English to "When your or your "
            )

            includePhrase(Felles.SivilstandEPSUbestemtForm(sivilstand))

            text(
                Bokmal to " endrer seg, blir reduksjonen av barnetilleggene vurdert på nytt.",
                Nynorsk to " endrar seg, blir reduksjonen av barnetilleggene vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                English to "'s income has been changed, your child supplement will be recalculated. 50 percent of the income that exceeds the exemption amount "
            )
            textExpr(
                Bokmal to "For ".expr() + ifElse(harTilleggForFlereFellesbarn, ifTrue = "barna", ifFalse = "barnet") + " som bor med begge sine foreldre blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer ".expr(),
                Nynorsk to "For ".expr() + ifElse(harTilleggForFlereFellesbarn, ifTrue = "barna", ifFalse = "barnet") + " som bur med begge foreldra sine blir 50 prosent av inntekta som overstig fribeløpet rekna om til et årleg beløp som svarer til ".expr(),
                English to "For ".expr() + ifElse(harTilleggForFlereFellesbarn, ifTrue = "children that live", ifFalse = "a child that lives") + " with both of their parents, 50 percent of the income that exceeds the exemption amount is recalculated to an annual amount of NOK ".expr()
            )
        }
    }
}