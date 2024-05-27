package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTOSelectors.aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTOSelectors.nasjonalEllerUtland
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
                English to "Information about your adjustment allowance",
            )
        }

        outline {
            showIf(utbetaling){
                paragraph {
                    text(
                        Bokmal to "Du har omstillingsstønad. ",
                        Nynorsk to "Du har omstillingsstønad. ",
                        English to "You are receiving an adjustment allowance. ",
                    )
                    text(
                        Bokmal to "LEGG TIL HVIS STØNADEN ER REDUSERT FOR INNTEKT: Omstillingsstønaden din er redusert etter en arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner per år. ",
                        Nynorsk to "LEGG TIL HVIS STØNADEN ER REDUSERT FOR INNTEKT: Omstillingsstønaden din har blitt redusert ut frå ei arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner per år. ",
                        English to "LEGG TIL HVIS STØNADEN ER REDUSERT FOR INNTEKT: Your adjustment allowance is reduced based on your income from employment of NOK <FORVENTET INNTEKT TOTALT, AVRUNDET> per year. ",
                    )
                    text(
                        Bokmal to "LEGG TIL HVIS STØNADEN IKKE ER REDUSERT FOR INNTEKT: Omstillingsstønaden din er i dag ikke redusert etter arbeidsinntekt eller annen inntekt som er likestilt med arbeidsinntekt.",
                        Nynorsk to "LEGG TIL HVIS STØNADEN IKKE ER REDUSERT FOR INNTEKT: Omstillingsstønaden din er i dag ikkje redusert ut frå arbeidsinntekt eller anna inntekt som er likestilt med arbeidsinntekt.",
                        English to "LEGG TIL HVIS STØNADEN IKKE ER REDUSERT FOR INNTEKT: Your current adjustment allowance is not reduced based on income from employment or other income that is equivalent to income from employment.",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Du er innvilget omstillingsstønad. Denne er i dag redusert etter en forventet " +
                                "inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner. Du får derfor ikke utbetalt omstillingsstønad.",
                        Nynorsk to "Du er innvilga omstillingsstønad. Denne har i dag blitt redusert ut frå ei " +
                                "forventa inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner. Du får difor ikkje utbetalt omstillingsstønad.",
                        English to "You are granted adjustment allowance. Your adjustment allowance is reduced based " +
                                "on your income from employment of NOK <FORVENTET INNTEKT TOTALT, AVRUNDET> per year. " +
                                "You will therefore not receive any adjustment allowance.",
                    )
                }
            }

            title2 {
                text(
                    Bokmal to "Krav til aktivitet ved mottak av omstillingsstønad",
                    Nynorsk to "Krav til aktivitet ved mottak av omstillingsstønad",
                    English to "Activity requirement upon receiving an adjustment allowance",
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
                    English to "Once six months have passed after the death, receiving an adjustment allowance is " +
                            "contingent upon working at least 50 percent work or being involved in another activity with " +
                            "the aim of finding employment. Once 12 months have passed since the death, 100 percent activity may be required.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du opplyste FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. i søknaden at du er i " +
                            "40 prosent arbeid/ikke er i arbeid.",
                    Nynorsk to "Du opplyste FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. i søknaden at du er i " +
                            "40 prosent arbeid/ikke er i arbeid.",
                    English to "You stated that FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. i søknaden at du er i " +
                            "40 prosent arbeid/ikke er i arbeid.",
                )
            }

            showIf(aktivitetsgrad.isOneOf(Aktivitetsgrad.UNDER_50_PROSENT, Aktivitetsgrad.IKKE_I_AKTIVITET) and nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)) {
                paragraph {
                    text(
                        Bokmal to "For å motta omstillingsstønad videre må du øke aktiviteten din. Se “Hvordan oppfylle aktivitetsplikten?”.  " +
                                "Hvis du ikke gjør noen av de andre aktivitetene som er nevnt, må du dokumentere at du er arbeidssøker " +
                                "via din lokale arbeidsformidling, eller på annen måte sannsynliggjøre at du er arbeidssøker.",
                        Nynorsk to "For å kunne få omstillingsstønad vidare må du auke aktiviteten din. Sjå «Korleis oppfyller du aktivitetsplikta?». " +
                                "Dersom du ikkje gjer nokon av dei andre aktivitetane som er nemnde, må du dokumentere via den lokale " +
                                "arbeidsformidlinga at du er arbeidssøkjar, eller på anna vis sannsynleggjere at du er arbeidssøkjar.",
                        English to "To receive an adjustment allowance in the future, you must increase your level of activity. " +
                                "See “How do I comply with the activity obligation?”.  If you are not doing any of the other activities mentioned, " +
                                "you must document your status as a job seeker through your local job centre, or otherwise document that you are a job seeker.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Er det en grunn til at du ikke kan være reell arbeidssøker eller annet som oppfyller " +
                                "aktivitetsplikten på minst 50 prosent, må du sende oss dokumentasjon på dette snarest mulig og " +
                                "senest innen <DATO 5 MND. ETTER DØDSFALL>. Se “Unntak for aktivitetsplikten” under.",
                        Nynorsk to "Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere " +
                                "anna som oppfyller aktivitetsplikta på minst 50 prosent, må du sende oss dokumentasjon på " +
                                "dette snarast mogleg og seinast innan <DATO 5 MND. ETTER DØDSFALL>. Sjå «Unntak frå aktivitetsplikta» under.",
                        English to "If there is any reason why you are unable to be a genuine job seeker or do something else " +
                                "that fulfils the activity obligation of at least 50 percent, you must send us documentation of this " +
                                "as soon as possible and no later than <DATO 5 MND. ETTER DØDSFALL>. " +
                                "See “Exemption from the activity obligation” below.",
                    )
                }

            }

            showIf(aktivitetsgrad.isOneOf(Aktivitetsgrad.UNDER_50_PROSENT, Aktivitetsgrad.IKKE_I_AKTIVITET) and nasjonalEllerUtland.equalTo(NasjonalEllerUtland.NASJONAL)) {
                paragraph {
                    text(
                        Bokmal to "For å motta omstillingsstønad videre må du øke aktiviteten din. Se “Hvordan oppfylle aktivitetsplikten?”. " +
                                "Hvis du ikke foretar deg noen av de andre aktivitetene som er nevnt, må du melde deg som reell arbeidssøker hos NAV. " +
                                "Dette innebærer at du sender meldekort, er aktiv med å søke jobber, samt deltar på de kurs som NAV tilbyr.",
                        Nynorsk to "For å kunne få omstillingsstønad vidare må du auke aktiviteten din. Sjå «Korleis oppfyller du aktivitetsplikta?». " +
                                "Dersom du ikkje gjer nokon av dei andre aktivitetane som er nemnde, må du melde deg som reell arbeidssøkjar hos NAV. " +
                                "Dette inneber at du sender meldekort, er aktiv med å søkje jobbar, og deltek på kursa som NAV tilbyr.",
                        English to "To receive an adjustment allowance in the future, you must increase your level of activity. " +
                                "See “How do I comply with the activity obligation?”.  If you do not undertake any of the other activities mentioned, " +
                                "you must register as a genuine job seeker with NAV. This means that you must send in the Employment Status Form, " +
                                "actively be looking for work, and participate in the courses offered by NAV.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Hvis du ikke kan registrere deg elektronisk må du møte opp på lokalkontoret ditt for å registrere deg som reell arbeidssøker.",
                        Nynorsk to "Dersom du ikkje kan registrere deg elektronisk, møter du opp på lokalkontoret ditt for å registrere deg som reell arbeidssøkjar.",
                        English to "If you are unable to register electronically, you must register as a genuine job seeker in person at your local NAV office.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Er det en grunn til at du ikke kan være reell arbeidssøker eller gjøre annet som " +
                                "oppfyller aktivitetsplikten på minst 50 prosent, må du sende oss dokumentasjon på dette " +
                                "snarest mulig og senest innen <DATO 5 MND. ETTER DØDSFALL>. Se “Unntak for aktivitetsplikten” under.",
                        Nynorsk to "Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere anna " +
                                "som oppfyller aktivitetsplikta på minst 50 prosent, må du sende oss dokumentasjon på dette " +
                                "snarast mogleg og seinast innan <DATO 5 MND. ETTER DØDSFALL>. Sjå «Unntak frå aktivitetsplikta» under.",
                        English to "If there is any reason why you are unable to be a genuine job seeker or do something " +
                                "else that fulfils the activity obligation of at least 50 percent, you must send us documentation " +
                                "of this as soon as possible and no later than <DATO 5 MND. ETTER DØDSFALL>. See “Exemption from the activity obligation” below.",
                    )
                }
            }

            showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.OVER_50_PROSENT) or not(utbetaling)) {
                paragraph {
                    text(
                        Bokmal to "Hvis situasjonen din er endret, må du gi oss informasjon om din nye situasjon " +
                                "snarest mulig og senest innen <DATO 5 MND. ETTER DØDSFALL>. Les mer om hvordan du kan " +
                                "fylle aktivitetsplikten og om unntak fra aktivitetsplikten lenger ned i brevet.",
                        Nynorsk to "Dersom situasjonen din har endra seg, må du gi oss informasjon om den nye " +
                                "situasjonen snarast mogleg og seinast innan <DATO 5 MND. ETTER DØDSFALL>. Lenger ned " +
                                "i brevet kan du lese meir om korleis du kan oppfylle aktivitetsplikta, og kva som er " +
                                "unntaka frå aktivitetsplikta.",
                        English to "If your situation has changed, you must provide us with information about your " +
                                "new situation as soon as possible and at the latest by <DATO 5 MND. ETTER DØDSFALL>. " +
                                "Read more about how you can comply with the activity obligation and exemption from " +
                                "the activity obligation farther down in this letter.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Du finner informasjon om hvordan du melder fra under “Du må melde fra om endringer”.",
                    Nynorsk to "Under «Du må melde frå om endringar» finn du meir informasjon om korleis du melder frå.",
                    English to "You will find more information about how to notify us in the section “You must notify us about any changes”.",
                )
            }

            title2 {
                text(
                    Bokmal to "Omstillingsstønaden skal reduseres etter inntekten din",
                    Nynorsk to "Omstillingsstønaden skal reduserast ut frå inntekta di",
                    English to "The adjustment allowance is reduced according to your income",
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
                    English to "The allowance will be reduced by 45 percent of your income from employment or other " +
                            "income that is equivalent to income from employment, if this is more than half of the basic " +
                            "amount in the national insurance (G). The allowance will be reduced based on what you declare " +
                            "as anticipated income for the current year.",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "Meld frå dersom inntekta di endrar seg",
                    English to "You must report any changes to your income",
                )
            }

            showIf(redusertEtterInntekt){
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis inntekten din endrer seg.",
                        Nynorsk to "For at du skal få rett utbetaling, er det viktig at du gir oss beskjed viss inntekta di endrar seg.",
                        English to "To receive the correct amount, you are obligated to inform us about any changes to your income.",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis " +
                                "du får en forventet årsinntekt som vil overstige et halvt grunnbeløp. Dette er per i dag 59 310 kroner. " +
                                "Grunnbeløpet blir justert hvert år fra 1. mai.",
                        Nynorsk to "For at du få rett utbetaling, er det viktig at du gir oss beskjed viss du får ei " +
                                "forventa årsinntekt som vil overstige eit halvt grunnbeløp. Dette er per i dag 59 310 kroner. " +
                                "Grunnbeløpet blir justert kvart år frå 1. mai.",
                        English to "To receive the correct amount, you are obligated to inform us about any changes to " +
                                "your anticipated annual income that exceeds one half of the basic amount. This is currently " +
                                "NOK 59 310. The basic amount is adjusted on 1 May each year.",
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
                    English to "If your income has changed, you must state the new anticipated gross income for the " +
                            "current year. If the change in income applies from before you were granted the allowance, " +
                            "you must also state your income from January up to and including the month before you were " +
                            "granted the allowance.",
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
                    English to "We will then adjust your allowance starting in the month after you inform us of the change. " +
                            "It is therefore important to notify us as soon as possible to receive the correct amount of adjustment " +
                            "allowance, so that any final settlement will be as correct as possible. You can find more information " +
                            "about final settlements online: ${Constants.OMS_ETTEROPPGJOER_URL}.",
                )
            }

            title2 {
                text(
                    Bokmal to "Hvordan oppfylle aktivitetsplikten?",
                    Nynorsk to "Korleis oppfyller du aktivitetsplikta?",
                    English to "How do I comply with the activity obligation?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du fyller aktivitetsplikten hvis du er minst 50 prosent aktiv ved å",
                    Nynorsk to "Du oppfyller aktivitetsplikta dersom du er minst 50 prosent aktiv ved å",
                    English to "You are in compliance with the activity obligation if you are at least 50 percent active",
                )
                list {
                    item {
                        text(
                            Bokmal to "jobbe",
                            Nynorsk to "jobbe",
                            English to "working",
                        )
                    }
                    item {
                        text(
                            Bokmal to "være selvstendig næringsdrivende",
                            Nynorsk to "vere sjølvstendig næringsdrivande",
                            English to "being self-employed/sole proprietor",
                        )
                    }
                    item {
                        text(
                            Bokmal to "etablere egen virksomhet (må godkjennes av NAV)",
                            Nynorsk to "etablere eiga verksemd (må godkjennast av NAV)",
                            English to "setting up your own business (must be approved by NAV)",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ta utdanning som er nødvendig og hensiktsmessig (må godkjennes av NAV)",
                            Nynorsk to "ta utdanning som er nødvendig og føremålstenleg (må godkjennast av NAV)",
                            English to "taking a necessary and suitable education (must be approved by NAV)",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "være reell arbeidssøker".expr() + ifElse(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)," i bostedslandet ditt", ""),
                            Nynorsk to "vere reell arbeidssøkjar".expr() + ifElse(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)," i landet der du er busett", ""),
                            English to "being a genuine job seeker".expr() + ifElse(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)," in your country of residence", ""),
                        )
                    }
                    item {
                        text(
                            Bokmal to "ha fått tilbud om jobb",
                            Nynorsk to "ha fått tilbod om jobb",
                            English to "have received a job offer",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Unntak fra aktivitetsplikten",
                    Nynorsk to "Unntak frå aktivitetsplikta",
                    English to "Exemption from the activity obligation",
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er mulig å få unntak fra aktivitetsplikten, men fortsatt ha rett til omstillingsstønad. Dette gjelder hvis du",
                    Nynorsk to "Det er mogleg å få unntak frå aktivitetsplikta, og framleis ha rett på omstillingsstønad. Dette gjeld dersom du",
                    English to "It is possible to be exempt from the activity obligation and still have the right to an adjustment allowance. This applies if you",
                )
                list {
                    item {
                        text(
                            Bokmal to "har omsorgen for barn under ett år",
                            Nynorsk to "har ansvar for barn under eitt år",
                            English to "are caring for child under one year of age",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har sykdom eller helseutfordringer som hindrer deg fra å være i minst 50 " +
                                    "prosent arbeid eller arbeidsrettet aktivitet, og du benytter restarbeidsevnen din",
                            Nynorsk to "har sjukdom eller helseutfordringar som hindrar deg i å vere i minst 50 " +
                                    "prosent arbeid eller arbeidsretta aktivitet, og du nyttar restarbeidsevna di",
                            English to "have an illness or health problems that hinder you from working at least 50 " +
                                    "percent or being involved in a work-related activity, and you use your residual ability to work",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar sykepenger for full arbeidsuførhet",
                            Nynorsk to "får sjukepengar for full arbeidsuførleik",
                            English to "are receiving sickness benefits for full disability",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar arbeidsavklaringspenger",
                            Nynorsk to "får arbeidsavklaringspengar",
                            English to "are receiving Work Assessment Allowance (AAP)",
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
                            English to "are prevented from working or work-related activity due to illness, injury or " +
                                    "reduced functionality of your child. The child's condition must be documented by a medical doctor. " +
                                    "You must also document that the child's condition is the reason why you are being prevented " +
                                    "from working or being involved in a work-related activity",
                        )
                    }
                    item {
                        text(
                            Bokmal to "er forhindret fra å være i slik aktivitet på grunn av omsorg for barn som " +
                                    "mangler tilfredsstillende tilsynsordning, og den manglende tilsynsordningen ikke skyldes deg selv",
                            Nynorsk to "er hindra i å vere i ein slik aktivitet grunna omsorg for barn som manglar " +
                                    "tilfredsstillande tilsynsordning, og du ikkje sjølv er skuld i den manglande tilsynsordninga",
                            English to "are prevented from engaging in such activity due to caring for a child who " +
                                    "lacks satisfactory supervision, and the lack of supervision is not your fault",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har kortvarig fravær fra aktivitet på grunn av sykdom eller skade hos deg " +
                                    "eller barn du har omsorg for",
                            Nynorsk to "har kortvarig fråvær frå aktivitet fordi anten du sjølv eller eit barn du " +
                                    "har omsorg for, har sjukdom eller skade",
                            English to "are absent from the activity temporarily due to illness or injury, either " +
                                    "yours or the child you are caring for",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har en arbeidsevnevurdering fra ditt lokale NAV-kontor som sier at du ikke kan arbeide",
                            Nynorsk to "har ei arbeidsevnevurdering frå det lokale NAV-kontoret ditt på at du ikkje kan jobbe",
                            English to "have completed the work capability assessment from your local NAV office that proves you cannot be employed",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp med å få ny jobb eller jobbe meir?",
                    English to "Do you need help getting a new job or more work?",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. " +
                            "Du kan finne flere jobbsøkertips og informasjon på ${Constants.ARBEID_URL}.",
                    Nynorsk to "NAV tilbyr ulike tenester og støtteordningar for deg som treng hjelp med å kome i " +
                            "arbeid. Du finn fleire jobbsøkjartips og informasjon på ${Constants.ARBEID_URL}.",
                    English to "The Norwegian Labour and Welfare Administration (NAV) offers various services and " +
                            "support schemes for those who need help finding a job. You can find more job seeker's tips " +
                            "and information online: ${Constants.ARBEID_URL}.",
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
                    English to "To apply for vacant positions in your area or other place around the country, you " +
                            "can see this webpage: arbeidsplassen.no. You can also set up e-mail notifications to receive " +
                            "information and offers about jobs that are suited to your situation. There are also many " +
                            "good tips on looking for work here: ${Constants.FINN_JOBBENE_URL}.",
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
                    English to "If you need help joining the work force or want to increase your work hours, you " +
                            "can get guidance and support from us. We can tell you about various opportunities in the " +
                            "labour market or talk to you about necessary education or other measures that can help you find work.",
                )
            }

            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                    English to "Do you have health problems?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser " +
                            "eller støtteordninger ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar " +
                            "eller støtteordningar ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    English to "If you are faced with difficult health challenges, you can investigate the possibilities " +
                            "for other benefits or support schemes through your local NAV office, or check out various " +
                            "opportunities online: ${Constants.HELSE_URL}.",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report any changes",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi minner om plikten din til å melde fra om endringer som kan ha betydning for " +
                            "stønaden du får fra oss. Du må melde fra hvis",
                    Nynorsk to "Vi minner om at du pliktar å melde frå om endringar som kan ha betydning for " +
                            "stønaden du får frå oss. Du må melde frå dersom",
                    English to "Please remember that you are obligated to notify us about changes that may have significance " +
                            "to the benefits or allowances you receive from NAV. You must report any changes if",
                )

                list {
                    item {
                        text(
                            Bokmal to "arbeidsinntekten din endrer seg",
                            Nynorsk to "arbeidsinntekta di endrar seg",
                            English to "your income from work changes",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidssituasjonen din endrer seg",
                            Nynorsk to "arbeidssituasjonen din endrar seg",
                            English to "your working situation changes",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får innvilget andre stønader fra NAV",
                            Nynorsk to "du får innvilga andre stønader frå NAV",
                            English to "you are granted other benefits from NAV",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du ikke lenger er arbeidssøker",
                            Nynorsk to "du er ikkje lenger arbeidssøkjar",
                            English to "you are no longer a job seeker",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du endrer, avbryter eller reduserer omfanget av utdanningen din",
                            Nynorsk to "du endrar, avbryt eller reduserer omfanget av utdanninga di",
                            English to "you change, discontinue or reduce the scope of your education/training",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du gifter deg",
                            Nynorsk to "du giftar deg",
                            English to "you get married",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du blir samboer med en du har felles barn med eller tidligere har vært gift med",
                            Nynorsk to "du blir sambuar med nokon du har felles barn med eller tidlegare har vore gift med",
                            English to "you become a cohabiting partner with someone you have common children with or were previously married to",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får felles barn med ny samboer",
                            Nynorsk to "du får barn med ny sambuar",
                            English to "you are expecting a child with a new cohabiting partner",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller skal flytte til et annet land",
                            Nynorsk to "du skal opphalde deg utanfor Noreg i meir enn seks månader, eller du skal flytte til eit anna land",
                            English to "you intend to stay outside of Norway for a period of more than six months or intend to move to another country",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får opphold i institusjon i minst tre måneder, eller fengsel i minst to måneder",
                            Nynorsk to "du skal vere på ein institusjon i minst tre månader, eller i fengsel i minst to månader",
                            English to "you are placed in an institution for at least three months, or jail for at least two months",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde fra om endringer på følgende måter:",
                    Nynorsk to "Du kan melde frå om endringar på følgjande måtar:",
                    English to "You can report changes in the following ways:",
                )

                list {
                    item {
                        text(
                            Bokmal to "sende en melding på ${Constants.SKRIVTILOSS_URL} (her får du ikke lagt ved dokumentasjon)",
                            Nynorsk to "Send ei melding på ${Constants.SKRIVTILOSS_URL} (her får du ikkje lagt ved dokumentasjon).",
                            English to "send us a message online: ${Constants.SKRIVTILOSS_URL} (you cannot attach documentation from this page)",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ettersende dokumentasjon fra søknad om omstillingsstønad. " +
                                    "Dette gjør du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                            Nynorsk to "Ettersend dokumentasjon frå søknad om omstillingsstønad. " +
                                    "Dette gjer du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}.",
                            English to "send more documentation later from your adjustment allowance application. " +
                                    "Do this online here: ${Constants.ETTERSENDE_OMS_URL}.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "sende brev til ${Constants.POSTADRESSE}",
                            Nynorsk to "Send brev til ${Constants.POSTADRESSE}",
                            English to "send a letter to ${Constants.POSTADRESSE}",
                        )
                    }
                }
            }

            showIf(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)){
                paragraph {
                    text(
                        Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside ${Constants.NAV_URL}, må du sende dokumentasjonen i posten.",
                        Nynorsk to "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår ${Constants.NAV_URL}, må du sende dokumentasjon per post.",
                        English to "Please send documentation as normal post if you do not use BankID or another login option.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke melder fra om endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
                    Nynorsk to "Dersom du får utbetalt for mykje stønad fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake det du ikkje hadde rett på.",
                    English to "If you fail to report changes and/or are paid too much benefits/allowance, NAV has the right to collect the incorrect amount.",
                )
            }

            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
