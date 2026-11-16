CREATE FUNCTION bekreft_nyhetsvarsel_via_visning() RETURNS trigger AS $$
BEGIN
    INSERT INTO nyhetsvarselstatus ("navIdent", "nyhetsvarselId", "gjennomfoertOmvisningen")
    VALUES (NEW."navIdent", NEW."nyhetsvarselId", NEW.bekreftet)
    ON CONFLICT ("navIdent", "nyhetsvarselId")
        DO UPDATE SET "gjennomfoertOmvisningen" = COALESCE(nyhetsvarselstatus."gjennomfoertOmvisningen", EXCLUDED."gjennomfoertOmvisningen");
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER bekreft_nyhetsvarsel_via_visning
    INSTEAD OF INSERT ON bekreftetNyhetsvarsel
    FOR EACH ROW EXECUTE FUNCTION bekreft_nyhetsvarsel_via_visning();
