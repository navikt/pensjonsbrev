package no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTOSelectors.automatiskBehandla
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattFraser

data class ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO(
    val beregning: BarnepensjonBeregning,
    val automatiskBehandla: Boolean,
    val erUnder18Aar: Boolean,
    val erBosattUtlandet: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfall :
    EtterlatteTemplate<ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_FORHAANDSVARSEL_GJENOPPSTAATT_UTFALL
    override val template = createTemplate(
        name = kode.name,
        letterDataType = ForhaandsvarselGjenoppstaattBarnepensjonRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024.",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(automatiskBehandla) {
                includePhrase(Automatisk(erBosattUtlandet))
            }.orShow { includePhrase(Manuelt(erBosattUtlandet)) }
            paragraph {
                text(
                    Bokmal to "Hvis du har andre ytelser fra NAV, Lånekassen, andre tjenestepensjonsordninger eller fra andre land enn Norge, må du selv undersøke hvilke konsekvenser barnepensjon fra folketrygden vil ha for deg.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Automatisk(val erBosattUtlandet: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "NAV har vurdert at du har rett til ny barnepensjon fra 1. januar 2024. Vi har lagt ved en beregning av hva du vil få i barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Beregningen er gjort med bakgrunn i de opplysningene vi har om deg. Du er selv ansvarlig for å sjekke om opplysningene er riktige. Meld fra til oss hvis du ser noen feil eller mangler. Hvis du ikke melder fra om endringer og får utbetalt for mye barnepensjon, kan barnepensjon som er utbetalt feil kreves tilbake.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis vi ikke hører fra deg innen fire uker, tar vi utgangspunkt i at beregningene våre er riktige og du får vedtak om innvilget barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du melder fra om endringer, vil vi gjøre endringer og du vil få vedtak gjort etter de nye opplysningene du gir oss.",
                    Nynorsk to "",
                    English to "",
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.ReglerForBarnepensjon)
            title2 {
                text(
                    Bokmal to "Du trenger ikke å søke ",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har mottatt barnepensjon tidligere. Du trenger derfor ikke å søke på nytt.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "De nye reglene gjelder fra 1. januar 2024. Selv om du var under 20 år før de nye reglene trådte i kraft, kan du ikke få utbetalt pensjon for tiden før regelendringen.",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Dette må du gjøre",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må sjekke at du har et kontonummer registrert hos NAV.",
                    Nynorsk to "",
                    English to "",
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.KontonummerOgSkatt(erBosattUtlandet))
        }
    }

    data class Manuelt(val erBosattUtlandet: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "NAV vurderer om du har rett til ny barnepensjon fra 1. januar 2024. Vi har lagt ved en foreløpig beregning av hva du kan få i barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Beregningen er gjort med bakgrunn i de opplysningene vi har om deg. Du er selv ansvarlig for å sjekke om opplysningene er riktige. Når du har kontrollert opplysningene, må du gi beskjed til oss om vi har riktige opplysninger eller om det er nye opplysninger vi må legge til grunn i behandlingen av barnepensjonen.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du vil få vedtak om barnepensjon så fort du har gitt oss informasjonen vi trenger.",
                    Nynorsk to "",
                    English to "",
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.ReglerForBarnepensjon)
            title2 {
                text(
                    Bokmal to "Du trenger ikke å søke",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har tidligere hatt utbetalt barnepensjon fra folketrygden. Du trenger derfor ikke å søke om ny barnepensjon. Du må imidlertid sjekke opplysningene og gi oss tilbakemelding snarest mulig for at vi kan gjøre vedtak i saken din.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "De nye reglene gjelder fra 1. januar 2024. Selv om du var under 20 år før de nye reglene trådte i kraft, kan du ikke få utbetalt pensjon i tiden før regelendringen.",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Dette må du gjøre",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må sjekke at du har et kontonummer registrert hos NAV. Du må også bekrefte at opplysningene som er vedlagt er riktige, eller gi nye opplysninger for å få riktig barnepensjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.KontonummerOgSkatt(erBosattUtlandet))
        }
    }
}