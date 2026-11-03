package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOK_AVTALELAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadTilAvtaleland : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_SOK_AVTALELAND
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)

    override val featureToggle = FeatureToggles.innhentingOpplysningerForside.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon med svartid søknad til avtaleland",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text(
                bokmal { + "Søknad til avtaleland" },
                nynorsk { + "Søknad til avtaleland" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til din søknad om uføretrygd. Vi har registrert at du har bodd i " + fritekst("land") + ". Dette er et land Norge har inngått trygdeavtale med. " },
                    nynorsk { + "Vi viser til din søknad om uføretrygd. Vi har registrert at du har budd i " + fritekst("land") + ". Dette er eit land Noreg har inngått trygdeavtale med. " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Merk at hvert land har egne vilkår og krav til opptjening for å ha rett til uføreytelse fra aktuelt land. Dersom du har spørsmål til dette ber vi deg kontakte trygdemyndighetene i avtalelandet. " },
                    nynorsk { + "Merk at kvart land har eigne vilkår og krav til opptening for å ha rett til uføreyting frå aktuelt land. Dersom du har spørsmål til dette ber vi deg kontakte trygdemyndigheitene i avtalelandet. " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Det er frivillig å søke uføreytelse til et avtaleland, og dette vil ikke ha innvirkning på behandling av din søknad om uføretrygd i Norge. " },
                    nynorsk { + "Det er frivillig å søkje uføreyting til eit avtaleland, og dette vil ikkje ha innverknad på behandling av din søknad om uføretrygd i Noreg. " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Vi ber om en tilbakemelding innen " },
                    nynorsk { + "Vi ber om ei tilbakemelding innan " },
                )
                text(
                    bokmal { + "2 uker frem i tid" },
                    nynorsk { + "2 veker fram i tid" },
                    BOLD,
                )
                text(
                    bokmal { + " på om du også ønsker å søke uføretrygd i " + fritekst("land") + " eller ikke. Dersom vi ikke mottar tilbakemelding innen frist, antar vi at du ikke ønsker å fremme en søknad om uføretrygd til avtalelandet. " },
                    nynorsk { + " på om du også ønskjer å søkje uføretrygd i " + fritekst("land") + " eller ikkje. Dersom vi ikkje mottek tilbakemelding innan fristen, går vi ut frå at du ikkje ønskjer å fremje ein søknad om uføretrygd til avtalelandet. " },
                )
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
