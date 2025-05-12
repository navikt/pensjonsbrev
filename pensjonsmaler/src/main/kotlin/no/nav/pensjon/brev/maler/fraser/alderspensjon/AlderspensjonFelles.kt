package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text


// infoAPinntekt_001
object InfoInntektAP: OutlinePhrase<LangBokmalNynorskEnglish>() {
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
object Utbetalingsinformasjon: OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                        "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger.",
                Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                        "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger.",
                English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                        "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon."
            )
        }
    }
}

// infoAP_001
object informasjonOmAlderspensjon: OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
object meldeFraOmEndringer: OutlinePhrase<LangBokmalNynorskEnglish>() {
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

// rettTilKlagePesys_001
object rettTilKlageAlderspensjon: OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du har rett til å klage ",
                Nynorsk to "",
                English to "",
            )
        }
    }
}