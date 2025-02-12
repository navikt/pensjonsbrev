package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU4050_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // [TBU4050_NN, TBU4050]

        paragraph {
            // IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))) {
                text(
                    Bokmal to "Uføretrygden din og barnetillegg",
                    Nynorsk to "Uføretrygda di og barnetillegg",
                )
            }

            // IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB = 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0))) {
                text(
                    Bokmal to "Barnetillegg",
                    Nynorsk to "Barnetillegg",
                )
            }
            textExpr(
                Bokmal to " for barn som bor med begge foreldre har vært riktig beregnet ut fra inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
                Nynorsk to " for barn som bur saman med begge foreldra sine har vore rett berekna ut frå inntekta i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
            )
        }
    }
}
