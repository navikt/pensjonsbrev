package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SUPPLERENDE_STOENAD_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

// infoAPinntekt_001
object InfoInntektAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon" },
                nynorsk { +"Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon" },
                english { +"Income from employment in addition to your retirement pension may increase your future pension" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du kan arbeide så mye du vil selv om du tar ut alderspensjon uten at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din øker."
                },
                nynorsk {
                    +"Du kan arbeide så mykje du vil sjølv om du tek ut alderspensjon, utan at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din aukar."
                },
                english {
                    +"You may combine work with drawing a pension, without deductions being made in your pension. " +
                            "If you continue to work, you may accumulate additional pension rights. This will apply up to and including the year you turn 75."
                },
            )
        }
    }
}

// utbetalingsInfoMndUtbet_001
object Utbetalingsinformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +
                    "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                            "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL."
                },
                nynorsk {
                    +
                    "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                            "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL."
                },
                english {
                    +
                    "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                            "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
                },
            )
        }
    }
}

class FlereBeregningsperioder(
    val antallPerioder: Expression<Int>,
    val totalPensjon: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    // flereBeregningsperioderVedlegg_001
    // TODO: Bør vi ikke heller her sjekke om dataene til vedlegget er med?
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(antallPerioder.greaterThan(1) and totalPensjon.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Du kan lese mer om andre beregningsperioder i vedlegget." },
                    nynorsk { +"Du kan lese meir om andre berekningsperiodar i vedlegget." },
                    english { +"There is more information about other calculation periods in the attachment." },
                )
            }
        }
    }
}

// infoAPOverskrift_001, infoAP_001
object InformasjonOmAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Hvor kan du få vite mer om alderspensjonen din?" },
                nynorsk { +"Kvar kan du få vite meir om alderspensjonen din?" },
                english { +"Where can you find out more about your retirement pension?" },
            )
        }
        // infoAP_001
        paragraph {
            text(
                bokmal {
                    +
                    "Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på $ALDERSPENSJON."
                },
                nynorsk {
                    +
                    "Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på $ALDERSPENSJON."
                },
                english {
                    +
                    "There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at $ALDERSPENSJON."
                },
            )
        }
        paragraph {
            text(
                bokmal { +"Informasjon om utbetalingene dine finner du på $DITT_NAV. Her kan du også endre kontonummeret ditt." },
                nynorsk { +"Informasjon om utbetalingane dine finn du på $DITT_NAV. Her kan du også endre kontonummeret ditt." },
                english {
                    +
                    "You can find more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number."
                },
            )
        }
    }
}

// meldEndringerPesys_001
object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du må melde fra om endringer" },
                nynorsk { +"Du må melde frå om endringar" },
                english { +"You must notify Nav if anything changes" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet," +
                            " eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav." +
                            " I slike tilfeller må du derfor straks melde fra til oss." +
                            " I vedlegget ser du hvilke endringer du må si fra om."
                },
                nynorsk {
                    +"Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet," +
                            " eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav." +
                            " I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om."
                },
                english {
                    +"If there are changes in your family situation or you are planning a long-term stay abroad," +
                            " or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav." +
                            " In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene." +
                            " Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav."
                },
                nynorsk {
                    +
                    "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane." +
                            " Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav."
                },
                english {
                    +"If your payments have been too high as a result of you failing to notify us of a change," +
                            " the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account," +
                            " and you are obligated to report any and all errors to Nav."
                },
            )
        }
    }
}

object MeldFraOmEndringer2 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // meldEndringerPesys_002
        // TODO: Denne er så godt som lik mange av dei andre meld fra om endringer. Bør samkjøres og legges i felles.
        title1 {
            text(
                bokmal { +"Du må melde fra om endringer" },
                nynorsk { +"Du må melde frå om endringar" },
                english { +"You must notify Nav if anything changes" },
            )
        }
        paragraph {
            text(
                bokmal { +"Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om." },
                nynorsk { +"Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om." },
                english {
                    +
                    "If your circumstances change, you must inform Nav immediately. The appendix specifies which changes you are obligated to notify us of."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +
                    "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav."
                },
                nynorsk {
                    +
                    "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav."
                },
                english {
                    +
                    "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav."
                },
            )
        }
    }
}

