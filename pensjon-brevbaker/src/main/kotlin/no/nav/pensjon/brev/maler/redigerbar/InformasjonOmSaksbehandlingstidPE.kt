package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDtoSelectors.SaksbehandlerValgSelectors.soeknadMottattFraUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDtoSelectors.SaksbehandlerValgSelectors.venterPaaSvarAFP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidPeDtoSelectors.saksbehandlerValg
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
object InformasjonOmSaksbehandlingstidPE : RedigerbarTemplate<InformasjonOmSaksbehandlingstidPeDto> {

    // MF_000130 (AP_INFO_STID_MAN)
    override val kode = Pesysbrevkoder.Redigerbar.PE_INFORMASJON_OM_SAKSBEHANDLINGSTID
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InformasjonOmSaksbehandlingstidPeDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om saksbehandlingstid",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
                text(
                    Bokmal to "Informasjon om saksbehandlingstiden vår",
                    Nynorsk to "Informasjon om saksbehandlingstida vår",
                    English to "Information about application processing time",
                )
            }
        outline {
            paragraph {
                val mottattDato = fritekst("dato")
                val ytelse = fritekst("ytelse")

                showIf(saksbehandlerValg.soeknadMottattFraUtland) {
                    val annetLand = fritekst("land")
                    textExpr(
                        Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om " + ytelse + " fra trygdemyndighetene i " + annetLand + ".",
                        Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om " + ytelse + " frå trygdeorgana i " + annetLand + ".",
                        English to "We received your application for ".expr() + ytelse + " from the National Insurance authorities in " + annetLand +
                                " on " + mottattDato + "."
                    )
                } orShow {
                        textExpr(
                            Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om " + ytelse + " fra folketrygden.",
                            Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om " + ytelse + " frå folketrygda.",
                            English to "We received your application for ".expr() + ytelse + " from the Norwegian National Insurance Scheme on " + mottattDato + ".",
                        )
                    }
                }
            showIf(saksbehandlerValg.venterPaaSvarAFP) {
                paragraph {
                    val uttaksDato = fritekst("uttaksdato")
                    val prosent = fritekst("uttaksgrad alderspensjon")
                    textExpr(
                        Bokmal to "Du har ikke høy nok opptjening til å ta ut ".expr() + prosent + " prosent alderspensjon fra " + uttaksDato
                                + ". Eventuelle AFP-rettigheter vil kunne gi deg rett til uttak av alderspensjon.",
                        Nynorsk to "Du har ikkje høg nok pensjonsopptjening til å ta ut ".expr() + prosent + " prosent alderspensjon frå " + uttaksDato
                                + ". Eventuelle AFP-rettar vil kunne gi deg rett til uttak av alderspensjon.",
                        English to "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() + prosent + " percent from " + uttaksDato
                                + ". If you are entitled to a contractual early retirement pension (AFP) in the private sector, you might be entitled to draw a retirement pension.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi vil behandle søknaden din når vi har mottatt informasjon fra Fellesordningen om dine AFP-rettigheter. Det tar normalt fire til seks uker før du får svar fra dem på søknaden din. " +
                                "Dette forutsetter at du har sendt inn søknad om AFP i privat sektor til Fellesordningen.",
                        Nynorsk to "Vi vil behandle søknaden din når vi har mottatt informasjon frå Fellesordningen om dine AFP-rettar. Det tek normalt fire til seks veker før du får svar frå dei på søknaden din. " +
                                "Dette føresett at du har sendt inn søknad om AFP i privat sektor til Fellesordningen.",
                        English to "We will process your application once we have received the necessary information from Fellesordningen about your AFP rights. Normally it takes four to six weeks for you to get a reply from them on your application. " +
                                "This requires that you have submitted an application for AFP to Fellesordningen.",
                    )
                }
            }
                paragraph {
                    val svartid = fritekst("svartid")
                    textExpr(
                        Bokmal to "Saksbehandlingstiden vår er vanligvis ".expr() + svartid + ".",
                        Nynorsk to "Saksbehandlingstida vår er vanlegvis ".expr() + svartid + ".",
                        English to "Our processing time for this type of application is usually ".expr() + svartid + ".",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss.",
                        Nynorsk to "Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss.",
                        English to "We will contact you if we need you to provide more information."
                    )
                }
            includePhrase(Felles.MeldeFraEndringer)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}