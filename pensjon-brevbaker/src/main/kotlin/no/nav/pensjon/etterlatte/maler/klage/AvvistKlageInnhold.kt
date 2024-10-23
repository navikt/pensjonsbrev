package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.datoForVedtaketKlagenGjelder
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.klageDato
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.sakType
import java.time.LocalDate


data class AvvistKlageInnholdDTO(
    val sakType: SakType,
    val klageDato: LocalDate,
    val datoForVedtaketKlagenGjelder: LocalDate?,
    val bosattUtland: Boolean = false,
    )

@TemplateModelHelpers
object AvvistKlageInnhold : EtterlatteTemplate<AvvistKlageInnholdDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.AVVIST_KLAGE_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvvistKlageInnholdDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Avvist klage",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har avvist klagen din",
                Language.Nynorsk to "Vi har avvist klaga di",
                Language.English to "We have rejected your appeal"
            )
        }

        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi viser til klagen din av ".expr() + klageDato.format(),
                    Language.Nynorsk to "Vi viser til klaga di av ".expr() + klageDato.format(),
                    Language.English to "We refer you to your appeal of ".expr() + klageDato.format()
                )
                ifNotNull(datoForVedtaketKlagenGjelder) { paaklagdVedtakDato ->
                    textExpr(
                        Language.Bokmal to " på vedtak om ".expr() + sakType.format() + " av " + paaklagdVedtakDato.format(),
                        Language.Nynorsk to " på vedtak om".expr() + sakType.format() + " av " + paaklagdVedtakDato.format(),
                        Language.English to "on our decision concerning".expr() + sakType.format() + " of " + paaklagdVedtakDato.format(),
                    )
                }
                text(
                    Language.Bokmal to ". Klagen avvises fordi <den er satt frem for sent/mangler fullmakt/andre grunner>.",
                    Language.Nynorsk to ". Klaga blir avvist fordi <ho har blitt sett fram for seint/det manglar fullmakt/andre grunner>",
                    Language.English to ". The appeal was rejected because <it was submitted late/missing correct authorisation/andre grunner>."
                )

            }
            title1 {
                text(
                    Language.Bokmal to "Rettslig grunnlag",
                    Language.Nynorsk to "Rettsleg grunnlag",
                    Language.English to "Legal basis"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det organet som har fattet vedtaket som er påklaget skal avvise klagen hvis "
                            + "vilkårene for å behandle den ikke foreligger. Dette framgår av folketrygdloven "
                            + "§ 21-1, jf. forvaltningsloven § 33 andre ledd.",
                    Language.Nynorsk to "Organet som har fatta vedtaket det blir klaga på, skal avvise klaga dersom " +
                            "vilkåra for klagebehandling ikkje er innfridde. Dette går fram av folketrygdlova " +
                            "§ 21-1, jf. forvaltingslova § 33 andre ledd.",
                    Language.English to "The agency that made the decision being appealed will reject an appeal if " +
                            "the conditions for processing a case are not met. This is based on the legal provisions found " +
                            "in Section 21-1 of the National Insurance Act; cf. Section 33(2) of the Public Administration Act."
                )
            }
            title1 {
                text(
                    Language.Bokmal to "Vurdering og konklusjon",
                    Language.Nynorsk to "Vurdering og konklusjon",
                    Language.English to "Evaluation and conclusion"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "VELG RIKTIG ALTERNATIV",
                    Language.Nynorsk to "VELG RIKTIG ALTERNATIV",
                    Language.English to "VELG RIKTIG ALTERNATIV"
                )
            }
            title2 {
                text(
                    Language.Bokmal to "Ved for sent framsatt klage:",
                    Language.Nynorsk to "Ved for sent framsatt klage:",
                    Language.English to "Ved for sent framsatt klage:"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "I vedtaket ble du orientert om klageadgang og klagefrist. Klagen din oversitter klagefristen med <antall dager/måneder> og er framsatt for sent.",
                    Language.Nynorsk to "I vedtaket blei du orientert om klagerett og -frist. Klagen din oversit klagefristen med <antal dagar/månader> og er framsett for seint.",
                    Language.English to "The decision letter provides more information about the right to appeal/deadline for appeals. " +
                            "Your appeal was sent after the deadline for appeals, which was <antall dager/måneder> and was therefore submitted late."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom det foreligger særlige grunner til at klagefristen er oversittet, " +
                            "eller det er særlige grunner til at klagen skal bli prøvd, kan det unntaksvis ses bort fra " +
                            "oversittelsen. Dette følger av forvaltningsloven § 31 første ledd.",
                    Language.Nynorsk to "Dersom det er særlege grunnar til at du ikkje rakk å klage innan fristen, " +
                            "eller det er særlege grunner til at klaga skal bli prøvd, kan ein unntaksvis sjå vekk frå fristbrotet. " +
                            "Dette går fram av forvaltingslova § 31 første ledd.",
                    Language.English to "If you have any special reasons for not complying with the deadline for " +
                            "appeal, or if there are special reasons for accepting your appeal, we may make an exception. " +
                            "The legal grounds for this are stated in Section 31(1) of the Public Administration Act."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Trygderettspraksis viser at det skal svært mye til før man ser bort fra " +
                            "oversittelse av klagefristen. Bakgrunnen for dette er at klagefristen i trygdesaker er " +
                            "vesentlig lenger enn ankefrist for sivile saker til domstolene og dobbelt så lang som klagefrist " +
                            "i andre deler av forvaltningen. Det er dermed lagt opp til at bestemmelsen skal forstås bokstavelig.",
                    Language.Nynorsk to "Det følgjer av trygderettspraksis at det skal særs mykje til for å behandle " +
                            "saka når klagefristen har gått ut. Bakgrunnen for dette er at klagefristen i trygdesaker er " +
                            "vesentleg lenger enn ankefristen for sivile saker til domstolane, og dobbelt så lang som " +
                            "klagefristen i andre delar av forvaltinga. Det er dermed lagt opp til at føresegna skal tolkast bokstaveleg.",
                    Language.English to "Legal practice at NAV indicates that making such exceptions requires very " +
                            "special circumstances. The reason for this is that the deadline for appeal in national insurance " +
                            "cases is significantly longer than the appeal deadline in civil cases in Norwegian courts and " +
                            "double the time compared to other state agencies. That is why this legal provision is to be understood and followed literally."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "(Eksempel – brukeren har ikke påberopt seg særlige grunner. Skriv om /føy " +
                            "til slik at tekst og drøftelse passer din sak) Du har ikke påberopt deg særlige grunner til " +
                            "at klagen er sendt inn for sent. Etter en gjennomgang av saken kan vi heller ikke se forhold " +
                            "knyttet til vedtakets innhold som kan utgjøre en særlig grunn til at saken blir behandlet på " +
                            "nytt selv om klagefristen er oversittet.",
                    Language.Nynorsk to "(Eksempel – brukeren har ikke påberopt seg særlige grunner. Skriv om /føy " +
                            "til slik at tekst og drøftelse passer din sak) Du har ikkje vist til særlege grunnar til at " +
                            "klaga har blitt sendt inn for seint. Etter ein gjennomgang av saka kan vi heller ikkje sjå " +
                            "at det ligg føre forhold knytt til innhaldet i vedtaket som utgjer særleg grunn til å " +
                            "behandle saka på nytt etter at klagefristen har gått ut.",
                    Language.English to "(Eksempel – brukeren har ikke påberopt seg særlige grunner. Skriv om /føy " +
                            "til slik at tekst og drøftelse passer din sak) You have not invoked any particular reasons why " +
                            "the appeal was submitted too late. After a review of the case, we are also unable to see any " +
                            "circumstances related to the content of the decision that would constitute a special reason " +
                            "for the case to be processed again, even if the appeal deadline has been exceeded."
                )
            }
            title2 {
                text(
                    Language.Bokmal to "Ved manglende fullmakt:",
                    Language.Nynorsk to "Ved manglende fullmakt:",
                    Language.English to "Ved manglende fullmakt:"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det følger av forvaltningsloven § 12 siste ledd at en fullmektig som ikke er advokat må fremlegge skriftlig fullmakt.",
                    Language.Nynorsk to "Det følgjer av forvaltingslova § 12 siste ledd at ein fullmektig som ikkje er advokat, må leggje fram skriftleg fullmakt.",
                    Language.English to "The last sentence of Section 12 of the Public Administration Act states " +
                            "that an authorised person who is not an attorney/lawyer must submit a written power of attorney."
                )
            }
            title2 {
                text(
                    Language.Bokmal to "Ved muntlig klage / manglende underskrift:",
                    Language.Nynorsk to "Ved muntlig klage / manglende underskrift:",
                    Language.English to "Ved muntlig klage / manglende underskrift:"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det følger av forvaltningsloven § 32 første ledd bokstav a og b at klagen må " +
                            "være skriftlig og signert eller ha en kvalifisert elektronisk signatur (jf. lov om elektroniske " +
                            "tillitstjenester § 1 og europaparlamentets- og rådsforordning (EU) nr. 910/2014 om elektronisk " +
                            "identifikasjon og tillitstjenester for elektroniske transaksjoner i det indre marked " +
                            "(elDAS- forordningen) art. 25 nr. 2).",
                    Language.Nynorsk to "Det følgjer av forvaltingslova § 32 første ledd bokstav a og b at klaga må " +
                            "vere skriftleg og signert eller ha ein kvalifisert elektronisk signatur (jf. lov om elektroniske " +
                            "tillitstenester § 1 og europaparlaments- og rådsforordning (EU) nr. 910/2014 om elektronisk " +
                            "identifikasjon og tillitstenester for elektroniske transaksjonar i den indre marknaden " +
                            "(elDAS-forordninga) art. 25 nr. 2).",
                    Language.English to "Letters [a] and [b] of Section 32(1) of the the Public Administration Act state " +
                            "that an appeal must be made in writing and signed, or have a qualified electronic signature " +
                            "(cf. Section 1 of the Electronic Trust Services Act and the European Parliament and Council " +
                            "Regulation (EU) No. 910/2014 on electronic identification) and trust services for electronic " +
                            "transactions in the internal market (elDAS regulation) Art. 25 no. 2)."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Den <dato> sendte vi deg et brev hvor vi opplyste om manglene ved klagen. " +
                            "Fristen for å rette disse manglene ble satt til <dato>. Vi har ikke mottatt tilbakemelding " +
                            "på dette. Vilkårene for å behandle klagen er dermed ikke oppfylt og klagen avvises, jf. forvaltningsloven § 33 andre ledd.",
                    Language.Nynorsk to "<dato> sende vi deg eit brev der vi opplyste om manglane ved klaga. " +
                            "Fristen for å rette desse manglane blei sett til <dato>. Vi har ikkje fått tilbakemelding på dette." +
                            "Vilkåra for å behandle klaga er dermed ikkje oppfylte, og klaga blir avist. Jf. forvaltingslova § 33 andre ledd. ",
                    Language.English to "On <dato>, we sent you a letter in which we informed you about the deficiencies in your appeal. " +
                            "The deadline for correcting these was set on <dato>. We have not received your feedback on this. " +
                            "The conditions for processing an appeal have therefore NOT been met, and your appeal is " +
                            "therefore rejected; cf. Section 33(2) of the Public Administration Act."
                )
            }
        }
    }
}