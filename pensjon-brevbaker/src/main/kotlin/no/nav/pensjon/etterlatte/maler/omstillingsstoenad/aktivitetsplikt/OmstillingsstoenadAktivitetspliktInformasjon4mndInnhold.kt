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
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadAktivitetspliktFraser
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

enum class Aktivitetsgrad { IKKE_I_AKTIVITET, UNDER_50_PROSENT, OVER_50_PROSENT }

enum class NasjonalEllerUtland { NASJONAL, UTLAND }

@TemplateModelHelpers
object OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold :
    EtterlatteTemplate<OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO> {
    override val kode: EtterlatteBrevKode =
        EtterlatteBrevKode.OMSTILLINGSSTOENAD_AKTIVITETSPLIKT_INFORMASJON_4MND_INNHOLD

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
            showIf(utbetaling) {
                paragraph {
                    text(
                        Bokmal to "Du mottar omstillingsstønad. Du får utbetalt <beløp> kroner per måned før skatt.",
                        Nynorsk to "Du har omstillingsstønad. Du får utbetalt <beløp> kroner i stønad kvar månad før skatt.",
                        English to "You are receiving an adjustment allowance. You will receive NOK <amount> each month before tax.",
                    )
                }

                showIf(redusertEtterInntekt) {
                    paragraph {
                        text(
                            Bokmal to "Omstillingsstønaden din er redusert etter en forventet arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner i år. LEGG TIL ETTER KRONER HVIS INNVILGET FRA FEBRUAR-AUGUST: fra <måned> og ut året.",
                            Nynorsk to "Omstillingsstønaden din har blitt redusert ut frå ei forventa arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner i år. LEGG TIL ETTER KRONER HVIS INNVILGET FRA FEBRUAR-AUGUST: frå <månad> og ut året.",
                            English to "Your adjustment allowance is reduced based on your income from employment of NOK <FORVENTET INNTEKT TOTALT, AVRUNDET> this year. LEGG TIL ETTER BELØP HVIS INNVILGET FRA FEBRUAR-AUGUST: from <måned> until the end of this year.",
                        )
                    }
                } orShow {
                    paragraph {
                        text(
                            Bokmal to "Omstillingsstønaden din er i dag ikke redusert etter arbeidsinntekt eller annen inntekt som er likestilt med arbeidsinntekt.",
                            Nynorsk to "Omstillingsstønaden din er i dag ikkje redusert ut frå arbeidsinntekt eller anna inntekt som er likestilt med arbeidsinntekt.",
                            English to "Your current adjustment allowance is not reduced based on income from employment or other income that is equivalent to income from employment.",
                        )
                    }
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

            showIf(
                aktivitetsgrad.isOneOf(
                    Aktivitetsgrad.UNDER_50_PROSENT,
                    Aktivitetsgrad.IKKE_I_AKTIVITET
                ) and nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)
            ) {
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
                                "senest innen tre uker fra datoen på dette brevet. Se “Unntak for aktivitetsplikten” under.",
                        Nynorsk to "Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere " +
                                "anna som oppfyller aktivitetsplikta på minst 50 prosent, må du sende oss dokumentasjon på " +
                                "dette snarast mogleg og seinast innan tre veker frå datoen på dette brevet. Sjå «Unntak frå aktivitetsplikta» under.",
                        English to "If there is any reason why you are unable to be a genuine job seeker or do something else " +
                                "that fulfils the activity obligation of at least 50 percent, you must send us documentation of this " +
                                "as soon as possible and no later than three weeks from the date of this letter. " +
                                "See “Exemption from the activity obligation” below.",
                    )
                }

            }

            showIf(
                aktivitetsgrad.isOneOf(
                    Aktivitetsgrad.UNDER_50_PROSENT,
                    Aktivitetsgrad.IKKE_I_AKTIVITET
                ) and nasjonalEllerUtland.equalTo(NasjonalEllerUtland.NASJONAL)
            ) {
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
                                "snarest mulig og senest innen tre uker fra datoen på dette brevet. Se “Unntak for aktivitetsplikten” under.",
                        Nynorsk to "Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere anna " +
                                "som oppfyller aktivitetsplikta på minst 50 prosent, må du sende oss dokumentasjon på dette " +
                                "snarast mogleg og seinast innan tre veker frå datoen på dette brevet. Sjå «Unntak frå aktivitetsplikta» under.",
                        English to "If there is any reason why you are unable to be a genuine job seeker or do something " +
                                "else that fulfils the activity obligation of at least 50 percent, you must send us documentation " +
                                "of this as soon as possible and no later than three weeks from the date of this letter. See “Exemption from the activity obligation” below.",
                    )
                }
            }

            showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.OVER_50_PROSENT) or not(utbetaling)) {
                paragraph {
                    text(
                        Bokmal to "Hvis situasjonen din er endret, må du gi oss informasjon om din nye situasjon " +
                                "snarest mulig og senest innen tre uker fra datoen på dette brevet. Les mer om hvordan du kan " +
                                "fylle aktivitetsplikten og om unntak fra aktivitetsplikten lenger ned i brevet.",
                        Nynorsk to "Dersom situasjonen din har endra seg, må du gi oss informasjon om den nye " +
                                "situasjonen snarast mogleg og seinast innan tre veker frå datoen på dette brevet. Lenger ned " +
                                "i brevet kan du lese meir om korleis du kan oppfylle aktivitetsplikta, og kva som er " +
                                "unntaka frå aktivitetsplikta.",
                        English to "If your situation has changed, you must provide us with information about your " +
                                "new situation as soon as possible and no later than three weeks from the date of this letter. " +
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
            paragraph {
                text(
                    Bokmal to "Hvis du trenger mer tid for å innhente dokumentasjon, må du kontakte oss snarest mulig " +
                            "og senest innen tre uker fra datoen på dette brevet.",
                    Nynorsk to "Viss du treng meir tid for å innhente dokumentasjon, må du kontakte oss snarast mogleg " +
                            "og seinast innan tre veker frå datoen på dette brevet.",
                    English to "If you need more time to obtain documentation, you must contact us as soon as possible " +
                            "and no later than three weeks from the date of this letter.",
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



            includePhrase(OmstillingsstoenadAktivitetspliktFraser.FellesInfoOmInntektsendring(redusertEtterInntekt))

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
                            Bokmal to "være reell arbeidssøker".expr() + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " i bostedslandet ditt", ""
                            ),
                            Nynorsk to "vere reell arbeidssøkjar".expr() + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " i landet der du er busett", ""
                            ),
                            English to "being a genuine job seeker".expr() + ifElse(
                                nasjonalEllerUtland.equalTo(
                                    NasjonalEllerUtland.UTLAND
                                ), " in your country of residence", ""
                            ),
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
                                    "prosent arbeid eller arbeidsrettet aktivitet, og du benytter din gjenværende arbeidsevne.",
                            Nynorsk to "har sjukdom eller helseutfordringar som hindrar deg i å vere i minst 50 " +
                                    "prosent arbeid eller arbeidsretta aktivitet, og du nyttar di attverande arbeidsevne",
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



            includePhrase(OmstillingsstoenadAktivitetspliktFraser.TrengerDuHjelpTilAaFaaNyJobb)
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.HarDuHelseutfordringer)
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.DuMaaMeldeFraOmEndringer(nasjonalEllerUtland))
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
