package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.maler.klageOgAnke.KlageOrienteringOmSaksbehandlingstid.Saksbehandlingstid.*
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.navEnhet.navn

// Erstatte PE_IY_03_153 Klage - orientering om saksbehandlingstid, og PE_IY_03_162 Klage - orientering om saksbehandlingstid ved Nav Klageinstans.

@TemplateModelHelpers
object KlageOrienteringOmSaksbehandlingstid : RedigerbarTemplate<EmptyRedigerbarBrevdataMedSaksbehandlerValg> {

    override val featureToggle = FeatureToggles.brevmalKlageOrienteringOmSaksbehandlingstid.toggle

    override val kode = PE_KLAGE_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om saksbehandlingstid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )


    ) {
        val saksbehandlingstid = saksbehandlervalg("saksbehandlingstid", "Saksbehandlingstid").enum<Saksbehandlingstid>()

        title {
            text(
                bokmal { +"Klage - " + fritekst("ytelse") + " - orientering om saksbehandlingstid" },
                english { +"Appeal - " + fritekst("ytelse") + " - indication of case processing time" }
            )
        }

        outline {
            paragraph {
                text(bokmal { +"Klageren: " }, english { +"Appellant: " }, BOLD)
                text(bokmal { +felles.bruker.fulltNavn() + " " }, english { +felles.bruker.fulltNavn() + " " })
                text(bokmal { +felles.bruker.foedselsnummer.format() }, english { +felles.bruker.foedselsnummer.format() })
            }
            paragraph {
                text(bokmal { +"Klagemotpart: " }, english { +"Other party: " }, BOLD)
                text(bokmal { +felles.avsenderEnhet.navn }, english { +felles.avsenderEnhet.navn })
            }

            showIf(saksbehandlingstid.isOneOf(SaksbehandlingstidVedNFPellerNAY)) {
                paragraph {
                    text(
                        bokmal { +"Vi har " + fritekst("mottaksdato for klagen") + " mottatt klagen over " + felles.avsenderEnhet.navn + " vedtak av " + fritekst("vedtaksdato") + "." },
                        english { +"On " + fritekst("mottaksdato for klagen") + " we received an appeal about " + felles.avsenderEnhet.navn + " decision of " + fritekst("vedtaksdato") + "." }
                    )
                }
            }.orShowIf(saksbehandlingstid.isOneOf(SaksbehandlingstidVedNavKlageinstans)) {
                paragraph {
                    text(
                        bokmal { +"Vi har " + fritekst("mottaksdato for klagen") + " mottatt klagen over " + fritekst("Nav saksbehandlingsenhet") + " vedtak av " + fritekst("vedtaksdato") + "." },
                        english { +"On " + fritekst("mottaksdato for klagen") + " we received an appeal about " + fritekst("Nav saksbehandlingsenhet") + " decision of " + fritekst("vedtaksdato") + "." }
                    )
                }
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
    enum class Saksbehandlingstid : SaksbehandlerValgEnum {
        SaksbehandlingstidVedNFPellerNAY,
        SaksbehandlingstidVedNavKlageinstans
    }
}
