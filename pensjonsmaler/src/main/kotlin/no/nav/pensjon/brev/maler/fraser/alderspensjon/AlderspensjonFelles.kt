package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SUPPLERENDE_STOENAD_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.text
import kotlin.math.E


// infoAPinntekt_001
object InfoInntektAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon",
                Nynorsk to "Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon",
                English to "Income from employment in addition to your retirement pension may increase your future pension"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan arbeide så mye du vil selv om du tar ut alderspensjon, uten at pensjonen din blir redusert. " +
                        "Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din øker.",
                Nynorsk to "Du kan arbeide så mykje du vil sjølv om du tek ut alderspensjon, utan at pensjonen din blir redusert. " +
                        "Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din aukar.",
                English to "You may combine work with drawing a pension, without deductions being made in your pension. " +
                        "If you continue to work, you may accumulate additional pension rights. This will apply up to and including the year you turn 75."
            )
        }
    }
}

// utbetalingsInfoMndUtbet_001
object Utbetalingsinformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                        "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL.",
                Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                        "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL.",
                English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                        "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
            )
        }
    }
}

// infoAPOverskrift_001, infoAP_001
object InformasjonOmAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hvor kan du få vite mer om alderspensjonen din?",
                Nynorsk to "Kvar kan du få vite meir om alderspensjonen din?",
                English to "Where can you find out more about your retirement pension?"
            )
        }
        paragraph {
            text(
                Bokmal to "Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på $ALDERSPENSJON.",
                Nynorsk to "Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på $ALDERSPENSJON.",
                English to "There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at $ALDERSPENSJON.",
            )
        }
        paragraph {
            text(
                Bokmal to "Informasjon om utbetalingene dine finner du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                Nynorsk to "Informasjon om utbetalingane dine finn du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                English to "You can find more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number.",
            )
        }
    }
}

// meldEndringerPesys_001
object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du må melde fra om endringer",
                Nynorsk to "Du må melde frå om endringar",
                English to "You must notify Nav if anything changes",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet,"
                        + " eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav."
                        + " I slike tilfeller må du derfor straks melde fra til oss."
                        + " I vedlegget ser du hvilke endringer du må si fra om.",
                Nynorsk to "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet,"
                        + " eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav."
                        + " I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om.",
                English to "If there are changes in your family situation or you are planning a long-term stay abroad,"
                        + " or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav."
                        + " In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of.",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene."
                        + " Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav.",
                Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane."
                        + " Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav.",
                English to "If your payments have been too high as a result of you failing to notify us of a change,"
                        + " the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account,"
                        + " and you are obligated to report any and all errors to Nav.",
            )
        }
    }
}

data class ArbeidsinntektOgAlderspensjon(
    val uttaksgrad: Expression<Int>,
    val uforeKombinertMedAlder: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Arbeidsinntekt og alderspensjon",
                Nynorsk to "Arbeidsinntekt og alderspensjon",
                English to "Earned income and retirement pension",
            )
        }
        // arbInntektAP
        paragraph {
            text(
                Bokmal to "Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker.",
                Nynorsk to "Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar.",
                English to "You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension.",
            )
        }
        // nyOpptjeningHelAP
        showIf(uttaksgrad.equalTo(100)) {
            paragraph {
                text(
                    Bokmal to "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig.",
                    Nynorsk to "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig.",
                    English to "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed.",
                )
            }
        }.orShow {
            // nyOpptjeningGradertAP
            paragraph {
                text(
                    Bokmal to "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå.",
                    Nynorsk to "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no.",
                    English to "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated.",
                )
            }
        }
        // arbInntektAPogUT
        showIf(uforeKombinertMedAlder) {
            paragraph {
                text(
                    Bokmal to "Uføretrygden din kan fortsatt bli redusert på grunn av inntekt. Du finner informasjon om inntektsgrensen i vedtak om uføretrygd.",
                    Nynorsk to "Uføretrygda di kan framleis bli redusert på grunn av inntekt. Du finn informasjon om inntektsgrensa i vedtak om uføretrygd.",
                    English to "Your disability benefit may still be reduced as a result of income. You can find information on the income limit in the decision on disability benefit.",
                )
            }
        }
    }
}

object PensjonsopptjeningInformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // vedleggBeregnPensjonsOpptjeningOverskrift
        title1 {
            text(
                Bokmal to "Pensjonsopptjeningen din",
                Nynorsk to "Pensjonsoppteninga di",
                English to "Your accumulated pension capital"
            )
        }
        // vedleggBeregnPensjonsOpptjening
        paragraph {
            text(
                Bokmal to "I nettjenesten Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsopptjeningen din for hvert enkelt år. Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg.",
                Nynorsk to "I nettenesta Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsoppteninga di for kvart enkelt år. Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg.",
                English to "Our online service 'Din pensjon' at $DIN_PENSJON_URL provides details on your accumulated rights for each year. Here you will be able to see your other types of pension rights we have registered."
            )
        }
    }
}

object ReguleringAvAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // reguleringAPOverskrift
        title1 {
            text(
                Bokmal to "Alderspensjonen din reguleres årlig",
                Nynorsk to "Alderspensjonen din blir regulert årleg",
                English to "Your retirement pension will be adjusted annually",
            )
        }
        // reguleringPen
        paragraph {
            text(
                Bokmal to "Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. " +
                        "Du får informasjon om dette på utbetalingsmeldingen din. På Nav.no kan du lese mer om hvordan pensjonene reguleres.",
                Nynorsk to "Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. " +
                        "Du får informasjon om dette på utbetalingsmeldinga di. På Nav.no kan du lese meir om korleis pensjonane blir regulerte.",
                English to "The pension amount will be adjusted with effect from 1 May, and the actual increase is usually paid retroactively in June. " +
                        "You will be informed about this on your payout notice. You can read more about how pensions are adjusted at Nav.no.",
            )
        }
    }
}

object ReguleringAvGjenlevendetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år.",
                Nynorsk to "Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år.",
                English to "The survivor’s supplement will not be adjusted when the pension increases from May 1st each year.",
            )
        }
    }
}

// infoInnvilgSupplerendeStonad
object SupplerendeStoenadAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(Bokmal to "Supplerende stønad", Nynorsk to "Supplerande stønad", English to "Supplementary benefit")
        }
        paragraph {
            text(
                Bokmal to "Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. " +
                        "Stønaden er behovsprøvd og all inntekt fra Norge og utlandet blir regnet med. Inntekten til eventuell ektefelle, " + "" +
                        "samboer eller registrert partner blir også regnet med. Du kan lese mer om supplerende stønad på nettsiden vår ${SUPPLERENDE_STOENAD_URL}.",
                Nynorsk to "Dersom du har kort butid i Noreg når du fyller 67 år, kan du søkje om supplerande stønad. " +
                        "Stønaden er behovsprøvd, og all inntekt frå Noreg og utlandet blir rekna med. Inntekta til eventuell ektefelle, " +
                        "sambuar eller registrert partnar skal også reknast med. Du kan lese meir om supplerande stønad på nettsida vår ${SUPPLERENDE_STOENAD_URL}.",
                English to "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. " +
                        "The benefit is means-tested and your total income from Norway and abroad is taken into account. " +
                        "The income of any spouse, cohabitant or registered partner will also be taken into account. " +
                        "You can read more about supplementary benefit at our website ${SUPPLERENDE_STOENAD_URL}.",
            )
        }
    }
}


