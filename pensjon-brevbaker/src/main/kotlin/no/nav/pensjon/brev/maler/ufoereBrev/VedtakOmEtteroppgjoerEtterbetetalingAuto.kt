package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.avviksbelopTFBUtenMinus
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.avviksbelopTSBUtenMinus
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.avviksbelopUTUtenMinus
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.avviksbelopUtenMinus
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.uforetrygdEtteroppgjorPeriodeFomYear
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksbrevVedtaksdataEtteroppgjorResultatTotalBelopTSB
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdaEtteroppgjorResultatTidligereBelopUT
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdataEtteroppgjorResultatInntektUT
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdataEtteroppgjorResultatTidligereBelopTFB
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdataEtteroppgjorResultatTidligereBelopTSB
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdataEtteroppgjorResultatTotalBelopTFB
import no.nav.pensjon.brev.api.model.maler.VedtakOmEtteroppgjoerEtterbetalingAutoDtoSelectors.vedtaksdataEtteroppgjorResultatTotalBelopUT
import no.nav.pensjon.brev.maler.fraser.ufoer.HarDuSpoersmaalEtteroppgjoer
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd.RettTilAAKlage
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd.ViktigAALeseHeleBrevet
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselOmEtteroppgjoerEtterbetalingAuto : AutobrevTemplate<VedtakOmEtteroppgjoerEtterbetalingAutoDto> {

    override val kode = Brevkode.AutoBrev.UT_ETTEROPPGJOER_ETTERBETALING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakOmEtteroppgjoerEtterbetalingAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Bytt dette til korrekt tittel",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {

        title{
          // Legg til noe her

        }
        outline {
            // Bruk EtteroppgjoerTolkning.isNyttEtteroppgjor()
//            TBU3301
//            IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt_New = true
//                    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'etterbet'
//                    OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'tilbakekr'
//                    )
//                    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI_New = true
//                    OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI_New = true
//                    OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser_New = true
//                    OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser_New = true
//                    )
//            ) THEN
//                    INCLUDE
//            ENDIF

              // Bruk BarnetilleggGrunnlagTolkning.harBarnetillegg() for "og barnetillegg"
//            IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN
//                    INCLUDE
//            ENDIF


            // Bruk EtteroppgjoerTolkning.avviksbeloepUfoeretrygd() for "uføretrygd" i "for lite"
//            IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN
//                    INCLUDE
//            ENDIF


            // Bruk  EtteroppgjoerTolkning.avviksbeloepUfoeretrygd() + EtteroppgjoerTolkning.avviksbeloepSaerkullsbarn + EtteroppgjoerTolkning.avviksbeloepFellesbarn()  for "og barnetillegg" i "for lite"
//            IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0
//                    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)) THEN
//            INCLUDE
//            ENDIF

            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                val avviksbelopUtenMinus = avviksbelopUtenMinus.format()
                textExpr(
                    Bokmal to "Vi har mottatt nye inntektsopplysninger fra Skatteetaten. Vi har ut fra disse opplysningene vurdert hva du skulle hatt i uføretrygd og barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Resultatet av etteroppgjøret viser at du har fått ".expr() + avviksbelopUtenMinus + " kroner for lite i uføretrygd og barnetillegg. Dette beløpet får du normalt utbetalt innen 7 dager.",
                    Nynorsk to "Vi har fått nye inntektsopplysningar frå Skatteetaten. Vi har ut frå desse opplysningane vurdert kva du skulle hatt i uføretrygd og barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Resultatet av etteroppgjeret viser at du har fått ".expr() + avviksbelopUtenMinus + " kroner for lite i uføretrygd og barnetillegg. Dette beløpet får du normalt utbetalt innan sju dagar.",
                    )
            }

            includePhrase(ViktigAALeseHeleBrevet)

            paragraph{
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "Grunngiving for vedtaket",
                )
            }
            paragraph{
                text(
                    Bokmal to "Hvert år når skatteoppgjøret er klart gjør vi et etteroppgjør av uføretrygden. Da kontrollerer vi om utbetalingen for året er riktig.",
                    Nynorsk to "Kvart år når skatteoppgjeret er klart, gjer vi eit etteroppgjer av uføretrygda. Etteroppgjeret kontrollerer om du fekk rett utbetalt i fjor. Då kontrollerer vi om utbetalinga for året er riktig.",
                )
            }

            // PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT
            // bruk Etteroppgjoer.avviksbeloepUfoeretrygd() for første og andre setning "for barn"

            // BarnetilleggGrunnlagTolkning.harBarnetillegg() for andre setning
            // for barn : PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB and PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB and  PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull

            // Bruk kombinasjonen av BarnetilleggFellesbarnTolkning, SaerkullsbarnTolkning og grunnlagTolkning for første "Det er personninngekten din.."
