package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_EOS_LEGEERKLAERING
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_LEGE
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object EOSLegeerklaering : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_EOS_LEGEERKLAERING
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "EØS-legeerklæring til lege (også til Storbritannia)",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Vi trenger en detaljert EØS legeerklæring" })
        }
        outline {
            paragraph {
                text(bokmal { + fritekst("Navn") + " har søkt om uføretrygd, og har bodd eller arbeidet i et annet EØS-land. Derfor trenger vi EØS – detaljert legeerklæring, Nav 45-12.00. " })
            }
            paragraph {
                text(bokmal { +"Vi bruker folketrygdlovens § 21-4 når vi innhenter opplysninger. " })
            }

            title1 {
                text(bokmal { + "Hvor finner du skjema?" })
            }
            paragraph {
                text(bokmal { + "Du finner skjemaet på nav.no/leger. På sidene «Uføretrygd – legens rolle» og «Legeerklæring og spesialisterklæring» er skjemaet tilgjengelig under «Skjema og søknad»." })
            }

            title1 {
                text(bokmal { + "Hvordan fyller du ut skjema?" })
            }
            paragraph {
                text(bokmal { + "Skjemaet må fylles ut elektronisk. Kun punkter som er relevante for pasienten skal fylles ut. Det er viktig å oppgi ICD-koder i punkt 3.1. PDF-en er interaktiv, og det kommer forskjellige felt avhengig av hva du har fylt ut og valgt. Bruk Adobe Acrobat Reader når du skal fylle ut skjemaet. Du må ha en oppdatert versjon av denne. " })
            }
            paragraph {
                text(bokmal { + "Se veiledning for hvordan du åpner skjemaet på nav.no/leger. Gå inn på nav.no/leger og trykk på Legeerklæring og spesialisterklæring - Skjema og søknad - EØS-detaljert legeerklæring. Her ligger det veiledning for både PC og Mac. "})
            }

            title1 {
                text(bokmal { + "Hvordan sender du skjema tilbake til Nav?" })
            }
            paragraph {
                text(bokmal { + "Når legeerklæringen er ferdig utfylt, må den skrives ut, undertegnes og sendes i posten til Nav. Husk «Førsteside til saken din» når du returnerer skjemaet slik at dokumentasjon kommer frem til riktig enhet. " })
            }
            paragraph {
                text(bokmal { +"Vi må få legeerklæringen snarest mulig og innen " })
                text(bokmal { +"frist 6 uker frem i tid" }, BOLD)
                text(bokmal { +". Vi betaler etter taksten L180. " })
            }
            paragraph {
                text(bokmal { + "Vi takker på forhånd for et godt samarbeid. Har du spørsmål, kan du ringe oss på $NAV_KONTAKTSENTER_TELEFON_LEGE. " })
            }
        }
    }
}
