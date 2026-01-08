package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland

class OmstillingsstoenadAktivitetspliktFraser {

    data class DuMaaMeldeFraOmEndringer(
        val nasjonalEllerUtland: Expression<NasjonalEllerUtland>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                    bokmal { +"Vi minner om plikten din til å melde fra om endringer som kan ha betydning for " +
                            "stønaden du får fra oss. Du må melde fra hvis" },
                    nynorsk { +"Vi minner om at du pliktar å melde frå om endringar som kan ha betydning for " +
                            "stønaden du får frå oss. Du må melde frå dersom" },
                    english { +"Please remember that you are obligated to notify us about changes that may have significance " +
                            "to the benefits or allowances you receive from Nav. You must report any changes if" },
                )

                list {
                    item {
                        text(
                            bokmal { +"arbeidsinntekten din endrer seg" },
                            nynorsk { +"arbeidsinntekta di endrar seg" },
                            english { +"your income from work changes" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"arbeidssituasjonen din endrer seg" },
                            nynorsk { +"arbeidssituasjonen din endrar seg" },
                            english { +"your working situation changes" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du får innvilget andre stønader fra Nav" },
                            nynorsk { +"du får innvilga andre stønader frå Nav" },
                            english { +"you are granted other benefits from Nav" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du ikke lenger er arbeidssøker" },
                            nynorsk { +"du er ikkje lenger arbeidssøkjar" },
                            english { +"you are no longer a job seeker" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du endrer, avbryter eller reduserer omfanget av utdanningen din" },
                            nynorsk { +"du endrar, avbryt eller reduserer omfanget av utdanninga di" },
                            english { +"you change, discontinue or reduce the scope of your education/training" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du gifter deg" },
                            nynorsk { +"du giftar deg" },
                            english { +"you get married" },
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
                            bokmal { +"du skal flytte til et annet land" },
                            nynorsk { +"du skal flytte til eit anna land" },
                            english { +"you are moving to another country" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du får opphold i institusjon i minst tre måneder, eller fengsel i minst to måneder" },
                            nynorsk { +"du skal vere på ein institusjon i minst tre månader, eller i fengsel i minst to månader" },
                            english { +"you are placed in an institution for at least three months, or jail for at least two months" },
                        )
                    }
                }
            }

            title2 {
                text(
                    bokmal { +"Hvordan kan du melde fra?" },
                    nynorsk { +"Korleis kan du melde frå?" },
                    english { +"How do i report changes?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan melde fra til oss om endringer ved å benytte endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                    nynorsk { +"Du kan melde frå om endringar ved å bruke endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                    english { +"You can report changes by use the change form or submit information on ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                )
            }

            paragraph {
                val postadresse = ifElse(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND), Constants.Utland.POSTADRESSE, Constants.POSTADRESSE)
                text(
                    bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du sende brev til " + postadresse + "." },
                    nynorsk { +"Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til " + postadresse + "." },
                    english { +"Please send documentation as normal post if you do not use BankID or another login option. Send to " + postadresse + "." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du ikke melder fra om endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake." },
                    nynorsk { +"Dersom du får utbetalt for mykje stønad fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake det du ikkje hadde rett på." },
                    english { +"If you fail to report changes and/or are paid too much allowance, Nav has the right to collect the incorrect amount." },
                )
            }
        }
    }

    object HarDuHelseutfordringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Har du helseutfordringer?" },
                    nynorsk { +"Har du helseutfordringar?" },
                    english { +"Do you have health problems?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser " +
                            "eller støtteordninger ved ditt lokale Nav-kontor og på ${Constants.HELSE_URL}." },
                    nynorsk { +"Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar " +
                            "eller støtteordningar ved ditt lokale Nav-kontor og på ${Constants.HELSE_URL}." },
                    english { +"If you are faced with difficult health challenges, you can investigate the possibilities " +
                            "for other benefits or support schemes through your local Nav office, or check out various " +
                            "opportunities online: ${Constants.HELSE_URL}." },
                )
            }
        }
    }

    object TrengerDuHjelpTilAaFaaNyJobb : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Trenger du hjelp til å få ny jobb eller jobbe mer?" },
                    nynorsk { +"Treng du hjelp med å få ny jobb eller jobbe meir?" },
                    english { +"Do you need help getting a new job or more work?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Nav tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. " +
                            "Du kan finne flere jobbsøkertips og informasjon på ${Constants.ARBEID_URL}." },
                    nynorsk { +"Nav tilbyr ulike tenester og støtteordningar for deg som treng hjelp med å kome i " +
                            "arbeid. Du finn fleire jobbsøkjartips og informasjon på ${Constants.ARBEID_URL}." },
                    english { +"The Norwegian Labour and Welfare Administration (Nav) offers various services and " +
                            "support schemes for those who need help finding a job. You can find more job seeker's tips " +
                            "and information online: ${Constants.ARBEID_URL}." },
                )
            }

            paragraph {
                text(
                    bokmal { +"På nettsiden arbeidsplassen.no kan du søke etter ledige stillinger i ditt område " +
                            "eller andre steder i landet. Her kan du også opprette varsler på e-post for å få " +
                            "meldinger om jobber som passer deg. Du finner også mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}." },
                    nynorsk { +"På nettsida arbeidsplassen.no kan du søkje etter ledige stillingar i nærområdet " +
                            "ditt eller andre stader i landet. Her kan du også opprette varsel på e-post for å få " +
                            "meldingar om jobbar som passar deg. Du finn i tillegg mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}." },
                    english { +"To apply for vacant positions in your area or other place around the country, you " +
                            "can see this webpage: arbeidsplassen.no. You can also set up e-mail notifications to receive " +
                            "information and offers about jobs that are suited to your situation. There are also many " +
                            "good tips on looking for work here: ${Constants.FINN_JOBBENE_URL}." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Trenger du hjelp til å komme i jobb eller ønsker å øke arbeidsinnsatsen din, " +
                            "kan du få veiledning og støtte fra oss. Vi kan fortelle deg om ulike muligheter i " +
                            "arbeidsmarkedet eller snakke med deg om nødvendig utdanning eller andre tiltak som " +
                            "kan hjelpe deg med å komme i arbeid." },
                    nynorsk { +"Viss du treng hjelp med å kome i jobb, eller ønskjer å auke arbeidsinnsatsen din, " +
                            "kan du få rettleiing og støtte frå oss. Vi kan informere deg om ulike moglegheiter i " +
                            "arbeidsmarknaden, eller snakke med deg om nødvendig utdanning eller andre tiltak som kan " +
                            "hjelpe deg med å kome i arbeid." },
                    english { +"If you need help joining the work force or want to increase your work hours, you " +
                            "can get guidance and support from us. We can tell you about various opportunities in the " +
                            "labour market or talk to you about necessary education or other measures that can help you find work." },
                )
            }
        }
    }

    data class FellesInfoOmInntektsendring(
        val redusertEtterInntekt: Expression<Boolean>,
        val halvtGrunnbeloep: Expression<Kroner>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Du må melde fra hvis inntekten din endrer seg" },
                    nynorsk { +"Meld frå dersom inntekta di endrar seg" },
                    english { +"You must report any changes to your income" },
                )
            }

            showIf(redusertEtterInntekt) {
                paragraph {
                    text(
                        bokmal { +"For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis inntekten din endrer seg." },
                        nynorsk { +"For at du skal få rett utbetaling, er det viktig at du gir oss beskjed viss inntekta di endrar seg." },
                        english { +"To receive the correct amount, you are obligated to inform us about any changes to your income." },
                    )
                }
            } orShow {
                // 0.5 av G
                paragraph {
                    text(
                        bokmal { +"For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis " +
                                "du får en forventet årsinntekt som vil overstige et halvt grunnbeløp. Dette er per i dag " + halvtGrunnbeloep.format() +
                                ". Grunnbeløpet blir justert hvert år fra 1. mai." },
                        nynorsk { +"For at du skal få rett utbetaling, er det viktig at du gir oss beskjed viss du får ei " +
                                "forventa årsinntekt som vil overstige eit halvt grunnbeløp. Dette er per i dag " + halvtGrunnbeloep.format() +
                                ". Grunnbeløpet blir justert kvart år frå 1. mai." },
                        english { +"To receive the correct amount, you are obligated to inform us about any changes to " +
                                "your anticipated annual income that exceeds one half of the basic amount. This is currently " +
                                 halvtGrunnbeloep.format() +". The basic amount is adjusted on 1 May each year." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Hvis inntekten din har endret seg, må du oppgi ny forventet brutto inntekt for " +
                            "inneværende år. Gjelder inntektsendringen fra før du ble innvilget stønaden, skal du også " +
                            "oppgi inntekt fra januar til og med måneden før du fikk innvilget stønaden." },
                    nynorsk { +"Dersom inntekta di har endra seg, må du oppgi ny forventa brutto inntekt for " +
                            "inneverande år. Viss inntektsendringa gjeld frå før du blei innvilga stønaden, oppgir du i " +
                            "tillegg inntekt frå januar til og med månaden før du fekk innvilga stønaden." },
                    english { +"If your income has changed, you must state the new anticipated gross income for the " +
                            "current year. If the change in income applies from before you were granted the allowance, " +
                            "you must also state your income from January up to and including the month before you were " +
                            "granted the allowance." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi vil justere stønaden fra måneden etter du gir beskjed. Meld derfor fra snarest " +
                            "mulig for å få mest mulig riktig utbetalt omstillingsstønad, slik at etteroppgjøret blir " +
                            "så riktig som mulig. Du kan finne mer informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}." },
                    nynorsk { +"Vi vil justere stønaden frå månaden etter du gir beskjed. Meld difor frå snarast " +
                            "råd, slik at du får utbetalt omstillingsstønaden du har krav på, og etteroppgjeret blir " +
                            "mest mogleg rett. Du finn meir informasjon om etteroppgjer på ${Constants.OMS_ETTEROPPGJOER_URL}." },
                    english { +"We will then adjust your allowance starting in the month after you inform us of the change. " +
                            "It is therefore important to notify us as soon as possible to receive the correct amount of adjustment " +
                            "allowance, so that any final settlement will be as correct as possible. You can find more information " +
                            "about final settlements online: ${Constants.OMS_ETTEROPPGJOER_URL}." },
                )
            }
        }
    }

    data class FellesOppfyllelseAktivitetsplikt(
        val nasjonalEllerUtland: Expression<NasjonalEllerUtland>,
        val tolvMaanederEtterDoedsfall: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            title2 {
                text(
                    bokmal { +"Hvordan oppfylle aktivitetsplikten?" },
                    nynorsk { +"Korleis oppfyller du aktivitetsplikta?" },
                    english { +"How do I comply with the activity obligation?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du fyller aktivitetsplikten hvis du er " + ifElse(tolvMaanederEtterDoedsfall, "100 prosent", "minst 50 prosent" ) + " aktiv ved å" },
                    nynorsk { +"Du oppfyller aktivitetsplikta dersom du er " + ifElse(tolvMaanederEtterDoedsfall, "100 prosent", "minst 50 prosent") + " aktiv ved å" },
                    english { +"You are in compliance with the activity obligation if you are " + ifElse(tolvMaanederEtterDoedsfall, "100 percent", "at least 50 percent") + " active" },
                )
                list {
                    item {
                        text(
                            bokmal { +"jobbe" },
                            nynorsk { +"jobbe" },
                            english { +"working" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"være selvstendig næringsdrivende" },
                            nynorsk { +"vere sjølvstendig næringsdrivande" },
                            english { +"being self-employed/sole proprietor" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"etablere egen virksomhet (må godkjennes av Nav)" },
                            nynorsk { +"etablere eiga verksemd (må godkjennast av Nav)" },
                            english { +"setting up your own business (must be approved by Nav)" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"ta utdanning som er nødvendig og hensiktsmessig (må godkjennes av Nav)" },
                            nynorsk { +"ta utdanning som er nødvendig og føremålstenleg (må godkjennast av Nav)" },
                            english { +"taking a necessary and suitable education (must be approved by Nav)" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"være reell arbeidssøker" + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " i bostedslandet ditt", ""
                            ) },
                            nynorsk { +"vere reell arbeidssøkjar" + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " i landet der du er busett", ""
                            ) },
                            english { +"being a genuine job seeker" + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " in your country of residence", ""
                            ) },
                        )
                    }
                    item {
                        text(
                            bokmal { +"ha fått tilbud om jobb" },
                            nynorsk { +"ha fått tilbod om jobb" },
                            english { +"have received a job offer" },
                        )
                    }
                }
            }

        }
    }

    object FellesOppfyllelseUnntakFraAktivitetsplikt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Unntak fra aktivitetsplikten" },
                    nynorsk { +"Unntak frå aktivitetsplikta" },
                    english { +"Exemption from the activity obligation" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Det er mulig å få unntak fra aktivitetsplikten, men fortsatt ha rett til omstillingsstønad. Dette gjelder hvis du" },
                    nynorsk { +"Det er mogleg å få unntak frå aktivitetsplikta, og framleis ha rett på omstillingsstønad. Dette gjeld dersom du" },
                    english { +"It is possible to be exempt from the activity obligation and still have the right to an adjustment allowance. This applies if you" },
                )
                list {
                    item {
                        text(
                            bokmal { +"har omsorgen for barn under ett år" },
                            nynorsk { +"har ansvar for barn under eitt år" },
                            english { +"are caring for child under one year of age" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"har sykdom eller helseutfordringer som hindrer deg fra å være i minst 50 " +
                                    "prosent arbeid eller arbeidsrettet aktivitet, og du benytter din gjenværende arbeidsevne" },
                            nynorsk { +"har sjukdom eller helseutfordringar som hindrar deg i å vere i minst 50 " +
                                    "prosent arbeid eller arbeidsretta aktivitet, og du nyttar di attverande arbeidsevne" },
                            english { +"have an illness or health problems that hinder you from working at least 50 " +
                                    "percent or being involved in a work-related activity, and you use your residual ability to work" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"mottar sykepenger for full arbeidsuførhet" },
                            nynorsk { +"får sjukepengar for full arbeidsuførleik" },
                            english { +"are receiving sickness benefits for full disability" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"mottar arbeidsavklaringspenger" },
                            nynorsk { +"får arbeidsavklaringspengar" },
                            english { +"are receiving Work Assessment Allowance (AAP)" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"er forhindret fra å være i arbeid eller arbeidsrettet aktivitet på grunn av " +
                                    "sykdom, skade eller funksjonsnedsettelse hos barnet ditt. Barnets tilstand må " +
                                    "dokumenteres av lege. Det må også dokumenteres at barnets tilstand er årsaken til " +
                                    "at du er forhindret fra å være i arbeid eller arbeidsrettet aktivitet" },
                            nynorsk { +"er hindra i å vere i arbeid eller arbeidsretta aktivitet fordi du har eit " +
                                    "barn med sjukdom, skade eller funksjonsnedsetjing. Tilstanden til barnet må vere " +
                                    "dokumentert av lege. Det må også dokumenterast at tilstanden til barnet er årsaka " +
                                    "til at du er hindra i å vere i arbeid eller arbeidsretta aktivitet" },
                            english { +"are prevented from working or work-related activity due to illness, injury or " +
                                    "reduced functionality of your child. The child's condition must be documented by a medical doctor. " +
                                    "You must also document that the child's condition is the reason why you are being prevented " +
                                    "from working or being involved in a work-related activity" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"er forhindret fra å være i slik aktivitet på grunn av omsorg for barn som " +
                                    "mangler tilfredsstillende tilsynsordning, og den manglende tilsynsordningen ikke skyldes deg selv" },
                            nynorsk { +"er hindra i å vere i ein slik aktivitet grunna omsorg for barn som manglar " +
                                    "tilfredsstillande tilsynsordning, og du ikkje sjølv er skuld i den manglande tilsynsordninga" },
                            english { +"are prevented from engaging in such activity due to caring for a child who " +
                                    "lacks satisfactory supervision, and the lack of supervision is not your fault" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"har kortvarig fravær fra aktivitet på grunn av sykdom eller skade hos deg " +
                                    "eller barn du har omsorg for" },
                            nynorsk { +"har kortvarig fråvær frå aktivitet fordi anten du sjølv eller eit barn du " +
                                    "har omsorg for, har sjukdom eller skade" },
                            english { +"are absent from the activity temporarily due to illness or injury, either " +
                                    "yours or the child you are caring for" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"har en arbeidsevnevurdering eller annen dokumentasjon fra din lokale arbeidsformidling eller annen instans som viser i hvor stor grad du kan arbeide" },
                            nynorsk { +"har ei arbeidsevnevurdering eller annan dokumentasjon frå den lokale arbeidsformidlinga eller ein annan instans som viser i kor stor grad du kan arbeide" },
                            english { +"have an assessment of your capacity for work or other documentation from your local job centre or other relevant body that specifies the degree to which you are able to work" },
                        )
                    }
                }
            }

        }
    }
}