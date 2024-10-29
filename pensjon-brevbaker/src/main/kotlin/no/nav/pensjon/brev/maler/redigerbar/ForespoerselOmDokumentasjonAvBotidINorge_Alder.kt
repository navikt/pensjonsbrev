package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.template.Element
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
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.saksnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.value
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object ForespoerselOmDokumentasjonAvBotidINorge_Alder : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = Brevkode.Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Forespørsel om dokumentasjon av botid i Norge",
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
                    Bokmal to "Navn:",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "				".expr() + felles.bruker.fulltNavn()
                )
            }
            paragraph {
                text(
                    Bokmal to "Fødselsdato:",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "			".expr() + felles.bruker.foedselsnummer.value
                )
            }
            paragraph {
                text(
                    Bokmal to "Saksnummer:",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                textExpr(
                    Bokmal to "			".expr() + felles.saksnummer,
                )
            }

            //[PE_IY_03_167_tekst]

            paragraph {
                text(
                    Bokmal to "Vi ber med dette om å få tilsendt oversikt over botiden (kopi av "
                )
                text(
                    Bokmal to "hovedregisterkort/navnekort",
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    Bokmal to " e.l) for ovennevnte person. <Fritekst: Vedkommende har opplyst at han/hun var sist bosatt i deres kommune fra <fritekst: mm.dd.år> til <<fritekst: mm.dd.år>."
                )
            }

            paragraph {
                text(
                    Bokmal to "All",
                    fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                )
                text(
                    Bokmal to " botid i Norge bes oppgitt.",
                )
            }
            //[PE_IY_03_167_tekst]

            paragraph {
                text(
                    Bokmal to "På forhånd takk for hjelpen.",
                )
            }
        }
    }
}

