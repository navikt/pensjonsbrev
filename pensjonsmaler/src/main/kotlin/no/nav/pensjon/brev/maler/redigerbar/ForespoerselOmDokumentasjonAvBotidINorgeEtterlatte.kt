package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDtoSelectors.SaksbehandlerValgSelectors.opplystOmBotid
import no.nav.pensjon.brev.api.model.maler.redigerbar.ForespoerselOmDokumentasjonAvBotidINorgeDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.model.LetterMetadataPensjon
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object ForespoerselOmDokumentasjonAvBotidINorgeEtterlatte : RedigerbarTemplate<ForespoerselOmDokumentasjonAvBotidINorgeDto> {

    // PE_IY_03_167 - dette er delen for barnep eller gjenlev
    override val kode = Pesysbrevkoder.Redigerbar.PE_FORESPOERSEL_DOKUM_BOTIDINORGE_ETTERLATTE
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.BARNEP, Sakstype.GJENLEV)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ForespoerselOmDokumentasjonAvBotidINorgeDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadataPensjon(
            displayTitle = "Forespørsel om dokumentasjon av botid i Norge - barnepensjon eller gjenlevendepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Forespørsel om dokumentasjon av botid i Norge",
            )
        }
        outline {
            //IF(PE_SaksData_Sakstype = "gjenlev" OR PE_SaksData_Sakstype = "barnep") THEN      INCLUDE ENDIF
            paragraph {
                text(
                    Bokmal to "Vi ber med dette om å få tilsendt oversikt over botiden (kopi av ",
                )
                text(Bokmal to "hovedregisterkort/navnekort", fontType = BOLD)
                textExpr(
                    Bokmal to " e.l) for ".expr() + fritekst("avdødes navn") + " med fødselsnummer " + fritekst("avdødes fødselsnummer") + ". ",
                )
                showIf(saksbehandlerValg.opplystOmBotid) {
                    val dato = fritekst("mm.dd.år")
                    textExpr(
                        Bokmal to "Det er opplyst om at ".expr() + fritekst("han/hun") + " var sist bosatt i deres kommune fra " + dato + " til " + dato + ".",
                    )
                }
            }
            //[PE_IY_03_167_tekst]

            paragraph {
                text(
                    Bokmal to "All",
                    BOLD,
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

