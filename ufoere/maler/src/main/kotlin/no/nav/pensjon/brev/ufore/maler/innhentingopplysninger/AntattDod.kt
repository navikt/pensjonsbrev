package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_ANTATT_DOD
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object AntattDod : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_ANTATT_DOD
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Antatt død",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Varsel om opphør av uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har fått melding om at " + fritekst("Navn") + " kan være antatt død. Vi har ikke fått en formell dødsmelding fra folkeregisteret, og derfor er vi usikre på om informasjonen vi har fått er riktig. " })
            }
            paragraph {
                text(bokmal { +"Hvis informasjonen vi har fått er feil, må du legitimere deg på et Nav-kontor eller sende inn en leveattest stemplet på norsk utenriksstasjon snarest, og senest " + fritekst("Dato") + ". Hvis vi ikke får en bekreftelse på at du lever, vil uføretrygden din opphøre etter folketrygdloven §§ 21-7 og 22-12. " })
            }
            paragraph {
                text(bokmal { +"Hvis du som etterlatt etter " + fritekst("Navn") + " leser dette brevet, ber vi deg om å varsle norske myndigheter om dødsfallet. " })
            }
        }
    }
}
