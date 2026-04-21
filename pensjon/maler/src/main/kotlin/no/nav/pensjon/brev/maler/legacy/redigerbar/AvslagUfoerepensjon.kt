package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_UP_07_010 Vedtak - avslag av uførepensjon
//Brevgruppe 3

@TemplateModelHelpers
object AvslagUfoerepensjon : RedigerbarTemplate<AvslagUfoerepensjonDto> {

//override val featureToggle = FeatureToggles.brevmalAvslagUfoerepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UP_AVSLAG_UFOEREPENSJON
    override val kategori = Brevkategori.UFOEREPENSJON
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på ufoerepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Uførepensjon fra folketrygden - Melding om avslag" },
                nynorsk { +"Uførepensjon frå folketrygda - melding om avslag" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Kravet ditt av " + pesysData.kravMottattDato.format() + " om uførepensjon er avjort den " + fritekst("Oppgi vedtaksdato") + "." },
                    nynorsk { +"Kravet ditt av " + pesysData.kravMottattDato.format() + " om uførepensjon er avjort den " + fritekst("Oppgi vedtaksdato") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Kravet er avslått, fordi vilkårene for å få uførepensjon ikke er oppfylt i ditt tilfelle." },
                    nynorsk { +"Kravet er avslått, fordi vilkåra for å få uførepensjon ikkje er oppfylte i ditt tilfelle." }
                )
            }
            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" }
                )
            }
            paragraph {
                text(
                    bokmal { +fritekst("Vedtaket er gjort etter folketrygdloven paragraf") },
                    nynorsk { +fritekst("Vedtaket er gjort etter folketrygdlova paragraf") }
                )
            }
            paragraph {
                text(
                    bokmal { +"Det er i alt fem vilkår som må være oppfylt for at du kan få uførepensjon:" },
                    nynorsk { +"Det er i alt fem vilkår som må vere oppfylte for at du kan få uførepensjon" }
                )
                list {
                    item {
                        text(
                            bokmal { +"du må som hovedregel ha vært medlem av folketrygden (bodd i Norge) de siste tre årene før du ble ufør. I visse tilfeller kan det gjøres unntak fra denne regelen" },
                            nynorsk { +"du må som hovudregel ha vore medlem i folketrygda (budd i Noreg) dei siste tre åra før du blei ufør. I visse høve kan det gjerast unntak frå denne regelen" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du må være mellom 18 og 67 år" },
                            nynorsk { +"du må vere mellom 18 og 67 år" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du må ha gjennomgått hensiktsmessig behandling og individuelle og hensiktsmessige arbeidsrettede tiltak" },
                            nynorsk { +"du må ha gjennomgått formalstenleg behandling og individuelle og formålstenlege arbeidsretta tiltak" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"du må ha varig sykdom, skade eller lyte, og den medisinske lidelsen må være hovedårsaken til nedsettelsen av arbeids- og inntektsevnen" },
                            nynorsk { +"du må ha varig sjukdom, skade eller lyte, og den medisinske lidinga må vere hovudårsaka til at arbeidsevna eller inntektsevna di er nedsett" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"inntekts- eller arbeidsevnen din må være varig nedsatt med minst halvparten" },
                            nynorsk { +"inntekts- eller arbeidsevna di må vere varig nedsett med minst halvparten" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du fyller ikke vilkåret/vilkårene:" },
                    nynorsk { +"" },
                )
                newline()
                text(
                    bokmal { + fritekst("Fjern alternativ som ikke passer") },
                    nynorsk { +"" }
                )
            }
        }

    }
}