@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

/**
 * Ufoere-port av legacy-vedlegget `vedleggOpplysningerBruktIBeregningUTLegacy` (pensjon/maler),
 * basert paa [OpplysningerBruktIBeregningUTLegacyDto] i stedet for PE-objektet PEgruppe10.
 *
 * Innholdet bygges opp gruppevis (trygdetid, inntekt, barnetillegg, etteroppgjoer ...) etter hvert
 * som de tilhoerende frasene migreres. @TemplateModelHelpers genererer selectors for DTO-en.
 */
@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorsk, OpplysningerBruktIBeregningUTLegacyDto>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om utrekninga" },
            )
        },
        includeSakspart = false,
    ) {
        title2 {
            text(
                bokmal { +"Opplysninger vi har brukt i beregningen fra " + beregning.beregningVirkningDatoFom.format() },
                nynorsk { +"Opplysningar vi har brukt i berekninga frå " + beregning.beregningVirkningDatoFom.format() },
            )
        }
        paragraph {
            text(
                bokmal {
                    +" Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                        beregning.grunnbeloep.format() + "."
                },
                nynorsk {
                    +" Folketrygdas grunnbeløp (G) nytta i berekninga er " +
                        beregning.grunnbeloep.format() + "."
                },
            )
        }
    }
