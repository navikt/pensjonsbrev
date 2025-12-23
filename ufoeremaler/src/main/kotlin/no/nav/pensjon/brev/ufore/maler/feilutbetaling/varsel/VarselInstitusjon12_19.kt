package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_INSTITUSJON
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselInstitusjon12_19: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_INSTITUSJON
    override val kategori = FEILUTBETALING
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling institusjonsopphold §12-19",
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
                text(bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " + fritekst("dato") +
                        " til og med " + fritekst("dato") + " . Grunnen til det, er at vi har fått opplysninger " +
                        "om at du har hatt fått dekket kost og losji over en lengre periode.  " })
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden påvirkes av om du får dekket kost og losji lengre enn 3 måneder. Dette gjelder også for opphold på institusjon i utlandet. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedtaket av " + fritekst("dato") + ", informerte vi deg om at opphold på institusjon med kost og losji, " +
                            "kan ha ført til at du har fått utbetalt for mye uføretrygd tilbake i tid.  " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Utbetalingen av uføretrygd skal ikke bli lavere hvis du forsørger ektefelle eller barn. " +
                            "Hvis du har hatt faste og nødvendige utgifter til bolig (husleie, strøm og/eller innboforsikring) i perioden vi mener du " +
                            "fikk utbetalt for mye, kan vi gjøre en ny vurdering av feilutbetalingen. Du må sende oss dokumentasjon på dine utgifter. " }
                )
            }
            includePhrase(FeilutbetalingFraser.FeilOpplysninger)
            title1 {
                text(
                    bokmal { + "Derfor mener vi du har fått utbetalt for mye " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har den dato fått opplysninger fra " + fritekst("kilde") + " om at du var innlagt ved " +
                            fritekst("institusjon") + " fra " + fritekst("dato") + ". " + fritekst("Evt. Til dato/idag") }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd i perioden " + fritekst("dato") + " til " + fritekst("dato") +
                            " samtidig som du hadde et lengre opphold på institusjon hvor du fikk dekket kost og losji. Utbetalingen din skulle " +
                            "vært lavere i denne perioden, og derfor har det skjedd en feilutbetaling på " + pesysData.feilutbetaltBrutto.format(CurrencyFormat) + " kroner.  " }
                )
            }
            includePhrase(FeilutbetalingFraser.KriterierTilbakekreving)
            includePhrase(FeilutbetalingFraser.KriterierIngenTilbakekreving)
            includePhrase(FeilutbetalingFraser.SlikUttalerDuDeg)
            includePhrase(FeilutbetalingFraser.HvaSkjerVidere)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}



