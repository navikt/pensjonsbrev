package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.maler.FeatureToggles.*
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.*

@TemplateModelHelpers
object KlageUtsattKlagefristUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = brevmalKlageUtsattKlagefristUT.toggle

    override val kode = UT_KLAGE_UTSATT_KLAGEFRIST
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)
    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - utsatt klagefrist",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV,
        )
    ) {
        title { text(bokmal { +"Klage - utsatt klagefrist" }) }

        outline {

            paragraph {
                text(
                    bokmal {
                        +"Vi viser til forespørsel om utsatt klagefrist, "
                        +"og gjør oppmerksom på at klagefristen i utgangspunktet fortsatt er seks uker selv om det er levert foreløpig klage. "
                        +"Vi viser til folketrygdloven § 21-12 femte ledd jf forvaltningsloven paragraf 29, "
                        +"§30 og 31 om regler for beregning av klagefristen, fristavbrudd og forlengelse av klagefristen."
                    }
                )
            }
            paragraph {
                text(bokmal { +"Vi må derfor be om at utfyllende klage og eventuell tilleggsinformasjon leveres innen to uker." })
            }
            paragraph {
                text(bokmal { +"Ved behov for ytterligere utsettelse av klagefristen må det sendes inn søknad om dette med ytterligere begrunnelse." })
            }
            paragraph {
                text(bokmal {
                    +"Utfyllende klage må sendes inn til skanning sammen med vedlagte forside til adresse oppgitt på forsidearket. "
                    +"På $NAV_URL kan du sende inn tilleggsopplysninger elektronisk ved bruk av BankID."
                })
            }
        }
    }
}