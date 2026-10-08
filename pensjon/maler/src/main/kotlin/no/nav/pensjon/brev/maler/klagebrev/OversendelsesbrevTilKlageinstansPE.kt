package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.saksnummer
import kotlin.collections.Set

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansPE : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelsesbrevTilKlageinstansPE.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_KLAGE_OVERSENDELSESBREV_TIL_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - Oversendelsesbrev til Nav Klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title { text(bokmal { +"Oversendelsesbrev til Nav Klageinstans " + fritekst("Fagområde") }) }

        outline {

            paragraph {
                text(bokmal { +"Klager: " })
                text(bokmal { +felles.bruker.fulltNavn() + " " })
                text(bokmal { +felles.bruker.foedselsnummer.format() })
                newline()
                text(bokmal { +"Saksnummer: " })
                text(bokmal { +felles.saksnummer })
            }

            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }
            paragraph {
                text(bokmal {
                    +"Vi viser til klage av "
                    +fritekst("dato") + " på vedtak av "
                    +fritekst("dato") + " der "
                    +fritekst("kort om resultatet i vedtaket")
                })
            }

            title1 { text(bokmal { +"Klagers anførsler" }) }
            paragraph { text(bokmal { +fritekst("Gjengi hovedinnholdet i klagers anførsler") }) }

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "Det skal gå fram hvorfor underinstansen opprettholder vedtaket. "
                                + "Det skal skrives en begrunnelse for hvorfor klager ikke fyller vilkårene. "
                                + "Klagers anførsler skal kommenteres / imøtegås. Drøftelsen skal avsluttes med en konklusjon."
                    )
                })
            }
        }
    }
}
