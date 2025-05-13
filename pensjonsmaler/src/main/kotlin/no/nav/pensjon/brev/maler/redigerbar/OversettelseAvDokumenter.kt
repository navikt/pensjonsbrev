package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OversettelseAvDokumenter : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_168
    override val kode = Pesysbrevkoder.Redigerbar.PE_OVERSETTELSE_AV_DOKUMENTER
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
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
                Bokmal to "Oversettelse av dokumenter",
            )
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to "Vi ber om bistand til å få oversatt vedlagte dokumenter fra ".expr() + fritekst("språk") + " til norsk.".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Det som ønskes oversatt er markert ".expr() + fritekst("med tusj, se vedlegg") + ".".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Husk å merke svarbrevet med saksreferansen."
                )
            }
            paragraph {
                // TODO: Kan felles.avsenderEnhet.navn redigeres i fritekst felt?
                textExpr(
                    Bokmal to "Honorar for oversettelsen vil mot regning bli dekket av ".expr() + fritekst("felles.avsenderEnhet.navn") + ".".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to fritekst("Svar for regning sendes til følgende adresse:") + "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "På forhånd takk for hjelpen."
                )
            }
        }
    }
}