package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.selectors.omstillingsstoenadBeregning.*
import no.nav.pensjon.etterlatte.maler.selectors.omstillingsstoenadBeregningsperiode.*

object OmstillingsstoenadInntektsjusteringFraser {

    data class HvorLengeKanDuFaaOmstillingsstoenad(
        val beregning: Expression<OmstillingsstoenadBeregning>,
        val omsRettUtenTidsbegrensning: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Hvor lenge kan du få omstillingsstønaden?" },
                    nynorsk { +"Kor lenge kan du få omstillingsstønad?" },
                    english { +"How long can you receive adjustment allowance?" },
                )
            }
            showIf(omsRettUtenTidsbegrensning) {
                paragraph {
                    text(
                        bokmal {
                            +"Du kommer inn under unntaksreglene for varighet av stønaden, fordi du har hatt " +
                                    "lav eller ingen inntekt de siste 5 årene før " +
                                    ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallstidspunktet") +
                                    ". Du får omstillingsstønad frem til du fyller 67 år, så lenge du oppfyller vilkårene."
                        },
                        nynorsk {
                            +"Du kjem inn under unntaksreglane for lengde på stønad, då du var utan " +
                                    "inntekt eller hadde låg inntekt dei siste 5 åra før " +
                                    ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                                    ". Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad fram til du " +
                                    "fyller 67 år. "
                        },
                        english {
                            +"If any of the rules for exemption from the duration of allowance apply to " +
                                    "you, because you had a low income or no income in the last 5 years before the " +
                                    ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                    ". You are eligible for the adjustment allowance until you turn " +
                                    "67 as long as you meet the conditions."
                        },
                    )
                }
            }.orShow {
                showIf(beregning.sisteBeregningsperiode.utbetaltBeloep.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Du får omstillingsstønad frem til det er gått 3 år " +
                                        ifElse(
                                            tidligereFamiliepleier,
                                            "siden pleieforholdet opphørte",
                                            "fra datoen for dødsfallet"
                                        ) + ", så " +
                                        "lenge du oppfyller vilkårene. Rett til omstillingsstønad faller uansett bort når du fyller 67 år."
                            },
                            nynorsk {
                                +"Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad " +
                                        "fram til det har gått 3 år frå datoen " +
                                        ifElse(
                                            tidligereFamiliepleier,
                                            "siden pleieforholdet opphørte",
                                            "for dødsfallet"
                                        ) +
                                        ". Rett til omstillingsstønad fell uansett bort når du fyller 67 år."
                            },
                            english {
                                +"You are eligible for adjustment allowance for 3 years from the " +
                                        ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                        ", as long as you meet the conditions for receiving the allowance. " +
                                        "The right to adjustment allowance ceases when you reach the age of 67."
                            },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Stønaden kan forlenges med inntil 2 år hvis du tar utdanning som er nødvendig " +
                                        "og hensiktsmessig, eller har behov for tiltak for å komme i arbeid. For å få " +
                                        "forlenget stønaden, må periodene være sammenhengende. Tiltak eller utdanning " +
                                        "må være i minst 50 prosent av full tid."
                            },
                            nynorsk {
                                +"Stønaden kan forlengast med inntil 2 år dersom du tek utdanning som er " +
                                        "nødvendig og føremålstenleg, eller har behov for tiltak for å kome i arbeid. " +
                                        "Stønaden kan forlengast, men då må periodane vere samanhengande. Tiltak eller " +
                                        "utdanning må vere i minst 50 prosent av full tid."
                            },
                            english {
                                +"The allowance may be extended by up to 2 years if you are undertaking education " +
                                        "that is necessary and appropriate, or if you require support to find work. To " +
                                        "have the allowance extended, the periods must be consecutive. The support or " +
                                        "education must be for at least 50 per cent of full-time hours."
                            },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal {
                                +"Du er innvilget omstillingsstønad frem til det er gått 3 år " +
                                        ifElse(
                                            tidligereFamiliepleier,
                                            "siden pleieforholdet opphørte",
                                            "fra datoen for dødsfallet"
                                        ) +
                                        ", så lenge du oppfyller vilkårene. Om det skjer endringer " +
                                        "i inntekten din kan dette gjør at du likevel vil få utbetalt stønad i denne perioden. "
                            },
                            nynorsk {
                                +"Under føresetnad av at du oppfyller vilkåra, får du omstillingsstønad " +
                                        "i 3 år frå datoen " + ifElse(
                                    tidligereFamiliepleier,
                                    "siden pleieforholdet opphørte",
                                    "for dødsfallet"
                                ) +
                                        ". Dersom inntekta di skulle endre seg, kan " +
                                        "dette gjere at du likevel får utbetalt stønad i denne perioden."
                            },
                            english {
                                +"You are eligible for adjustment allowance for 3 years from the " +
                                        ifElse(tidligereFamiliepleier, "care period ended", " date of the death") +
                                        ", as long as you meet the conditions for receiving the allowance. " +
                                        "Changes to your income may make you eligible for allowance in this period. "
                            },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Les mer om hvor lenge du kan få på " + Constants.OMS_HVORLENGE_URL + "." },
                    nynorsk { +"Les meir på " + Constants.OMS_HVORLENGE_URL + " om kor lenge du kan få stønad." },
                    english { +"Read more about the duration of allowance online: " + Constants.OMS_HVORLENGE_URL + "." },
                )
            }
        }
    }

    data class Aktivitetsplikt(
        val tidligereFamiliepleier: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må være i aktivitet fra 6 måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") },
                    nynorsk { +"Du må vere i aktivitet når det har gått 6 månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") },
                    english { +"You are obligated to be active starting 6 months after the " +
                        ifElse(tidligereFamiliepleier, "after care period ended", "death") },
                )
            }
            paragraph {
                text(
                    bokmal { +"Når det er gått 6 måneder etter " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                        " er du pliktig til å være i minst 50 prosent aktivitet for å motta omstillingsstønad. Når det" +
                        " er gått 12 måneder etter dødsfallet, kan det kreves at du er i inntil 100 prosent arbeid eller" +
                        " annen aktivitet med sikte på å komme i arbeid for å motta omstillingsstønad. Les mer om" +
                        " aktivitetsplikt og unntak av denne på ${Constants.OMS_AKTIVITETSPLIKT_URL}." },
                    nynorsk { +"For at du skal kunne halde fram med å få omstillingsstønad når det" +
                        " har gått 6 månader sidan " +
                        ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                        ", må du vere i minst 50 prosent aktivitet. Når det har gått 12 månader etter dødsfallet, kan" +
                        " det krevjast at du er i inntil 100 prosent arbeid eller anna aktivitet med sikte på å kome i" +
                        " arbeid for å få omstillingsstønad. Les meir om aktivitetsplikt og unntak på" +
                        " ${Constants.OMS_AKTIVITETSPLIKT_URL}." },
                    english { +"Once 6 months have passed since the " +
                        ifElse(tidligereFamiliepleier, "care period ended", "death") +
                        ", you are obligated to be active at least 50 percent to receive the adjustment allowance." +
                        " When 12 months have passed since the death, you may be required to work full time, or" +
                        " participate in other activity with the goal of returning to work, in order to receive" +
                        " adjustment allowance. Find out more about the duty to take action and exemptions from it at" +
                        " ${Constants.OMS_AKTIVITETSPLIKT_URL}." },
                )
            }
        }
    }

    data class DuKanKlagePaaVedtaket(
        val inntektsaar: Expression<Int>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du kan klage på vedtaket" },
                    nynorsk { +"Du kan klage på vedtaket" },
                    english { +"You have the right to appeal" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener vedtaket er feil, har du rett til å klage. Du må sende klagen innen " +
                            "6 uker fra 1. januar " + inntektsaar.format() + ". Dette følger av folketrygdloven " +
                            "§ 21-12. Du finner mer informasjon om hvordan du kan klage på ${Constants.KLAGE_URL}." },
                    nynorsk { +"Dersom du meiner vedtaket er feil, har du rett til å klage. Du må sende klaga innan " +
                            "6 veker frå 1. januar " + inntektsaar.format() + ". Dette følgjer av folketrygdlova " +
                            "§ 21-12. Du finn meir informasjon om korleis du kan klage på ${Constants.KLAGE_URL}." },
                    english { +"If you believe the decision is incorrect, you have the right to appeal. You must " +
                            "submit your appeal within 6 weeks from 1 January " + inntektsaar.format() + ". This is " +
                            "set out in Section 21-12 of the National Insurance Act. You can find more information " +
                            "on how to appeal at ${Constants.Engelsk.KLAGE_URL}." },
                )
            }
            includePhrase(Felles.KlageveiledningOgKlagebehandling)
        }
    }

    object Utbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Få oversikt over utbetalingene dine" },
                    nynorsk { +"Få oversikt over utbetalingane dine" },
                    english { +"Payment of adjustment allowance" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får omstillingsstønad den 20. hver måned, eller senest siste virkedag før denne datoen." },
                    nynorsk { +"Du får omstillingsstønad den 20. kvar månad, eller seinast siste verkedag før denne datoen." },
                    english { +"You will receive your adjustment allowance on the 20th of each month, or at the " +
                            "latest on the last working day before that date." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan se alle utbetalingene dine på ${Constants.MIN_SIDE_URL}. Der kan du også " +
                            "endre kontonummer. Hvis du har reservert deg mot digital kommunikasjon fra det " +
                            "offentlige, får du utbetalingsmelding i posten. Du kan også melde fra om endring i " +
                            "kontonummer via post. Kontakt oss hvis du trenger hjelp til dette." },
                    nynorsk { +"Du kan sjå alle utbetalingane dine på ${Constants.MIN_SIDE_URL}. Der kan du òg " +
                            "endra kontonummer. Dersom du har reservert deg mot digital kommunikasjon frå det " +
                            "offentlege, får du utbetalingsmelding i posten. Du kan òg melde frå om endring i " +
                            "kontonummer per post. Ta kontakt om du treng hjelp til det." },
                    english { +"You can view all your payments at ${Constants.MIN_SIDE_URL}. You can also change " +
                            "your account number there. If you have opted out of receiving digital communications " +
                            "from public authorities, you will receive a payment notification by post. You can also " +
                            "notify us of a change to your account number by post. Please contact us if you need " +
                            "help with this." },
                )
            }
        }
    }

    object HuskAaSjekkeSkattekortet : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Husk å sjekke skattekortet ditt" },
                    nynorsk { +"Hugs å sjekke skattekortet ditt" },
                    english { +"Remember to check your tax card" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du må betale skatt av omstillingsstønad. Dette vedtaket kan derfor ha betydning for " +
                            "beregningen av skatten din. Du bør sjekke at skattekortet ditt er oppdatert med det du " +
                            "forventer å ha av inntekt i løpet av året. Du kan endre skattekortet ditt hos " +
                            "Skatteetaten. Du finner mer informasjon på ${Constants.SKATTETREKK_URL}." },
                    nynorsk { +"Du må betale skatt av omstillingsstønad. Dette vedtaket kan derfor ha betydning for " +
                            "berekning av skatten din. Du bør sjekke at skattekortet ditt er oppdatert med det du " +
                            "forventar å ha av inntekt i løpet av året. Du kan endre skattekortet ditt hos " +
                            "Skatteetaten. Du finn meir informasjon på ${Constants.SKATTETREKK_URL}." },
                    english { +"You must pay tax on the adjustment allowance. This decision may therefore affect " +
                            "the calculation of your tax. You should check that your tax card is updated with the " +
                            "income you expect to receive during the year. You can change your tax card with the " +
                            "Norwegian Tax Administration (Skatteetaten). You can find more information at " +
                            "${Constants.SKATTETREKK_URL}." },
                )
            }
        }
    }

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" },
                    english { +"You must report any changes" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis det skjer endringer i inntekten eller livssituasjonen din, må du melde fra til " +
                            "Nav for at du skal få utbetalt riktig beløp. Dette følger av folketrygdloven § 21-3." },
                    nynorsk { +"Dersom det skjer endringar i inntekta eller livssituasjonen din, må du melde frå til " +
                            "Nav for at du skal få utbetalt riktig beløp. Dette følgjer av folketrygdlova § 21-3." },
                    english { +"If there are any changes to your income or personal circumstances, you must notify " +
                            "Nav so that you receive the correct amount. This is in accordance with Section 21-3 of " +
                            "the National Insurance Act." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du må melde fra til Nav hvis" },
                    nynorsk { +"Du må melde frå til Nav dersom" },
                    english { +"You must notify Nav if" },
                )
                list {
                    item {
                        text(
                            bokmal { +"arbeidsinntekten din endrer seg" },
                            nynorsk { +"arbeidsinntekta di endrar seg" },
                            english { +"your work income changes" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"arbeidssituasjonen din endrer seg" },
                            nynorsk { +"arbeidssituasjonen din endrar seg" },
                            english { +"your work situation changes" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du får innvilget andre stønader fra Nav" },
                            nynorsk { +"du får innvilga andre stønader frå Nav" },
                            english { +"you are granted any other benefits by Nav" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du blir innvilget full uføretrygd (omstillingsstønaden skal stoppe)" },
                            nynorsk { +"du blir innvilga full uføretrygd (omstillingsstønaden skal stoppe)" },
                            english { +"you will be granted full disability benefit (the adjustment allowance will stop)" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du ikke lenger er arbeidssøker" },
                            nynorsk { +"du ikkje lenger er arbeidssøkar" },
                            english { +"you are no longer seeking work" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du endrer, avbryter eller reduserer omfanget av utdanningen din" },
                            nynorsk { +"du endrar, avbryt eller reduserer omfanget av utdanninga di" },
                            english { +"you change, discontinue or reduce the scope of your education" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du gifter deg eller inngår partnerskap" },
                            nynorsk { +"du giftar deg eller inngår partnerskap" },
                            english { +"you are getting married or initiating cohabitation" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du blir samboer med en du har felles barn med eller tidligere har vært gift med" },
                            nynorsk { +"du blir sambuar med nokon du har felles barn med eller tidlegare har vore gift med" },
                            english { +"you become a cohabiting partner with someone you have common children with or were previously married to" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du får felles barn med ny samboer" },
                            nynorsk { +"du får barn med ny sambuar" },
                            english { +"you are expecting a child with a new cohabiting partner" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller skal flytte til et annet land" },
                            nynorsk { +"du skal opphalde deg utanfor Noreg i meir enn seks månader, eller du skal flytte til eit anna land" },
                            english { +"you stay outside of Norway for more than 6 months or move to another country" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du får varig opphold i institusjon" },
                            nynorsk { +"du får varig opphald i institusjon" },
                            english { +"you reside permanently in an institution" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du sitter i varetekt, soner straff eller er under forvaring" },
                            nynorsk { +"du sit i varetekt, sonar straff eller er under forvaring" },
                            english { +"you are in police custody, serving a sentence or in detention" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du har andre opplysninger som kan bety noe for retten til omstillingsstønad" },
                            nynorsk { +"du har andre opplysningar som kan bety noko for retten til omstillingsstønad" },
                            english { +"you have other information that may be relevant to your entitlement to adjustment allowance" },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du kan lese mer om pliktene til å gi riktige opplysninger til Nav, og hvordan du kan " +
                            "melde fra om endringer: ${Constants.ENDRINGER_URL}. Her kan du også ettersende " +
                            "dokumentasjon til saken din." },
                    nynorsk { +"Du kan lese meir om pliktene til å gi rett opplysningar til Nav, og korleis du kan " +
                            "melde frå om endringar: ${Constants.ENDRINGER_URL}. Her kan du òg ettersende " +
                            "dokumentasjon til saka di." },
                    english { +"You can read more about your obligations to provide accurate information to Nav, " +
                            "and how to report any changes: ${Constants.Engelsk.ENDRINGER_URL}. You can also submit " +
                            "supporting documents for your case here." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du flytter og endrer adresse så endrer du dette på ${Constants.FOLKEREGISTERET_URL}." },
                    nynorsk { +"Dersom du flyttar og endrar adresse så endrar du det på ${Constants.FOLKEREGISTERET_URL}." },
                    english { +"If you move house and change your address, you must update this at ${Constants.FOLKEREGISTERET_URL}." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Ta kontakt med oss hvis det har skjedd endringer som du er usikker på om du må melde fra om." },
                    nynorsk { +"Ta kontakt med oss dersom det har skjedd endringar som du er usikker på om du må melde frå om." },
                    english { +"Please contact us if there have been any changes and you are unsure whether you need to report them." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har fått utbetalt for mye fordi du ikke har meldt fra om at inntekten eller " +
                            "livssituasjonen din har endret seg, må du vanligvis betale tilbake pengene. Det er derfor " +
                            "viktig at du selv følger med på utbetalinger fra Nav og melder fra om eventuelle feil." },
                    nynorsk { +"Dersom du har fått utbetalt for mykje fordi du ikkje har meldt frå om at inntekta eller " +
                            "livssituasjonen din har endra seg, må du vanlegvis betale tilbake pengane. Det er derfor " +
                            "viktig at du sjølv følgjer med på utbetalingar frå Nav og melder frå om feil." },
                    english { +"If you have been overpaid because you did not report a change in your income or " +
                            "personal circumstances, you will usually have to repay the money. It is therefore " +
                            "important that you keep track of payments from Nav yourself and report any errors." },
                )
            }
        }
    }

    object InntektenDin : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Inntekten din" },
                    nynorsk { +"Inntekta di" },
                    english { +"Your income" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For at du skal motta korrekt omstillingsstønad, er det viktig at du informerer oss " +
                            "hvis inntekten din endrer seg. Vi justerer omstillingsstønaden fra måneden etter at du " +
                            "har gitt beskjed." },
                    nynorsk { +"For at du skal få rett omstillingsstønad, er det viktig at du informerer oss dersom " +
                            "inntekta di endrar seg. Vi justerer omstillingsstønaden frå månaden etter at du har gitt " +
                            "beskjed." },
                    english { +"In order for you to receive the correct adjustment allowance, it is important that " +
                            "you notify us of any changes to your income. We will adjust your adjustment allowance " +
                            "from the month after you have notified us." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Omstillingsstønaden blir justert når grunnbeløpet i folketrygden (G) endres 1. mai " +
                            "hvert år. Inntekten vi legger til grunn for beregningen av stønaden, blir derimot ikke " +
                            "oppdatert automatisk. Du må derfor også melde fra hvis inntekten din endrer seg som " +
                            "følge av denne justeringen." },
                    nynorsk { +"Omstillingsstønaden vert justert når grunnbeløpet i folketrygda (G) endrar seg frå " +
                            "1. mai kvart år. Inntekta som stønaden din er redusert etter, vert ikkje automatisk " +
                            "endra. Du må derfor melde frå dersom inntekta di endrar seg som følgje av justeringa i " +
                            "grunnbeløpet." },
                    english { +"The adjustment allowance is adjusted when the basic amount under the National " +
                            "Insurance Scheme (G) changes on 1 May each year. However, the income we use as the basis " +
                            "for calculating the allowance is not updated automatically. You must therefore also " +
                            "notify us if your income changes as a result of this adjustment." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan lese mer om omstillingsstønad og inntekt på ${Constants.OMS_INNTEKT_URL}." },
                    nynorsk { +"Du kan lese meir om omstillingsstønad og inntekt på ${Constants.OMS_INNTEKT_URL}." },
                    english { +"You can read more about the adjustment allowance and income at ${Constants.OMS_INNTEKT_URL}." },
                )
            }
        }
    }
}
