package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltEtterReform
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltFoerReform


@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverk : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vedtak - endring av barnepensjon" },
                nynorsk { +"Vi har endra barnepensjonen din" },
                english { +"Draft decision – adjustment of children's pension" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Vi viser til at du er innvilget barnepensjon. " +
                            "Stortinget har vedtatt nye regler for barnepensjon som gjelder fra 1. januar 2024." },
                    nynorsk { +"Du er innvilga barnepensjon. Stortinget har vedteke nye reglar for barnepensjon. Dei nye reglane gjeld frå og med 1. januar 2024." },
                    english { +"You are currently receiving a children’s pension. The Norwegian Parliament (Stortinget) has adopted some new rules regarding children's pensions that came into force on 1 January 2024." },
                )
            }
            paragraph {
                text(
                    bokmal { +"De nye reglene for barnepensjon gir" },
                    nynorsk { +"Dei nye reglane for barnepensjon gjer" },
                    english { +"The new rules for children’s pensions now include" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"økt sats" },
                            nynorsk { +"auka sats" },
                            english { +"an increased rate" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"økt aldersgrense fra 18 år til 20 år" },
                            nynorsk { +"auka aldersgrense frå 18 år til 20 år" },
                            english { +"raised the age limit from 18 to 20 years" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"ingen søskenjustering" },
                            nynorsk { +"ingen søskenjustering" },
                            english { +"no sibling adjustment" },
                        )
                    }
                }
            }

            title2 {
                text(
                    bokmal { +"Hva betyr endringene for deg?" },
                    nynorsk { +"Kva betyr endringane for deg?" },
                    english { +"What do the changes mean for you?" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du får høyere barnepensjon." },
                    nynorsk { +"Du får høgare barnepensjon." },
                    english { +"Your children's pension will now increase." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du fikk " + utbetaltFoerReform.format()
                            + " per måned i pensjon til 31. desember 2023. " +
                            "Du får " + utbetaltEtterReform.format() + " før skatt per måned fra 1. januar 2024." },
                    nynorsk { +"Du fekk " + utbetaltFoerReform.format()
                            + " per månad i pensjon til 31. desember 2023. " +
                            "Du får " + utbetaltEtterReform.format() + " før skatt per månad frå 1. januar 2024." },
                    english { +"You were receiving " + utbetaltFoerReform.format()
                            + " per month until 31 December 2023." +
                            "The gross (pre-tax) amount you will receive is " + utbetaltEtterReform.format()
                            + " per month starting 1 January 2024." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-4, 18-5 og 22-13." },
                    nynorsk { +"Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova §§ 18-4, 18-5 og 22-13." },
                    english { +"This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections 18-4, 18-5 and 22-13." },
                )
            }
        }
    }
}
