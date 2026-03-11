package no.nav.pensjon.brev.ufore.maler.opphor

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_OPPHOR_ORIENTERING
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object OpphorOrientering : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_OPPHOR_ORIENTERING
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om konsekvenser av å si ifra seg retten til uføretrygd",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Orientering om konsekvenser av å si ifra seg retten til uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har " + fritekst("dato") + " mottatt brev/henvendelse fra deg hvor du uttaler at du ønsker å si ifra deg retten til uføretrygd. Vi ønsker i denne forbindelse å informere deg om konsekvensene av dette valget." })
            }
            paragraph {
                text(bokmal { +"Konsekvensene av å si i fra seg uføretrygden: " })
                list {
                    item { text(bokmal { +"Dersom du på et senere tidspunkt ønsker å få igjen uføretrygden må du gjennom hele søknadsprosessen med ditt lokale NAV-kontor på nytt." }) }
                    item { text(bokmal { +"Ved ny søknad kan du kan risikere å få en dårligere beregning enn den du har nå." }) }
                    item { text(bokmal { +"Du kan etter et eventuelt opphør av uføretrygden stå uten inntekt fram til eventuell ny prosess med ny søknad om uføretrygd er gjennomført." }) }
                }
            }
            paragraph {
                text(bokmal { +"Det er også en mulighet å ha en «hvilende rett» til uføretrygd dersom inntekt ved siden av uføretrygden overstiger 80 prosent av oppjustert inntekt før uførhet. " +
                        "80 prosent av oppjustert inntekt før uførhet er per nå for " + fritekst("beløp") + " kroner. Dette beløpet vil endre seg med årlig endring i folketrygdens grunnbeløp. " +
                        "Denne retten kan du ha i inntil 10 år ved arbeid ved siden av uføretrygd. Du kan dermed ha inntekter over denne grensen, og ikke få utbetalt uføretrygd i inntil 10 år uten at retten til uføretrygden opphører." })
            }
            paragraph {
                text(bokmal { +"Vi ber deg om en skriftlig tilbakemelding dersom du fortsatt ønsker å si ifra deg retten din til uføretrygd." })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
