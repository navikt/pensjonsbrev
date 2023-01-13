@file:Suppress("unused")

package no.nav.pensjon.brev.api.model

enum class Sivilstand {
    ENSLIG,
    ENKE,
    GIFT,
    GIFT_LEVER_ADSKILT,
    SEPARERT,
    PARTNER,
    PARTNER_LEVER_ADSKILT,
    SEPARERT_PARTNER,
    SAMBOER1_5,
    SAMBOER3_2,
}

enum class Sakstype {
    AFP,
    ALDER,
    UFOEREP,
    BARNEP,
}

enum class Institusjon {
    FENGSEL,
    HELSE,
    SYKEHJEM,
    INGEN,
}

enum class Beregningsmetode {
    AUSTRALIA,
    CANADA,
    CHILE,
    EOS,
    FOLKETRYGD,
    INDIA,
    ISRAEL,
    NORDISK,
    PRORATA,
    SOR_KOREA,
    SVEITS,
    USA
}

enum class KravAarsak {
    AFP_EO,                           // AFP etteroppgjør
    ALDERSOVERGANG,                   // Aldersovergang
    ANNEN_ARSAK,                      // Annen årsak til saksbehandling
    ANNEN_ARSAK_END_IN,               // Annen årsak til inntektsendring
    ANNEN_FOR_END_IN,                 // Annen forelder har endret inntekt
    BARN_DOD,                         // Dødsfall barn
    BARN_ENDRET_INNTEKT,              // Inntekt til barn er endret
    BEGGE_FOR_END_IN,                 // Begge forsørgerne har endret inntekt
    ENDRET_INNTEKT,                   // Inntekt til bruker er endret
    ENDRET_OPPTJENING,                // Opptjeningsgrunnlag er endret
    ENDRING_IFU,                      // Endring av IFU
    ENDR_ANNEN_SAK,                   // Bruker har annen sak som er endret
    EPS_ENDRET_INNTEKT,               // Inntekt til tilstøtende er endret
    EPS_NY_YTELSE,                    // Tilstøtende har fått innvilget pensjon
    EPS_NY_YTELSE_UT,                 // Tilstøtende har fått innvilget uføretrygd
    EPS_OPPH_YTELSE_UT,               // Tilstøtende sin uføretrygd er opphørt
    GJNL_SKAL_VURD,                   // Gjenlevendetilegg skal vurderes
    GODSKR_OVERF,                     // Godskriving/overføring
    GOMR,                             // G-omregning
    HJEMMEBER_FOR_UT,                 // Hjemmeberegning for uføretrygd
    INNT_KONTROLL,                    // Inntektskontroll
    INNVANDRET,                       // Innvandring
    INSTOPPHOLD,                      // Institusjonsopphold på bruker/tilstøtende
    KLAGE_ANKE,                       // Klage/anke
    KONTR_3_17,                       // Kontroll 3-17 a
    NYE_OPPLYSNINGER,                 // Nye opplysninger på bruker
    NY_SOKNAD,                        // Ny søknad
    OMGJ_ETTER_ANKE,                  // Omgjøring etter anke
    OMGJ_ETTER_KLAGE,                 // Omgjøring etter klage
    OMREGN_UFORETRYGD,                // Omregning til uføretrygd
    OPPHOR,                           // Opphør av brukers ytelse
    OPPL_UTLAND,                      // Opplysninger fra utlandet
    REGULERING,                       // Regulering av brukers ytelse
    REKONSTRUKSJON,                   // Rekonstruksjon
    SIVILSTANDSENDRING,               // Sivilstandsendring
    TILBAKEKREVING,                   // Tilbakekreving
    TILSTOT_ENDR_YTELSE,              // Tilstøtende sin sak er endret
    TILSTOT_OPPHORT,                  // Opphør av tilstøtendes ytelse
    TILST_DOD,                        // Dødsfall tilstøtende
    UFOREBER_V_GTGRD,                 // Uføreberegning ved garantigrad
    UFOREBER_V_TIDUFT,                // Uføreber. ved tidl. Uføretidspunkt
    UFOREOVERGANG,                    // Uføretrygdovergang
    UTTAKSGRAD,                       // Uttaksgrad er endret eller oppdatert
    UTVANDRET,                        // Utvandring/eksport
    UT_EO,                            // Etteroppgjør
    UT_VURDERING_EO,                  // Vurdering Etteroppgjør
    VURDER_FORSORG,                   // Forsørgingstillegg skal vurderes
    VURD_SIVILST,                     // Avvik i vurdert sivilstand for berørte saker
    AVSLAG_UT,                        // Avslag uføretrygd
    AVSLAG,                           // Avslag
    AVSLAG_INVAL_ENKEP,               // Avslag (Invalide-/eller enkepensjon)
    AVSLAG_UNG_UFR,                   // Avslag ung ufør
    BARN_UTDS_FLYTTEUTG,              // Barnetilsyn/utd.stønad/flytteutg.
    BARNETILLEGG,                     // Barnetillegg
    EKSPORT,                          // Eksport
    EKTEFELLETILLEGG,                 // Ektefelletillegg
    ETTEROPPGJOR,                     // Etteroppgjør
    GJENLEVENDERETT,                  // Gjenlevenderett
    GJENLEVENDETILLEGG,               // Gjenlevendetillegg
    GRADSENDRINGER,                   // Gradsendringer
    HVIL_STONADSRETT,                 // Hvilende stønadsrett
    MEDISINSK_STONAD,                 // Medisinsk stønad
    MEDL_TRYGDETID,                   // Medlemskap/trygdetid
    MEDL_TRYGDETID_OPPTJ,             // Medlemskap/trygdetid/opptjening
    OMSORG_FOR_SMA_BARN,              // Omsorg for små barn
    OPPHOR_HJELPELOS,                 // Opphør av hjelpeløshetsbidrag
    OPPHOR_RED_UFG,                   // Opphør/reduksjon uføregrad
    OPPHOR_REDUKSJON,                 // Opphør/reduksjon
    OPPHOR_TLG_HJELP_HUS,             // Opphør av tillegg til hjelp i huset
    OPPTJENINGSGRUNNLAG,              // Opptjeningsgrunnlag
    OVERGANGSSTONAD,                  // Overgangsstønad
    OVRFR_OMSORGSPOENG,               // Overføring av omsorgsopptjening
    PENSJONSVILKAR,                   // Pensjonsvilkår
    PLEIE_ELDR_SYK_FUNK,              // Pleie eldre/syke/ funksjonshemmede
    SAMORDNING,                       // Samordning
    TIDLIGUTTAK,                      // Tidliguttak
    UFG_IFU_OG_IEU,                   // Uføregrad/IFU og IEU
    UFR_GRAD,                         // Uføregrad
    UFR_PENSJON_GRAD,                 // Uføre-/pensjonsgrad
    UFR_TIDSPUNKT,                    // Uføretidspunkt
    UTBET_AVKORT,                     // Utbetaling/avkortning
    VILKAR,                           // Vilkår
    VIRK_TIDSPUNKT,                   // Virkningstidspunkt
    YRK_SKADE_SYK,                    // Yrkesskade/-sykdom
    YRKESSKADE,                       // Yrkesskade
    ANNET,                            // Annet
    BODD_ARB_UTL,                     // Bodd/arbeidet i avtaleland
    UT_OMGJ_ANKE_EO,                  // Omgjøring etter anke
    UT_OMGJ_KLAGE_EO,                 // Omgjøring etter klage
    SOKNAD_BT,                        // Søknad om barnetillegg
    OMREGNING,                        // Omregning pga regelendring
    OMR_FEILRETTING,                  // Omregning etter feilretting
    VURDER_SERSKILT_SATS;             // Særskilt sats for forsørger skal vurderes
}