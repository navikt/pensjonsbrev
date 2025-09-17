package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDtoSelectors.SaksbehandlervalgSelectors.vurdertUngUfor
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2Selectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object AvslagUforetrygdDemo : RedigerbarTemplate<AvslagUforetrygdDemoDto> {

    override val featureToggle = FeatureToggles.utAvslagUforetrygdDemo.toggle

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
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { + "Vi fikk din søknad om uføretrygd den 20. august 2025, vi har avslått søknaden"})
            }
            title1 {
                text(bokmal { + "Derfor får du ikke uføretrygd"})
            }
            paragraph {
                text(bokmal { + "Vi avslår søknaden din fordi du ikke har gjennomført all hensiktsmessig behandling, som kan bedre inntektsevnen din."})
            }
            paragraph {
                text(bokmal { + "Funksjonsnedsettelsen vises i form av X. Følgene av dette er at du ikke klarer å fungere i arbeid. " +
                        "Det er forsøkt behandling i form av X. Det er for tidlig å ta stilling til effekten av behandlingen "})
                fritekst("fordi du fortsatt er under behandling/det finnes behandling som ikke er forsøkt.")
            }
            paragraph {
                text(bokmal {+ "Fastlegen din vurderer at videre behandling i form av"})
                fritekst("X")
                text(bokmal {+ "kan bedre inntektsevnen din. ROL/spesialist vurderer"})
                fritekst("X")
            }
            paragraph {
                text(bokmal {+ "Vi mener at du ikke har fått all hensiktsmessig behandling som kan bedre inntektsevnen. "})
                fritekst("Individuell begrunnelse, vær konkret, f.eks utredning, samarbeidende spesialist, " +
                        "anbefalt konkret behandling som ikke er forsøkt, våre retningslinjer ved spesielle sykdomstilstander, alder")
            }
            paragraph {
                text(bokmal {+ "Det kan ikke utelukkes at behandlingen kan bedre funksjons- og inntektsevnen. " +
                        "Samlet sett vurderer vi det som hensiktsmessig at behandlingen forsøkes. Fordi du har ikke fått " +
                        "all hensiktsmessig behandling, er det for tidlig å ta stilling til om hensiktsmessig arbeidsrettede tiltak er prøvd."})
            }
            paragraph {
                text(bokmal {+ "Vi kan derfor ikke vurdere om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt. " +
                        "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd."})
            }
            paragraph {
                text(bokmal {+ " Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7. "})
            }
        }
    }
}
