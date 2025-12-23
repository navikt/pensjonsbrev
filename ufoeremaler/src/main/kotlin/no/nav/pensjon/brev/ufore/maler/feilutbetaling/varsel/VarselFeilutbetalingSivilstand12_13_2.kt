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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_SIVILSTAND
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselFeilutbetalingSivilstand12_13_2: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_SIVILSTAND
    override val kategori = FEILUTBETALING
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling sivilstand §12-13 andre ledd",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val feilutbetaltBeloep =
        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd " })
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " +  fritekst("dato") + " til og med " +  fritekst("dato") + ". " +
                            "Grunnen til det, er at vi har fått opplysninger om at du har fått en samboer eller ektefelle." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Din sivilstand påvirker beregningen av din uføretrygd, og er årsaken til at vi vurderer at du har fått utbetalt for mye. " +
                            "I vedtaket av " +  fritekst("dato") + ", informerte vi deg om at endringen i din sivilstand kan ha ført til feilutbetalinger tilbake i tid. " }
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
                    bokmal { + "Vi har den " +  fritekst("dato") + " mottatt opplysninger fra " +  fritekst("kilde") +
                            " om at du " +  fritekst("handling") + " den " +  fritekst("dato") + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Den " + fritekst("dato") + " fikk du vedtak om innvilgelse av X prosent uføretrygd. I søknaden dato fylte du ut at du var enslig. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Fordi du får uføretrygd beregnet etter reglene for minsteytelse, påvirker din sivilstand beregningen av din uføretrygd. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du får en lavere utbetaling av uføretrygd fra måneden etter at sivilstanden din ble endret. Dette står i folketrygdloven § 22-12 femte ledd. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd som enslig i perioden " +  fritekst("dato") + " til " +
                            fritekst("dato") + " , og her har det oppstått en feilutbetaling på " + pesysData.feilutbetaltBrutto.format(LocalizedFormatter.CurrencyFormat)  +
                            " kroner. Utbetalingen din skulle vært endret fra måneden etter at du " +  fritekst("handling") + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + fritekst("Fritekst")}
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



