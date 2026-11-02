package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_AV_BOTIDSOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InnhentingAvBotidsopplysninger : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_AV_BOTIDSOPPLYSNINGER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av botidsopplysninger",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Innhenting av botidsopplysninger" })
        }
        outline {
            paragraph {
                text(bokmal { + "Vedrørende " + fritekst("navn") + ". "})
            }
            paragraph {
                text(bokmal { + "Som et ledd i behandlingen av stønadssak ber vi om dokumentasjon på all botid i Norge, og gjerne kopi av hovedregisterkort om dette finnes. " })
            }
            paragraph {
                text(bokmal { + "Vi ber om at opplysningene sendes inn sammen med vedlagt forside til oppgitt adresse på forsidearket snarest mulig. " })
            }
        }
    }
}