object ArbeidsinntektOgAlderspensjonKort : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // arbInntektAPOverskrift_001
        title1 {
            text(
                bokmal { +"Arbeidsinntekt og alderspensjon" },
                nynorsk { +"Arbeidsinntekt og alderspensjon" },
                english { +"Earned income and retirement pension" }
            )
        }

        // arbInntektAP_001
        paragraph {
            text(
                bokmal { +"Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker." },
                nynorsk { +"Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar." },
                english { +"You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension." }
            )
        }
    }
}

data class ArbeidsinntektOgAlderspensjon(
    val innvilgetFor67: Expression<Boolean>,
    val uttaksgrad: Expression<Int>,
    val uforeKombinertMedAlder: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        includePhrase(ArbeidsinntektOgAlderspensjonKort)
        // nyOpptjeningHelAP
        showIf(uttaksgrad.equalTo(100)) {
            paragraph {
                text(
                    bokmal {
                        +
                        "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig."
                    },
                    nynorsk {
                        +
                        "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig."
                    },
                    english {
                        +
                        "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed."
                    },
                )
            }
        }
        showIf(uttaksgrad.lessThan(100)) {
            // nyOpptjeningGradertAP
            paragraph {
                text(
                    bokmal {
                        +
                        "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå."
                    },
                    nynorsk {
                        +
                        "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no."
                    },
                    english {
                        +
                        "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated."
                    },
                )
            }
        }
        showIf(innvilgetFor67) {
            includePhrase(UfoereAlder.UfoereKombinertMedAlder(uforeKombinertMedAlder))
        }
    }
}

object ReguleringAvAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // reguleringAPOverskrift
        title1 {
            text(
                bokmal { +"Alderspensjonen din reguleres årlig" },
                nynorsk { +"Alderspensjonen din blir regulert årleg" },
                english { +"Your retirement pension will be adjusted annually" },
            )
        }
        // reguleringPen
        paragraph {
            text(
                bokmal {
                    +"Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. " +
                            "Du får informasjon om dette på utbetalingsmeldingen din. På $NAV_URL kan du lese mer om hvordan pensjonene reguleres."
                },
                nynorsk {
                    +"Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. " +
                            "Du får informasjon om dette på utbetalingsmeldinga di. På $NAV_URL kan du lese meir om korleis pensjonane blir regulerte."
                },
                english {
                    +
                    "The pension amount will be adjusted with effect from 1 May, and the actual increase is usually paid retroactively in June. " +
                            "You will be informed about this on your payout notice. You can read more about how pensions are adjusted at $NAV_URL."
                },
            )
        }
    }
}

object ReguleringAvGjenlevendetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år." },
                nynorsk { +"Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år." },
                english { +"The survivor’s supplement will not be adjusted when the pension increases from May 1st each year." },
            )
        }
    }
}

// infoInnvilgSupplerendeStonad
object SupplerendeStoenadAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Supplerende stønad" },
                nynorsk { +"Supplerande stønad" },
                english { +"Supplementary benefit" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. " +
                            "Stønaden er behovsprøvd og all inntekt fra Norge og utlandet blir regnet med. Inntekten til eventuell ektefelle, " +
                            "" +
                            "samboer eller registrert partner blir også regnet med. Du kan lese mer om supplerende stønad på nettsiden vår $SUPPLERENDE_STOENAD_URL."
                },
                nynorsk {
                    +"Dersom du har kort butid i Noreg når du fyller 67 år, kan du søkje om supplerande stønad. " +
                            "Stønaden er behovsprøvd, og all inntekt frå Noreg og utlandet blir rekna med. Inntekta til eventuell ektefelle, " +
                            "sambuar eller registrert partnar skal også reknast med. Du kan lese meir om supplerande stønad på nettsida vår $SUPPLERENDE_STOENAD_URL."
                },
                english {
                    +
                    "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. " +
                            "The benefit is means-tested and your total income from Norway and abroad is taken into account. " +
                            "The income of any spouse, cohabitant or registered partner will also be taken into account. " +
                            "You can read more about supplementary benefit at our website $SUPPLERENDE_STOENAD_URL."
                },
            )
        }
    }
}

