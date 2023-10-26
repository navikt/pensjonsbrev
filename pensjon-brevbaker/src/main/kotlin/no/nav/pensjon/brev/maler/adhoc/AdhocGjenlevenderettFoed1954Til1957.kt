package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AdhocGjenlevenderettFoed1954Til1957 : AutobrevTemplate<EmptyBrevdata> {

    override val kode: Brevkode.AutoBrev =
        Brevkode.AutoBrev.PE_AP_ADHOC_2023_REGLERENDRET_INNVGJT_20_19A

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nye regler for gjenlevenderett i alderspensjon fra 2024",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Til deg som har gjenlevenderett i alderspensjonen",
                Nynorsk to "Til deg som har attlevanderett i alderspensjonen",
                English to "Your survivor's rights in your retirement pension"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevenderett i alderspensjonen fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevanderett i alderspensjonen frå 1. januar 2024.",
                    English to "The Norwegian Parliament has decided new rules regarding survivor's rights in the retirement pension from the 1st of January 2024."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har gjenlevenderett i alderspensjonen din beregnet med en andel etter både gamle (kapittel 19) og nye regler (kapittel 20) i folketrygdloven.",
                    Nynorsk to "Du har attlevanderett i alderspensjonen din berekna med ein del etter både gamle (kapittel 19) og nye reglar (kapittel 20) i folketrygdlova.",
                    English to "You have survivor’s rights in your retirement pension, calculated in part, after both the old Chapter 19 rules and the new Chapter 20 rules of the National Insurance Act."
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra januar 2024 vil gjenlevenderett i alderspensjonen din vises på utbetalingen som et eget gjenlevendetillegg både for nye og gamle regler."
                            + " Disse tilleggene skal ikke lenger reguleres når pensjonene øker 1. mai hvert år.",
                    Nynorsk to "Frå januar 2024 vil attlevanderetten i alderspensjonen din visast på utbetalinga som eit eige attlevandetillegg både for nye og gamle reglar."
                            + " Desse tillegga skal ikkje lenger regulerast når pensjonane aukar 1. mai kvart år.",
                    English to "From January 2024, the survivor's rights in your retirement pension will be visible in the payment as supplements calculated after both the new and old rules."
                            + " These supplements will no longer be adjusted annually when Norwegian pensions increase on the 1st of May each year."
                )
            }
            paragraph {
                text(
                    Bokmal to "Alderspensjonen som er basert på din egen opptjening, blir fortsatt regulert 1. mai hvert år.",
                    Nynorsk to "Alderspensjonen som er basert på di eiga opptening, blir framleis regulert 1. mai kvart år.",
                    English to "However, your retirement pension, which is based on your specific earnings, will continue to be adjusted on the 1st of May each year."
                )
            }

            title1 {
                text(
                    Bokmal to "Gjenlevendetillegget etter nye regler blir utfaset",
                    Nynorsk to "Attlevandetillegget etter nye reglar blir fasa ut",
                    English to "Survivor's supplement based on new rules phases out"
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegget i alderspensjon etter nye regler skal fases ut.",
                    Nynorsk to "Attlevandetillegget i alderspensjon etter nye reglar skal fasast ut.",
                    English to "Survivor's supplement in retirement pension calculated after the new Chapter 20 rules will be phased out."
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 2024 blir dette tillegget redusert med samme beløp som alderspensjonen din øker ved den årlige reguleringen."
                            + " Tillegget vil dermed bli lavere og etter hvert opphøre. Når tillegget er borte, vil alderspensjonen øke som normalt igjen.",
                    Nynorsk to "Frå 2024 blir dette tillegget redusert med same beløp som alderspensjonen din aukar ved den årlege reguleringa."
                            + " Tillegget vil dermed bli lågare og etter kvart bli borte. Når tillegget er borte, vil alderspensjonen auke som normalt igjen.",
                    English to "From 2024, this supplement will be reduced by the same amount by which your retirement pension increases with the annual adjustment."
                            + " The supplement, therefore, will be gradually reduced until it ceases to exist. When the supplement has been removed,"
                            + " your retirement pension will increase as normal with the annual adjustment again."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du tar ut mindre enn 100 prosent alderspensjon, har det betydning for gjenlevendetillegget ditt."
                            + " Ved senere økning av uttaksgrad vil ikke gjenlevendetillegget økes.",
                    Nynorsk to "Viss du tar ut mindre enn 100 prosent alderspensjon, verkar det inn på attlevandetillegget ditt."
                            + " Viss du aukar uttaksgraden seinare, vil ikkje attlevandetillegget auke.",
                    English to "If you take out less than 100 percent retirement pension, has significance for your survivor's supplement."
                            + " If you should later increase the withdrawal rate of your retirement pension, the survivor's supplement will not increase."
                )
            }

            title1 {
                text(
                    Bokmal to "Hva vil skje i saken din?",
                    Nynorsk to "Kva vil skje i saka di?",
                    English to "What will happen in your case?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Mot slutten av dette året får du et brev som viser hva som er gjenlevendetillegg etter gamle og nye regler og hva som er alderspensjonen din.",
                    Nynorsk to "Mot slutten av dette året får du eit brev som viser kva som er attlevandetillegg etter gamle og nye reglar og kva som er alderspensjonen din.",
                    English to "Towards the end of the year, you will receive a letter that informs of what is your survivor's rights and what is your retirement pension."
                )
            }

            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "Notify NAV about changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du mister gjenlevenderetten hvis du gifter deg, flytter sammen med en du tidligere har vært gift med eller har barn med, eller får barn med en samboer.",
                    Nynorsk to "Du mistar attlevandetillegget om du giftar deg, flytter saman med ein du tidlegare har vore gift med eller har barn med, eller får barn med ein sambuar.",
                    English to "You forfeit survivor's rights if you marry, cohabit with a former spouse or parent of your children, or have children with a cohabitee."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får andre endringer i familiesituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din."
                            + " I slike tilfeller må du derfor straks melde fra til NAV. Se hva du må melde fra om på ${Constants.PENSJON_ENDRING_URL}.",
                    Nynorsk to "Viss du får endringar i familiesituasjon eller planlegg opphald i eit anna land, kan det påverke utbetalinga di."
                            + " I slike tilfelle må du derfor straks melde frå til NAV. Sjå kva du må melde frå om på ${Constants.PENSJON_ENDRING_URL}.",
                    English to "If you have other changes in your family situation or you plan to live abroad, this may influence your benefits."
                            + " You are obliged to notify NAV as soon as you are aware of any of these changes."
                            + " You can find out what you are required to report at ${Constants.PENSJON_ENDRING_URL}."
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                            + " Viss du ikkje finn svar på ${Constants.NAV_URL}, kan du ringje oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "You can find more information at ${Constants.ALDERSPENSJON_GJENLEVENDE_URL}."
                            + " At ${Constants.KONTAKT_URL} you can chat or write to us."
                            + " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON},"
                            + " weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                )
            }
        }
    }
}