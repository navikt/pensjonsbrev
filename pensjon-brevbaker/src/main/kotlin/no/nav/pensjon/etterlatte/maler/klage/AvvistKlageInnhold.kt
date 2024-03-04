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
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.datoForVedtaketKlagenGjelder
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.klageDato
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType
import java.time.LocalDate


data class AvvistKlageInnholdDTO(
    val sakType: SakType,
    val klageDato: LocalDate,
    val datoForVedtaketKlagenGjelder: LocalDate,

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
                Language.Nynorsk to "Vi har avvist klagen din",
                Language.English to "TODO"
            )
        }

        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi viser til klagen din av ".expr() + klageDato.format() + " på vedtak om "
                            + sakType.format(SaktypeFormatter) + " av " + datoForVedtaketKlagenGjelder.format() + ". "
                            + "Klagen avvises fordi <den er satt frem for sent/mangler fullmakt/andre grunner>.",
                    Language.Nynorsk to "Dette er en placeholder for vedtaksbrevet for avvisning av klage".expr(),
                    Language.English to "Dette er en placeholder for vedtaksbrevet for avvisning av klage".expr()
                )
            }
            title1 {
                text(
                    Language.Bokmal to "Rettslig grunnlag",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det organet som har fattet vedtaket som er påklaget skal avvise klagen hvis "
                            + "vilkårene for å behandle den ikke foreligger. Dette framgår av folketrygdloven "
                            + "§ 21-1, jf. forvaltningsloven § 33 andre ledd.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            title1 {
                text(
                    Language.Bokmal to "Vurdering og konklusjon",
                    Language.Nynorsk to "",
                    Language.English to ""
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
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "I vedtaket ble du orientert om klageadgang og klagefrist. Klagen din oversitter klagefristen med <antall dager/måneder> og er framsatt for sent.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom det foreligger særlige grunner til at klagefristen er oversittet, eller det er særlige grunner til at klagen skal bli prøvd, kan det unntaksvis ses bort fra oversittelsen. Dette følger av forvaltningsloven § 31 første ledd.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Trygderettspraksis viser at det skal svært mye til før man ser bort fra oversittelse av klagefristen. Bakgrunnen for dette er at klagefristen i trygdesaker er vesentlig lenger enn ankefrist for sivile saker til domstolene og dobbelt så lang som klagefrist i andre deler av forvaltningen. Det er dermed lagt opp til at bestemmelsen skal forstås bokstavelig.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "(Eksempel – brukeren har ikke påberopt seg særlige grunner. Skriv om /føy til slik at tekst og drøftelse passer din sak) Du har ikke påberopt deg særlige grunner til at klagen er sendt inn for sent. Etter en gjennomgang av saken kan vi heller ikke se forhold knyttet til vedtakets innhold som kan utgjøre en særlig grunn til at saken blir behandlet på nytt selv om klagefristen er oversittet.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            title2 {
                text(
                    Language.Bokmal to "Ved manglende fullmakt:",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Det følger av forvaltningsloven § 12 siste ledd at en fullmektig som ikke er advokat må fremlegge skriftlig fullmakt.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            title2 {
                text(Language.Bokmal to "Ved muntlig klage / manglende underskrift:",
                    Language.Nynorsk to "",
                    Language.English to "")
            }
            paragraph {
                text(
                    Language.Bokmal to "Det følger av forvaltningsloven § 32 første ledd bokstav a og b at klagen må være skriftlig og signert eller ha en kvalifisert elektronisk signatur (jf. lov om elektroniske tillitstjenester § 1 og europaparlamentets- og rådsforordning (EU) nr. 910/2014 om elektronisk identifikasjon og tillitstjenester for elektroniske transaksjoner i det indre marked (elDAS- forordningen) art. 25 nr. 2).",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Den <dato> sendte vi deg et brev hvor vi opplyste om manglene ved klagen. Fristen for å rette disse manglene ble satt til <dato>. Vi har ikke mottatt tilbakemelding på dette. Vilkårene for å behandle klagen er dermed ikke oppfylt og klagen avvises, jf. forvaltningsloven § 33 andre ledd.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
        }
    }
}