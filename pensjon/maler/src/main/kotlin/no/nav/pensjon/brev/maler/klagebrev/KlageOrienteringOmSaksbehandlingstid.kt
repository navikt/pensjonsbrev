package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.maler.klagebrev.KlageOrienteringOmSaksbehandlingstid.Klageren.*
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer
import kotlin.collections.Set


@TemplateModelHelpers
object KlageOrienteringOmSaksbehandlingstid : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOrienteringOmSaksbehandlingstid.toggle

    override val kode = PE_KLAGE_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om saksbehandlingstid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )


    ) {
        val klageren = saksbehandlervalg("aklageren", "Velg hvem som er Klageren:").enum<Klageren>()
        val foreloepigVedtak = saksbehandlervalg("bforeloepigVedtak", "Velg om det klages på et foreløpig vedtak (utland)").bool()

        title { text(bokmal { +"Klage - " + fritekst("ytelse") + " - orientering om saksbehandlingstid" }) }

        outline {

            paragraph {
                text(bokmal { +"Klageren: " }, BOLD)
                showIf(klageren.isOneOf(BRUKER)) {
                    text(bokmal { +felles.bruker.fulltNavn() + " " })
                    text(bokmal { +felles.bruker.foedselsnummer.format() })
                }.orShowIf(klageren.isOneOf(VERGEFULLMEKTIG)) {
                    text(bokmal { +fritekst("navn / fødselsnummer") })
                }.orShowIf(klageren.isOneOf(ADVOKATFULLMEKTIG)) {
                    text(bokmal { +fritekst("navn / firmanavn / organisasjonsnummer") })
                }
            }

            paragraph {
                text(bokmal { +"Klagemotpart: " })
                text(bokmal { +"Nav familie- og pensjonsytelser" })
            }

            paragraph {
                text(bokmal {
                    +"Vi har "
                    +fritekst("dato") + " mottatt klagen over Nav familie- og pensjonsytelser sitt vedtak av "
                    +fritekst("dato") + "."
                })
            }

            title1 { text(bokmal { +"Behandlingstid" }) }
            showIf(foreloepigVedtak.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"Det påklagede vedtaket er et foreløpig vedtak. "
                        +"Klagen din gjelder spørsmål som vi ikke kan ta stilling til før vi har fått nødvendig informasjon fra utlandet. "
                        +"Når vi har mottatt denne informasjonen vil vi se på saken din på nytt. "
                        +"Du vil få beskjed fra oss om resultatet i saken når det er klart."
                    })
                }
            }.orShow {
                paragraph {
                    text(bokmal {
                        +"Saksbehandlingstiden er vanligvis " + fritekst("antall dager/uker/måneder") + ". "
                        +"Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed."
                    })
                }
            }

            title1 { text(bokmal { +"Meld fra om endringer" }) }
            paragraph {
                text(bokmal {
                    +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av saken din. "
                    +"Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende."
                })
            }
        }
    }

    enum class Klageren(override val displayText: String) : SaksbehandlerValgEnum {
        BRUKER("Bruker"),
        VERGEFULLMEKTIG("Verge/Fullmektig"),
        ADVOKATFULLMEKTIG("Advokatfirma/Fullmektig")
    }
}