// innvilgelseAPogAFPPrivat
data class AfpPrivatErBrukt(
    val uttaksgrad: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +
                    "Du får " + uttaksgrad.format() +
                            " prosent alderspensjon fordi summen av alderspensjonen og den avtalefestede pensjonen din (AFP) gjør at du har rett til alderspensjon før du fyller 67 år."
                },
                nynorsk {
                    +
                    "Du får " + uttaksgrad.format() +
                            " prosent alderspensjon fordi summen av alderspensjonen og den avtalefesta pensjonen din (AFP) gjer at du har rett til alderspensjon før 67 år."
                },
                english {
                    +
                    "You have been granted " + uttaksgrad.format() +
                            " percent retirement pension because your total retirement pension and contractual early retirement pension (AFP) makes you eligible for retirement pension before the age of 67."
                },
            )
        }
    }
}

object SoktAFPPrivatInfo : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Du har også søkt om avtalefestet pensjon (AFP), og du vil få et eget vedtak om dette." },
                nynorsk { +"Du har også søkt om avtalefesta pensjon (AFP), og du vil få eit eige vedtak om dette." },
                english {
                    +
                    "You have also applied for contractual early retirement pension (AFP) and will receive a separate decision on this."
                },
            )
        }
    }
}

object Skatteplikt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Spørsmål om skatteplikt til Norge etter flytting til utlandet må rettes til Skatteetaten." +
                            " Du må selv avklare spørsmål om skatteplikt til det landet du bor i med skattemyndighetene der."
                },
                nynorsk {
                    +"Spørsmål om skatteplikt til Noreg etter flytting til utlandet må rettast til Skatteetaten. " +
                            " Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skatteorgana der."
                },
                english {
                    +
                    "Questions about tax liability to Norway after moving abroad must be directed to the Norwegian Tax Administration." +
                            " You must clarify questions about tax liability to your country of residence with the local tax authorities."
                },
            )
        }
    }
}

object InnvilgelseAPForeloepigBeregning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Dette er en foreløpig beregning" },
                nynorsk { +"Dette er ei førebels berekning" },
                english { +"This is a preliminary calculation" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +
                    "Fordi du har arbeidet eller bodd i et land Norge har trygdeavtale med, er dette en foreløpig beregning basert på trygdetiden din i Norge. " +
                            "Når vi har mottatt nødvendig informasjon fra andre land som du har bodd eller arbeidet i, vil vi beregne pensjonen din på nytt og sende deg et endelig vedtak."
                },
                nynorsk {
                    +
                    "Fordi du har arbeidd eller budd i eit land Noreg har trygdeavtale med, er dette ei førebels berekning basert på trygdetida di i Noreg. " +
                            "Når vi har fått nødvendig informasjon frå andre land som du har budd eller arbeidd i, bereknar vi pensjonen din på nytt og sender deg eit endeleg vedtak."
                },
                english {
                    +
                    "Because you have worked or lived in a country that Norway has a social security agreement with, this is a preliminary calculation based on your period of national insurance cover in Norway. " +
                            "Once we have received the necessary information from the other countries that you have lived or worked in, we will re-calculate your pension and send you a final decision."
                },
            )
        }
    }
}

