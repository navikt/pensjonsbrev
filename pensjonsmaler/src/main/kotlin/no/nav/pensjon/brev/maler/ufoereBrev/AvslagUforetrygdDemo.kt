package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDto
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

object AvslagUforetrygdDemo : RedigerbarTemplate<AvslagUforetrygdDemoDto> {

    override val kode = Pesysbrevkoder.Redigerbar.UT_AVSLAG_UFOERETRYGD_DEMO
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        name = "Avslag uføretrygd demo",
        letterDataType = AvslagUforetrygdDemoDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd demo",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text(Bokmal to "Du har fått avslag på uføretrygd demo")
        }
        outline {
            paragraph {
                text(Bokmal to "Det er veldig synd, og du har vår dypeste sympati. Under følger begrunnelsen for avslaget")
                fritekst( "Begrunnelse for avslaget")
            }
        }
    }
}