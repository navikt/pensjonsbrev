package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object OrienteringOmSaksbehandlingstidRedigerbar : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    // PE_UP_07_105
    override val kode = Ufoerebrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid av søknad om uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
        letterDataType = OrienteringOmSaksbehandlingstidDto::class
    ) {
        val soeknadOversendesTilUtlandet = saksbehandlervalg("soeknadOversendesTilUtlandet", "Søknad oversendes til utlandet").bool()

        title {
            text(
                bokmal { +"Orientering om svartid" },
                nynorsk { +"Orientering om svartid" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi viser til søknaden din om uføretrygd som vi mottok " + fritekst("dato") + "." },
                    nynorsk { +"Vi viser til søknaden din om uføretrygd som vi tok imot " + fritekst("dato") + "." },
                )
            }
            includePhrase(OrienteringOmSaksbehandlingstid.SoknadenBehandles)
            showIf(soeknadOversendesTilUtlandet) {
                paragraph {
                    text(
                        bokmal { +"Søknaden din vil også bli oversendt utlandet fordi du har opplyst at du har bodd/arbeidet i et land Norge har trygdeavtale med." },
                        nynorsk { +"Søknaden din vil også bli send til utlandet fordi du har opplyst at du har budd/arbeidd i eit land Noreg har trygdeavtale med. " },
                    )
                }
            }
            includePhrase(OrienteringOmSaksbehandlingstid.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}