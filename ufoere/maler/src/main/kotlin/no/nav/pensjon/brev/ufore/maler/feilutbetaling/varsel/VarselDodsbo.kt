package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.VEDTAK
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_DODSBO
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingDodsboSaksbehandlervalgSelectors.kjentBobestyrer
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselDodsbo: RedigerbarTemplate<FeilutbetalingVarselDodsboDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_DODSBO
    override val kategori = FEILUTBETALING
    override val brevkontekst = VEDTAK
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling dødsbo",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val dato = fritekst ("dato")
        val navn = fritekst ("navn")
        val kilde = fritekst ("kilde")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(CurrencyFormat)


        title {
            text(
                bokmal { +"Vi vurderer om dødsbo etter " + navn + " må betale tilbake uføretrygd " },
                nynorsk { + "Vi vurderer om dødsbu etter NAVN må betale tilbake uføretrygd "}
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi vurderer at " + navn + " kan ha fått utbetalt for mye i uføretrygd fra og med " +
                        dato + " til og med " + dato + ". Grunnen til det, er at vi har fått opplysninger om at " + navn + " døde " + dato + ". " },
                    nynorsk { + "Vi vurderer at " + navn + " kan ha fått utbetalt for mykje i uføretrygd frå og med " +
                            dato + " til og med " + dato + ". Grunnen til det er at vi har fått opplysningar om at " + navn + " døydde " + dato + ". "}
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedtaket av " + dato + ", informerte vi om at dødsfallet kan ha ført til at " + navn +
                        " kan ha fått utbetalt for mye uføretrygd tilbake i tid. " },
                    nynorsk { + "I vedtaket av " + dato + " informerte vi om at dødsfallet kan ha ført til at " + navn +
                            " kan ha fått utbetalt for mykje uføretrygd tilbake i tid. "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "I folketrygdloven § 22-12 sjette ledd første punktum kan du lese mer om hvordan dødsfall påvirker utbetalingen av uføretrygd. " },
                    nynorsk { + "I folketrygdlova § 22-12 sjette ledd første punktum kan du lese meir om korleis dødsfall påverkar utbetalinga av uføretrygd. "}
                    )
            }
            title1 {
                text(
                    bokmal { + "Dette kan du gjøre hvis vi har feil opplysninger " },
                    nynorsk { + "Dette kan du gjere dersom vi har feil opplysningar "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Dødsboet har rett til å uttale seg og gi oss nye opplysninger før vi fatter et vedtak. " +
                        "Dette må gjøres innen 14 dager etter at dette varselet er mottatt, se avsnittet «Slik uttaler du deg» for mer informasjon. " },
                    nynorsk { + "Dødsbuet har rett til å uttale seg og gi oss nye opplysningar før vi fattar eit vedtak. " +
                            "Dette må gjerast innan 14 dagar etter at dette varselet er mottatt. Sjå avsnittet «Slik uttaler du deg» for meir informasjon. "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. " +
                        "Dødsboet får et vedtak på vegne av " + navn + " når saken er ferdig behandlet. " },
                    nynorsk { + "Dette er berre eit varsel om at vi vurderer å krevje tilbake det feilutbetalte beløpet. " +
                            "Dødsbuet får eit vedtak på vegner av " + navn + " når saka er ferdig behandla. "}
                )
            }
            title1 {
                text(
                    bokmal { + "Derfor mener vi " + navn + " har fått utbetalt for mye " },
                    nynorsk { + "Derfor meiner vi " + navn + " har fått utbetalt for mykje "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Vi har den " + dato + " fått opplysninger fra " + kilde + " om at " + navn + " døde " + dato + ". " },
                    nynorsk { + "Vi har den " + dato + " fått opplysningar frå " + kilde + " om at " + navn + " døydde " + dato + ". "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden opphører ved utgangen av måneden den som får uføretrygd dør. " },
                    nynorsk { + "Uføretrygda blir stoppa ved utgangen av månaden den som får uføretrygd døyr. "}
                    )
            }
            paragraph {
                text(
                    bokmal { + dato + " fikk utbetalt uføretrygd i perioden " + dato + " til " + dato + " etter at dødsfallet hadde skjedd. " +
                        "Utbetalingen skulle vært stanset i denne perioden, og derfor har det skjedd en feilutbetaling på " + bruttoFeilutbetalt + " kroner.  " },
                    nynorsk { + navn + " fekk utbetalt uføretrygd i perioden " + dato + " til " + dato + " etter at dødsfallet hadde skjedd. " +
                            "Utbetalinga skulle vore stansa i denne perioden, og derfor har det skjedd ei feilutbetaling på " + bruttoFeilutbetalt + " kroner. "}
                    )
            }

            paragraph {
                text(
                    bokmal { + "Vi vurderer nå om dette beløpet skal kreves tilbake fra dødsboet. " },
                    nynorsk { + "Vi vurderer no om dette beløpet skal krevjast tilbake frå dødsbuet. "}

                    )
            }
            showIf(saksbehandlerValg.kjentBobestyrer) {
                paragraph {
                    text(
                        bokmal { + "Vi har fått opplysninger om at skifteattest er utstedt til deg. Derfor får du dette varselet på vegne av boet. " },
                        nynorsk { + "Vi har fått opplysningar om at skifteattest er utstedt til deg. Derfor får du dette varselet på vegner av buet."}
                        )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Vi mangler opplysninger om hvem som er arvinger / representant for dødsboet / hvem som har fått utstedt skifteattest. Vi ber deg om å sende oss disse opplysningene. " },
                        nynorsk { + "Vi manglar opplysningar om kven som er arvingar / representant for dødsbuet / kven som har fått utstedt skifteattest. Vi ber deg om å sende oss desse opplysningane. "}
                        )
                }
            }
            includePhrase(FeilutbetalingFraser.KriterierTilbakekreving)
            includePhrase(FeilutbetalingFraser.KriterierIngenTilbakekreving)

            title1 {
                text(
                    bokmal { + "Slik kan boet uttale seg "},
                    nynorsk { + "Slik kan buet uttale seg "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener vi har feil opplysninger, har du rett til å uttale deg på vegne av boet før vi tar den endelige " +
                        "avgjørelsen om tilbakebetaling. Fristen for å gi uttalelse er 14 dager etter at dette brevet er mottatt. " },
                    nynorsk { + "Dersom du meiner vi har feil opplysningar, har du rett til å uttale deg på vegner av buet før vi tek den endelege " +
                            "avgjerda om tilbakebetaling. Fristen for å gi uttale er 14 dagar etter at dette brevet er mottatt. "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE}.  " },
                    nynorsk { + "Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE}. "}
                    )
            }
            title1 {
                text(
                    bokmal { + "Hva skjer videre i saken "},
                    nynorsk { + "Kva skjer vidare i saka "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Vi vil vurdere saken og sende boet et vedtak. Hvis boet må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om tilbakebetaling. " },
                    nynorsk { + "Vi vil vurdere saka og sende buet eit vedtak. Dersom buet må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om tilbakebetaling. "}
                    )
            }
            title1 {
                text(
                    bokmal { + "Boet har rett til innsyn  "},
                    nynorsk { + "Buet har rett til innsyn "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Boet har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven, § 18. " },
                    nynorsk { + "Buet har som hovudregel rett til å sjå sakens dokument etter reglane i forvaltningslova § 18. "}
                    )
            }
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}



