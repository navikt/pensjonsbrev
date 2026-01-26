package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG_FOSTERFORELDER
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadBarnetilleggFosterForelder : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_SOKNAD_BARNETILLEGG_FOSTERFORELDER
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
                text(bokmal { +"Du har søkt om barnetillegg for barn som du er fosterforelder til. For at vi skal kunne vurdere og beregne barnetillegget, ber vi om at du sender inn bekreftelse på hva du får i godtgjørelse fra det offentlige. " })
            }
            paragraph {
                text(bokmal { +"Det kan gis fosterhjemsgodtgjørelse til personer som har fosterbarn. Fosterhjemsgodtgjørelse består av utgiftsdekning og arbeidsgodtgjørelse. " })
            }
            paragraph {
                text(bokmal { +"Arbeidsgodtgjørelsen skattlegges som lønn og anses som personinntekt. Det er personinntekt etter skatteloven § 12-2 som har betydning for reduksjon av barnetillegg på grunn av inntekt etter folketrygdloven § 12-16. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL} " })
            }
            paragraph {
                text(bokmal { +"Vi ber om at du sender opplysningene til oss innen to uker etter at du har fått dette brevet. " })
            }
            paragraph {
                text(bokmal { +"Hvis vi ikke får nødvendige opplysninger innen fristen, kan vi avslå søknaden på grunn av manglende opplysninger. " })
            }
            paragraph {
                text(bokmal { +"I folketrygdloven § 21-3 finner du informasjon om opplysningsplikten din til Nav. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
