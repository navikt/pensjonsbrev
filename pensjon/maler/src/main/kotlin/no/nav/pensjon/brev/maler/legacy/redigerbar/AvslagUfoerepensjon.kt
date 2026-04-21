package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
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
                    nynorsk { +"Du fyller ikkje vilkåret/vilkåa:" },
                )
                newline()
                text(
                    bokmal { + "<Fjern alternativ som ikke passer>" },
                    nynorsk { +"<Fjern alternativ som ikke passer>" }
                )
                list {
                    item {
                        text(
                            bokmal { +"om forutgående medlemskap" },
                            nynorsk { +"om tidlegare medlemskap" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om å ha gjennomgått hensiktsmessig behandling" },
                            nynorsk { +"om å ha gjennomgått formålstenleg behandling" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om å ha gjennomført individuelle og hensiktsmessige arbeidsrettede tiltak" },
                            nynorsk { +"om å ha gjennomført individuelle og formålstenlege arbeidsretta tiltak" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om å ha en varig sykdom" },
                            nynorsk { +"om å ha ein varig sjukdom" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om at sykdommen er hovedårsaken til den nedsatte inntektsevnen" },
                            nynorsk { +"om at sjukdommen er hovudårsaka til den nedsette inntektsevna" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om at inntektsevnen er varig nedsatt med minst 50 prosent" },
                            nynorsk { +"om at inntektsevna er varig nedsett med minst 50 prosent" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +fritekst("Oppgi konkret grunngiving") },
                    nynorsk { +fritekst("Oppgi konkret grunngiving") }
                )
            }
            title1 {
                text(
                    bokmal { +"Du kan klage på vedtaket" },
                    nynorsk { +"Du kan klage på vedtaket" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. "
                        + "Bruk gjerne skjemaet som du finner på $KLAGE_URL. Trenger du hjelp, er du velkommen til å ringe oss på telefon $KONTAKT_URL" },
                    nynorsk { +"Du kan klage på vedtaket innan seks veker frå du mottok det. Klagen må vere skriftleg og innehalde namn, fødselsnummer og adresse. "
                    + "Bruk gjerne skjemaet som du finn på $KLAGE_URL. Treng du hjelp, er du velkommen til å ringe oss på telefon $KONTAKT_URL."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Sammen med dette brevet sender vi deg også en orientering om klage- og ankebehandling." },
                    nynorsk { +"Saman med dette brevet sender vi deg også ei orientering om klage- og ankebehandling." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi gjør oppmerksom på at du etter forvaltningsloven paragraf 18 har rett til å se sakens dokumenter." },
                    nynorsk { +"Vi gjer merksam på at du etter forvaltningslova paragraf 18 har rett til å sjå saksdokumenta." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Husk at du kan finne mer generell informasjon om regelverket og rettighetene dine på $NAV_URL." },
                    nynorsk { +"Hugs at du kan finne meir generell informasjon om regelverket og rettane dine på $NAV_URL." }
                )
            }
        }
    }
}