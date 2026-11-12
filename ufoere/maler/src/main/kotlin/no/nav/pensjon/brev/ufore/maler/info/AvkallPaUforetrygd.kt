package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvkallPaUforetrygd : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Ufoerebrevkoder.Redigerbar.UT_AVKALL_PA_UFORETRYGD
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val featureToggle = FeatureToggles.avkallPaUforetrygd.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering- avkall på uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Orientering om konsekvenser av å si ifra seg retten til uføretrygd" },
                nynorsk { +"Orientering om konsekvensar av å seie frå seg retten til uføretrygd" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Vi har " + fritekst("dato") + " mottatt brev/henvendelse fra deg hvor du uttaler at du ønsker å si ifra deg retten til uføretrygd. Vi ønsker i denne forbindelse å informere deg om konsekvensene av dette valget." },
                    nynorsk { +"Vi har " + fritekst("dato") + " motteke brev/førespurnad frå deg der du uttaler at du ønskjer å seie frå deg retten til uføretrygd. Vi ønskjer i denne samanhengen å informere deg om konsekvensane av dette valet." },
                )
            }
            title1 {
                text(
                    bokmal { +"Konsekvensene av å si fra seg uføretrygden:" },
                    nynorsk { +"Konsekvensane av å seie frå seg uføretrygda:" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Dersom du på et senere tidspunkt ønsker å få igjen uføretrygden må du gjennom hele søknadsprosessen med ditt lokale Nav-kontor på nytt." },
                            nynorsk { +"Dersom du på eit seinare tidspunkt ønskjer å få att uføretrygda må du gjennom heile søknadsprosessen med ditt lokale Nav-kontor på nytt." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Ved ny søknad kan du risikere å få en dårligere beregning enn den du har nå." },
                            nynorsk { +"Ved ny søknad kan du risikere å få ei dårlegare berekning enn den du har no." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Du kan etter et eventuelt opphør av uføretrygden stå uten inntekt fram til eventuell ny prosess med ny søknad om uføretrygd er gjennomført." },
                            nynorsk { +"Du kan etter eit eventuelt opphøyr av uføretrygda stå utan inntekt fram til eventuell ny prosess med ny søknad om uføretrygd er gjennomført." },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"I stedet for å si fra deg retten til uføretrygd, kan du ha «hvilende rett». Når du har så høy inntekt at du ikke får utbetalt uføretrygd, kalles det å ha hvilende rett. Du kan ha hvilende rett i inntil 5 år, med mulighet for forlengelse i 5 år til. Etter 10 år med sammenhengende hvilende rett, opphører vi uføretrygden din. Du vil få både varsel- og vedtaksbrev før vi gjør endringer i uføretrygden din." },
                    nynorsk { +"I staden for å seie frå deg retten til uføretrygd, kan du ha «kvilande rett». Når du har så høg inntekt at du ikkje får utbetalt uføretrygd, blir det kalla å ha kvilande rett. Du kan ha kvilande rett i inntil 5 år, med moglegheit for forlenging i 5 år til. Etter 10 år med samanhengande kvilande rett, opphøyrer vi uføretrygda di. Du vil få både varsel- og vedtaksbrev før vi gjer endringar i uføretrygda di." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi ber deg om en skriftlig tilbakemelding dersom du fortsatt ønsker å si ifra deg retten din til uføretrygd." },
                    nynorsk { +"Vi ber deg om ei skriftleg tilbakemelding dersom du framleis ønskjer å seie frå deg retten din til uføretrygd." },
                )
            }
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
