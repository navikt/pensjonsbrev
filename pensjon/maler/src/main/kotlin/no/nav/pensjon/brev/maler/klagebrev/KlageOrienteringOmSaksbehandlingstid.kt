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
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
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
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om saksbehandlingstid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )


    ) {
        val klageren = saksbehandlervalg("klageren", "Velg hvem som er Klageren:").enum<Klageren>()

        title {
            text(
                bokmal { +"Klage - " + fritekst("ytelse") + " - orientering om saksbehandlingstid" },
                english { +"Appeal - " + fritekst("ytelse") + " - update on case processing time" },
            )
        }

        outline {

            showIf(klageren.isOneOf(BRUKER)) {
                paragraph {
                    text(bokmal { +"Klageren: " }, english { +"Appellant: " }, BOLD)
                    text(bokmal { +felles.bruker.fulltNavn() + " " }, english { +felles.bruker.fulltNavn() + " " })
                    text(bokmal { +felles.bruker.foedselsnummer.format() }, english { +felles.bruker.foedselsnummer.format() })
                }
            }.orShowIf(klageren.isOneOf(VERGEFULLMEKTIG)) {
                paragraph {
                    text(bokmal { +"Klageren: " }, english { +"Appellant: " }, BOLD)
                    text(bokmal { +fritekst("navn / fødselsnummer") }, english { +fritekst("name / national identity number") })
                }
            }.orShowIf(klageren.isOneOf(ADVOKATFULLMEKTIG)) {
                paragraph {
                    text(bokmal { +"Klageren: " }, english { +"Appellant: " }, BOLD)
                    text(bokmal { +fritekst("navn / firmanavn / organisasjonsnummer") }, english { +fritekst("name / company name / organisation number") })
                }
            }

            paragraph {
                text(bokmal { +"Klagemotpart: " }, english { +"Respondent: " }, BOLD)
                text(bokmal { +"Nav familie- og pensjonsytelser" }, english { +"Nav Family and Pension Benefits" })
            }

            paragraph {
                text(bokmal {
                    +"Vi har "
                    +fritekst("dato") + " mottatt klagen over Nav familie- og pensjonsytelser sitt vedtak av "
                    +fritekst("dato") + "."
                }, english {
                    +"We received the appeal on "
                    +fritekst("date") + " against the decision of Nav Family and Pension Benefits dated "
                    +fritekst("date") + "."
                })
            }
            paragraph {
                text(bokmal {
                    +"Vi har " + fritekst("mottaksdato for klagen") + " mottatt klagen over "
                    +fritekst("Nav saksbehandlingsenhet") + " vedtak av "
                    +fritekst("vedtaksdato") + "."
                }, english {
                    +"We received the appeal on " + fritekst("date the appeal was received") + " against the decision of "
                    +fritekst("Nav case processing unit") + " dated "
                    +fritekst("decision date") + "."
                })
            }

            title1 { text(bokmal { +"Behandlingstid" }, english { +"Processing time" }) }
            paragraph {
                text(bokmal {
                    +"Det påklagede vedtaket er et foreløpig vedtak. "
                    +"Klagen din gjelder spørsmål som vi ikke kan ta stilling til før vi har fått nødvendig informasjon fra utlandet. "
                    +"Når vi har mottatt denne informasjonen vil vi se på saken din på nytt. "
                    +"Du vil få beskjed fra oss om resultatet i saken når det er klart."
                }, english {
                    +"The decision you are appealing is provisional. "
                    +"Your appeal concerns matters that we cannot decide until we have received the necessary information from abroad. "
                    +"Once we have received this information, we will review your case. "
                    +"We will notify you of the outcome when it is ready."
                })
            }

            title1 { text(bokmal { +"Meld fra om endringer" }, english { +"Notify us of changes" }) }
            paragraph {
                text(bokmal {
                    +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av saken din. "
                    +"Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende."
                }, english {
                    +"Please keep us informed of any circumstances that may affect the decision in your case. "
                    +"These may include changes in your health, work, income, marital status, and similar circumstances."
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
