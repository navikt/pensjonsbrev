package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.value
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object ForespoerselOmDokumentasjonAvBotidINorgeAlder : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_167 - dette er delen for alder
    override val kode = Pesysbrevkoder.Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Forespørsel om dokumentasjon av botid i Norge - alder",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Forespørsel om dokumentasjon av botid i Norge"
            )
        }
        outline {
            //[PE_IY_03_167_tekst]

            paragraph {
                text(
                    Bokmal to "Vi ber med dette om å få tilsendt oversikt over botiden (kopi av "
                )
                text(Bokmal to "hovedregisterkort/navnekort", fontType = FontType.BOLD)
                textExpr(
                    Bokmal to " e.l) for ".expr() + felles.bruker.fulltNavn() + " med fødselsnummer " + felles.bruker.foedselsnummer.value + ". "
                            + fritekst("Vedkommende har opplyst at han/hun var sist bosatt i deres kommune fra mm.dd.år til mm.dd.år.")
                )
            }

            paragraph {
                text(Bokmal to "All", fontType = FontType.BOLD)
                text(Bokmal to " botid i Norge bes oppgitt.")
            }
            //[PE_IY_03_167_tekst]

            paragraph {
                text(Bokmal to "På forhånd takk for hjelpen.")
            }
        }
    }
}

