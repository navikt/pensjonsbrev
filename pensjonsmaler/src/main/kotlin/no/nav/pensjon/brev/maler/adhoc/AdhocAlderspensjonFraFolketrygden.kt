package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocAlderspensjonFraFolketrygden : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ADHOC_2024_REGLERENDRET_GJR_AP_MNTINDV
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Alderspensjon fra folketrygden",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Alderspensjon fra folketrygden" },
                nynorsk { + "Alderspensjon frå folketrygda" },
                english { + "Retirement pension from the National Insurance Scheme" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Stortinget har vedtatt nye regler for gjenlevenderett i alderspensjonen fra 1. januar 2024." },
                    nynorsk { + "Stortinget har vedtatt nye reglar for attlevanderett i alderspensjonen frå 1. januar 2024." },
                    english { + "The Norwegian Parliament has decided new rules regarding survivor's rights in the retirement pension from the 1st of January 2024." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Reglene har ingen praktisk betydning for deg. Samlet pensjon før skatt er den samme som før. "
                            + "Vi har bare endret metoden for beregningen av alderspensjonen din." },
                    nynorsk { + "Reglane har ingen praktisk tyding for deg. Samla pensjon før skatt er den same som før. "
                            + "Vi har berre endra metoden for utrekninga av alderspensjonen din." },
                    english { + "The rules have no practical significance for you. Your total pension amount before tax is the same as before. "
                            + "We have only changed the method for calculating your retirement pension." }
                )
            }
            title1 {
                text(
                    bokmal { + "Endret metode for beregning" },
                    nynorsk { + "Endra metode for utrekning" },
                    english { + "Changed method for calculation" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Metoden for beregningen av alderspensjonen din gjør at du nå får et gjenlevendetillegg. "
                            + "Dette erstatter det som før het " + quoted("minstenivåtillegg individuelt") + "." },
                    nynorsk { + "Metoden for utrekninga av alderspensjonen din gjer at du no får eit attlevandetillegg. "
                            + "Dette erstattar det som før heitte " + quoted("minstenivåtillegg individuelt") + "." },
                    english { + "The method for calculating your retirement pension means that you now receive a survivor’s supplement. "
                            + "This replaces what was previously called " + quoted("minimum pension supplement") + "." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Alderspensjonen reguleres hvert år med gjennomsnittet av pris- og lønnsveksten." },
                    nynorsk { + "Alderspensjonen blir regulert kvart år med gjennomsnittet av pris- og lønnsveksten." },
                    english { + "Retirement pension is adjusted annually by the average of consumer price and wage growth." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Gjenlevendetillegget vil ikke bli regulert. For at du ikke skal tape på dette, "
                            + "vil du ved reguleringen få et nytt minstenivåtillegg individuelt." },
                    nynorsk { + "Attlevandetillegget vil ikkje bli regulert. For at du ikkje skal tape på dette, "
                            + "vil du ved reguleringa få eit nytt minstenivåtillegg individuelt." },
                    english { + "The survivor’s supplement will not be adjusted. To ensure that you do not lose out on this, "
                            + "you will receive a new individual minimum pension supplement at the time of adjustment." }
                )
            }
            title1 {
                text(
                    bokmal { + "Lurer du på hva du har utbetalt?" },
                    nynorsk { + "Lurer du på kva du har fått utbetalt?" },
                    english { + "Would you like to know how much you will receive?" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan logge deg på ${Constants.UTBETALINGSOVERSIKT_URL} for å se utbetalingene dine." },
                    nynorsk { + "Du kan logge deg på ${Constants.UTBETALINGSOVERSIKT_URL} for å sjå utbetalingane dine." },
                    english { + "Log in to ${Constants.UTBETALINGSOVERSIKT_URL} to see your payments." },
                )
            }
            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                    english { + "Notify Nav about changes" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du får endringer i familiesituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din. "
                            + "I slike tilfeller må du derfor straks melde fra til Nav. Se hva du må melde fra om på ${Constants.PENSJON_ENDRING_URL}." },
                    nynorsk { + "Viss du får endringar i familiesituasjon eller planlegg opphald i eit anna land, kan det påverke utbetalinga di. "
                            + "I slike tilfelle må du derfor straks melde frå til Nav. Sjå kva du må melde frå om på ${Constants.PENSJON_ENDRING_URL}." },
                    english { + "If you have changes in your family situation or you plan to live abroad, this may influence your benefits. "
                            + "You are obliged to notify Nav as soon as you are aware of any of these changes. "
                            + "You can find out what you are required to report at ${Constants.PENSJON_ENDRING_URL}." }
                )
            }

            includePhrase(Felles.HarDuSpoersmaal(Constants.REGULERING_ALDERSPENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))
        }
    }
}