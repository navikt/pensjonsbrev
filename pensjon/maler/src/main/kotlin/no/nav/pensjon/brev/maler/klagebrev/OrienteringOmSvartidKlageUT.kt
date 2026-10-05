package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

@TemplateModelHelpers
object OrienteringOmSvartidKlageUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_ORIENTERING_OM_SVARTID
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om svartid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Klage - orientering om svartid" },
                english { +"Appeal - indication of response time" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi viser til din klage på " + fritekst("ytelse") + "." },
                    english { +"We refer to your appeal regarding " + fritekst("ytelse") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har mottatt klagen din, og den vil bli behandlet så snart som mulig." },
                    english { +"We have received your appeal, and it will be processed as soon as possible." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi vil kontakte deg dersom vi trenger mer informasjon." },
                    english { +"We will contact you if we need more information." }
                )
            }
        }
    }
}