package no.nav.pensjon.brev.maler.fraser

// MF-000094
// Vedtak - innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.text

val omsorgsopptjen_Tittel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
            Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
            English to "Earned pension savings for unpaid care work for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>"
        )
    }
}

val omsorgsopptjenHjelpestoenadInnledn_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit>  {
    paragraph {
        text(
            Bokmal to "Du har fått godkjent pensjonsopptjening for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng> fordi du utfører omsorgsarbeid for barn som har rett til forhøyet hjelpestønad etter sats 3 eller 4.",
            Nynorsk to "Du har fått godkjent pensjonsopptening for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng> fordi du utfører omsorgsarbeid for barn som har rett til forhøgd hjelpestønad etter sats 3 eller 4.",
            English to "You have been credited pension earnings for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>, because you care for a child who is entitled to higher rate assistance allowance at rate 3 or rate 4."
        )
    }
}

val omsorgsopptjenHjelpestKap20Hjemmel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
            English to "This decision was made pursuant to the provisions of sections 3-16, first paragraph, litra b, 20-8 first paragraph, litra b, and 20-21 of the National Insurance Act."
        )
    }
}

val omsorgsopptjenHjelpestKap3Hjemmel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven § 3-16 første ledd bokstav b.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 3-16 første ledd bokstav b.",
            English to "This decision was made pursuant to the provisions of section 3-16, first paragraph, litra b of the National Insurance Act."
        )
    }
}

val omsorgsopptjenInfoOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Hva er pensjonsopptjening for omsorgsarbeid?",
            Nynorsk to "Kva er pensjonsopptening for omsorgsarbeid?",
            English to "What are pension earnings for care work?"
        )
    }
}

val omsorgsopptjenInfo_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Pensjonsopptjeningen fra omsorgsarbeidet tilsvarer det du ville fått av en inntekt på 4,5 ganger grunnbeløpet for det aktuelle året. Du kan lese mer om dette på nav.no, og få oversikt over pensjonsopptjeningen din på <felles.kontaktinformasjon.nettsted>. Her finner du også omsorgsopptjening som du har fått godkjent.",
            Nynorsk to "Pensjonsoppteninga frå omsorgsarbeidet svarer til det du ville ha fått av ei inntekt på 4,5 gonger grunnbeløpet for det aktuelle året. Du kan lese meir om dette på nav.no, og få oversikt over pensjonsoppteninga di på <felles.kontaktinformasjon.nettsted>. Her finn du også omsorgsopptening som du har fått godkjent.",
            English to "The pension savings you earn for care work are equivalent to what you would have earned on an income of 4.5 times the National Insurance basic amount (“G”) for the year in question. You can read more about this at nav.no and see your earned pension savings at <felles.kontaktinformasjon.nettsted>. Here you will also see your approved pension earnings for care work. "
        )
    }
}

val omsorgsopptjenOverforingInfoOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Overføring av omsorgsopptjening til den andre forelderen",
            Nynorsk to "Overføring av omsorgsopptening til den andre forelderen",
            English to "Transferring pension earnings for care work to the other parent"
        )
    }
}

val omsorgsopptjenOverforingInfo_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Den av foreldrene som får barnetrygden vil automatisk få omsorgsopptjeningen. Opptjeningen kan overføres til den andre forelderen hvis dere er sammen om omsorgen for barnet. Da må dere fylle ut skjemaet «Overføring av omsorgsopptjening» (NAV 03-16.10), som dere finner på nav.no/skjema.",
            Nynorsk to "Den av foreldra som får barnetrygda, får automatisk omsorgsoppteninga. Oppteninga kan overførast til den andre forelderen dersom de er saman om omsorga for barnet. Då må de fylle ut skjemaet «Overføring av omsorgsopptening» (NAV 03-16.10), som de finn på nav.no/skjema.",
            English to "The child benefit recipient will automatically be credited the pension earnings for care work.” The pension earnings can be transferred to the other parent if you share the care of the child. To transfer pension earnings, complete the form “Transfer of pension earnings for care work” (form no. NAV 03-16.10), which you will find at nav.no/skjema."
        )
    }
}

val omsorgsopptjenHjelpestonadAutoGodkjennInfoTittel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Automatisk godkjenning av omsorgsopptjening",
            Nynorsk to "Automatisk godkjenning av omsorgsopptening",
            English to "Automatic approval of pension earnings for care work"
        )
    }
}

val omsorgsopptjenHjelpestonadAutoGodkjennInfo_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Omsorgsopptjening blir godkjent automatisk hvert år, så lenge barnet har rett til forhøyet hjelpestønad sats 3 eller 4. Denne stønaden kan gis fram til barnet har fylt 18 år. Hvis du fortsatt utfører ulønnet omsorgsarbeid når hjelpestønaden opphører, må du selv sette fram søknad om omsorgsopptjening.",
            Nynorsk to "Omsorgsopptening blir godkjent automatisk kvart år så lenge barnet har rett til forhøgd hjelpestønad sats 3 eller 4. NAV kan gi denne stønaden fram til barnet har fylt 18 år. Dersom du framleis utfører ulønt omsorgsarbeid når hjelpestønaden tek slutt, må du sjølv søkje om omsorgsopptening.",
            English to "Pension earnings for care work are credited automatically each year for as long as the child is entitled to higher rate assistance allowance at rate 3 or rate 4. This allowance can be granted until the child turns 18 years. If you continue to care for the child after it is eligible for assistance allowance, you must submit a separate application to earn pension savings for care work."
        )
    }
}
