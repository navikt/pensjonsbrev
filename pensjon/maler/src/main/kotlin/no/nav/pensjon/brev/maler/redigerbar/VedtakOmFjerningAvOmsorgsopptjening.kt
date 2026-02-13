package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDtoSelectors.SaksbehandlerValgSelectors.aktuelleAar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmFjerningAvOmsorgsopptjening : RedigerbarTemplate<VedtakOmFjerningAvOmsorgsopptjeningDto> {

    override val featureToggle = FeatureToggles.vedtakOmFjerningAvOmsorgspoeng.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_OM_FJERNING_AV_OMSORGSOPPTJENING
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon // Litt usikker på denne

    override val template = createTemplate(
        languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - fjerning av omsorgsopptjening",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Omsorgsopptjening for " + saksbehandlerValg.aktuelleAar + " er fjernet" },
                english { + "Care credits for " + saksbehandlerValg.aktuelleAar + " has been removed" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har tidligere fått godskrevet omsorgsopptjening for " + saksbehandlerValg.aktuelleAar + ". Ved en gjennomgang av saken din har vi blitt oppmerksomme på at du ikke oppfyller vilkårene for rett til omsorgsopptjening for " + saksbehandlerValg.aktuelleAar + "." },
                    english { + "You were previously credited with care credits for " + saksbehandlerValg.aktuelleAar + ". Upon reviewing your case, we have become aware that you do not meet the conditions for entitlement to care credits for " + saksbehandlerValg.aktuelleAar + "." }
                )
            }
            includePhrase(Vedtak.BegrunnelseOverskrift)
            paragraph {
                text(
                    bokmal { + "For å ha rett til omsorgsopptjening er det blant annet et krav at " + fritekst("omsorgsyteren har vært medlem i folketrygden i minst 6 måneder i det aktuelle året / angi evt. annet relevant vilkår") + ". Du har ikke rett til omsorgsopptjening for " + saksbehandlerValg.aktuelleAar + " fordi " + fritekst("du ikke var medlem i folketrygden i minst seks måneder / angi evt. annen årsak") + ". Vedtaket er gjort etter folketrygdloven §§ 3-16 og 20-8." },
                    english { + "To be entitled to care credits, it is required, among other things, that " + fritekst("the caregiver has been a member of the National Insurance Scheme for at least 6 months in the relevant year / specify any other relevant condition") + ". You are not entitled to care credits for " + saksbehandlerValg.aktuelleAar + " because " + fritekst("you were not a member of the National Insurance Scheme for at least six months / specify any other reason") + ". The decision is made pursuant to the National Insurance Act §§ 3-16 and 20-8." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du ble godskrevet omsorgsopptjening ved en feil. Vi har derfor gjort om vedtaket etter Forvaltningsloven § 35 første ledd bokstav c." },
                    english { + "You were credited with care credits by mistake. We have therefore reversed the decision pursuant to the Public Administration Act § 35 first paragraph letter c." }
                )
            }
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}