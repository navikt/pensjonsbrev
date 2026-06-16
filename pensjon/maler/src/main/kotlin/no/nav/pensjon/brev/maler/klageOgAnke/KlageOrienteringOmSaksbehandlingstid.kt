package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.PesysDataSelectors.foedselsnummer
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.PesysDataSelectors.navAvsenderEnhet
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDtoSelectors.pesysData
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
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
                text(bokmal { +"Klageren:  " }, english { +"Appellant:  " }, BOLD)
                text(bokmal { +pesysData.navn + "  " }, english { +pesysData.navn + "  " })
                text(bokmal { +pesysData.foedselsnummer.format() }, english { +pesysData.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Klagemotpart:  " }, english { +"Other party:  " }, BOLD)
                text(bokmal { +pesysData.navAvsenderEnhet }, english { +pesysData.navAvsenderEnhet })
            }
            paragraph {
                text(
                    bokmal { +"Vi har " + fritekst("mottaksdato for klagen") + " mottatt klagen over " + pesysData.navAvsenderEnhet + " vedtak av " + fritekst("vedtaksdato") + "." },
                    english { +"On " + fritekst("mottaksdato for klagen") + " we received an appeal about " + pesysData.navAvsenderEnhet + " decision of " + fritekst("vedtaksdato") + "." }
                )
            }
            title1 { text(bokmal { +"Behandlingstid" }, english { +"Processing time" }) }
            paragraph {
                text(
                    bokmal { +"Saksbehandlingstiden er vanligvis " + fritekst("antall dager/uker/måneder") + ". " },
                    english { +"The processing time is normally " + fritekst("number of days/weeks/months") + ". " }
                )
                text(
                    bokmal { +"Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed." },
                    english { +"If the processing of your case has not been completed within that time, you will be notified." }
                )
            }
            title1 { text(bokmal { +"Meld fra om endringer" }, english { +"Please notify us of changes" }) }
            paragraph {
                text(
                    bokmal { +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av saken din. Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende." },
                    english { +"Please keep us informed about circumstances that can affect the decision on your case. These might be changes in circumstances relating to health, work, income, civil status and similar." }
                )
            }
        }
    }
}
