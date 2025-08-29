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
import no.nav.pensjon.brev.template.dsl.text
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
            displayTitle = "Informasjon om saksbehandlingstid uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                text(
                    bokmal { + "Informasjon om forlenget saksbehandlingstid" },
                    nynorsk { + "Informasjon om forlenget saksbehandlingstid" },
                    english { + "Information about application processing delay" },
                )
            }.orShow {
                text(
                    bokmal { + "Informasjon om saksbehandlingstiden vår" },
                    nynorsk { + "Informasjon om saksbehandlingstida vår" },
                    english { + "Information about our application processing time" },
                )
            }
        }
        outline {
            paragraph {
                val mottattDato = fritekst("dato")

                showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                    val aarsak = fritekst("årsak til forsinkelse")
                    text(
                        bokmal { + "Vi har " + mottattDato + " mottatt din søknad om uføretrygd. "
                                + "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. "
                                + "Forsinkelsen skyldes " + aarsak + "." },
                        nynorsk { + "Vi har " + mottattDato + " fått søknaden din om uføretrygd. "
                                + "Det vil dessverre ta oss lengre tid enn venta å behandle kravet. "
                                + "Forsinkinga skuldast " + aarsak + "." },
                        english { + "We have received your application for disabilty benefit on the " + mottattDato + ". "
                                + "Due to delays in " + aarsak + ", "
                                + "the processing of your case will take longer than we anticipated." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Vi har " + mottattDato + " mottatt søknaden din om uføretrygd fra folketrygden." },
                        nynorsk { + "Vi har " + mottattDato + " fått søknaden din om uføretrygd frå folketrygda." },
                        english { + "We have received your application for disability benefit from the Norwegian National Insurance Scheme on " + mottattDato + "." },
                    )
                }
            }
            showIf(saksbehandlerValg.forlengetSaksbehandlingstid) {
                title1 {
                    text(
                        bokmal { + "Ny svartid" },
                        nynorsk { + "Ny svartid" },
                        english { + "New estimated date for completion" },
                    )
                }
                paragraph {
                    val svartid = fritekst("svartid")
                    text(
                        bokmal { + "Vi antar at kravet ditt kan bli ferdigbehandlet innen " + svartid + "." },
                        nynorsk { + "Vi reknar med at kravet ditt kan bli ferdigbehandla innan " + svartid + "." },
                        english { + "Without further delays, we assume the processing of your case to be completed within " + svartid + "." }
                    )
                }
            }.orShow {
                paragraph {
                    val svartid = fritekst("svartid")
                    text(
                        bokmal { + "Saksbehandlingstiden vår er vanligvis " + svartid + "." },
                        nynorsk { + "Saksbehandlingstida vår er vanlegvis " + svartid + "." },
                        english { + "Our processing time is usually " + svartid + "." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss." },
                    nynorsk { + "Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss." },
                    english { + "We will contact you if we need you to provide more information." }
                )
            }
            includePhrase(Felles.MeldeFraEndringer)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}



