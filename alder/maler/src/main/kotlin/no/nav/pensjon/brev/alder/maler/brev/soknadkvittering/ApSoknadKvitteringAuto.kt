package no.nav.pensjon.brev.alder.maler.brev.soknadkvittering

import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDto
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.afpPrivat
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatSelectors.arbeidsgiverNavn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.AfpPrivatSelectors.soktAfpPrivat
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.familieforhold
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.FamilieforholdSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.innledning
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.erNyttRegelverk
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.iverksettelsesdato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.InnledningSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.personopplysninger
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.foedselsnummer
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.navn
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.PersonopplysningerSelectors.statsborgerskap
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.utland
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDtoSelectors.UtlandSelectors.harBoddArbeidetUtland
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Kvittering for innsendt søknad om alderspensjon.
 *
 * Denne malen genererer et PDF-dokument som journalføres som kvittering
 * på at søknad om alderspensjon er mottatt. Brevet distribueres IKKE til bruker.
 *
 * Innholdet tilsvarer den gamle Velocity/XSL-FO-baserte kvitteringen (alderspensjon.xsl).
 */
@TemplateModelHelpers
object ApSoknadKvitteringAuto : AutobrevTemplate<ApSoknadKvitteringAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.AP_SOKNAD_KVITTERING

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Kvittering - Søknad om alderspensjon",
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Søknad om alderspensjon" },
                )
            }
            outline {
                // Innledning
                title1 {
                    text(bokmal { +"Opplysninger om pensjoneringen" })
                }
                paragraph {
                    text(bokmal { +"Ønsket pensjoneringstidspunkt: ".expr() + innledning.iverksettelsesdato.format() })
                }
                showIf(innledning.erNyttRegelverk) {
                    paragraph {
                        text(bokmal { +"Uttaksgrad: ".expr() + innledning.uttaksgrad.format() + " %" })
                    }
                }

                // Personopplysninger
                title1 {
                    text(bokmal { +"Personopplysninger" })
                }
                paragraph {
                    text(bokmal { +"Navn: ".expr() + personopplysninger.navn })
                }
                paragraph {
                    text(bokmal { +"Fødselsnummer: ".expr() + personopplysninger.foedselsnummer })
                }
                paragraph {
                    text(bokmal { +"Statsborgerskap: ".expr() + personopplysninger.statsborgerskap })
                }

                // Familieforhold
                title1 {
                    text(bokmal { +"Familieforhold" })
                }
                paragraph {
                    text(bokmal { +"Sivilstand: ".expr() + familieforhold.sivilstand })
                }

                // Utland
                title1 {
                    text(bokmal { +"Opplysninger om opphold utenfor Norge" })
                }
                showIf(utland.harBoddArbeidetUtland) {
                    paragraph {
                        text(bokmal { +"Har bodd/arbeidet utenfor Norge etter fylte 16 år: Ja" })
                    }
                }.orShow {
                    paragraph {
                        text(bokmal { +"Har bodd/arbeidet utenfor Norge etter fylte 16 år: Nei" })
                    }
                }

                // AFP Privat
                ifNotNull(afpPrivat) { afp ->
                    showIf(afp.soktAfpPrivat) {
                        title1 {
                            text(bokmal { +"AFP i privat sektor" })
                        }
                        paragraph {
                            text(bokmal { +"Søkt AFP i privat sektor: Ja" })
                        }
                        ifNotNull(afp.arbeidsgiverNavn) { arbgiver ->
                            paragraph {
                                text(bokmal { +"Arbeidsgiver: ".expr() + arbgiver })
                            }
                        }
                    }
                }
            }
        }
}
