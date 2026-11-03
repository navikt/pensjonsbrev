package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
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


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "EØS-skjema fra bruker (Skjema for opplysninger om opphold i EØS-land)",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Innsending av opplysninger om utenlandsopphold" })
        }
        outline {
            paragraph {
                text(bokmal { + "Vi har mottatt søknaden din om uføretrygd der du har oppgitt at du har bodd, arbeidet eller oppholdt deg i et annet EØS-land. " })
            }

            title1 {
                text(bokmal { + "Du må fylle ut «Skjema for opplysninger om opphold i EØS-land»." })
            }
            paragraph {
                text(bokmal { + "Myndighetene i landet eller landene du har oppgitt i søknaden, trenger informasjon om dine opphold for å identifisere deg og vurdere søknaden din. Du kan også sende inn vedlegg som arbeidsattester, pensjonsbevis eller yrkesbevis som kan hjelpe myndighetene i andre land å vurdere dine rettigheter. " })
            }
            paragraph {
                text(bokmal { + "Om du har studert i utlandet med lån fra Statens lånekasse, vennligst send oss dokumentasjon på det. " })
            }
            paragraph {
                text(bokmal { + "Dette brevet kan du lese på Min side på nav.no, men skjemaet du skal fylle ut er ikke tilgjengelig i elektronisk versjon. Vi har sendt både brev, skjema og forsideark til deg i posten. "})
            }
            paragraph {
                text(bokmal { + "Sender du skjemaet i posten, må du legge ved forsidearket. Adressen er oppgitt på forsidearket. "})
            }
            paragraph {
                text(bokmal { + "Fristen for å sende inn skjemaet og vedlegg er to uker. Fristen gjelder fra du mottar dette brevet og skjema i posten. Du kan be om utsettelse av fristen ved å kontakte Nav. " })
            }

            title1 {
                text(bokmal { + "Hva skjer videre?" })
            }
            paragraph {
                text(bokmal { + "Vi vil videresende opplysningene dine til landene du har oppgitt at du har bodd, arbeidet eller opphold deg i. Landet eller landene vil behandle søknaden din, og du vil motta et brev med endelig beregning av ytelsen når de har gjort dette. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
