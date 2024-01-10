package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.InkluderVenterSvarAFPSelectors.uttakAlderspensjonProsent
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.InkluderVenterSvarAFPSelectors.uttaksDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.inkluderVenterSvarAFP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.land
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.mottattSoeknad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.svartidUker
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDtoSelectors.ytelse
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InformasjonOmSaksbehandlingstid : RedigerbarTemplate<InformasjonOmSaksbehandlingstidDto> {
    override val kode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID

    override val template = createTemplate(
        name = kode.name,
        letterDataType = InformasjonOmSaksbehandlingstidDto::class,
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
                val mottattDato = mottattSoeknad.format()
                ifNotNull(land) { land ->
                    textExpr(
                        Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om " + ytelse + " fra trygdemyndighetene i " + land + ".",
                        Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om " + ytelse + " fra trygdeorgana i " + land + ".",
                        English to "We received your application for ".expr() + ytelse + " from the National Insurance authorities in " + land +
                                " on " + mottattDato + "."
                    )
                } orShow {
                    textExpr(
                        Bokmal to "Vi har ".expr() + mottattDato + " mottatt søknaden din om " + ytelse + " fra folketrygden.",
                        Nynorsk to "Vi har ".expr() + mottattDato + " fått søknaden din om " + ytelse + " frå folketrygda.",
                        English to "We received your application for ".expr() + ytelse + " from the Norwegian National Insurance Scheme on " + mottattDato + ".",
                    )
                }

                // TODO: Midlertidig liste slik at frontend har noe å eksperimentere med
                list {
                    item {
                        text(Bokmal to "punkt 1 - TODO fjern punktliste", Nynorsk to "punkt 1 - TODO fjern punktliste", English to "item 1 - TODO remove itemlist")
                    }
                    item {
                        text(Bokmal to "punkt 2 - TODO fjern punktliste", Nynorsk to "punkt 2 - TODO fjern punktliste", English to "item 2 - TODO remove itemlist")
                    }
                    item {
                        text(Bokmal to "punkt 3 - TODO fjern punktliste", Nynorsk to "punkt 3 - TODO fjern punktliste", English to "item 3 - TODO remove itemlist")
                    }
                }
            }
            ifNotNull(inkluderVenterSvarAFP) {
                val prosent = it.uttakAlderspensjonProsent.format()
                val uttaksDato = it.uttaksDato.format()
                paragraph {
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
                textExpr(
                    Bokmal to "Saksbehandlingstiden vår er vanligvis ".expr() + svartidUker.format() + " uker.",
                    Nynorsk to "Saksbehandlingstida vår er vanlegvis ".expr() + svartidUker.format() + " uker.",
                    English to "Our processing time for this type of application is usually ".expr() + svartidUker.format() + " weeks.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss.",
                    Nynorsk to "Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss.",
                    English to "We will contact you if we need you to provide more information."
                )
            }

            title1 {
                text(
                    Bokmal to "Meld fra om endringer",
                    Nynorsk to "Meld frå om endringar",
                    English to "Duty to report changes",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må melde fra til oss med en gang hvis det skjer endringer som kan ha betydning for saken din, for eksempel ved endring av sivilstand eller ved flytting.",
                    Nynorsk to "Du må melde frå til oss med ein gong dersom det skjer endringar som kan ha noko å seie for saka di, for eksempel ved endring av sivilstand eller ved flytting.",
                    English to "You must notify us immediately if there are any changes that may affect your case, such as a change in your marital status or if you move."
                )
            }
        }
    }
}