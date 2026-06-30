package no.nav.pensjon.brev.ufore.maler.lovendringer2026

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AutoBrev.UT_VARSEL_REVERSERING_AV_MINSTESATS
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselOmReverseringAvMinstesats : AutobrevTemplate<EmptyAutobrevdata> {

    override val kode = UT_VARSEL_REVERSERING_AV_MINSTESATS

    override val template = createTemplate(
        languages = languages(Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Omgjøring av kutt i minstesats uføretrygd",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Omgjøring av kutt i minstesats uføretrygd" },
                nynorsk { +"Omgjering av kutt i minstesats uføretrygd" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Vi har tidligere informert deg om at minstesatsen for uføretrygden din skulle bli lavere. Dette skyldtes en lovendring vedtatt av Stortinget i 2025." },
                    nynorsk { +"Vi har tidlegare informert deg om at minstesatsen for uføretrygda di skulle bli lågare. Dette skuldast ei lovendring vedteken av Stortinget i 2025." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Stortinget har nå vedtatt å reversere denne endringen. Fordi reverseringen først trer i kraft 1. oktober, må vi beregne uføretrygden din etter den lavere satsen frem til denne datoen." },
                    nynorsk { +"Stortinget har no vedteke å reversere denne endringa. Fordi reverseringa først tek til å gjelde 1. oktober, må vi rekne ut uføretrygda di etter den lågare satsen fram til denne datoen." },
                )
            }

            title1 {
                text(
                    bokmal { +"Slik påvirkes dine utbetalinger" },
                    nynorsk { +"Slik vert utbetalingane dine påverka" },
                )
            }
            title2 {
                text(
                    bokmal { +"Juli, august og september" },
                    nynorsk { +"Juli, august og september" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du skal ha utbetalt uføretrygd etter en minstesats på 2,379 G" },
                    nynorsk { +"Du skal ha utbetalt uføretrygd etter ein minstesats på 2,379 G" },
                )
            }
            title2 {
                text(
                    bokmal { +"Oktober" },
                    nynorsk { +"Oktober" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Minstesatsen vil gå opp igjen fra oktober. Du skal ha utbetalt uføretrygd etter en minstesats på 2,379 G. Du vil få et nytt vedtaksbrev med beregninger om hvordan utbetalingen din blir fremover." },
                    nynorsk { +"Minstesatsen vil gå opp att frå oktober. Du skal ha utbetalt uføretrygd etter ein minstesats på 2,379 G. Du vil få eit nytt vedtaksbrev med utrekningar om korleis utbetalinga di vert framover." },
                )
            }
            title2 {
                text(
                    bokmal { +"Etterbetaling" },
                    nynorsk { +"Etterbetaling" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Dette kan føre til at du kan få en etterbetaling. Dersom du skal ha en etterbetaling vil dette fremkomme i nytt vedtak du vil få senere." },
                    nynorsk { +"Dette kan føre til at du kan få ei etterbetaling. Dersom du skal ha ei etterbetaling vil dette kome fram i nytt vedtak du vil få seinare." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi beklager for ulempene dette medfører for deg." },
                    nynorsk { +"Vi beklagar for ulempene dette medfører for deg." },
                )
            }
        }
    }
}