//            IF((PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true)
//                    OR(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true))
//            AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true THEN
//                    INCLUDE
//            ENDIF


            paragraph{
                text(
                    Bokmal to "Det er din pensjonsgivende inntekt som avgjør hvor mye du skulle ha fått i uføretrygd og gjenlevendetillegg. " +
                            "For barn som ikke bor med begge foreldre er det Det er personinntekten din som har betydning for størrelsen på barnetillegget. " +
                            "For barn som bor sammen med begge foreldre er det Det er personinntekten til deg og annen forelder som har betydning for størrelsen på barnetillegget.",
                    Nynorsk to "Det er den pensjonsgivande inntekta som avgjer kor mykje du skulle ha fått i uføretrygd og attlevandetillegg. For barn som ikkje bur saman med begge foreldra sine er det Det er personinntekta di som har noko å seie for storleiken på barnetillegget ditt. For barn som bur saman med begge foreldra sine er det Det er personinntekta di og personinntekta til den andre forelderen som har noko å seie for storleiken på barnetillegget ditt.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Vi gjør først en vurdering av om du har fått for mye eller for lite i uføretrygd. Uføretrygden regnes med som personinntekt, og har derfor betydning for om du har fått for mye eller for lite i barnetillegg. Fordi du mottok barnetillegg både for barn som bodde sammen med begge sine foreldre og for barn som ikke bodde sammen med begge foreldrene, har vi for begge barnetilleggene vurdert om du har fått for mye eller for lite.",
                    Nynorsk to "Vi vurderer først om du har fått for mykje eller for lite i uføretrygd. Uføretrygda blir rekna med som personinntekt og har derfor betydning for om du har fått for mykje eller for lite i barnetillegg. Fordi du fekk barnetillegg både for barn som budde saman med begge foreldra sine, og for barn som ikkje budde saman med begge foreldra, har vi for begge barnetillegga vurdert om du har fått for mykje eller for lite.",
                )
            }
            paragraph{
                val persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT = persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT.format()
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                val vedtaksdataEtteroppgjorResultatInntektUT = vedtaksdataEtteroppgjorResultatInntektUT.format()

                textExpr(
                    Bokmal to "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på ".expr() + persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT + " kroner i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. Etterbetaling du har fått fra NAV trekkes fra dette beløpet. Vi har derfor brukt ".expr() + vedtaksdataEtteroppgjorResultatInntektUT + " kroner i vurderingen av om du har fått for mye eller for lite i uføretrygd.",
                    Nynorsk to "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på ".expr() + persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT + " kroner i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Inntekt du hadde før du fekk innvilga uføretrygd, og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. Etterbetalinga du fekk frå NAV blir trekt frå dette beløpet. Vi har derfor brukt ".expr() + vedtaksdataEtteroppgjorResultatInntektUT + " kroner når vi har vurdert om du har fått for mykje eller for lite i uføretrygd.",
                )
            }
            paragraph{
                val vedtaksdataEtteroppgjorResultatTotalBelopUT = vedtaksdataEtteroppgjorResultatTotalBelopUT.format()
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopUT + " kroner i uføretrygd i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fikk imidlertid ".expr() + vedtaksdaEtteroppgjorResultatTidligereBelopUT.format() + " kroner. Du har derfor fått ".expr() + avviksbelopUTUtenMinus.format() + " kroner for myelite i uføretrygd.",
                    Nynorsk to "Du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopUT + " kroner i uføretrygd i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fekk derimot ".expr() + vedtaksdaEtteroppgjorResultatTidligereBelopUT.format() + " kroner. Du har derfor fått ".expr() + avviksbelopUTUtenMinus.format() + " kroner for mykjelite i uføretrygd.",
                )
            }
            paragraph{
                val persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT = persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT.format()
                val vedtaksdataEtteroppgjorResultatTotalBelopUT = vedtaksdataEtteroppgjorResultatTotalBelopUT.format()
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                val vedtaksdaEtteroppgjorResultatTidligereBelopUT = vedtaksdaEtteroppgjorResultatTidligereBelopUT.format()
                val avviksbelopUTUtenMinus = avviksbelopUTUtenMinus.format()
                textExpr(
                    Bokmal to "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på ".expr() + persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT + " kroner. Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. Etterbetaling du har fått fra NAV trekkes fra dette beløpet. Disse opplysningene viser at du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopUT + " kroner i uføretrygd i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fikk imidlertid ".expr() + vedtaksdaEtteroppgjorResultatTidligereBelopUT + " kroner. Du har derfor fått ".expr() + avviksbelopUTUtenMinus + " kroner for myelite i uføretrygd.",
                    Nynorsk to "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på ".expr() + persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT + " kroner. Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. Etterbetalinga du fekk frå NAV blir trekt frå dette beløpet. Desse opplysningane viser at du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopUT + " kroner i uføretrygd i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fekk derimot ".expr() + vedtaksdaEtteroppgjorResultatTidligereBelopUT + " kroner. Du har derfor fått ".expr() + avviksbelopUTUtenMinus + " kroner for mykjelite i uføretrygd.",
                )
            }
            paragraph{
                val vedtaksdataEtteroppgjorResultatTotalBelopTFB = vedtaksdataEtteroppgjorResultatTotalBelopTFB.format()
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                val vedtaksdataEtteroppgjorResultatTidligereBelopTFB = vedtaksdataEtteroppgjorResultatTidligereBelopTFB.format()
                val avviksbelopTFBUtenMinus = avviksbelopTFBUtenMinus.format()
                textExpr(
                    Bokmal to "Personinntekten til både deg og annen forelder har betydning for størrelsen på barnetillegget for barn som bor med begge sine foreldreditt. Uføretrygden blir regnet med som personinntekt. Vi har mottatt inntektsopplysninger for deg og annen forelder. Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. Etterbetaling du har fått fra NAV trekkes fra dette beløpet. Opplysningene om inntekt for deg og annen forelder viser at du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopTFB + " kroner i barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fikk imidlertid ".expr() + vedtaksdataEtteroppgjorResultatTidligereBelopTFB + " kroner. Du har derfor fått ".expr() + avviksbelopTFBUtenMinus + " kroner for litemye i barnetillegg for barn som bor med begge foreldre.",
                    Nynorsk to "Både personinntekta di og personinntekta til den andre forelderen har betydning for storleiken på barnetillegget for barn som bur saman med begge foreldra sine. Uføretrygda blir rekna med som personinntekt. Vi har fått inntektsopplysningar for deg og for den andre forelderen. Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Inntekt du hadde før du fekk innvilga uføretrygd og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. Etterbetalinga du fekk frå NAV blir trekt frå dette beløpet. Opplysningane om inntekta til deg og den andre forelderen viser at du skulle ha fått ".expr() + vedtaksdataEtteroppgjorResultatTotalBelopTFB + " kroner i barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ". Du fekk derimot ".expr() + vedtaksdataEtteroppgjorResultatTidligereBelopTFB + " kroner. Du har derfor fått ".expr() + avviksbelopTFBUtenMinus + " kroner for litemykje i barnetillegg for barnet/barna som bur saman med begge foreldra sine.",
                )
            }
            paragraph{
                textExpr(
                    Bokmal to "Personinntekten din har betydning for størrelsen på barnetillegget for barn som ikke bor sammen med begge foreldrene ditt. Uføretrygden blir regnet med som personinntekt. Vi har mottatt inntektsopplysninger for deg. Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. Etterbetaling du har fått fra NAV trekkes fra dette beløpet. Opplysninger om inntekten din viser at du skulle ha fått ".expr() + vedtaksbrevVedtaksdataEtteroppgjorResultatTotalBelopTSB.format() + " kroner i barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear.format() + ". Du fikk imidlertid ".expr() + vedtaksdataEtteroppgjorResultatTidligereBelopTSB.format() + " kroner. Du har derfor fått ".expr() + avviksbelopTSBUtenMinus.format() + " kroner for litemye i barnetillegg for barn som ikke bor med begge foreldre.",
                    Nynorsk to "Personinntekta di har betydning for storleiken på barnetillegget for barn som ikkje bur saman med begge foreldra sine. Uføretrygda blir rekna med som personinntekt. Vi har fått inntektsopplysningar for deg. Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Inntekt du hadde før du fekk innvilga uføretrygd og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. Etterbetalinga du fekk frå NAV blir trekt frå dette beløpet. Opplysningane om inntekta di viser at du skulle ha fått ".expr() + vedtaksbrevVedtaksdataEtteroppgjorResultatTotalBelopTSB.format() + " kroner i barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear.format() + ". Du fekk derimot ".expr() + vedtaksdataEtteroppgjorResultatTidligereBelopTSB.format() + " kroner. Du har derfor fått ".expr() + avviksbelopTSBUtenMinus.format() + " kroner for litemykje i barnetillegg for barn som ikkje bur med begge foreldra.",
                )
            }
            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Uføretrygden din har vært riktig beregnet ut fra inntekt i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                    Nynorsk to "Uføretrygda di har vore rett berekna ut frå inntekta di i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                )
            }
            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Barnetillegget ditt Barnetilleggene dine har vært riktig beregnet ut fra inntekt i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                    Nynorsk to "Barnetillegget ditt Barnetilleggane dine har vore rett berekna ut frå inntekta i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                )
            }
            paragraph{
                textExpr(
                    Bokmal to "Uføretrygden din og barnetilleggBarnetillegg for barn som bor med begge foreldre har vært riktig beregnet ut fra inntekt i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear.format() + ".",
                    Nynorsk to "Uføretrygda di og barnetilleggBarnetillegg for barn som bur saman med begge foreldra sine har vore rett berekna ut frå inntekta i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear.format() + ".",
                )
            }
            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Uføretrygden din og barnetilleggBarnetillegg for barn som ikke bor med begge foreldre har vært riktig beregnet ut fra inntekt i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                    Nynorsk to "Uføretrygda di og barnetilleggBarnetillegg for barn som ikkje bur saman med begge foreldra sine har vore rett berekna ut frå inntekta i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                )
            }
            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Til sammen har du fått ".expr() + avviksbelopUtenMinus.format() + " kroner for lite i uføretrygd og barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                    Nynorsk to "Til saman har du fått ".expr() + avviksbelopUtenMinus.format() + " kroner for lite i uføretrygd og barnetillegg i ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + ".",
                )
            }
            paragraph{
                text(
                    Bokmal to "Du kan lese mer om etteroppgjør i vedlegget «Opplysninger om etteroppgjøret».",
                    Nynorsk to "Du kan lese meir om etteroppgjer i vedlegget «Opplysningar om etteroppgjeret».",
                )
            }
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-14.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-14.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 12-16.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 12-16.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 12-18.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 12-18.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-16 og 12-18.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-16 og 12-18.",
                )
            }
            paragraph{
                text(
                    Bokmal to "For deg som har fått flere vedtak om etteroppgjør for samme år",
                    Nynorsk to "For deg som har fått fleire vedtak om etteroppgjer for same år",
                )
            }
            paragraph{
                val uforetrygdEtteroppgjorPeriodeFomYear = uforetrygdEtteroppgjorPeriodeFomYear.format()
                textExpr(
                    Bokmal to "Vi har mottatt nye inntektsopplysninger fra Skatteetaten for året ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + " og vi har derfor gjort et nytt etteroppgjør. Ditt nye etteroppgjør erstatter ikke tidligere etteroppgjør for samme år. Det betyr at alle vedtak om etteroppgjør er gjeldende.",
                    Nynorsk to "Vi har fått nye inntektsopplysningar frå Skatteetaten for året ".expr() + uforetrygdEtteroppgjorPeriodeFomYear + " og vi har derfor gjort eit nytt etteroppgjer. Det nye etteroppgjeret ditt erstattar ikkje tidlegare etteroppgjer for same år. Det betyr at alle vedtak om etteroppgjer gjeld.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Etterbetaling av beløpet",
                    Nynorsk to "Etterbetaling av beløpet",
                )
            }