data class InnvilgelseAPUttakEndr(
    val uforeKombinertMedAlder: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Du kan søke om å endre pensjonen din" },
                nynorsk { +"Du kan søkje om å endre pensjonen din" },
                english { +"You can apply to change your pension" },
            )
        }
        // innvilgelseAPUttakEndr_002
        paragraph {
            text(
                bokmal {
                    +"Du kan ha mulighet til å ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon." +
                            " Etter at du har begynt å ta ut alderspensjon, kan du gjøre endringer med 12 måneders mellomrom." +
                            " Hvis du har høy nok opptjening, kan du ta ut 100 prosent alderspensjon når du selv ønsker det. Du kan alltid stanse pensjonen."
                },
                nynorsk {
                    +"Du kan ha høve til å ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon." +
                            " Etter at du har starta med å ta ut alderspensjon, kan du gjere endringar med tolv månaders mellomrom." +
                            " Dersom du har høg nok opptening, kan du ta ut 100 prosent alderspensjon når du sjølv ønskjer det. Du kan alltid stanse pensjonen."
                },
                english {
                    +"You are entitled to draw retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent." +
                            " Once you have started drawing your pension, you can make changes at 12-monthly intervals." +
                            " If you have high enough pension earnings, you can withdraw your full retirement pension whenever you want. You can stop drawing your pension at any time."
                },
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL for å se om du kan endre alderspensjonen din." },
                nynorsk { +"Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL for å sjå om du kan endre alderspensjonen din." },
                english { +"Use the pension calculator on $DIN_PENSJON_URL to see if you can change your retirement pension." },
            )
        }
        showIf(uforeKombinertMedAlder) {
            paragraph {
                text(
                    bokmal { +"Summen av uføregraden og alderspensjonen din kan ikke overstige 100 prosent." },
                    nynorsk { +"Summen av uføregraden og alderspensjonen din kan ikkje gå over 100 prosent." },
                    english {
                        +
                        "The percentage of disability benefit and the percentage of retirement pension combined may not exceed 100 percent."
                    },
                )
            }
        }
    }
}

object RettTilKlageUtland : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +
                    "Hvis du ønsker å klage på vedtak fra utenlandske trygdemyndigheter, må du kontakte trygdemyndighetene i det enkelte landet."
                },
                nynorsk {
                    +
                    "Dersom du ynskjer å klage på vedtak frå utanlandske trygdeorgan, må du kontakte trygdeorganet i det enkelte landet."
                },
                english {
                    +
                    "If you want to appeal a decision made by a foreign national insurance authority, you must get in contact with the national insurance authority in the relevant country."
                },
            )
        }
    }
}

object SkattAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Det er egne skatteregler for pensjon" },
                nynorsk { +"Det er eigne skattereglar for pensjon" },
                english { +"Pensions are subject to special tax rules" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du bør endre skattekortet når du begynner å ta ut alderspensjon." +
                            " Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister." +
                            " Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss."
                },
                nynorsk {
                    +"Du bør endre skattekortet når du byrjar å ta ut alderspensjon. " +
                            " Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar." +
                            " Vi får skattekortet elektronisk. Du skal derfor ikkje sende det til oss."
                },
                english {
                    +"When you start draw retirement pension, you should change your tax deduction card." +
                            " You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners." +
                            " We will receive the tax card directly from the Norwegian Tax Administration, meaning you do not need to send it to us."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +
                    "På $DIN_PENSJON_URL kan du se hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det." +
                            " Dersom du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed."
                },
                nynorsk {
                    +
                    "På $DIN_PENSJON_URL kan du sjå kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det." +
                            " Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed."
                },
                english {
                    +"At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want." +
                            " If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                },
            )
        }
    }
}

object PensjonsopptjeningInformasjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // vedleggBeregnPensjonsOpptjeningOverskrift
        title1 {
            text(
                bokmal { +"Pensjonsopptjeningen din" },
                nynorsk { +"Pensjonsoppteninga di" },
                english { +"Your accumulated pension capital" },
            )
        }
        // vedleggBeregnPensjonsOpptjening
        paragraph {
            text(
                bokmal {
                    +
                    "I nettjenesten Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsopptjeningen din for hvert enkelt år. Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg."
                },
                nynorsk {
                    +
                    "I nettenesta Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsoppteninga di for kvart enkelt år. Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg."
                },
                english {
                    +
                    "Our online service " + quoted("Din pensjon") +
                            " at $DIN_PENSJON_URL provides details on your accumulated rights for each year. Here you will be able to see your other types of pension rights we have registered."
                },
            )
        }
    }
}

