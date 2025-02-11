package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InformasjonOmForlengetSaksbehandlingstidUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    // PE_IY_05_041
    override val kode = Pesysbrevkoder.Redigerbar.UT_INFORMASJON_OM_FORLENGET_SAKSBEHANDLINGSTID
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = EmptyRedigerbarBrevdata::class,
            languages = languages(Bokmal, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon om forlenget saksbehandlingstid",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Informasjon om forlenget saksbehandlingstid",
                    English to "Information about application processing delay",
                )
            }
            outline {
                paragraph {
                    val dato = fritekst("dato")
                    val aarsak = fritekst("årsak til forsinkelse")

                    textExpr(
                        Bokmal to "Vi har ".expr() + dato + " mottatt din søknad om uføretrygd. " +
                            "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. " +
                            "Forsinkelsen skyldes ".expr() + aarsak + ".".expr(),
                        English to "We have received your application for disabilty benefit on the ".expr() + dato + ". " +
                            "Due to delays in ".expr() + aarsak + ", " +
                            "the processing of your case will take longer than we anticipated.".expr(),
                    )
                }
                title1 {
                    text(
                        Bokmal to "Ny svartid",
                        English to "New estimated date for completion",
                    )
                }
                paragraph {
                    val frist = fritekst("antall dager/uker/måneder")
                    textExpr(
                        Bokmal to "Vi antar at kravet ditt kan bli ferdigbehandlet innen ".expr() + frist + ".".expr(),
                        English to "Without further delays, we assume the processing of your case to be completed within ".expr() + frist + ".".expr(),
                    )
                }
                includePhrase(Felles.MeldeFraEndringer)
                includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
            }
        }
}
