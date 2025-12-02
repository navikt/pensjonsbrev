package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDtoSelectors.SaksbehandlerValgSelectors.soeknadOversendesTilUtlandet
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object OrienteringOmSaksbehandlingstid : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    // PE_UP_07_105
    override val kode = Pesysbrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid (uføretrygd)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            //[PE_UP_07_105_overskrift]
            text(
                bokmal { + "Orientering om svartid" },
                nynorsk { + "Orientering om svartid" },
            )
        }

        outline {
            //[PE_UP_07_105_TB3018-3021,TB124]

            paragraph {
                val mottattDato = fritekst("dato")
                text(
                    bokmal { + "Vi har " + mottattDato + " mottatt søknaden din om uføretrygd." },
                    nynorsk { + "Vi har " + mottattDato + " mottatt søknaden din om uførepensjon." },
                )
            }

            paragraph {
                text(
                    bokmal { + "Søknaden vil bli behandlet så snart som mulig og senest innen 4 måneder. " },
                    nynorsk { + "Søknaden vil bli behandla så snart som mogleg og seinast innan 4 månader. " },
                )
            }

            paragraph {
                text(
                    bokmal { + "Hvis søknaden ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss. " },
                    nynorsk { + "Viss søknaden ikkje vert avgjort i løpet av denne tida, vil du høyre nærmare frå oss. " },
                )
            }

            showIf(saksbehandlerValg.soeknadOversendesTilUtlandet) {
                paragraph {
                    text(
                        bokmal { + "Søknaden din vil også bli oversendt utlandet fordi du har opplyst at du har bodd/arbeidet i et land Norge har trygdeavtale med." },
                        nynorsk { + "Søknaden din vil også bli send til utlandet fordi du har opplyst at du har budd/arbeidd i eit land Noreg har trygdeavtale med. " },
                    )
                }
            }

            title1 {
                text(
                    bokmal { + "Meld fra om endringer" },
                    nynorsk { + "Meld frå om endringar" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av søknaden din. Det kan være endringer i medisinske forhold, arbeid, inntekt, sivilstand og lignende. " },
                    nynorsk { + "Vi ber om at du held oss orientert om forhold som kan ha noko å seie for avgjerda av søknaden din. Det kan vere endringar i medisinske forhold, arbeid, inntekt, sivilstand og liknande. " },
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}