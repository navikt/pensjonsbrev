package no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTOSelectors.automatiskBehandla
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

data class ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO(
    val beregning: BarnepensjonBeregning,
    val automatiskBehandla: Boolean,
    val erUnder18Aar: Boolean,
    val erBosattUtlandet: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfall :
    EtterlatteTemplate<ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_FORHAANDSVARSEL_GJENOPPSTAATT_UTFALL
    override val template = createTemplate(
        name = kode.name,
        letterDataType = ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024.",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(automatiskBehandla) {
                includePhrase(Automatisk(erUnder18Aar, erBosattUtlandet))
            }.orShow { includePhrase(Manuelt(erUnder18Aar, erBosattUtlandet)) }

        }
    }

    data class Automatisk(
        val erUnder18Aar: Expression<Boolean>,
        val erBosattUtlandet: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "NAV har vurdert at du har rett til ny barnepensjon fra 1. januar 2024. Vi har lagt ved en beregning av hva du vil få i barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Beregningen er gjort med bakgrunn i de opplysningene vi har om deg. Du er selv ansvarlig for å sjekke om opplysningene er riktige. Meld fra til oss hvis du ser noen feil eller mangler. Hvis du ikke melder fra om endringer og får utbetalt for mye barnepensjon, kan barnepensjon som er utbetalt feil kreves tilbake.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis vi ikke hører fra deg innen fire uker, tar vi utgangspunkt i at beregningene våre er riktige og du får vedtak om innvilget barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du melder fra om endringer, vil vi gjøre endringer og du vil få vedtak gjort etter de nye opplysningene du gir oss.",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Regler for barnepensjon før 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Barnepensjon ble utbetalt til den måneden du fylte 18 år",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Barnepensjon ble utbetalt etter 18 år hvis du var under utdanning eller var lærling/praktikant, og dødsfallet skyldtes yrkesskade/yrkessykdom eller du var foreldreløs",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Barnepensjon ble beregnet samlet når flere barn med barnepensjon ble oppdratt sammen, og beløpet ble utbetalt likt til hvert barn.",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }
            }
            title2 {
                text(
                    Bokmal to "Regler for barnepensjon fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Satsen for barnepensjon er økt",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Barnepensjon utbetales til du fyller 20 år",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Det er ikke lenger et krav om at du er under utdanning, er lærling eller praktikant for å få barnepensjon etter fylte 18 år",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Satsen for barnepensjon er den samme, uavhengig av antall søsken.",
                            Nynorsk to "",
                            English to "",
                        )
                    }
                }
            }
            title2 {
                text(
                    Bokmal to "Du trenger ikke å søke ",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har mottatt barnepensjon tidligere. Du trenger derfor ikke å søke på nytt.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "De nye reglene gjelder fra 1. januar 2024. Selv om du var under 20 år før de nye reglene trådte i kraft, kan du ikke få utbetalt pensjon for tiden før regelendringen.",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Dette må du gjøre",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må sjekke at du har et kontonummer registrert hos NAV.",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(erBosattUtlandet.not()) {
                paragraph {
                    text(
                        Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer per post. Du må da legge ved kopi av gyldig legitimasjon.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Du må kontakte Skatteetaten for å avklare om du bør endre skattekortet eller sende inn frivillig skattetrekk til NAV.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Hvis du logger på nav.no med BankID, Buypass eller Comfides, kan du endre kontonummer i \"Personopplysninger\" på www.${Constants.NAV_URL}. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du finner skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Husk underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    data class Manuelt(
        val erUnder18Aar: Expression<Boolean>,
        val erBosattUtlandet: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            TODO("Not yet implemented")
        }
    }
}