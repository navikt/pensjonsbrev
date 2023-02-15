package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere


import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text


/* Include IF brevkode not(
PE_UT_05_100
PE_UT_04_300
PE_UT_07_100
)
AND beloepsgrense != 6000
AND kravAarsakType != soknad_bt
AND IF brevkode not(
PE_UT_04_108
PE_UT_04_109
PE_UT_06_300
PE_UT_07_200
PE_UT_14_300
)
 */

data class SlikBeregnerViUfoeretrygdenDin(
    val harKonvertertSak: Expression<Boolean>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // TBU011V
        title1 {
            text(
                Bokmal to "Slik beregner vi uføretrygden din",
                Nynorsk to "Slik bereknar vi uføretrygda di",
                English to "This is how your disability benefit is calculated"
            )
        }
        showIf(not(harKonvertertSak)) {
            // TBU012V - hente ut året fra uføretidspunktet
            paragraph {
                text(
                    Bokmal to "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    Nynorsk to "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir teken med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    English to "In calculating your disability benefit, we base calculations on your average income for the best three of the last five years prior to the onset of your disability. Income up to six times the National Insurance basic amount (G) is included in the calculation. The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at ${Constants.GRUNNBELOEP_URL}."
                )
            }

            //TBU013V
            paragraph {
                text(
                    Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Fordi ligningen din for året før du ble ufør ikke er ferdig, bruker vi her gjennomsnittsinntekten i de tre beste av de fire siste årene før du ble ufør.",
                    Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Fordi likninga di for året før du blei ufør, ikkje er ferdig, bruker vi her gjennomsnittsinntekta i dei tre beste av dei fire siste åra før du blei ufør.",
                    English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. Because the tax assessment for the year of the onset of your disability is not yet completed, we have based these calculations on the best three of the last four years prior to the onset of your disability."
                )
            }

            paragraph {
                text(
                    Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at ${Constants.GRUNNBELOEP_URL}."
                )
            }

            paragraph {
                text(
                    Bokmal to "Når ligningen er ferdig, vil uføretrygden din bli beregnet på nytt. Du vil få et nytt vedtaksbrev om dette.",
                    Nynorsk to "Når likninga er ferdig, blir uføretrygda di berekna på nytt. Du får eit nytt vedtaksbrev om dette.",
                    English to "Once the tax assessment is completed, your disability benefit will be recalculated. You will receive a new letter informing you of any new developments."
                )
            }

            // TBU014V
            paragraph {
                text(
                    Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har vært i militæret, eller hatt sivil førstegangstjeneste. Inntekt i denne perioden skal utgjøre minst tre ganger gjennomsnittlig G (folketrygdens grunnbeløp). Du hadde en høyere inntekt i året før du avtjente førstegangstjeneste, og vi bruker derfor denne inntekten i beregningen.",
                    Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har vore i militæret, eller hatt sivil førstegongsteneste. Inntekt i denne perioden skal utgjere minst tre gonger gjennomsnittleg G (grunnbeløpet i folketrygda). Du hadde ei høgare inntekt i året før du avtente førstegongstenesta, og vi bruker derfor denne inntekta i berekninga.",
                    English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. You have served in the military, or completed your initial service as a civilian. Income during this period must total no less than three times the average G (National Insurance basic amount). Your income was higher in the year prior to your initial service, and we have thus applied this incomed in the calculations."
                )
            }

            paragraph {
                text(
                    Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at ${Constants.GRUNNBELOEP_URL}."
                )
            }

            // TBU015V
            paragraph {
                text(
                    Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har hatt pensjonsopptjening på grunnlag av omsorgsarbeid i ett eller flere av disse årene. Vi bruker disse årene i beregningen, hvis dette er en fordel for deg.",
                    Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har hatt pensjonsopptening på grunnlag av omsorgsarbeid i eitt eller fleire av desse åra. Vi bruker desse åra i berekninga dersom dette er ein fordel for deg.",
                    English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. You have earned pension points due to care work during one or more of these years. We will include these years in the calculation, if this is to your advantage."
                )
            }

            paragraph {
                text(
                    Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                    English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at ${Constants.GRUNNBELOEP_URL}."
                )
            }

            // TBU016V - brukere med inntekt i utlandet
            data class InntektIUtlandet(
                val beregningsmetode: Expression<Beregningsmetode>,

                ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
                override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                    showIf(beregningsmetode.isOneOf(Beregningsmetode.EOS)) {
                        paragraph {
                            text(
                                Bokmal to "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                                Nynorsk to "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet i folketrygda (G) blir teke med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på ${Constants.GRUNNBELOEP_URL}.",
                                English to "In calculating your disability benefit, we base calculations on your average income for the best three of the last five years prior to the onset of your disability. Income up to six times the National Insurance basic amount (G) is included in the calculation. The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at ${Constants.GRUNNBELOEP_URL}."
                            )
                        }

                        paragraph {
                            text(
                                Bokmal to "Du hadde inntekt i utlandet i minst ett av de fem siste årene før du ble ufør. Vi bruker ikke denne inntekten når vi beregner uføretrygden din. For å kompensere for dette, erstatter vi disse årene med et gjennomsnitt av årene du har hatt inntekt i Norge i denne femårsperioden. Du kan se hvilke år vi har brukt i tabellen «Inntekt lagt til grunn for beregning av uføretrygden din».",
                                Nynorsk to "Du hadde inntekt i utlandet i minst eitt av dei fem siste åra før du blei ufør. Vi bruker ikkje denne inntekta når vi bereknar uføretrygda di. For å kompensere for dette erstattar vi desse åra med eit gjennomsnitt av åra du har hatt inntekt i Noreg i denne femårsperioden. Du kan sjå kva år vi har brukt, i tabellen «Inntekt lagd til grunn for berekning av uføretrygda di».",
                                English to "You had income abroad for at least one of the last five years prior to the onset of your disability. This income will not be included when we calculate your disability benefit. To compensate, we will instead apply an average of your income from Norway during this five-year period. You can see which years we have applied in the table called \"Income included in the basis for calculation of your disability benefit\"."
                            )
                        }

                        paragraph {
                            text(
                                Bokmal to "Når vi beregner gjennomsnittet bruker vi bare de årene du hadde inntekt i Norge i femårsperioden. Hvis du hadde inntekt i Norge og i utlandet samme år, bruker vi den inntekten som er best for deg.",
                                Nynorsk to "Når vi bereknar gjennomsnittet, bruker vi berre dei åra du hadde inntekt i Noreg i femårsperioden. Dersom du hadde inntekt i Noreg og i utlandet same året, bruker vi den inntekta som er best for deg.",
                                English to "When we calculate the average income, we only include the years you had income in Norway during this five-year period. If you had income in Norway and abroad during the same year, we apply the income that will benefit you most."
                            )
                        }
                    }
                }
            }
        }
    }
}

