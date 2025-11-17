package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.omsorgsopptjeningsaar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.omsorgspersonNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_IY_04_001_vedtak_innvilgelse_omsorgspoeng

@TemplateModelHelpers
object VedtakOmInnvilgelseAvOmsorgspoeng : RedigerbarTemplate<VedtakOmInnvilgelseAvOmsorgspoengDto> {

    //override val featureToggle = FeatureToggles.
    override val kode = Pesysbrevkoder.Redigerbar.PE_VEDTAK_OM_INNVILGELSE_AV_OMSORGSPOENG
    override val kategori = TemplateDescription.Brevkategori.OMSORGSOPPTJENING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.OMSORG)

    override val template = createTemplate(
        languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - fjerning av omsorgsopptjening",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du har rett til pensjonsopptjening" },
                english { +"Crediting of acquired rights for care work - notification of decision" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Nav har godkjent at du få omsorgsopptjening for " + pesysData.omsorgsopptjeningsaar + "." },
                    english { +"Nav has decided that you should be credited with acquired rights for care work in " + pesysData.omsorgsopptjeningsaar + "." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi har lagt til grunn at omsorgsarbeidet for " + pesysData.omsorgspersonNavn +" er minst 22 timer per uke i minst seks måneder i " + pesysData.omsorgsopptjeningsaar + "." },
                    english {+""}
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter folketrygdloven § 3-16 første ledd bokstav b, og §§ 20-8 første ledd bokstav b og 20-21 hvis du er født etter 1953."
                    },
                    english {
                        +"The decision has been made in accordance with section 3-16, first paragraph, letter b of the National Insurance Act, and sections 20-8 first paragraph, "
                        +"letter b and 20-21 of the National Insurance Act. "
                        +"The basis of the decision is that the care provided for " + pesysData.omsorgspersonNavn + " has amounted to at least 22 hours per week, "
                        +"and that the duration of the care was at least six months in " + pesysData.omsorgsopptjeningsaar + "."
                    }
                )
            }
            title1 {
                text(
                    bokmal { +"Hva er omsorgsopptjening?" },
                    english { +"What are acquired rights for care work?" }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Omsorgsopptjening gir deg pensjonsopptjening 4,5 ganger folketrygdens grunnbeløp for det aktuelle året. "
                    },
                    english { +"" }
                )
                text(
                    bokmal { +"Det kan gi en høyere pensjon enn du ellers ville hatt. " },
                    english { +"" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Omsorgsopptjening kan ikke kombineres med annen opptjening. " },
                    english { +"" }
                )
                text(
                    bokmal { +"Har du annen pensjonsopptjening i det aktuelle året som er høyere enn omsorgsopptjeningen, vil den opptjeningen legges til grunn." },
                    english { +"" }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"På nav.no/din-pensjon får du oversikt over hele pensjonsopptjeningen din. "
                        +"Her finner du også omsorgsopptjeningen som du har godkjent."
                    },
                    english {
                        +"Nav has an online pension service Din Pensjon on nav.no which shows your pensionable income and how much pension you have accumulated. "
                        +"On this page you can also find details of the aquired rights you have been accredited for care work."
                    }
                )
            }
            includePhrase(Felles.HarDuSpoersmaalBokmalEnglish.omsorg)
        }
    }
}


