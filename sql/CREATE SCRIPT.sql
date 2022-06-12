drop table if exists Miesta cascade;
CREATE TABLE Miesta (
    ID SERIAL PRIMARY KEY,
    adresa varchar(20000),
    mesto varchar(20000),
    psc int,
    stat varchar(20000)
);
drop table if exists Zakaznik cascade;
CREATE TABLE Zakaznik (
    ID SERIAL PRIMARY KEY,
    ICO bigint,
    DIC bigint,
    meno varchar(20000),
    email varchar(20000),
    cislo varchar(20000),
    ISIC varchar(20000)
);
ALTER TABLE Zakaznik
ADD miesto integer REFERENCES Miesta (ID) ON DELETE CASCADE;
drop table if exists Dopyt cascade;
CREATE TABLE Dopyt (
    ID SERIAL PRIMARY KEY,
    priame_vyhladanie varchar UNIQUE,
    pocet_vyhladani bigint
);
drop table if exists Vyhladavanie cascade;
CREATE TABLE Vyhladavanie (
    ID SERIAL  PRIMARY KEY,
    zakaznik_id integer REFERENCES Zakaznik (ID) ON DELETE CASCADE,
    priame_vyhladanie int,
    datum_vyhladavania DATE
);

ALTER TABLE Vyhladavanie
ADD dopyt_id integer REFERENCES Dopyt (ID) ON DELETE CASCADE;
drop table if exists Produkty cascade;
CREATE TABLE Produkty (
    ID SERIAL  PRIMARY KEY,
    meno_produktu varchar,
    specifikacie varchar,
    popis varchar(20000),
    cena int,
    dostupnost int,
    upc bigint,
    kategoria varchar(20000),
    zlava int,
    reklamovanost double PRECISION,
    pocet_predanych_kusov bigint
);
drop table if exists PROD_ATRIB_LIST cascade;
CREATE TABLE PROD_ATRIB_LIST (
    ID SERIAL NOT NULL PRIMARY KEY,
    produkt integer REFERENCES Produkty (ID) ON DELETE CASCADE,
    Meno varchar(20000)
);


drop table if exists Atributy cascade;
CREATE TABLE Atributy (
    ID SERIAL  PRIMARY KEY,
    produkty_id integer REFERENCES PROD_ATRIB_LIST (ID) ON DELETE CASCADE,
    hodnota varchar(20000)
);
drop table if exists Kosik cascade;
CREATE TABLE Kosik (
    ID SERIAL  PRIMARY KEY,
    zakaznik integer REFERENCES Zakaznik (ID) ON DELETE CASCADE,
    Meno varchar(20000)
);
drop table if exists Obsah_Kosika cascade;
CREATE TABLE Obsah_Kosika (
    ID SERIAL  PRIMARY KEY,
    produkt integer REFERENCES Produkty (ID) ON DELETE CASCADE,
    kosik integer REFERENCES Kosik (ID) ON DELETE CASCADE,
    pocet_poloziek int
);
drop table if exists Objednavky cascade;
CREATE TABLE Objednavky (
    ID SERIAL  PRIMARY KEY,
    zakaznik integer REFERENCES Zakaznik (ID) ON DELETE CASCADE,
    kosik integer REFERENCES Kosik (ID) ON DELETE CASCADE,
    datum DATE,
    poznamka VARCHAR,
    stav int
);
drop table if exists Doprava cascade;
CREATE TABLE Doprava (
    ID SERIAL  PRIMARY KEY,
    zakaznik integer REFERENCES Objednavky (ID) ON DELETE CASCADE,
    track_id varchar,
    datum DATE,
    miesto_dorucenia int REFERENCES Miesta (ID) ON DELETE CASCADE,
    typ_dovozu INT,
    stav INT
);
drop table if exists Faktury cascade;
CREATE TABLE Faktury (
    ID SERIAL  PRIMARY KEY,
    objednavky int REFERENCES Objednavky (ID) ON DELETE CASCADE,
    suma int,
    datum_platby DATE,
    datum_vystavenia DATE,
    miesto_dorucenia int REFERENCES Miesta (ID) ON DELETE CASCADE,
    poznamka varchar
);