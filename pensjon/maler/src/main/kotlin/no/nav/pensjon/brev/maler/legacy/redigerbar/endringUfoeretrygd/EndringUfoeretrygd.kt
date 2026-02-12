package no.nav.pensjon.brev.maler.legacy.redigerbar.endringUfoeretrygd

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


/* IF PE_Vedtaksdata_Kravhode_KravArsakType <> 'tilst_dod'
AND NOT ( PE_Vedtaksdata_Kravhode_KravGjelder = "revurd"
AND   PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
AND  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2() )
THEN INCLUDE */

@TemplateModelHelpers
object EndringUfoeretrygd : RedigerbarTemplate<EndringUfoeretrygdDto> {

    // override val featuretoggle = FeatureToggles.brevmalUtEndring.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRING_UFOERETRYGD
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring av uføretrgd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            /* IF PE_Vedtaksdata_Kravhode_KravArsakType <> 'tilst_dod'
            AND NOT ( PE_Vedtaksdata_Kravhode_KravGjelder = "revurd"
            AND   PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
            AND  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2() )
            THEN INCLUDE */
            text(
                bokmal { +"Nav har endret uføretrygden din" },
                nynorsk { +"Nav har endret uføretrygden din" },
                english { +"Nav has changed your disability benefit" }
            )
            // IF PE_UT_KravLinjeKode_Og_PaaFolgende_bt_avsl() THEN INCLUDE
            text(
                bokmal { +"Nav har avslått søknaden din om barnetillegg" },
                nynorsk { +"Nav har avslått søknaden din om barnetillegg" },
                english { +"Nav has denied your application for child supplement" }
            )
            /* IF PE_Vedtaksdata_Kravhode_KravArsakType = "tilst_dod"
            AND NOT ( PE_Vedtaksdata_Kravhode_KravGjelder = "revurd"
            AND PE_Vedtaksdata_Kravhode_KravArsakType = "soknad_bt"
            AND  PE_UT_KravLinjeKode_Og_PaaFolkgende_bt_avslag_v2() )
            THEN INCLUDE */
            text(
                bokmal { +"Nav har regnet om uføretrygden din" },
                nynorsk { +"Nav har rekna om uføretrygda di" },
                english { +"Nav has altered your disability benefit" }
            )
        }
        outline {
includePhrase(KravArsakTypeUlikTilstDod.TBU2287til2297(pesysData.pe))

        }
    }
}