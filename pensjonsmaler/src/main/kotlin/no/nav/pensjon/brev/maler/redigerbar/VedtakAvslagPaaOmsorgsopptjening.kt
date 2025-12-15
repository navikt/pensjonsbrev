package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.PesysDataSelectors.navEnhet
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidEtter69Aar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.omsorgsarbeidFoer1992
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_010

@TemplateModelHelpers
object VedtakAvslagPaaOmsorgsopptjening : RedigerbarTemplate<VedtakAvslagPaaOmsorgsopptjeningDto> {

    //override val featureToggle = FeatureToggles.vedtakOmInnvilgelseAvOmsorgspoeng.toggle
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_AVSLAG_PAA_OMSORGSOPPTJENING
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på omsorgsopptjening",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du har ikke rett til omsorgsopptjening" },
                nynorsk { +"" },
                english { +"" }
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            paragraph {
                text(
                    bokmal { +pesysData.navEnhet + " viser til søknaden din om å få godskrevet pensjonsopptjening for <Fritekst: pleie- og omsorgsarbeid/omsorg for barn år " },
                    nynorsk { +"" },
                    english { +"" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Søknaden er avslått, fordi vilkårene for å få omsorgsopptjening ikke er oppfylt i ditt tilfelle." },
                    nynorsk { +"" },
                    english { +"" }
                )
            }
            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"" },
                    english { +"" }
                )
            }
            paragraph {
                val hjemmel = fritekst(
                    "Velg et alternativ eller fyller inn tekst selv. "
                            + "Vær oppmerksom på at hjemmelen for avslag kan være § 3-16 og tilhørende forskrift, "
                            + "men også § 20-21 (for personer født etter 1953 og opptjeningsår før 2009) "
                            + "eller § 20-8 (for personer født etter 1953 og opptjeningsår fom 2010). "
                            + "Benyttes hjemlene i kapittel 20 skal OMSORGSPOENG erstattes med OMSORGSOPPTJENING. "
                            + "Hjemmelen kan også være AFP-tilskottsloven hvis vedtaket gjelder pensjonsopptjening for omsorg for barn under 7 år før 1992."
                )
            }
            showIf(saksbehandlerValg.omsorgsarbeidFoer1992) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskriften til folketrygdloven paragraf 3-16 fjerdeledd om godskriving av pensjonspoeng (omsorgspoeng) for omsorgsarbeid for en syk, en funksjonshemmet eller en eldre person." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Ordningen med pensjonsopptjening for omsorgsarbeid ble innført fra og med 1992. Du kan ikke få godskrevet opptjening for omsorgsarbeid foe en syk funksjonshemmet eller eldre person som er utført før 1992." },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
            }
            showIf(saksbehandlerValg.omsorgsarbeidEtter69Aar) {
                paragraph {
                    text(
                        bokmal { +"" },
                        nynorsk { +"" },
                        english { +"" }
                    )
                }
            }
        }
    }
}