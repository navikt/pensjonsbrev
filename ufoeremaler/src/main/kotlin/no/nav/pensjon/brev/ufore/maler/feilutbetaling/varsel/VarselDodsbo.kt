package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_DODSBO
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingDodsboSaksbehandlervalgSelectors.kjentBobestyrer
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingDodsboSaksbehandlervalgSelectors.ukjentBobestyrer
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingVarselDodsboDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselDodsbo: RedigerbarTemplate<FeilutbetalingVarselDodsboDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_DODSBO
    override val kategori = FEILUTBETALING
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling dødsbo",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd " })
        }
        outline {
            paragraph {
                text(bokmal { + "Vi vurderer at " + fritekst("navn") + " kan ha fått utbetalt for mye i uføretrygd fra og med " +
                        fritekst("dato") + " til og med " + fritekst("dato") +
                        " . Grunnen til det, er at vi har fått opplysninger om at " + fritekst("navn") + " døde " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { + "I vedtaket av " + fritekst("dato") + ", informerte vi om at dødsfallet kan ha ført til at " + fritekst("navn") +
                        " kan ha fått utbetalt for mye uføretrygd tilbake i tid. " })
            }
            paragraph {
                text(bokmal { + "I folketrygdloven paragraf 22-12 sjette ledd første punktum kan du lese mer om hvordan dødsfall påvirker utbetalingen av uføretrygd. " })
            }
            title1 {
                text(bokmal { + "Dette kan du gjøre hvis vi har feil opplysninger " })
            }
            paragraph {
                text(bokmal { + "Dødsboet har rett til å uttale seg og gi oss nye opplysninger før vi fatter et vedtak. " +
                        "Dette må gjøres innen 14 dager etter at dette varselet er mottatt, se avsnittet \"Slik uttaler du deg\" for mer informasjon. " })
            }
            paragraph {
                text(bokmal { + "Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. " +
                        "Dødsboet får et vedtak på vegne av " + fritekst("navn") + " når saken er ferdig behandlet. " })
            }
            paragraph {
                text(bokmal { + "Derfor mener vi " + fritekst("navn") + " har fått utbetalt for mye " })
            }
            paragraph {
                text(bokmal { + "Vi har den " + fritekst("dato") + " fått opplysninger fra " + fritekst("kilde") +
                        " om at " + fritekst("navn") + " døde " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { + "Uføretrygden opphører ved utgangen av måneden den som får uføretrygd dør. " })
            }
            paragraph {
                text(bokmal { + "" + fritekst("dato") + " fikk utbetalt uføretrygd i perioden dato til dato etter at dødsfallet hadde skjedd. " +
                        "Utbetalingen skulle vært stanset i denne perioden, og derfor har det skjedd en feilutbetaling på " +
                        pesysData.feilutbetaltBrutto.format(LocalizedFormatter.CurrencyFormat) + " kroner.  " })
            }
            paragraph {
                text(bokmal { + "Vi vurderer nå om dette beløpet skal kreves tilbake fra dødsboet. " })
            }
            showIf(saksbehandlerValg.kjentBobestyrer) {
                paragraph {
                    text(bokmal { + "Vi har fått opplysninger om at skifteattest er utstedt til deg. Derfor får du dette varselet på vegne av boet. " })
                }
            }
            showIf(saksbehandlerValg.ukjentBobestyrer) {
                paragraph {
                    text(bokmal { + "Vi mangler opplysninger om hvem som er arvinger / representant for dødsboet / hvem som har fått utstedt skifteattest. Vi ber deg om å sende oss disse opplysningene. " })
                }
            }
            includePhrase(FeilutbetalingFraser.KriterierTilbakekreving)
            includePhrase(FeilutbetalingFraser.KriterierIngenTilbakekreving)

            title1 {
                text(bokmal { + "Slik kan boet uttale seg "})
            }
            paragraph {
                text(bokmal { + "Hvis du mener vi har feil opplysninger, har du rett til å uttale deg på vegne av boet før vi tar den endelige " +
                        "avgjørelsen om tilbakebetaling. Fristen for å gi uttalelse er 14 dager etter at dette brevet er mottatt. " })
            }
            paragraph {
                text(bokmal { + "Du kan skrive til oss på nav.no/kontakt eller ringe oss på telefon 55 55 33 33.  " })
            }
            title1 {
                text(bokmal { + "Hva skjer videre i saken "})
            }
            paragraph {
                text(bokmal { + "Vi vil vurdere saken og sende boet et vedtak. Hvis boet må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om tilbakebetaling. " })
            }
            title1 {
                text(bokmal { + "Boet har rett til innsyn  "})
            }
            paragraph {
                text(bokmal { + "Boet har også som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven, § 18. " })
            }
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}



