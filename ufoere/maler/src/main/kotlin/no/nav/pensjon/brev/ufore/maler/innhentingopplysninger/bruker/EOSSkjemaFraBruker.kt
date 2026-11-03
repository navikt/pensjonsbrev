package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger.bruker

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_EOS_SKJEMABRUKER
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object EOSSkjemaFraBruker : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_EOS_SKJEMABRUKER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)

    override val featureToggle = FeatureToggles.innhentingOpplysningerForside.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "EØS-skjema fra bruker (Skjema for opplysninger om opphold i EØS-land)",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text(
                bokmal { + "Innsending av opplysninger om utenlandsopphold" },
                nynorsk { + "Innsending av opplysningar om utanlandsopphald" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi har mottatt søknaden din om uføretrygd der du har oppgitt at du har bodd, arbeidet eller oppholdt deg i et annet EØS-land. " },
                    nynorsk { + "Vi har motteke søknaden din om uføretrygd der du har oppgitt at du har budd, arbeidd eller opphalde deg i eit anna EØS-land. " },
                )
            }

            title1 {
                text(
                    bokmal { + "Du må fylle ut «Skjema for opplysninger om opphold i EØS-land»." },
                    nynorsk { + "Du må fylle ut «Skjema for opplysningar om opphald i EØS-land»." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Myndighetene i landet eller landene du har oppgitt i søknaden, trenger informasjon om dine opphold for å identifisere deg og vurdere søknaden din. Du kan også sende inn vedlegg som arbeidsattester, pensjonsbevis eller yrkesbevis som kan hjelpe myndighetene i andre land å vurdere dine rettigheter. " },
                    nynorsk { + "Myndigheitene i landet eller landa du har oppgitt i søknaden, treng informasjon om dine opphald for å identifisere deg og vurdere søknaden din. Du kan også sende inn vedlegg som arbeidsattestar, pensjonsbevis eller yrkesbevis som kan hjelpe myndigheitene i andre land å vurdere dine rettar. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Om du har studert i utlandet med lån fra Statens lånekasse, vennligst send oss dokumentasjon på det. " },
                    nynorsk { + "Om du har studert i utlandet med lån frå Statens lånekasse, ver venleg og send oss dokumentasjon på det. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette brevet kan du lese på Min side på nav.no, men skjemaet du skal fylle ut er ikke tilgjengelig i elektronisk versjon. Vi har sendt både brev, skjema og forsideark til deg i posten. " },
                    nynorsk { + "Dette brevet kan du lese på Min side på nav.no, men skjemaet du skal fylle ut er ikkje tilgjengeleg i elektronisk versjon. Vi har sendt både brev, skjema og forsideark til deg i posten. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Sender du skjemaet i posten, må du legge ved forsidearket. Adressen er oppgitt på forsidearket. " },
                    nynorsk { + "Sender du skjemaet i posten, må du leggje ved forsidearket. Adressa er oppgitt på forsidearket. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Fristen for å sende inn skjemaet og vedlegg er to uker. Fristen gjelder fra du mottar dette brevet og skjema i posten. Du kan be om utsettelse av fristen ved å kontakte Nav. " },
                    nynorsk { + "Fristen for å sende inn skjemaet og vedlegg er to veker. Fristen gjeld frå du mottek dette brevet og skjema i posten. Du kan be om utsetjing av fristen ved å kontakte Nav. " },
                )
            }

            title1 {
                text(
                    bokmal { + "Hva skjer videre?" },
                    nynorsk { + "Kva skjer vidare?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi vil videresende opplysningene dine til landene du har oppgitt at du har bodd, arbeidet eller opphold deg i. Landet eller landene vil behandle søknaden din, og du vil motta et brev med endelig beregning av ytelsen når de har gjort dette. " },
                    nynorsk { + "Vi vil vidaresende opplysningane dine til landa du har oppgitt at du har budd, arbeidd eller opphalde deg i. Landet eller landa vil behandle søknaden din, og du vil motta eit brev med endeleg berekning av ytinga når dei har gjort dette. " },
                )
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
