package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
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

data class OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
    val aktivitetsgrad: Aktivitetsgrad,
)

enum class Aktivitetsgrad { IKKE_I_AKTIVITET, UNDER_50_PROSENT, OVER_50_PROSENT };

object AktivitetsgradFormatter : LocalizedFormatter<Aktivitetsgrad>(), StableHash by StableHash.of("AktivitetsgradFormatter") {
    override fun apply(aktivitetsgrad: Aktivitetsgrad, spraak: Language): String {
        return aktivitetsgrad.name
    }
}


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
                Nynorsk to "TODO",
                English to "TODO",
            )
        }

        outline {
            paragraph {
                text(
                    Bokmal to "Bokmål",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "Bokmål ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                    Nynorsk to "Nynorsk ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                    English to "Engelsk ".expr() + aktivitetsgrad.format(AktivitetsgradFormatter),
                )
            }

            paragraph {
                text(
                    Bokmal to "Du finner informasjon om hvordan du melder fra under “Du må melde fra om endringer”.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Omstillingsstønaden skal reduseres etter inntekten din",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Stønaden skal reduseres med 45 prosent av arbeidsinntekten din og inntekt som er " +
                            "likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygden (G). " +
                            "Stønaden blir redusert ut fra det du oppgir som forventet inntekt for gjeldende år.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            // REDUSERT INNTEKT
            paragraph {
                text(
                    Bokmal to "HVIS STØNADEN ER REDUSERT ETTER INNTEKT",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
                newline()
                text(
                    Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis inntekten din endrer seg.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            // IKKE REDUSERT INNTEKT
            paragraph {
                text(
                    Bokmal to "HVIS STØNADEN IKKE ER REDUSERT ETTER INNTEKT",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
                newline()
                text(
                    Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis du får en forventet årsinntekt som vil overstige et halvt grunnbeløp. Dette er per i dag 59 310 kroner. Grunnbeløpet blir justert hvert år fra 1. mai.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis inntekten din har endret seg, må du oppgi ny forventet brutto inntekt for " +
                            "inneværende år. Gjelder inntektsendringen fra før du ble innvilget stønaden, skal du også " +
                            "oppgi inntekt fra januar til og med måneden før du fikk innvilget stønaden.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi vil justere stønaden fra måneden etter du gir beskjed. Meld derfor fra snarest " +
                            "mulig for å få mest mulig riktig utbetalt omstillingsstønad, slik at etteroppgjøret blir " +
                            "så riktig som mulig. Du kan finne mer informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Hvordan oppfylle aktivitetsplikten?",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du fyller aktivitetsplikten hvis du er minst 50 prosent aktiv ved å",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
                list {
                    item {
                        text(
                            Bokmal to "jobbe",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "være selvstendig næringsdrivende",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "etablere egen virksomhet (må godkjennes av NAV)",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ta utdanning som er nødvendig og hensiktsmessig (må godkjennes av NAV)",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "være reell arbeidssøker",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ha fått tilbud om jobb",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Unntak fra aktivitetsplikten",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er mulig å få unntak fra aktivitetsplikten, men fortsatt ha rett til omstillingsstønad. Dette gjelder hvis du",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
                list {
                    item {
                        text(
                            Bokmal to "har omsorgen for barn under ett år",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har sykdom eller helseutfordringer som hindrer deg fra å være i minst 50 " +
                                    "prosent arbeid eller arbeidsrettet aktivitet, og du benytter restarbeidsevnen din",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar sykepenger for full arbeidsuførhet",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "mottar arbeidsavklaringspenger",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "er forhindret fra å være i arbeid eller arbeidsrettet aktivitet på grunn av " +
                                    "sykdom, skade eller funksjonsnedsettelse hos barnet ditt. Barnets tilstand må " +
                                    "dokumenteres av lege. Det må også dokumenteres at barnets tilstand er årsaken til " +
                                    "at du er forhindret fra å være i arbeid eller arbeidsrettet aktivitet",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "er forhindret fra å være i slik aktivitet på grunn av omsorg for barn som " +
                                    "mangler tilfredsstillende tilsynsordning, og den manglende tilsynsordningen ikke skyldes deg selv",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har kortvarig fravær fra aktivitet på grunn av sykdom eller skade hos deg " +
                                    "eller barn du har omsorg for",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "har en arbeidsevnevurdering fra ditt lokale NAV-kontor som sier at du ikke kan arbeide",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                }
            }

            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. " +
                            "Du kan finne flere jobbsøkertips og informasjon på ${Constants.ARBEID_URL}.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "På nettsiden arbeidsplassen.no kan du søke etter ledige stillinger i ditt område " +
                            "eller andre steder i landet. Her kan du også opprette varsler på e-post for å få " +
                            "meldinger om jobber som passer deg. Du finner også mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Trenger du hjelp til å komme i jobb eller ønsker å øke arbeidsinnsatsen din, " +
                            "kan du få veiledning og støtte fra oss. Vi kan fortelle deg om ulike muligheter i " +
                            "arbeidsmarkedet eller snakke med deg om nødvendig utdanning eller andre tiltak som " +
                            "kan hjelpe deg med å komme i arbeid.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser " +
                            "eller støtteordninger ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi minner om plikten din til å melde fra om endringer som kan ha betydning for " +
                            "stønaden du får fra oss. Du må melde fra hvis",
                    Nynorsk to "TODO",
                    English to "TODO",
                )

                list {
                    item {
                        text(
                            Bokmal to "arbeidsinntekten din endrer seg",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidssituasjonen din endrer seg",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får innvilget andre stønader fra NAV",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du ikke lenger er arbeidssøker",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du endrer, avbryter eller reduserer omfanget av utdanningen din",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du gifter deg",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du blir samboer med en du har felles barn med eller tidligere har vært gift med",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får felles barn med ny samboer",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller skal flytte til et annet land",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får opphold i institusjon i minst tre måneder, eller fengsel i minst to måneder",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde fra om endringer på følgende måter:",
                    Nynorsk to "TODO",
                    English to "TODO",
                )

                list {
                    item {
                        text(
                            Bokmal to "sende en melding på ${Constants.SKRIVTILOSS_URL} (her får du ikke lagt ved dokumentasjon)",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ettersende dokumentasjon fra søknad om omstillingsstønad. " +
                                    "Dette gjør du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                    item {
                        text(
                            Bokmal to "sende brev til ${Constants.POSTADRESSE}",
                            Nynorsk to "TODO",
                            English to "TODO",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke melder fra om endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
                    Nynorsk to "TODO",
                    English to "TODO",
                )
            }

            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