object UfoereAlder {
    class UfoereKombinertMedAlder(
        val ufoereKombinertMedAlder: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(ufoereKombinertMedAlder) {
                paragraph {
                    text(
                        bokmal {
                            +
                            "Uføretrygden din kan fortsatt bli redusert på grunn av inntekt. Du finner informasjon om inntektsgrensen i vedtak om uføretrygd."
                        },
                        nynorsk {
                            +
                            "Uføretrygda di kan framleis bli redusert på grunn av inntekt. Du finn informasjon om inntektsgrensa i vedtak om uføretrygd."
                        },
                        english {
                            +
                            "Your disability benefit may still be reduced as a result of income. You can find information on the income limit in the decision on disability benefit."
                        },
                    )
                }
            }
        }
    }

    class DuFaar(
        val totalPensjon: Expression<Kroner>,
        val virkDatoFom: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +
                        "Du får " + totalPensjon.format() + " hver måned før skatt fra " + virkDatoFom.format() +
                                ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din."
                    },
                    nynorsk {
                        +
                        "Du får " + totalPensjon.format() + " kvar månad før skatt frå " + virkDatoFom.format() +
                                ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di."
                    },
                    english {
                        +
                        "You will receive " + totalPensjon.format() + " every month before tax from " + virkDatoFom.format() +
                                ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                    },
                )
            }
        }
    }
}

// feilutbetalingAP_001
object FeilutbetalingAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Feilutbetaling" },
                nynorsk { +"Feilutbetaling" },
                english { +"Incorrect payment" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +
                    "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mye utbetalt. Vi vil sende deg et eget varselbrev om en eventuell tilbakebetaling."
                },
                nynorsk {
                    +
                    "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mykje utbetalt. Vi vil sende deg eit eige varselbrev om ei eventuell tilbakebetaling."
                },
                english {
                    +
                    "We have reduced your retirement pension for a previous period. You have therefore been paid too much. We will send you a separate notice letter concerning possible repayment."
                },
            )
        }
    }
}

// beløpAP_001
class DuFaarHverMaaned(
    val totalPensjon: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(totalPensjon.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Du får " + totalPensjon.format() + " i alderspensjon fra folketrygden hver måned før skatt." },
                    nynorsk { +"Du får " + totalPensjon.format() + " i alderspensjon frå folketrygda kvar månad før skatt." },
                    english {
                        +
                        "You will receive " + totalPensjon.format() +
                                " every month before tax as retirement pension through the National Insurance Act."
                    },
                )
            }
        }
    }
}

object TrygdetidTittel : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Trygdetid" },
                nynorsk { +"Trygdetid" },
                english { +"National insurance coverage" },
            )
        }
    }
}

data class DerforHar(
    val initiertAvBrukerEllerVerge: Expression<Boolean>,
    val initiertAvNav: Expression<Boolean>,
) : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(initiertAvBrukerEllerVerge) {
            text(
                bokmal { +"Derfor har vi avslått søknaden din." },
                nynorsk { +"Derfor har vi avslått søknaden din." },
                english { +"We have declined your application for this reason." },
            )
        }.orShowIf(initiertAvNav) {
            text(
                bokmal { +"Derfor har du ikke rettigheter etter avdøde." },
                nynorsk { +"Derfor har du ikkje rettar etter avdøde." },
                english { +"You have no survivor’s rights in your retirement pension for this reason." },
            )
        }
    }
}

// nyBeregningAPTittel_001
data class BeregnaPaaNytt(
    val virkDatoFom: Expression<LocalDate>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Vi har beregnet alderspensjonen din på nytt fra " + virkDatoFom.format() },
            nynorsk { +"Vi har berekna alderspensjonen din på nytt frå " + virkDatoFom.format() },
            english { +"We have recalculated your retirement pension from " + virkDatoFom.format() },
        )
    }
}
