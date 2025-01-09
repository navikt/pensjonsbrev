package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.navn

@TemplateModelHelpers
object ForhaandsvarselVedTilbakekreving : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_05_027
    override val kode = Pesysbrevkoder.Redigerbar.PE_FORHAANDSVARSEL_VED_TILBAKEKREVING
    override val kategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt beløp",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Varsel om at Nav vurderer å kreve tilbake feilutbetalt beløp",
                Nynorsk to "Varsel om at Nav vurderer å krevje tilbake feilutbetalt beløp",
                English to "Notification that Nav is considering demanding repayment of incorrectly paid beløp"
            )
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to felles.avsenderEnhet.navn + " viser til vedtak av ".expr() + fritekst("dato") +
                            " hvor det er lagt til grunn at du har fått utbetalt for mye ".expr() + fritekst("type ytelse") +
                            " i perioden fra og med ".expr() + fritekst("dato fra og med") + " til og med ".expr() +
                            fritekst("dato til og med") + ".".expr(),
                    Nynorsk to felles.avsenderEnhet.navn + " viser til vedtak av ".expr() + fritekst("dato") +
                            " der det er lagt til grunn at du har fått utbetalt for mykje ".expr() + fritekst("type ytelse") +
                            " i perioden frå og med ".expr() + fritekst("dato fra og med") + " til og med ".expr() +
                            fritekst("dato til og med") + ".".expr(),
                    English to felles.avsenderEnhet.navn + " refers to the decision of ".expr() + fritekst("dato") + "," +
                            " which indicates that you have received over-payment of ".expr() + fritekst("type ytelse") +
                            " during the period from and including ".expr() + fritekst("dato fra og med") + " up to and including ".expr() +
                            fritekst("dato til og med") + ".".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Beløpet er ".expr() + fritekst("beløp") + " kr. " +
                            "Det feilutbetalte beløpet er summen av utbetaling som mottakeren ikke hadde krav på.".expr(),
                    Nynorsk to "Beløpet er ".expr() + fritekst("beløp") + " kr. " +
                            "Det feilutbetalte beløpet er summen av utbetaling som mottakeren ikkje hadde krav på.".expr(),
                    English to "The amount is NOK ".expr() + fritekst("beløp") + ". " +
                            "The incorrectly paid amount is the total of the payments to which the recipient was not entitled."
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette brevet gir informasjon om",
                    Nynorsk to "Dette brevet gir informasjon om",
                    English to "This letter gives information about"
                )
                list {
                    item {
                        text(
                            Bokmal to "årsak til feilutbetaling",
                            Nynorsk to "årsaka til feilutbetalinga",
                            English to "reason for incorrect payment"
                        )
                    }
                    item {
                        text(
                            Bokmal to "reglene for tilbakekreving i folketrygdloven kapittel 22",
                            Nynorsk to "reglane for tilbakekrevjing i folketrygdlova kapittel 22",
                            English to "rules for demanding repayment in section 22 of the Social Security Act"
                        )
                    }
                    item {
                        text(
                            Bokmal to "hvordan du går fram hvis du ønsker å uttale deg i saken",
                            Nynorsk to "korleis du går fram dersom du ønskjer å uttale deg i saka",
                            English to "how to proceed if you wish to submit an opinion in this case "
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Vi understreker at dette brevet er et forhåndsvarsel etter forvaltningsloven paragraf 16 om at det er aktuelt å vurdere tilbakekreving av feilutbetalt beløp. " +
                            "Brevet må derfor ikke oppfattes som et vedtak om tilbakekreving. Før vi går videre i saksbehandlingen ønsker vi at du gir svar på forhåndsvarselet – se siste avsnitt i brevet. " +
                            "Du vil motta et vedtak når saken er ferdig behandlet.",
                    Nynorsk to "Vi understrekar at dette brevet er eit førehandsvarsel etter forvaltingslova paragraf 16 om at det er aktuelt å vurdere tilbakekrevjing av feilutbetalt beløp. " +
                            "Brevet må derfor ikkje oppfattast som noko vedtak om tilbakekrevjing. Før vi går vidare i saksbehandlinga, " +
                            "ønskjer vi at du gir svar på førehandsvarselet – sjå siste avsnittet i brevet. Du vil få eit vedtak når saka er ferdig behandla.",
                    English to "We stress that this letter is a prior notification, in accordance with paragraph 16 of the Public Administration Act, " +
                            "that Nav may consider demanding repayment of the overpaid amount. This letter must not therefore be interpreted as a demand for repayment. " +
                            "Before we proceed to consider this case, we would like you to respond to this prior notification - see the last paragraph of this letter. " +
                            "You will receive notification of the decision when consideration of the case has been completed."
                )
            }
            title1 {
                text(
                    Bokmal to "Hva som har skjedd",
                    Nynorsk to "Kva som har skjedd ",
                    English to "What has happened"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to fritekst(
                        "Gjør kort greie for hva som har skjedd i saken – relevant faktum ut fra hvilke tilbakekrevingshjemler det kan være aktuelt å benytte: " +
                                "Årsak til at feilen oppsto, uaktsomhet fra mottakers side ved å forårsake og/eller motta feilutbetalt beløp, hvordan og når feilen ble oppdaget osv."
                    ) + "".expr(),
                    Nynorsk to fritekst(
                        "Gjør kort greie for hva som har skjedd i saken – relevant faktum ut fra hvilke tilbakekrevingshjemler det kan være aktuelt å benytte: " +
                                "Årsak til at feilen oppsto, uaktsomhet fra mottakers side ved å forårsake og/eller motta feilutbetalt beløp, hvordan og når feilen ble oppdaget osv."
                    ) + "".expr(),
                    English to fritekst(
                        "Gjør kort greie for hva som har skjedd i saken – relevant faktum ut fra hvilke tilbakekrevingshjemler det kan være aktuelt å benytte: " +
                                "Årsak til at feilen oppsto, uaktsomhet fra mottakers side ved å forårsake og/eller motta feilutbetalt beløp, hvordan og når feilen ble oppdaget osv."
                    ) + "".expr()
                )
            }
            title1 {
                text(
                    Bokmal to "Reglene for tilbakekreving",
                    Nynorsk to "Reglane for tilbakekrevjing",
                    English to "The rules for demanding repayment"
                )
            }
            paragraph {
                text(
                    Bokmal to "Ved en feilutbetaling skal Nav vurdere om det skal fattes vedtak om å kreve tilbake det feilutbetalte beløpet fra mottakeren. " +
                            "De aktuelle bestemmelsene om tilbakekreving står i folketrygdloven paragrafene 22-15 og 22-16. " +
                            "Grunnlaget for tilbakekreving kan være at en person har mottatt noe som vedkommende forsto eller burde forstå at han/hun ikke hadde rett til, " +
                            "og/eller at personen selv har medvirket til feilutbetalingen ved forsettlig eller uaktsomt å gi mangelfulle eller feilaktige opplysninger. " +
                            "I enkelte tilfeller vil feilutbetalte beløp kunne kreves tilbake selv om det ikke foreligger uaktsomhet eller kritikkverdige forhold hos mottakeren. ",
                    Nynorsk to "Ved ei feilutbetaling skal Nav vurdere om det skal fattast vedtak om å krevje tilbake det feilutbetalte beløpet frå mottakaren. " +
                            "Dei aktuelle føresegnene om tilbakekrevjing står i folketrygdlova paragrafane 22-15 og 22-16. " +
                            "Grunnlaget for tilbakekrevjing kan vere at ein person har motteke noko som vedkommande forstod eller burde ha forstått at han/ho ikkje hadde rett til, " +
                            "og/eller at personen sjølv har medverka til feilutbetalinga ved forsettleg eller aktlaust å gi mangelfulle eller feilaktige opplysningar. " +
                            "I enkelte tilfelle vil feilutbetalte beløp kunne krevjast tilbake sjølv om det ikkje ligg føre aktløyse eller kritikkverdige forhold hos mottakaren.",
                    English to "In the event of an incorrect payment, Nav will consider whether a decision should be made to demand repayment by the recipient of the incorrectly paid amount. " +
                            "The relevant provisions regarding a demand for repayment are in paragraphs 22-15 and 22-16 of the Social Security Act. " +
                            "The grounds for demanding repayment may be that a person has received an amount that he or she understood, or should have understood, " +
                            "that he or she was not entitled to and/or that the person concerned has contributed through deliberately or negligently giving incomplete or incorrect information. " +
                            "In some cases a demand for repayment of incorrectly paid amounts may be made even if there is no intent or negligence on the part of the recipient. "
                )
            }
            paragraph {
                text(
                    Bokmal to "Et feilutbetalt beløp kan kreves tilbake med mindre særlige grunner taler mot det. " +
                            "Beløpet kan kreves tilbake fullt ut eller reduseres. Dette avhenger av graden av uaktsomhet hos mottakeren, " +
                            "størrelsen på det feilutbetalte beløpet, og om feilen helt eller delvis kan tilskrives Nav. " +
                            "Hvis den som kravet retter seg mot har handlet forsettlig eller grovt uaktsomt, " +
                            "kan det etter folketrygdloven paragraf 22-17a beregnes et rentetillegg på 10 prosent av tilbakekrevingsbeløpet.",
                    Nynorsk to "Eit feilutbetalt beløp kan krevjast tilbake med mindre særlege grunnar taler mot det. " +
                            "Beløpet kan krevjast tilbake fullt ut eller reduserast. Dette er avhengig av graden av aktløyse hos mottakaren, " +
                            "storleiken på det feilutbetalte beløpet og om feilen heilt eller delvis kan tilskrivast Nav. " +
                            "Dersom den som kravet rettar seg mot, har handla forsettleg eller grovt aktlaust, " +
                            "kan det etter folketrygdlova paragraf 22-17a bereknast eit rentetillegg på 10 prosent av tilbakekrevjingsbeløpet.",
                    English to "A demand may be made for repayment of an incorrectly paid amount unless specific grounds indicate otherwise. " +
                            "A demand may be made for repayment in full or in part. This depends on the degree of the recipient’s negligence, " +
                            "the size of the incorrectly paid amount and whether the mistake was partly or entirely Nav’s. " +
                            "If the person to whom the demand for repayment is directed has acted with intent or gross negligence, " +
                            "additional interest of 10% of the amount to be repaid may be demanded, in accordance with paragraph 22-17a of the Social Security Act."
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom det blir gjort vedtak om tilbakebetaling vil det bli gitt nærmere informasjon om hvordan tilbakebetalingen skal skje.",
                    Nynorsk to "Dersom det blir gjort vedtak om tilbakebetaling, vil det bli gitt nærmare informasjon om korleis tilbakebetalinga skal skje.",
                    English to "If a decision is taken to demand repayment, further information will be provided about how repayment is to be made."
                )
            }
            title1 {
                text(
                    Bokmal to "Hvordan du går fram hvis du ønsker å uttale deg i saken",
                    Nynorsk to "Korleis du går fram dersom du ønskjer å uttale deg i saka",
                    English to "How to proceed if you wish to submit an opinion in this case"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har rett til å uttale deg, skriftlig eller muntlig, før vi tar den endelige avgjørelsen om tilbakebetaling. Du har også som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven, paragraf 18. Fristen for å gi uttale er 14 dager etter at du har mottatt dette brevet. Du kan ta kontakt med oss på telefonnummer ".expr() + fritekst(
                        "tlfnr"
                    ) + ", eller du kan sende et skriftlig svar til:".expr(),
                    Nynorsk to "Du har rett til å uttale deg, skriftleg eller munnleg, før vi tek den endelege avgjerda om tilbakebetaling. Du har òg som hovudregel rett til å sjå saksdokumenta etter føresegnene i forvaltingslova paragraf 18. Fristen for å uttale seg er 14 dagar etter at du har fått dette brevet. Du kan ta kontakt med oss på telefonnummer ".expr() + fritekst(
                        "tlfnr"
                    ) + ", eller du kan sende eit skriftleg svar til:".expr(),
                    English to "You have the right to submit an opinion, either in writing or verbally, before we take a final decision regarding repayment. As a general rule, you are also entitled to see the case documents, in accordance with the provisions of paragraph 18 of the Public Administration Act. The deadline for submitting an opinion is 14 days after you have received this letter. You can contact us by telephone on ".expr() + fritekst(
                        "tlfnr"
                    ) + " or you can send a written response to:".expr()
                )
            }
            includePhrase(Felles.ReturTilEtterstadOslo)
        }
    }
}