//            paragraph{
//                textExpr(
//                    Bokmal to "I etterbetalingen trekker vi skatt etter standardsatser fra Skatteetaten. På utbetalingsmeldingen ser du hvilken skattesats som er brukt. ".expr() + FF01_NAV + " kan redusere etterbetalingen din dersom du har:",
//                    Nynorsk to "I etterbetalinga trekkjer vi skatt etter standardsatsar frå Skatteetaten. På utbetalingsmeldinga ser du kva skattesats som er brukt. ".expr() + FF01_NAV + " kan redusere etterbetalinga di dersom du har:",
//                )
//            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Feilutbetalingsgjeld hos NAV",
                            Nynorsk to "Feilutbetalingsgjeld hos NAV",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Ettergitt feilutbetaling av uføretrygd og barnetillegg for samme år",
                            Nynorsk to "ettergitt feilutbetaling av uføretrygd og barnetillegg for same år",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Bidragsgjeld hos NAV",
                            Nynorsk to "Bidragsgjeld hos NAV",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Utbetaling fra en tjenestepensjonsordning som krever refusjon",
                            Nynorsk to "Utbetaling frå ei tenestepensjonsordning som krev refusjon",
                        )
                    }
                }
            }

            paragraph{
                text(
                    Bokmal to "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, mottar du et eget brev om dette. Refusjon fra en tjenestepensjonsordning ser du på utbetalingsmeldingen.",
                    Nynorsk to "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, får du eit eige brev om dette. Refusjon frå ei tenestepensjonsordning ser du på utbetalingsmeldinga.",
                )
            }
            paragraph{
                text(
                    Bokmal to "Du må melde fra om endringer i inntekten",
                    Nynorsk to "Du må melde frå om endringar i inntekta",
                )
            }
//            paragraph{
//                textExpr(
//                    Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden og barnetillegget blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på ".expr() + FF01_webadresse_nav + ".",
//                    Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda og barnetillegget blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på ".expr() + FF01_webadresse_nav + ".",
//                )
//            }

            includePhrase(RettTilAAKlage)
            includePhrase(HarDuSpoersmaalEtteroppgjoer)

            paragraph{
                text(
                    Bokmal to "Med vennlig hilsen",
                    Nynorsk to "Med vennleg helsing",
                )
            }
//            paragraph{
//                textExpr(
//                    Bokmal to .expr() + felles.avsenderEnhet + ,
//                    Nynorsk to .expr() + felles.avsenderEnhet + ,
//                )
//            }
            paragraph{
                text(
                    Bokmal to "Saken har blitt automatisk saksbehandlet av vårt fagsystem. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
                    Nynorsk to "Saka er automatisk saksbehandla av fagsystemet vårt. Vedtaksbrevet er derfor ikkje skrive under av saksbehandlar.",
                )
            }
        }
    }
}