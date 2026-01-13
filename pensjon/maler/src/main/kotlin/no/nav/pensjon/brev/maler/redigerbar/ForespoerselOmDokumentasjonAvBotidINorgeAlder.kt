package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDtoSelectors.SaksbehandlerValgSelectors.opplystOmBotid
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.value
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object ForespoerselOmDokumentasjonAvBotidINorgeAlder : RedigerbarTemplate<ForespoerselOmDokumentasjonAvBotidINorgeDto> {

    // PE_IY_03_167 - dette er delen for alder
    override val kode = Pesysbrevkoder.Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Forespørsel om dokumentasjon av botid i Norge - alder",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Forespørsel om dokumentasjon av botid i Norge" }
            )
        }
        outline {
            //[PE_IY_03_167_tekst]

            paragraph {
                text(
                    bokmal { + "Vi ber med dette om å få tilsendt oversikt over botiden (kopi av " }
                )
                text(bokmal { + "hovedregisterkort/navnekort" }, fontType = FontType.BOLD)
                text(
                    bokmal { + " e.l) for " + felles.bruker.fulltNavn() + " med fødselsnummer " + felles.bruker.foedselsnummer.value + ". " },
                )
                showIf(saksbehandlerValg.opplystOmBotid) {
                    val dato = fritekst("mm.dd.år")
                    text(
                        bokmal { + "Vedkommende har opplyst at " + fritekst("han/hun") + " var sist bosatt i deres kommune fra " + dato + " til " + dato + "." },
                    )
                }
            }

            paragraph {
                text(bokmal { + "All" }, fontType = FontType.BOLD)
                text(bokmal { + " botid i Norge bes oppgitt." })
            }
            //[PE_IY_03_167_tekst]

            paragraph {
                text(bokmal { + "På forhånd takk for hjelpen." })
            }
        }
    }
}

