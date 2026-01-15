package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.value
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.navn

@TemplateModelHelpers
object BekreftelsePaaFlyktningstatus : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_049
    override val kode = Pesysbrevkoder.Redigerbar.PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på flyktningstatus",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Bekreftelse på flyktningstatus" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + felles.avsenderEnhet.navn + " ønsker å få bekreftet om " + felles.bruker.fulltNavn() + " med fødselsnummer " +
                            felles.bruker.foedselsnummer.value + " er registrert med flyktningstatus hos dere." }
                )
            }
            paragraph {
                text(bokmal { + "Vi ønsker i den forbindelse kopi av vedtaket og dato for ankomst til Norge." })
            }
        }
    }
}

