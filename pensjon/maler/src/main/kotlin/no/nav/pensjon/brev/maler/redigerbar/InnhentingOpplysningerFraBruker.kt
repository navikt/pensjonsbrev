package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Alderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.navn

@TemplateModelHelpers
object InnhentingOpplysningerFraBruker : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_048
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_OPPLYSNINGER_FRA_BRUKER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = Sakstype.pensjon

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhente opplysninger",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Du må sende oss flere opplysninger" },
                english { + "Collection of detailed information" },
            )
        }

        outline {

            paragraph {
                val henvendelse = fritekst("blankett/brev/henvendelse")
                val dato = fritekst("dato")
                val opplysninger = fritekst("opplysning 1")

                text(
                    bokmal { + felles.avsenderEnhet.navn + " har mottatt en " + henvendelse + " fra deg " + dato + ". For å kunne behandle henvendelsen mangler vi følgende opplysninger: " },
                    english { + felles.avsenderEnhet.navn + " received a " + henvendelse + " from you on " + dato + ". In order to process your request, we need the following information from you: " },
                )
                list {
                    item {
                        text(
                            bokmal { + opplysninger },
                            english { + opplysninger },
                        )
                    }
                }
            }


            paragraph {
                val dato = fritekst("dato")
                text(
                    bokmal { + "Vi ber deg derfor om å sende oss overnevnte opplysninger innen " + dato + " til adresse:" },
                    english { + "Please send us the above information by " + dato + ", to the following address:" },
                )
            }
            includePhrase(Alderspensjon.Returadresse)

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}

