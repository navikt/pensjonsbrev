package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OrienteringOmSvartidKlageUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_ORIENTERING_OM_SVARTID
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - orientering om svartid",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        val orienteringOmSvartid = saksbehandlervalg("orienteringOmSvartid", "Velg brev Orientering om svartid:").enum<OrienteringOmSvartid>()
        val saksinnsyn = saksbehandlervalg("saksinnsyn", "Saksinnsyn og NKS har ikke sendt ut dokumenter ennå").bool()


        title {
            text(bokmal { +"Orientering om svartid " })
            showIf(orienteringOmSvartid.isOneOf(OrienteringOmSvartid.Klage)) {
                text(bokmal { +"- klage" })
            }.orShowIf(orienteringOmSvartid.isOneOf(OrienteringOmSvartid.Omgjoerring)) {
                text(bokmal { +"- krav om omgjøring" })
            }
        }

        outline {
            showIf(orienteringOmSvartid.isOneOf(OrienteringOmSvartid.Klage)) {
                paragraph { text(bokmal { +"Vi har den " + fritekst("dato") + " mottatt klagen din over vedtaket vårt av " + fritekst("datoVedtak") + "." }) }

                title1 { text(bokmal { +"Behandlingstid" }) }
                paragraph {
                    text(bokmal {
                        +"Saksbehandlingstiden hos Nav arbeid og ytelser er inntil 4 måneder. "
                        +"Dersom vi ikke finner grunnlag for å gjøre om vedtaket vårt, vil vi sende klagen din over til Nav klageinstans for videre behandling. "
                        +"Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed."
                    })
                }
            }.orShowIf(orienteringOmSvartid.isOneOf(OrienteringOmSvartid.Omgjoerring)) {
                paragraph {
                    text(bokmal {
                        +"Vi har den " + fritekst("dato") + " mottatt kravet ditt om omgjøring av vedtaket vårt av " + fritekst("datoVedtak") + ". "
                        +"Du ber om omgjøring av " + fritekst("eks. uføretidspunktet/ung ufør") + "."
                    })
                }

                title1 { text(bokmal { +"Behandlingstid" }) }
                paragraph {
                    text(bokmal {
                        +"Saksbehandlingstiden hos Nav arbeid og ytelser er inntil 4 måneder. "
                        +"Dersom vi ikke finner grunnlag for å gjøre om vedtaket vårt, vil vi sende kravet ditt om omgjøring over til Nav klageinstans for videre behandling. "
                        +"Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed."
                    })
                }
            }

            showIf(saksinnsyn.equalTo(true)) {
                title1 { text(bokmal { +"Saksinnsyn" }) }
                paragraph {
                    text(bokmal {
                        +"Du har bedt om innsyn i saken, og du vil få tilsendt kopi av sakens dokumenter av Nav kontaktsenter så snart som mulig. "
                        +"Utfyllende klage må sendes innen to uker etter at du har mottatt kopi av sakens dokumenter."
                    })
                }
                paragraph { text(bokmal { +"På Nav.no kan du sende inn den utfyllende klagen elektronisk ved bruk av BankID." }) }
                paragraph {
                    text(bokmal {
                        +"Hvis du er representert av advokat eller fullmektig kan denne ettersende utfyllende klage eller ytterligere kommentarer på vegne av deg. "
                        +"Det må da benyttes forside for innsendelse. "
                        +"Forside kan enkelt lages på Nav.no/ettersendelse."
                    })
                }
            }

            title1 { text(bokmal { +"Meld fra om endringer" }) }
            paragraph {
                text(
                    bokmal {
                        +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av saken din. "
                        +"Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende."
                    }
                )
            }
        }
    }

    enum class OrienteringOmSvartid(override val displayText: String) : SaksbehandlerValgEnum {
        Klage("Orientering om svartid - klage"),
        Omgjoerring("Orientering om svartid - krav om omgjøring")
    }
}
