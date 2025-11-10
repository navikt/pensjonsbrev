package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.brukerNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.PesysDataSelectors.omsorgsopptjeningsaar
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

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
                bokmal { +"Godskriving av pensjonsopptjening ved omsorgsarbeid - melding om vedtak" },
                english { +"Crediting of acquired rights for care work - notification of decision" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Nav har vedtatt at du skal godskrives omsorgsopptjening for " + pesysData.omsorgsopptjeningsaar + "." },
                    english { +"Nav has decided that you should be credited with acquired rights for care work in respect of " + pesysData.omsorgsopptjeningsaar + "." }
                )
            }
            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    english { +"Reason for decision" }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter folketrygdloven paragraf 3-16 første ledd bokstav b, og paragrafene 20-8 første ledd bokstav b og 20-21 hvis du er født etter 1953. "
                        +"Det er lagt til grunn for vedtaket at omsorgen for " + pesysData.brukerNavn + " utgjør minst 22 timer per uke, "
                        +"og at omsorgen har hatt en varighet på minst seks måneder av " + pesysData.omsorgsopptjeningsaar + "."
                    },
                    english {
                        +"The decision has been made in accordance with section 3-16, first paragraph, letter b of the National Insurance Act, and sections 20-8 first paragraph, "
                        +"letter b and 20-21 of the National Insurance Act if your year of birth is after 1953. "
                        +"The basis of the decision is that the care provided for " + pesysData.brukerNavn + " has amounted to at least 22 hours per week, "
                        +"and that the duration of teh care was at least six months in " + pesysData.omsorgsopptjeningsaar + "."
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
                    bokmal { +"Omsorgsopptjeningen tilsvarte for hvert år før 2010 en inntekt på fire ganger folketrygdens grunnbeløp. "
                        +"Fra og med 2010 tilsvarer omsorgsopptjeningen 4,5 ganger grunnbeløpet. "
                        +"Omsorgsopptjening kan bidra til at du får høyere pensjon enn du ellers ville fått." },
                    english { +"In the years before 2010, acquired rights for care work were calculated as an income of four times the national insurance basic amount (G) per year. "
                    +"From 2010, acquired rights for care work equal 4.5 times the national insurance basic amount. "
                        +"Acquired rights for care work may help ensure that you receive a higher pension that you would have done otherwise. "}
                )
            }
            paragraph {
                text(
                    bokmal { +"Omsorgsopptjening gis ved at en inntekt tilsvarende fire eller 4,5 ganger grunnbeløpet legges til grunn for opptjeningen for det aktuelle året. "
                        +"Annen opptjening kan ikke legges sammen med omsorgsopptjening. "
                        +"Dersom du har annen pensjonsopptjening i det aktuelle året som er høyere enn omsorgsopptjeningen legges denne opptjeningen til grunn." },
                    english { +"Acquired rights for care work are considered equivalent to the rights acquired by an income of four or 4.5 times the national insurance basic amount for the year. "
                            +"Other earnings may not be combined with acquired rights for care work. "
                        +"If, in the year in question, you accumulated pension rights in some other way and these pension rights are higher than the acquired rights for care work, "
                        +"the higher pension rights are used to calculate your pension."},
                )
            }
            paragraph {
                text(
                    bokmal { +"I nettjenesten Din pensjon på nav.no får du oversikt over hele pensjonsopptjeningen din. "
                        +"Her finner du også omsorgsopptjening som du har fått godskrevet." },
                    english { +"Nav has an online pension service Din Pensjon on nav.no which shows your pensionable income and how much pension you have accumulated. "
                        +"On this page you can also find details of the aquired rights you have been accredited for care work." }
                )
            }





            }
        }
    }
}

