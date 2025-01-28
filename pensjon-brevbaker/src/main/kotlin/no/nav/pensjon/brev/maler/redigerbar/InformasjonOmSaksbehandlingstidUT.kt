package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDtoSelectors.SaksbehandlerValgSelectors.forlengetSaksbehandlingstid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidUtDtoSelectors.saksbehandlerValg
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
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InformasjonOmSaksbehandlingstidUT : RedigerbarTemplate<InformasjonOmSaksbehandlingstidUtDto> {

    // MF 000130 (AP_INFO_STID_MAN)
    override val kode = Pesysbrevkoder.Redigerbar.UT_INFORMASJON_OM_SAKSBEHANDLINGSTID
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InformasjonOmSaksbehandlingstidUtDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om saksbehandlingstid",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                text(
                    Bokmal to "Informasjon om forlenget saksbehandlingstid",
                    Nynorsk to "Informasjon om forlenget saksbehandlingstid",
                    English to "Information about application processing delay",
                )
            }.orShow {
                text(
                    Bokmal to "Informasjon om saksbehandlingstiden vår",
                    Nynorsk to "Informasjon om saksbehandlingstida vår",
                    English to "Information about our application processing time",
                )
            }
        }
        outline {
            paragraph {
                val mottattDato = fritekst("dato")

                showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                    val aarsak = fritekst("årsak til forsinkelse")
                    textExpr(
                        Bokmal to "Vi har ".expr() + mottattDato + " mottatt din søknad om uføretrygd. "
                                + "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. "
                                + "Forsinkelsen skyldes ".expr() + aarsak + ".".expr(),
                        Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om uføretrygd. "
                                + "Det vil dessverre ta oss lengre tid enn venta å handsame kravet. "
                                + "Forsinkinga skuldast ".expr() + aarsak + ".".expr(),
                        English to "We have received your application for disabilty benefit on the ".expr() + mottattDato + ". "
                                + "Due to delays in ".expr() + aarsak + ", "
                                + "the processing of your case will take longer than we anticipated.".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om uføretrygd fra folketrygden.",
                        Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om uføretrygd frå folketrygda.",
                        English to "We have received your application for disability benefit from the Norwegian National Insurance Scheme on ".expr() + mottattDato + ".",
                    )
                }
            }
            showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                title1 {
                    text(
                        Bokmal to "Ny svartid",
                        Nynorsk to "Ny svartid",
                        English to "New estimated date for completion",
                    )
                }
                paragraph {
                    showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                        val svartid = fritekst("svartid")
                        textExpr(
                            Bokmal to "Vi antar at kravet ditt kan bli ferdigbehandlet innen ".expr() + svartid + ".",
                            Nynorsk to "Vi reknar med at kravet ditt kan bli ferdigbehandla innan ".expr() + svartid + ".",
                            English to "Without further delays, we assume the processing of your case to be completed within ".expr() + svartid + "."
                        )
                    }.orShow {
                        val svartid = fritekst("svartid")
                        textExpr(
                            Bokmal to "Saksbehandlingstiden vår er vanligvis ".expr() + svartid + ".",
                            Nynorsk to "Saksbehandlingstida vår er vanlegvis ".expr() + svartid + ".",
                            English to "Our processing time is usually ".expr() + svartid + ".",
                        )
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss.",
                        Nynorsk to "Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss.",
                        English to "We will contact you if we need you to provide more information."
                    )
                }
                includePhrase(Felles.MeldeFraEndringer)
                includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
            }
        }
    }
}