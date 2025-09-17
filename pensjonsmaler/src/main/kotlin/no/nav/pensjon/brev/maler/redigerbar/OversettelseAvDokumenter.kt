package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OversettelseAvDokumenter : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_168
    override val kode = Pesysbrevkoder.Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Oversettelse av dokumenter",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Oversettelse av dokumenter" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi ber om bistand til å få oversatt vedlagte dokumenter fra " + fritekst("språk") + " til norsk." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det som ønskes oversatt er markert " + fritekst("med tusj, se vedlegg") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Husk å merke svarbrevet med saksreferansen." }
                )
            }
            paragraph {
                // TODO: Kan felles.avsenderEnhet.navn redigeres i fritekst felt?
                text(
                    bokmal { + "Honorar for oversettelsen vil mot regning bli dekket av " + fritekst("felles.avsenderEnhet.navn") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { + fritekst("Svar for regning sendes til følgende adresse:") + "" }
                )
            }
            paragraph {
                text(
                    bokmal { + "På forhånd takk for hjelpen." }
                )
            }
        }
    }
}