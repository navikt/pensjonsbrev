package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTOSelectors.er12MndVarsel

data class OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO(
    val er12MndVarsel: Boolean
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfall :
    EtterlatteTemplate<OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VARSEL_AKTIVITETSPLIKT_UTFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadVarselAktivitetspliktRedigerbartUtfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadataEtterlatte(
                    displayTitle = "Varselbrev - stans om ikke akt.plikt oppfylt",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }
            outline {
                showIf(er12MndVarsel) {
                    paragraph {
                        text(
                            Bokmal to
                                    "Vi viser til informasjonsbrev av <dato infobrev er sendt>.",
                            Nynorsk to
                                    "Vi viser til informasjonsbrevet av <dato infobrev er sendt>.",
                            English to
                                    "We refer to the information letter, dated <dato infobrev er sendt>."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                    "Når det er gått seks måneder etter dødsfallet, er det et krav for å motta omstillingsstønad at du er i minst 50 " +
                                    "prosent arbeid eller annen aktivitet med sikte på å komme i arbeid. Når det har gått 12 måneder siden dødsfallet, " +
                                    "kan det stilles krav om 100 prosent aktivitet. Som det fremgår av informasjonsbrevet du har mottatt er det noen unntak " +
                                    "som gjør at du likevel kan motta omstillingsstønad videre. ",
                            Nynorsk to
                                    "For å kunne halde fram med å få omstillingsstønad når det har gått seks månader sidan dødsfallet, må du vere i minst 50 " +
                                    "prosent arbeid eller annan aktivitet med sikte på å kome i arbeid. Når det er gått 12 månader sidan dødsfallet" +
                                    " kan det krevjast 100 prosent aktivitet. Som det kjem fram av informasjonsbrevet du har fått er det nokre unntak" +
                                    " som gjer at du likevel kan få omstillingsstønad vidare.",
                            English to
                                    "By six months after the death, adjustment allowance recipients are required to work or participate in other activity aimed at finding work." +
                                    " This activity requirement is equivalent to at least 50 percent of full-time work or activity. When 12 months have passed since the death, " +
                                    "a requirement for 100 percent activity may be imposed. As can be seen from the information letter you have received, " +
                                    "there are some exceptions which mean that you can still continue to receive adjustment allowance.",
                        )
                    }

                }
                // Hvis 6 mnd varsel
                .orShow {
                    paragraph {
                        text(
                            Bokmal to
                                    "Vi viser til vedtak om innvilgelse av omstillingsstønad av <dato vedtaksbrev> " +
                                    "og informasjonsbrev av <dato infobrev er sendt>.",
                            Nynorsk to
                                    "Vi viser til vedtaket om innvilging av omstillingsstønad av <dato vedtaksbrev> " +
                                    "og informasjonsbrevet av <dato infobrev er sendt>.",
                            English to
                                    "We refer to the decision to grant adjustment allowance, dated <dato vedtaksbrev>, " +
                                    "and the information letter, dated <dato infobrev er sendt>.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                    "Når det er gått seks måneder etter dødsfallet, er det et krav for å motta omstillingsstønad " +
                                    "at du er i minst 50 prosent arbeid eller annen aktivitet med sikte på å komme i arbeid. " +
                                    "Som det fremgår av informasjonsbrevet du har mottatt er det noen unntak som gjør at du likevel kan motta omstillingsstønad videre.",
                            Nynorsk to
                                    "For å kunne halde fram med å få omstillingsstønad når det har gått seks månader sidan " +
                                    "dødsfallet, må du vere i minst 50 prosent arbeid eller annan aktivitet med sikte på å kome " +
                                    "i arbeid. Som det kjem fram av informasjonsbrevet du har fått er det nokre unntak som gjer at du likevel kan få omstillingsstønad vidare.",
                            English to
                                    "By six months after the death, adjustment allowance recipients are required to work or " +
                                    "participate in other activity aimed at finding work. This activity requirement is equivalent " +
                                    "to at least 50 percent of full-time work or activity. As can be seen from the information letter you have received, there are some exceptions which mean that you can still continue to receive adjustment allowance.",
                        )
                    }
                }

                paragraph {
                    text(
                        Bokmal to
                                "Du må gi oss de opplysninger og levere nødvendig dokumentasjon slik at vi kan " +
                                "vurdere om du fortsatt har rett til omstillingsstønad.",
                        Nynorsk to
                                "Det er ditt ansvar å gi oss opplysningane og dokumentasjonen vi treng for å vurdere om " +
                                "du har rett på omstillingsstønad vidare.",
                        English to
                                "You must provide the information and documentation we need to assess whether you " +
                                "still qualify for adjustment allowance.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dette følger av folketrygdloven §§ 17-7 og 21-3.",
                        Nynorsk to "Dette følgjer av folketrygdlova §§ 17-7 og 21-3.",
                        English to "This follows from Sections 17-7 and 21-3 of the National Insurance Act.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Omstillingsstønaden stanses ved manglende aktivitet",
                        Nynorsk to "Omstillingsstønaden blir stansa ved manglande aktivitet",
                        English to "The adjustment allowance will be terminated if the activity requirement is not met",
                    )
                }

                showIf(er12MndVarsel) {
                    paragraph {
                        text(
                            Bokmal to
                                    "Vi har ikke fått opplysninger som tilsier at du er i opp mot 100 prosent aktivitet eller " +
                                    "at du har grunn til å være unntatt fra aktivitetsplikten. Du har tidligere opplyst at <SETT INN FORKLARING>.",
                            Nynorsk to
                                    "Vi har ikkje fått opplysningar som tilseier at du er i opp mot 100 prosent aktivitet, " +
                                    "eller at du har rett på fritak frå aktivitetsplikta. Du har tidlegare opplyst at <SETT INN FORKLARING>.",
                            English to
                                    "We have not received any information to indicate that you are participating in work or an activity " +
                                    "at a level equivalent up to 100 percent, nor have we received any information to indicate that you " +
                                    "are exempt from the activity requirement. You have previously stated that <SETT INN FORKLARING>.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                    "Hvis du ikke dokumenterer opp mot 100 prosent aktivitet eller grunn til unntak fra aktivitet " +
                                    "som nevnt i informasjonsbrevet innen tre uker fra datoen på dette brevet, vil utbetaling av " +
                                    "omstillingsstønad stanse fra <DATO>.",
                            Nynorsk to
                                    "Viss du ikkje dokumenterer opp mot 100 prosent aktivitet eller grunn til unntak frå aktivitet" +
                                    " som nemnt i informasjonsbrevet innan tre veker frå datoen på dette brevet, vil utbetalinga av" +
                                    " omstillingsstønad stanse frå <DATO>.",
                            English to "If you do not submit documentation of participating in an activity equivalent to 100 percent," +
                                    " or reasons for being exempt from the activity requirement as mentioned in the information letter, " +
                                    "within three weeks from the date of this letter, the adjustment allowance payments will be terminated from <DATO>."
                        )
                    }

                }
                // Hvis 6 mnd varsel
                .orShow {
                    paragraph {
                        text(
                            Bokmal to
                                    "Vi har ikke fått opplysninger som tilsier at du er i minst 50 prosent aktivitet eller " +
                                    "at du har grunn til å være unntatt fra aktivitetsplikten. I søknaden din om omstillingsstønad opplyste du at <sett inn forklaring>.",
                            Nynorsk to
                                    "Vi har ikkje fått opplysningar som tilseier at du er i minst 50 prosent aktivitet, " +
                                    "eller at du har rett på fritak frå aktivitetsplikta. I søknaden din om omstillingsstønad opplyste du at <sett inn forklaring>.",
                            English to
                                    "We have not received any information to indicate that you are participating in work or " +
                                    "an activity at a level equivalent to at least 50 percent of full-time work, nor have we " +
                                    "received any information to indicate that you are exempt from the activity requirement. In your application for adjustment allowance, you stated that <sett inn forklaring>.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                    "Hvis du ikke dokumenterer minst 50 prosent aktivitet eller grunn til unntak fra aktivitet " +
                                    "som nevnt i informasjonsbrevet innen tre uker fra datoen på dette brevet, vil utbetaling av " +
                                    "omstillingsstønad stanse fra <dato>.",
                            Nynorsk to
                                    "Viss du ikkje dokumenterer minst 50 prosent aktivitet eller grunn til unntak frå aktivitet " +
                                    "som nemnt i informasjonsbrevet, innan tre veker frå datoen på dette brevet, vil utbetalinga " +
                                    "av omstillingsstønad stanse frå <dato>.",
                            English to "If you do not submit documentation of participating in an activity equivalent to " +
                                    "50 percent of full time, or reasons for being exempt from the activity requirement as mentioned " +
                                    "in the information letter, within three weeks from the date of this letter, the adjustment allowance " +
                                    "payments will be terminated from <dato>."
                        )
                    }
                }
            }
        }
}
