package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.maler.FeatureToggles.*
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object KlageBegrunnelseForVedtakUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = brevmalKlageBegrunnelseForVedtakUT.toggle

    override val kode = UT_KLAGE_BEGRUNNELSE_FOR_VEDTAK
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - begrunnelse for vedtak",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        val klageFristUtgaatt = saksbehandlervalg("klageFristUtgaatt", "Klagefristen er utgått").bool()

        title { text(bokmal { +"Begrunnelse for vedtak" }) }

        outline {

            paragraph {
                text(bokmal {
                    +"Vi viser til henvendelsen din av "
                    +fritekst("dato") + " hvor du ber om en ytterligere begrunnelse for vedtaket av "
                    +fritekst("dato") + "."
                })
            }

            paragraph { text(bokmal { +"Saksbehandlers begrunnelse" }, ITALIC) }
            paragraph { text(bokmal { +"Vi håper at denne forklaringen gav en bedre forståelse av vedtaket." }) }
            showIf(klageFristUtgaatt) {
                paragraph {
                    text(bokmal { +"Vi minner om at fristen for å klage er seks uker fra den datoen du mottok vedtaket." })
                }
            }
        }
    }
}