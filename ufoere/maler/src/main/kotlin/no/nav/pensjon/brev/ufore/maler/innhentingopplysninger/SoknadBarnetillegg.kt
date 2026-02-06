package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadBarnetillegg : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_SOKNAD_BARNETILLEGG
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Din søknad om barnetillegg i uføretrygd",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Din søknad om barnetillegg i uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Du har søkt om barnetillegg for barn som ikke bor hos deg. For å kunne behandle din søknad, trenger vi at du sender oss dokumentasjon på forsørgelse av barn. " })
            }
            paragraph {
                text(bokmal { +"Dokumentasjon på forsørgelse kan være avtale om delt bosted, samværsavtale, avtale om bidrag. Dokumentasjonen må ha dato og være undertegnet av begge foreldrene. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider nav.no/soknad eller nav.no/ettersende " })
            }
            paragraph {
                text(bokmal { +"Vi ber om at du sender opplysningene til oss innen to uker etter at du har fått dette brevet. " })
            }
            paragraph {
                text(bokmal { +"Dersom vi ikke får nødvendige opplysninger innen frist, kan søknaden din bli avslått på grunn av manglende opplysninger. " })
            }
            paragraph {
                text(bokmal { +"Folketrygdloven § 21-3 omhandler opplysningsplikten din til Nav. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
