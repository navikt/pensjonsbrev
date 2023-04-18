package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.InntektFoerUfoereBegrunnelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

/*
Hvis
((brevkode <> «PE_UT_07_100»
OG
Brevkode <> PE_UT_05_100 OG brevkode <> PE_UT_04_115
OG Brevkode <> PE_UT_04_103 OG Brevkode <> PE_UT_06_100
OG Brevkode <> PE_UT_04_300 OG Brevkode <> PE_UT_14_300
OG
(IFUBegrunnelse <> «» ELLER IEUBegrunnelse <> «»))
ELLER
<Brevkode> = PE_UT_04_500
ELLER
(KravArsakType = sivilstandsendring
OG mottarminsteytelse = true))

OG <KravArsakType> <> soknad_bt
OG
<KravarsakType> <> endring_ifu
ELLER
((Brevkode = PE_UT_04_108 ELLER Brevkode = PE_UT_04 109)
OG
<AndelYtelseAvOIFU> > 95 %
OG
<KravArsakType> <> soknad_bt
OG
<KravarsakType> <> endring_ifu
)
OG
Brevkode <> PE_UT_07_200
ELLER
Brevkode <> PE_UT_06_300
 */


data class SlikFastsetterViInntektenDinFoerDuBleUfoer(
    val harDelvisUfoergrad: Expression<Boolean>,
    val inntektFoerUfoereBegrunnelse: Expression<InntektFoerUfoereBegrunnelse>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                Bokmal to "Inntekten før du ble ufør har betydning for hvordan vi fastsetter uføregraden din, og eventuell justering av uføretrygd ut fra inntekt. Inntekten før du ble ufør har ingen sammenheng med hvordan vi beregner uføretrygden din.",
                Nynorsk to "Inntekta di før du blei ufør har betydning for korleis vi fastset uføregraden din, og ei eventuell justering av uføretrygda ut frå inntekt. Inntekta di før du blei ufør har ingen samanheng med korleis vi bereknar uføretrygda di.",
                English to "We establish your grade of disability based on your income prior to disability. We also use your income prior to disability when adjusting your disability benefit based on income. Your income prior to disability is used to calculate the amount of disability benefit."
            )
        }

        // TBU048V
        paragraph {
            text(
                Bokmal to "Inntekten før du ble ufør har betydning for hvordan vi fastsetter uføregraden din, og eventuell justering av uføretrygd ut fra inntekt. Inntekten før du ble ufør har ingen sammenheng med hvordan vi beregner uføretrygden din.",
                Nynorsk to "Når vi fastset inntekta di før du blei ufør tek vi utgangspunkt i den normale inntektssituasjonen din før du blei ufør. Dersom du ikkje har arbeidd i full stilling, vil inntekta di bli rekna om til ein årsinntekt i full stilling. Inntekt før du blei ufør blir fastsett forskjellig for arbeidstakare, sjølvstendig næringsdrivande og personar utan inntekt.",
                English to "When we establish you income prior to your disability, we base our calculations on your normal income prior to your date of disability. If you did not work full time, your pensionable income will be recalculated to its full-time equivalent. Your income prior to your disability will be established differently for employees, self-employed and persons without an income."
            )
        }

        paragraph {
            text(
                Bokmal to "For deg som er arbeidstaker",
                Nynorsk to "For deg som er arbeidstakar",
                English to "If you are employed"
            )
            text(
                Bokmal to "Inntekten din før du ble ufør skal som hovedregel tilsvare de inntektsmulighetene du hadde eller kunne hatt før uføretidspunktet. I de fleste tilfeller vil vi bruke den pensjonsgivende inntekten du hadde året før du ble ufør eller sykepengegrunnlaget ditt",
                Nynorsk to "Inntekta di før du blei ufør skal som hovudregel tilsvare dei inntektsmoglegheitane du hadde eller kunne hatt før uføretidspunktet. I dei fleste tilfeller vil vi bruke den pensjonsgivande inntekta du hadde året før du blei ufør eller sjukepengegrunnlaget ditt.",
                English to "As a main rule, your income prior to disability equals your ability to have an income before the time of disability. We will in most cases use your pensionable income the year prior to your illness or the base of calculation for your sickness benefit."
            )
            // TBU049V
            showIf(harDelvisUfoergrad) {
                text(
                    Bokmal to "Hvis du er i arbeid kan vi fastsette inntekten din før du ble ufør ut fra din nåværende stillingsandel. Inntekten du har skal regnes om til en årsinntekt i full stilling. Årsinntekten blir deretter justert tilbake til uføretidspunktet ditt og vil tilsvare inntekten din før du ble ufør. Dette gjør vi for at du skal få riktig uføregrad.",
                    Nynorsk to "Dersom du er i arbeid kan vi fastsetje inntekta di før du blei ufør ut frå den nåværande stillingsdelen din. Inntekta skal reknas om til ei årsinntekt i full stilling. Årsinntekta blir deretter justert tilbake til uføretidspunktet ditt og vil tilsvare inntekta di før du blei ufør. Dette gjer vi for at du skal få riktig uføregrad.",
                    English to "We will in certain cases establish your income prior to disability on the basis of your employment fraction. Your pensionable income will be recalculated to its full-time equivalent. We will then readjust your yearly income to your date of disability. This will equal to your income prior to your date of disability. We do this to ensure that your grade of disability is correct."
                )
            }
        }

        // TBU200V
        paragraph {
            text(
                Bokmal to "For deg som er selvstendig næringsdrivende",
                Nynorsk to "For deg som er sjølvstendig næringsdrivande",
                English to "If you are self-employed"
            )
            text(
                Bokmal to "Inntekten din før du ble ufør skal tilsvare de inntektsmulighetene du hadde før uføretidspunktet. Vi bruker gjennomsnittet av din pensjonsgivende inntekt for de tre siste årene før du ble ufør. Alle tre årene skal være med, selv om inntektene har variert mye.  Det kan gjøres unntak fra denne regelen, dersom sykdom eller skade har redusert inntektsmulighetene dine over flere år.",
                Nynorsk to "Inntekta di før du blei ufør skal svare til dei inntektsmoglegheitene du hadde før uføretidspunktet. Vi bruker gjennomsnittet av den pensjonsgivande inntekta di for dei tre siste åra før du blei ufør. Alle tre åra skal vere med, sjølv om inntektene har variert mykje.  Det kan gjerast unntak frå denne regelen, dersom sjukdom eller skade har redusert inntektsmoglegheitene dine over fleire år.",
                English to "Your income prior to disability should correspond to the income opportunities you had before the disability. We calculate your average income from the last three years before the disability. All three years will be included, regardless of any income variation. Exceptions to this rule can be made if illness or injury has reduced your income opportunities over several years."
            )
        }

        // TBU201V / INCLUDE IF brevkode = "PE_UT_04_500 OR
        showIf(inntektFoerUfoereBegrunnelse.isOneOf(InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_3, InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_4, InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_5)) {
            paragraph {
                text(
                    Bokmal to "Minstenivå på inntekt før uførhet ",
                    Nynorsk to "Minstenivå på inntekt før du blei ufør",
                    English to "The minimum level of income prior to date of disability"
                )
            }
            paragraph {
                text(
                    Bokmal to "Inntekten din før du ble ufør skal ikke settes lavere enn:",
                    Nynorsk to "Inntekta di før du blei ufør skal ikkje setjas lågare enn:",
                    English to "Your income prior to your date of disability will not be set lower than:"
                )
                list {
                    item {
                        text(Bokmal to "3,3 ganger grunnbeløpet dersom du lever sammen med ektefelle/partner/samboer. Samboerforholdet ditt må ha vart i minst 12 av de siste 18 månedene.",
                            Nynorsk to "3,5 ganger grunnbeløpet dersom du er enslig.",
                            English to "4,5 ganger grunnbeløpet dersom du har rett til uføretrygd med rettighet som ung ufør."
                        )
                    }
                    item {
                        text(Bokmal to "3,3 gonger grunnbeløpet dersom du lever saman med ektefelle/partner/sambuar. Sambuarforholdet ditt må ha vart i minst 12 av dei siste 18 månadene.",
                            Nynorsk to "3,5 gonger grunnbeløpet dersom du er einsleg.",
                            English to "4,5 gonger grunnbeløpet dersom du har rett til uføretrygd med rett som ung ufør."
                        )
                    }
                    item {
                        text(Bokmal to "3.3 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months.",
                            Nynorsk to "3.5 times the National Insurance basic amount if you are single.",
                            English to "4.5 times the National Insurance basic amount if you are a young disabled individual."
                        )
                    }
                }
            }
        }

        // TBU053
        showIf(harDelvisUfoergrad) {
            paragraph {
                text(
                    Bokmal to "Endring av inntekt før du ble ufør",
                    Nynorsk to "Endring av inntekt før du blei ufør",
                    English to "Change of income before your disability"
                )
                text(
                    Bokmal to "For å ha rett til å endre den fastsatte inntekten din før du ble ufør, må du ha hatt en varig inntektsøkning uten at stillingsandelen din har økt. Inntekt før uførhet kan bare endres dersom du mottar gradert uføretrygd.",
                    Nynorsk to "For å ha rett til å endre den fastsette inntekta di før du blei ufør, må du ha hatt ein varig inntektsaukning utan at stillingsdelen din har blitt auka. Inntekta før du blei ufør kan bare endras dersom du får gradert uføretrygd.",
                    English to "In order for you to have the right to amend your established income prior to your disability, you must have had a permanent increase in income without an increase in your percentage of full-time equivalent. Income prior to disability may only be amended if you are receiving a graduated disability benefit."
                )
            }
        }
    }
}

