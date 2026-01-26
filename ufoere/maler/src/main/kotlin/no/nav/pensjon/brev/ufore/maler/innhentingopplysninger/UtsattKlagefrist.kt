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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_UTSATT_KLAGEFRIST
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object UtsattKlagefrist : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_UTSATT_KLAGEFRIST
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Utsatt klagefrist",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Utsatt klagefrist" })
        }
        outline {
            paragraph {
                text(bokmal { +"Du har bedt oss om utsatt klagefrist. Klagefristen er fortsatt seks uker, selv om vi har fått en foreløpig klage. " })
            }
            paragraph {
                text(bokmal { +"Vi ber deg om å sende oss en utfyllende klage og eventuell tilleggsinformasjon innen to uker. Hvis vi ikke har fått utfyllende klage innen fristen, vil vi behandle klagen ut fra de opplysningene vi har i saken. " })
            }
            paragraph {
                text(bokmal { +"Trenger du mer tid, kan du sende oss en søknad og be om ytterligere utsettelse av klagefristen. Du må gi oss en begrunnelse for hvorfor du trenger mer tid. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL} " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
