package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.PesysDataSelectors.navAvsenderEnhet
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Erstatte PE_IY_03_153 Klage - orientering om saksbehandlingstid ved Nav pensjon

@TemplateModelHelpers
object KlageOrienteringOmSaksbehandlingstid : RedigerbarTemplate<KlageOrienteringOmSaksbehandlingstidDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_KLAGE_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst: Brevkontekst = Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om saksbehandlingstid ved Nav pensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )

    ) {
        title {
            text(
                bokmal { +fritekst("Fyll inn ytelse") + "- orientering om saksbehandlingstid" },
                english { +fritekst("Fyll inn ytelse") + "- indication of case prosessing time" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi har " + fritekst("fyll inn mottaksdato for klagen") + " mottatt klagen over " + pesysData.navAvsenderEnhet + fritekst("fyll inn vedtaksdato") + "." },
                    english { +"On " + fritekst("fyll inn vedtaksdato") + " we received an appeal about " + pesysData.navAvsenderEnhet + fritekst("fyll inn vedtaksdato") + "." }
                )
            }
        }
    }
}
