package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTOSelectors.aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTOSelectors.redusertEtterInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTOSelectors.utbetaling

data class OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
    val aktivitetsgrad: Aktivitetsgrad,
    val utbetaling: Boolean,
    val redusertEtterInntekt: Boolean,
    val nasjonalEllerUtland: NasjonalEllerUtland
)

enum class Aktivitetsgrad { IKKE_I_AKTIVITET, UNDER_50_PROSENT, OVER_50_PROSENT };

enum class NasjonalEllerUtland { NASJONAL, UTLAND};

@TemplateModelHelpers
object OmstillingstoenadAktivitetspliktInformasjon4mndInnhold :
    EtterlatteTemplate<OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AKTIVITETSPLIKT_INFORMASJON_4MND_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata =
        LetterMetadata(
            displayTitle = "Informasjon om omstillingsstønaden din",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Informasjon om omstillingsstønaden din",
                Nynorsk to "Informasjon om omstillingsstønaden din",
                English to "TODO",
            )
        }

        outline {
            showIf(utbetaling){
                paragraph {
                    text(
                        Bokmal to "Du har omstillingsstønad. ",
                        Nynorsk to "Du har omstillingsstønad. ",
                        English to "TODO",
                    )
                    text(
                        Bokmal to "LEGG TIL HVIS STØNADEN ER REDUSERT FOR INNTEKT: Omstillingsstønaden din er redusert etter en arbeidsinntekt på <Forventet inntekt totalt, avrundet> kroner per år. ",
                        Nynorsk to "LEGG TIL HVIS STØNADEN ER REDUSERT FOR INNTEKT: Omstillingsstønaden din har blitt redusert ut frå ei arbeidsinntekt på <Forventet inntekt totalt, avrundet> kroner per år. ",
                        English to "TODO",
                    )
                    text(
                        Bokmal to "LEGG TIL HVIS STØNADEN ER IKKE REDUSERT FOR INNTEKT: Omstillingsstønaden din er i dag ikke redusert etter arbeidsinntekt eller annen inntekt som er likestilt med arbeidsinntekt.",
                        Nynorsk to "LEGG TIL HVIS STØNADEN ER IKKE REDUSERT FOR INNTEKT: Omstillingsstønaden din er i dag ikkje redusert ut frå arbeidsinntekt eller anna inntekt som er likestilt med arbeidsinntekt.",
                        English to "TODO",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Du er innvilget omstillingsstønad. Denne er i dag redusert etter en forventet " +
                                "inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET>. Du får derfor ikke utbetalt omstillingsstønad.",
                        Nynorsk to "Du er innvilga omstillingsstønad. Denne har i dag blitt redusert ut frå ei " +
                                "forventa inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET>. Du får difor ikkje utbetalt omstillingsstønad.",
                        English to "TODO",
                    )
                }
            }

            title2 {
                text(
                    Bokmal to "Krav til aktivitet ved mottak av omstillingsstønad",
                    Nynorsk to "Krav til aktivitet ved mottak av omstillingsstønad",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Når det er gått seks måneder etter dødsfallet, er det et krav for å motta " +
                            "omstillingsstønad at du er i minst 50 prosent arbeid eller annen aktivitet med sikte på å " +
                            "komme i arbeid. 12 måneder etter dødsfallet kan det kreves 100 prosent aktivitet.",
                    Nynorsk to "For å kunne halde fram med å få omstillingsstønad når det har gått seks månader " +
                            "sidan dødsfallet, må du vere i minst 50 prosent arbeid eller annan aktivitet med sikte på " +
                            "å kome i arbeid. 12 månader etter dødsfallet kan det bli stilt krav om 100 prosent aktivitet.",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du opplyste FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. i søknaden at du er i " +
                            "40 prosent arbeid/ikke er i arbeid.",
                    Nynorsk to "Du opplyste FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. i søknaden at du er i " +
                            "40 prosent arbeid/ikke er i arbeid.",
                    English to "TODO",
                )
            }

            showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.UNDER_50_PROSENT) or aktivitetsgrad.equalTo(Aktivitetsgrad.IKKE_I_AKTIVITET)) {
                paragraph {
                    text(
                        Bokmal to "For å motta omstillingsstønad videre må du øke aktiviteten din. Se “Hvordan oppfylle aktivitetsplikten?”.  Hvis du ikke foretar deg noen av de andre aktivitetene som er nevnt, må du melde deg som reell arbeidssøker hos NAV. Dette innebærer at du sender meldekort, er aktiv med å søke jobber, samt deltar på de kurs som NAV tilbyr.",
                        Nynorsk to "For å kunne få omstillingsstønad vidare må du auke aktiviteten din. Sjå «Korleis oppfyller du aktivitetsplikta?».  Dersom du ikkje gjer nokon av dei andre aktivitetane som er nemnde, må du melde deg som reell arbeidssøkjar hos NAV. Dette inneber at du sender meldekort, er aktiv med å søkje jobbar, og deltek på kursa som NAV tilbyr.",
                        English to "TODO",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Hvis du ikke kan registrere deg elektronisk må du møte opp på lokalkontoret ditt for å registrere deg som reell arbeidssøker.",
                        Nynorsk to "Dersom du ikkje kan registrere deg elektronisk, møter du opp på lokalkontoret ditt for å registrere deg som reell arbeidssøkjar.",
                        English to "TODO",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Er det en grunn til at du ikke kan være reell arbeidssøker eller gjøre annet som oppfyller aktivitetsplikten på minst 50 prosent, må du sende oss dokumentasjon på dette snarest mulig og senest innen <DATO 5 MND. ETTER DØDSFALL>. Se “Unntak for aktivitetsplikten” under.",
                        Nynorsk to "Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere anna som oppfyller aktivitetsplikta på minst 50 prosent, må du sende oss dokumentasjon på dette snarast mogleg og seinast innan <DATO 5 MND. ETTER DØDSFALL>. Sjå «Unntak frå aktivitetsplikta» under.",
                        English to "TODO",
                    )
                }
            }

            showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.OVER_50_PROSENT) and not(utbetaling)) {
                paragraph {
                    text(
                        Bokmal to "Hvis situasjonen din er endret, må du gi oss informasjon om din nye situasjon " +
                                "snarest mulig og senest innen <dato 5 mnd. etter dødsfall>. Les mer om hvordan du kan " +
                                "fylle aktivitetsplikten og om unntak fra aktivitetsplikten lenger ned i brevet.",
                        Nynorsk to "Dersom situasjonen din har endra seg, må du gi oss informasjon om den nye " +
                                "situasjonen snarast mogleg og seinast innan <dato 5 mnd. etter dødsfall>. Lenger ned " +
                                "i brevet kan du lese meir om korleis du kan oppfylle aktivitetsplikta, og kva som er " +
                                "unntaka frå aktivitetsplikta.",
                        English to "TODO",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Du finner informasjon om hvordan du melder fra under “Du må melde fra om endringer”.",
                    Nynorsk to "Under «Du må melde frå om endringar» finn du meir informasjon om korleis du melder frå.",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Omstillingsstønaden skal reduseres etter inntekten din",
                    Nynorsk to "Omstillingsstønaden skal reduserast ut frå inntekta di",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Stønaden skal reduseres med 45 prosent av arbeidsinntekten din og inntekt som er " +
                            "likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygden (G). " +
                            "Stønaden blir redusert ut fra det du oppgir som forventet inntekt for gjeldende år.",
                    Nynorsk to "Stønaden skal reduserast med 45 prosent av arbeidsinntekta di og inntekt som er " +
                            "likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygda (G). " +
                            "Stønaden blir redusert ut frå det du oppgir som forventa inntekt for gjeldande år.",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "Meld frå dersom inntekta di endrar seg",
                    English to "TODO",
                )
            }

            showIf(redusertEtterInntekt){
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis inntekten din endrer seg.",
                        Nynorsk to "For at du skal få rett utbetaling, er det viktig at du gir oss beskjed viss inntekta di endrar seg.",
                        English to "TODO",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis du får en forventet årsinntekt som vil overstige et halvt grunnbeløp. Dette er per i dag 59 310 kroner. Grunnbeløpet blir justert hvert år fra 1. mai.",
                        Nynorsk to "For at du få rett utbetaling, er det viktig at du gir oss beskjed viss du får ei forventa årsinntekt som vil overstige eit halvt grunnbeløp. Dette er per i dag 59 310 kroner. Grunnbeløpet blir justert kvart år frå 1. mai.",
                        English to "TODO",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis inntekten din har endret seg, må du oppgi ny forventet brutto inntekt for " +
                            "inneværende år. Gjelder inntektsendringen fra før du ble innvilget stønaden, skal du også " +
                            "oppgi inntekt fra januar til og med måneden før du fikk innvilget stønaden.",
                    Nynorsk to "Dersom inntekta di har endra seg, må du oppgi ny forventa brutto inntekt for " +
                            "inneverande år. Viss inntektsendringa gjeld frå før du blei innvilga stønaden, oppgir du i " +
                            "tillegg inntekt frå januar til og med månaden før du fekk innvilga stønaden.",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi vil justere stønaden fra måneden etter du gir beskjed. Meld derfor fra snarest " +
                            "mulig for å få mest mulig riktig utbetalt omstillingsstønad, slik at etteroppgjøret blir " +
                            "så riktig som mulig. Du kan finne mer informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "Vi vil justere stønaden frå månaden etter du gir beskjed. Meld difor frå snarast " +
                            "råd, slik at du får utbetalt omstillingsstønaden du har krav på, og etteroppgjeret blir " +
                            "mest mogleg rett. Du finn meir informasjon om etteroppgjer på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Hvordan oppfylle aktivitetsplikten?",
                    Nynorsk to "Korleis oppfyller du aktivitetsplikta?",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du fyller aktivitetsplikten hvis du er minst 50 prosent aktiv ved å",
                    Nynorsk to "Du oppfyller aktivitetsplikta dersom du er minst 50 prosent aktiv ved å",
                    English to "TODO",
                )
                list {
                    item {
                        text(
                            Bokmal to "jobbe",
                            Nynorsk to "jobbe",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "være selvstendig næringsdrivende",
                            Nynorsk to "vere sjølvstendig næringsdrivande",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "etablere egen virksomhet (må godkjennes av NAV)",
                            Nynorsk to "etablere eiga verksemd (må godkjennast av NAV)",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ta utdanning som er nødvendig og hensiktsmessig (må godkjennes av NAV)",
                            Nynorsk to "ta utdanning som er nødvendig og føremålstenleg (må godkjennast av NAV)",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "være reell arbeidssøker",
                            Nynorsk to "vere reell arbeidssøkjar",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ha fått tilbud om jobb",
                            Nynorsk to "ha fått tilbod om jobb",
                            English to "TODO",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Unntak fra aktivitetsplikten",
                    Nynorsk to "Unntak frå aktivitetsplikta",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er mulig å få unntak fra aktivitetsplikten, men fortsatt ha rett til omstillingsstønad. Dette gjelder hvis du",
                    Nynorsk to "Det er mogleg å få unntak frå aktivitetsplikta, og framleis ha rett på omstillingsstønad. Dette gjeld dersom du",
                    English to "TODO",
                )
                list {
                    item {
                        text(
                            Bokmal to "har omsorgen for barn under ett år",
                            Nynorsk to "har ansvar for barn under eitt år",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har sykdom eller helseutfordringer som hindrer deg fra å være i minst 50 " +
                                    "prosent arbeid eller arbeidsrettet aktivitet, og du benytter restarbeidsevnen din",
                            Nynorsk to "har sjukdom eller helseutfordringar som hindrar deg i å vere i minst 50 " +
                                    "prosent arbeid eller arbeidsretta aktivitet, og du nyttar restarbeidsevna di",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar sykepenger for full arbeidsuførhet",
                            Nynorsk to "får sjukepengar for full arbeidsuførleik",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar arbeidsavklaringspenger",
                            Nynorsk to "får arbeidsavklaringspengar",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "er forhindret fra å være i arbeid eller arbeidsrettet aktivitet på grunn av " +
                                    "sykdom, skade eller funksjonsnedsettelse hos barnet ditt. Barnets tilstand må " +
                                    "dokumenteres av lege. Det må også dokumenteres at barnets tilstand er årsaken til " +
                                    "at du er forhindret fra å være i arbeid eller arbeidsrettet aktivitet",
                            Nynorsk to "er hindra i å vere i arbeid eller arbeidsretta aktivitet fordi du har eit " +
                                    "barn med sjukdom, skade eller funksjonsnedsetjing. Tilstanden til barnet må vere " +
                                    "dokumentert av lege. Det må også dokumenterast at tilstanden til barnet er årsaka " +
                                    "til at du er hindra i å vere i arbeid eller arbeidsretta aktivitet",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "er forhindret fra å være i slik aktivitet på grunn av omsorg for barn som " +
                                    "mangler tilfredsstillende tilsynsordning, og den manglende tilsynsordningen ikke skyldes deg selv",
                            Nynorsk to "er hindra i å vere i ein slik aktivitet grunna omsorg for barn som manglar " +
                                    "tilfredsstillande tilsynsordning, og du ikkje sjølv er skuld i den manglande tilsynsordninga",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har kortvarig fravær fra aktivitet på grunn av sykdom eller skade hos deg " +
                                    "eller barn du har omsorg for",
                            Nynorsk to "har kortvarig fråvær frå aktivitet fordi anten du sjølv eller eit barn du " +
                                    "har omsorg for, har sjukdom eller skade",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har en arbeidsevnevurdering fra ditt lokale NAV-kontor som sier at du ikke kan arbeide",
                            Nynorsk to "har ei arbeidsevnevurdering frå det lokale NAV-kontoret ditt på at du ikkje kan jobbe",
                            English to "TODO",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp med å få ny jobb eller jobbe meir?",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. " +
                            "Du kan finne flere jobbsøkertips og informasjon på ${Constants.ARBEID_URL}.",
                    Nynorsk to "NAV tilbyr ulike tenester og støtteordningar for deg som treng hjelp med å kome i " +
                            "arbeid. Du finn fleire jobbsøkjartips og informasjon på ${Constants.ARBEID_URL}.",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "På nettsiden arbeidsplassen.no kan du søke etter ledige stillinger i ditt område " +
                            "eller andre steder i landet. Her kan du også opprette varsler på e-post for å få " +
                            "meldinger om jobber som passer deg. Du finner også mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}.",
                    Nynorsk to "På nettsida arbeidsplassen.no kan du søkje etter ledige stillingar i nærområdet " +
                            "ditt eller andre stader i landet. Her kan du også opprette varsel på e-post for å få " +
                            "meldingar om jobbar som passar deg. Du finn i tillegg mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}.",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Trenger du hjelp til å komme i jobb eller ønsker å øke arbeidsinnsatsen din, " +
                            "kan du få veiledning og støtte fra oss. Vi kan fortelle deg om ulike muligheter i " +
                            "arbeidsmarkedet eller snakke med deg om nødvendig utdanning eller andre tiltak som " +
                            "kan hjelpe deg med å komme i arbeid.",
                    Nynorsk to "Viss du treng hjelp med å kome i jobb, eller ønskjer å auke arbeidsinnsatsen din, " +
                            "kan du få rettleiing og støtte frå oss. Vi kan informere deg om ulike moglegheiter i " +
                            "arbeidsmarknaden, eller snakke med deg om nødvendig utdanning eller andre tiltak som kan " +
                            "hjelpe deg med å kome i arbeid.",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser " +
                            "eller støtteordninger ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar " +
                            "eller støtteordningar ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi minner om plikten din til å melde fra om endringer som kan ha betydning for " +
                            "stønaden du får fra oss. Du må melde fra hvis",
                    Nynorsk to "Vi minner om at du pliktar å melde frå om endringar som kan ha betydning for " +
                            "stønaden du får frå oss. Du må melde frå dersom",
                    English to "TODO",
                )

                list {
                    item {
                        text(
                            Bokmal to "arbeidsinntekten din endrer seg",
                            Nynorsk to "arbeidsinntekta di endrar seg",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidssituasjonen din endrer seg",
                            Nynorsk to "arbeidssituasjonen din endrar seg",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får innvilget andre stønader fra NAV",
                            Nynorsk to "du får innvilga andre stønader frå NAV",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du ikke lenger er arbeidssøker",
                            Nynorsk to "du er ikkje lenger arbeidssøkjar",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du endrer, avbryter eller reduserer omfanget av utdanningen din",
                            Nynorsk to "du endrar, avbryt eller reduserer omfanget av utdanninga di",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du gifter deg",
                            Nynorsk to "du giftar deg",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du blir samboer med en du har felles barn med eller tidligere har vært gift med",
                            Nynorsk to "du blir sambuar med nokon du har felles barn med eller tidlegare har vore gift med",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får felles barn med ny samboer",
                            Nynorsk to "du får barn med ny sambuar",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller skal flytte til et annet land",
                            Nynorsk to "du skal opphalde deg utanfor Noreg i meir enn seks månader, eller du skal flytte til eit anna land",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får opphold i institusjon i minst tre måneder, eller fengsel i minst to måneder",
                            Nynorsk to "du skal vere på ein institusjon i minst tre månader, eller i fengsel i minst to månader",
                            English to "TODO",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde fra om endringer på følgende måter:",
                    Nynorsk to "Du kan melde frå om endringar på følgjande måtar:",
                    English to "TODO",
                )

                list {
                    item {
                        text(
                            Bokmal to "sende en melding på ${Constants.SKRIVTILOSS_URL} (her får du ikke lagt ved dokumentasjon)",
                            Nynorsk to "Send ei melding på ${Constants.SKRIVTILOSS_URL} (her får du ikkje lagt ved dokumentasjon).",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ettersende dokumentasjon fra søknad om omstillingsstønad. " +
                                    "Dette gjør du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                            Nynorsk to "Ettersend dokumentasjon frå søknad om omstillingsstønad. " +
                                    "Dette gjer du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}.",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "sende brev til ${Constants.POSTADRESSE}",
                            Nynorsk to "Send brev til ${Constants.POSTADRESSE}",
                            English to "TODO",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke melder fra om endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
                    Nynorsk to "Dersom du får utbetalt for mykje stønad fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake det du ikkje hadde rett på.",
                    English to "TODO",
                )
            }

            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
