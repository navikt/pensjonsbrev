package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltEtterReform
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltFoerReform


@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverk : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vedtak - endring av barnepensjon",
                Language.Nynorsk to "Vi har endra barnepensjonen din",
                Language.English to "Draft decision – adjustment of children's pension",
            )
        }
        outline {
            paragraph {
                text(
                    Language.Bokmal to "Vi viser til at du er innvilget barnepensjon. " +
                            "Stortinget har vedtatt nye regler for barnepensjon som gjelder fra 1. januar 2024.",
                    Language.Nynorsk to "Du er innvilga barnepensjon. Stortinget har vedteke nye reglar for barnepensjon. Dei nye reglane gjeld frå og med 1. januar 2024.",
                    Language.English to "You are currently receiving a children’s pension. The Norwegian Parliament (Stortinget) has adopted some new rules regarding children's pensions that came into force on 1 January 2024.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "De nye reglene for barnepensjon gir",
                    Language.Nynorsk to "Dei nye reglane for barnepensjon gjer",
                    Language.English to "The new rules for children’s pensions now include",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "økt sats",
                            Language.Nynorsk to "auka sats",
                            Language.English to "an increased rate",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "økt aldersgrense fra 18 år til 20 år",
                            Language.Nynorsk to "auka aldersgrense frå 18 år til 20 år",
                            Language.English to "raised the age limit from 18 to 20 years",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "ingen søskenjustering",
                            Language.Nynorsk to "ingen søskenjustering",
                            Language.English to "no sibling adjustment",
                        )
                    }
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr endringene for deg?",
                    Language.Nynorsk to "Kva betyr endringane for deg?",
                    Language.English to "What do the changes mean for you?",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Du får høyere barnepensjon.",
                    Language.Nynorsk to "Du får høgare barnepensjon.",
                    Language.English to "Your children's pension will now increase.",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du fikk ".expr() + utbetaltFoerReform.format()
                            + " kroner per måned i pensjon til 31. desember 2023. " +
                            "Du får " + utbetaltEtterReform.format() + " kroner før skatt per måned fra 1. januar 2024.",
                    Language.Nynorsk to "Du fekk ".expr() + utbetaltFoerReform.format()
                            + " kroner per månad i pensjon til 31. desember 2023. " +
                            "Du får " + utbetaltEtterReform.format() + " kroner før skatt per månad frå 1. januar 2024.",
                    Language.English to "You were receiving NOK ".expr() + utbetaltFoerReform.format()
                            + " per month until 31 December 2023." +
                            "The gross (pre-tax) amount you will receive is NOK " + utbetaltEtterReform.format()
                            + " per month starting 1 January 2024.",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-4, 18-5 og 22-13.",
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova §§ 18-4, 18-5 og 22-13.",
                    Language.English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections 18-4, 18-5 and 22-13.",
                )
            }
        }
    }
}
