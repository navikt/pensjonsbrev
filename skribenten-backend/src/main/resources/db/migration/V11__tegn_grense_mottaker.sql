alter table mottaker
    alter column navn type varchar(128) using navn::varchar(128),
    alter column postnummer type varchar(4) using postnummer::varchar(4),
    alter column poststed type varchar(50) using poststed::varchar(50),
    alter column adresselinje1 type varchar(128) using adresselinje1::varchar(128),
    alter column adresselinje2 type varchar(128) using adresselinje2::varchar(128),
    alter column adresselinje3 type varchar(128) using adresselinje3::varchar(128);

