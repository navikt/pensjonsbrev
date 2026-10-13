package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.UT_KLAGE_INNHENTING_AV_OPPLYSNINGER
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object KlageInnhentingAvOpplysningerUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageInnhentingAvOpplysningerUT.toggle

    override val kode = UT_KLAGE_INNHENTING_AV_OPPLYSNINGER
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = Brevkontekst.ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - Innhenting av opplysninger",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(bokmal { +"Nav har behov for flere opplysninger" })
        }

        outline {
            paragraph {
                text(bokmal {
                    +"Vi har den "
                    +fritekst("dato") + " mottatt klagen din over vedtaket vårt av "
                    +fritekst("dato") + "."
                })
            }

            paragraph {
                text(bokmal {
                    +"Det følger av forvaltningsloven § 32 at klagen skal være skriftlig og underskrevet. "
                    +"Klagen skal også nevne hvilket vedtak det klages på og hvilken endring som ønskes. "
                    +"Dersom klagen er mangelfull, setter vi en kort frist slik at klager kan rette opp dette. "
                    +"Dersom manglene ikke blir rettet innen fristen og vilkårene for å behandle klagen ikke er oppfylt, skal klagen avvises. Dette følger av forvaltningsloven § 33. "
                })
            }

            paragraph {
                text(bokmal {
                    +fritekst(
                        "Tilpass etter hva som er mangelen i klagen. "
                                + "Vi ber derfor om at du underskriver på vedlagt kopi av klagen / "
                                + "opplyser om hvilket vedtak du klager på / "
                                + "opplyser om hvilken endring du ønsker i vedtaket/ hvorfor vedtaket er feil."
                    )
                })
            }

            paragraph {
                text(bokmal {
                    +fritekst(
                        "Klagen må sendes inn til skanning sammen med vedlagte forside til adressen oppgitt på forsidearket. "
                                + "På $NAV_URL kan du sende inn dokumenter elektronisk ved bruk av BankID. "
                                + "Det beste er om klagen sendes til oss på nytt på denne måten."
                    )
                })
            }

            paragraph {
                text(bokmal {
                    +"Vi ber deg sende klagen innen "
                    +fritekst("klagefristenes utløp/14 dager") + ". "
                    +"Dersom du trenger hjelp til å utforme klagen ber vi deg ta kontakt med ditt lokale Nav-kontor."
                })
            }

            paragraph {
                text(bokmal { +"Klagen din kan bli avvist dersom vi ikke mottar klagen innen fristen." })
            }
        }
    }
}
