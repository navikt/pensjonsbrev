package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.omsorgsopptjeningsaar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.omsorgspersonNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL_INNLOGGET
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_001_vedtak_innvilgelse_omsorgspoeng

@TemplateModelHelpers
object VedtakOmInnvilgelseAvOmsorgspoeng : RedigerbarTemplate<VedtakOmInnvilgelseAvOmsorgspoengDto> {

    override val featureToggle = FeatureToggles.vedtakOmInnvilgelseAvOmsorgspoeng.toggle
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_OM_INNVILGELSE_AV_OMSORGSPOENG
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av omsorgsopptjening",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du har rett til pensjonsopptjening" },
                nynorsk { +"Du har rett til pensjonsopptening " },
                english { +"You are entitled to pension accrual " }
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            paragraph {
                text(
                    bokmal { +"Nav har godkjent at du får omsorgsopptjening for " + pesysData.omsorgsopptjeningsaar + "." },
                    nynorsk { +"Nav har godkjent at du får omsorgsopptening for " + pesysData.omsorgsopptjeningsaar + "." },
                    english { +"Nav has approved that you will be credited with acquired rights for care work in " + pesysData.omsorgsopptjeningsaar + "." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har lagt til grunn at omsorgsarbeidet for " + pesysData.omsorgspersonNavn + " er minst 22 timer per uke i minst seks måneder i " + pesysData.omsorgsopptjeningsaar + "." },
                    nynorsk { +"Vi har lagt til grunn at omsorgsarbeidet for " + pesysData.omsorgspersonNavn + " er minst 22 timar per veke i minst seks månader i " + pesysData.omsorgsopptjeningsaar + "." },
                    english { +"We have assumed that the care work for " + pesysData.omsorgspersonNavn + " is at least 22 hours per week for at least six months in " + pesysData.omsorgsopptjeningsaar + "." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven § 3-16 første ledd bokstav b, og §§ 20-8 første ledd bokstav b og 20-21." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova § 3‑16 første ledd bokstav b, og §§ 20‑8 første ledd bokstav b og 20‑21." },
                    english { +"The decision has been made persuant to the National Insurance Act § 3-16, first paragraph, letter b, and §§ 20-8, first paragraph, letter b, and 20-21."
                    }
                )
            }
            title1 {
                text(
                    bokmal { +"Hva er omsorgsopptjening?" },
                    nynorsk { +"Kva er omsorgsopptening?" },
                    english { +"What are acquired rights for care work?" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Omsorgsopptjening gir deg pensjonsopptjening på 4,5 ganger folketrygdens grunnbeløp for det aktuelle året. " },
                    nynorsk { +"Omsorgsopptening gir deg pensjonsopptening på 4,5 gonger grunnbeløpet i folketrygda for det aktuelle året. " },
                    english { +"Acquired rights for care work give you a pension accrual of 4.5 times the National Insurance basic amount. " }
                )
                text(
                    bokmal { +"Det kan gi en høyere pensjon enn du ellers ville hatt. " },
                    nynorsk { +"Det kan gi ein høgare pensjon enn du elles ville hatt. " },
                    english { +"It may give you a higher pension than you otherwise would have had." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Omsorgsopptjening kan ikke kombineres med annen opptjening. " },
                    nynorsk { +"Omsorgsopptening kan ikkje kombinerast med anna opptening. " },
                    english { +"Acquired rights for care work cannot be combined with other pension accrual. " }
                )
                text(
                    bokmal { +"Har du annen pensjonsopptjening i det aktuelle året som er høyere enn omsorgsopptjeningen, vil den opptjeningen legges til grunn." },
                    nynorsk { +"Har du anna pensjonsopptening i det aktuelle året som er høgare enn omsorgsoppteninga, legg vi den oppteninga til grunn. " },
                    english { +"If you have other pension accrual in the relevant year that is higher than the care rights, that higher accrual will be applied instead. " }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"På $DIN_PENSJON_URL_INNLOGGET får du oversikt over hele pensjonsopptjeningen din. "
                        +"Her finner du også omsorgsopptjeningen du har fått godkjent."
                    },
                    nynorsk {
                        +"På $DIN_PENSJON_URL_INNLOGGET får du oversikt over heile pensjonsoppteninga di. "
                        +"Her finn du også omsorgsoppteninga du har fått godkjent."
                    },
                    english {
                        +"At $DIN_PENSJON_URL_INNLOGGET you get an overview of your entire pension accrual. Here you will also find the care rights that you have been credited."
                    }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal.omsorg)
        }
    }
}


