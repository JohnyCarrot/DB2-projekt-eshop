BEGIN;
CREATE EXTENSION IF NOT EXISTS tsm_system_rows;
-- niektore veci su skopcene od Dr. Šimku (konkretne zo sample projektu)
truncate table atributy, doprava, dopyt, faktury, kosik, miesta, objednavky, obsah_kosika, produkty, prod_atrib_list, vyhladavanie,zakaznik restart identity cascade;
--zacinam pomocnymi generovacimi funkciami--
drop index if exists idx_atributy;
drop index if exists idx_doprava;
drop index if exists idx_dopyt;
drop index if exists idx_faktury;
drop index if exists idx_kosik;
drop index if exists idx_miesta;
drop index if exists idx_objednavky;
drop index if exists idx_obsah_kosika;
drop index if exists idx_atrib_list;
drop index if exists idx_produkty;
drop index if exists idx_vyhladavanie;
drop index if exists idx_zakaznik;
drop table if exists staty cascade;
create table staty
(
   meno varchar
);

insert into staty (meno)
values	('Slovensko'), ('Česko'), ('Poľsko'), ('Maďarsko'), ('Rakúsko'),
	('Nemecko'), ('India'), ('Taiwan'), ('USA'), ('Austrália'),
	('Rumunsko'), ('Ukraina'), ('Írsko'), ('Vatikán'), ('Arménsko'),
	('Somálsko'), ('Kuba'), ('Venezuela'), ('Čierna Hora'), ('Čile');
drop table if exists adresy cascade;
create table adresy
(
   meno varchar
);

insert into adresy (meno)
values	('Hurbanová'), ('Bajkalská'), ('Dohnányho'), ('Družba'), ('Andrejova'),
	('Šútyho'), ('Kotlebová'), ('Ulica Súdruha Stalina'), ('Červené námestie'), ('Kočajdu'),
	('Čoláková'), ('Ulica nemeckého Ovčiaka'), ('Grilová'), ('Riečna cesta'), ('Námestie slobody'),
	('Kamenná'), ('Náhodná ulica'), ('Drevenná'), ('Letecká ulica'), ('Riečna cesta');

drop table if exists mesta cascade;
create table mesta
(
   meno varchar
);

insert into mesta (meno)
values	('Bratislava'), ('Košice'), ('Trenčín'), ('Trnava'), ('Topoľčany'),
	('Nitra'), ('Kamanová'), ('Robot'), ('Pičín'), ('Aňala'),
	('Šarkan'), ('Rozkoš'), ('Potvorice'), ('Haluzice'), ('Držkovce'),
	('Plešivec'), ('Krvavé šenky'), ('Vybúchanec'), ('Lada'), ('Dedina Mládeže');

-- do tadeto sa generuju pre kolonku miesta--atributy
drop table if exists meno cascade;
create table meno
(
   meno varchar
);

insert into meno (meno)
values	('Rolex'), ('Andrej'), ('bc.Marek'), ('Jožo'), ('Michal'),
	('Jakub'), ('Roman'), ('Saimon'), ('Konsuela'), ('Roko'),
	('Sofia'), ('Ľubomíra'), ('Samuel'), ('Lukáš'), ('Jozef'),
	('Bohuš'), ('Ľuboš'), ('Robert'), ('Lada'), ('Juraj');

drop table if exists priezvisko cascade;
create table priezvisko
(
   meno varchar
);

insert into priezvisko (meno)
values	('Slížik'), ('Voznár'), ('Hupka'), ('Trnava'), ('Boglárka'),
	('Kováč'), ('Šuster'), ('Robot'), ('Horvátová'), ('Aňala'),
	('Nevolbová'), ('Rozkoš'), ('Fábry'), ('Konc'), ('Držkovič'),
	('Špageta'), ('Šenky'), ('Fiko'), ('Hada'), ('Mag');

drop table if exists email cascade;
create table email
(
   meno varchar
);

insert into email (meno)
values	('nikto@gmail.com'), ('pussykiller69@centrum.sk'), ('ovciansky_tribunal@azet.sk'), ('hrdi-slovak@seznam.cz'), ('majkl.dzeksn@gavari.ru'),
	('wakanada@4ever.com'), ('ruzovy-kon@gay.eu'), ('null@pointer.exception'), ('foku@me.sk'), ('lala@sample-email.sk'),
	('definitely-not@16.sk'), ('random@mail.com'), ('nobody@bad-karma.com'), ('google_was_my_idea@gmail.com'), ('microsoft.sucks@outlook.com'),
	('marvel-is-copy-of@dc.com'), ('sausage@vegan.com'), ('kiss-my-axe@lgbti.com'), ('stinky@pinky.com'), ('average-student@vssvalzbety.sk');


drop table if exists isic cascade;
create table isic
(
   meno varchar
);

insert into isic (meno)
values	('S142502011854'), ('S159745632178'), ('S956328040972'), ('S156987489655'), ('S023056103982'),
	('S236213473105'), ('S106975302872'), ('S632547821402'), ('S641235789314'), ('S232123647942'),
	('S985654523657'), ('S412987620304'), ('S965452301202'), ('S312547985234'), ('S123658896512'),
	('S034789654023'), ('S236547952013'), ('S854636902839'), ('S012358793201'), ('S012897456312');

drop table if exists tel cascade;
create table tel
(
   meno varchar
);

insert into tel (meno)
values	('+421982456321'), ('+421254789321'), ('+421952365789'), ('+420905236517'), ('+39102587963'),
	('+421902354010'), ('+421908886555'), ('+420904632789'), ('+421958792159'), ('+421925879521'),
	('+420982548956'), ('+22902875216'), ('+422620215879'), ('+421963254750'), ('+421908287237'),
	('+421902135698'), ('+421907776333'), ('+421902447999'), ('+4219022224666'), ('+42192225444');

 -- tu konci zakaznik tabulka--

drop table if exists prod_atrib_spec cascade;
create table prod_atrib_spec
(
   meno varchar
);

insert into prod_atrib_spec (meno)
values	('Špecifikácie'), ('Vlastnosti'), ('Výkon a formát'), ('Výbava'), ('Účinnosť'),
	('Konektory'), ('Rozmery'), ('Typ'), ('Konštrukcia'), ('Procesor'),
	('GPU'), ('Grafická karta'), ('Základná doska'), ('Disk'), ('Operačná pamäť'),
	('Produktový rad'), ('Vstupy'), ('Výstupy'), ('Napájanie'), ('Vlastnosti batérie');
--tu konci prodprod_atrib_list--
drop table if exists meno_produktu cascade;
create table meno_produktu
(
   meno varchar
);

insert into meno_produktu (meno)
values	('Sencor 2598'), ('I7 870'), ('Seasonic M-II 512'), ('Tesla CyberTruck AWD'), ('Can am 570 DPS PRO'),
	('Microsoft bestest wireless mouse'), ('CyberPunk 2077'), ('Kingstom 512GB 5Ghz RAM'), ('Samsung 3070 EVO 20TB '), ('PowerPC 2000'),
	('RTX 3090tie'), ('Apple stand PRO MAX xDRetardi ULTRA'), ('Nothing'), ('Starlink'), ('Pokemon Emerald'),
	('Mattoni'), ('0 budget headphones'), ('BenZ 40K23 Super monitor'), ('20A fuse'), ('Enterprise RedHat Linux');

drop table if exists specifikacie cascade;
create table specifikacie
(
   meno varchar
);

insert into specifikacie (meno)
values	('Gril plynový, s bočným horákom, typ: BBQ, plocha na grilovanie 2720 cm2, rozmery grilu V 116×Š 134×H 50 cm, materiál plochy na grilovanie: liatina, materiál grilu: nerez, počet horákov 4, výkon grilu 13.65 kW, hmotnosť 41kg'), ('Vonkajší ohrievač – výkon 13kW, s poistkou proti prevrhnutiu, s regulátorom hadice a s regulátorom výkonu, materiál: oceľ, rozmery 222.2×81.3×81.3cm (V×Š×H), čierna farba'), ('Herný notebook – AMD Ryzen 5 5600H, 17.3" IPS antireflexný 1920 × 1080 144Hz, RAM 16GB DDR4, NVIDIA GeForce RTX 3060 6GB 130 W, SSD 512GB, numerická klávesnica, podsvietená klávesnica, webkamera, USB-C, WiFi 6, hmotnosť 2.98kg, Bez operačného systému'), ('Herný notebook – AMD Ryzen 7 5800H, 16" IPS antireflexný 2560 × 1600 165Hz, RAM 16GB DDR4, NVIDIA GeForce RTX 3070 8GB 140 W, SSD 1000GB, numerická klávesnica, podsvietená RGB klávesnica, webkamera, USB-C, WiFi 6, hmotnosť 2.45kg, Bez operačného systému'), ('Herný notebook – Intel Core i5 10300H Comet Lake, 15.6" IPS matný 1920 × 1080 144Hz, RAM 8GB DDR4, NVIDIA GeForce GTX 1650 4GB, SSD 512GB, numerická klávesnica, podsvietená klávesnica, webkamera, USB 3.2 Gen 1, USB 3.2 Gen 2, USB-C, WiFi 6, hmotnosť 2.3kg, Windows 11 Home'),
	('Herná konzola – k TV, SSD 825 GB, Blu-ray (4K), možnosť hrania v 4K, 2× herný ovládač'), ('Procesor 6-jadrový, 12 vláken, 3.7GHz (TDP 65W), Boost 4.6 GHz, 32MB L3 cache, bez integrovaného grafického čipu, socket AMD AM4, Vermeer, box chladič, Wraith Stealth'), ('LCD monitor Quad HD 2560×1440, displej IPS, 16:9, odozva 4ms, obnovovacia frekvencia 75Hz, FreeSync, jas 350cd/m2, kontrast 1000:1, DisplayPort 1.2, HDMI 1.4, slúchadlový výstup, nastaviteľná výška, pivot, repro, VESA'), ('SSD disk 2.5", SATA III, TLC (Triple-Level Cell), rýchlosť čítania 560MB/s, rýchlosť zápisu 530MB/s, životnosť 300TBW'), ('Ventilátor do PC 120×25mm, 200RPM–1800RPM, 12V, maximálna hlučnosť 22.5dB, 4pin PWM, PWM'),
	('RTX 3090tie'), ('Apple stand PRO MAX xDRetardi ULTRA'), ('Nothing'), ('Starlink'), ('Pokemon Emerald'),
	('Externý disk 2,5" s pripojením Micro USB-B, rozhranie USB 3.2 Gen 1 (USB 3.0), kapacita 1000GB'), ('Základná doska AMD B550, socket AMD AM4, PCI Express 4.0, 2× PCIe x16, 2× PCIe x1, 4× DDR4 5100MHz (OC), 6× SATA III, 2× M.2, USB 3.2 Gen 2, USB-C, RJ-45 (LAN) 1Gbps, RJ-45 (LAN) 2,5Gbps, HDMI, DisplayPort, 8ch zvuková karta, RGB podsvietenie, formát ATX'), ('WiFi USB adaptér – vysokoziskový 802.11a/b/g/n/ac až 600Mbps, Dual-Band (433Mbps na 5GHz + 200Mbps na 2.4GHz), USB 2.0, vysokozisková externá anténa'), ('20A poistka'), ('SSD disk 2.5", SATA III, TLC (Triple-Level Cell), rýchlosť čítania 560MB/s, rýchlosť zápisu 540MB/s, životnosť 75TBW');

drop table if exists popis cascade;
create table popis
(
   meno varchar
);

insert into popis (meno)
values	('Hlavná grilovacia plocha s rozmermi 64 × 38 cm je osadená štyrmi horákmi s piezoelektrickými zapaľovaním. Ľahkú mobilitu grilu CATTARA MASTER CHEEF zaisťujú dve kolieska na jeho spodnej strane. Súčasťou grilu je tiež spojovacia hadica a redukčný ventil. '), ('Použiť môžete aj bočný varič so samostatnou reguláciou. Praktický rošt grilu udrží pripravené pochúťky teplé aj po dlhšiu dobu. Bočná strana grilu CATTARA MASTER CHEEF je vybavená háčikmi na zavesenie vášho kuchynského príslušenstva.'), ('Neoceniteľný pomocník na záhradu sa skrýva v nožniciach na trávu FERRIDA GS 8036, ktoré zaujmú svojou nízkou hmotnosťou
či kompaktnými rozmermi. S nožnicami sa tak skvele pracuje a manipuluje. Súčasťou balenia sú dve lišty - akumulátorovými nožnicami GS 8036 môžete upravovať nielen trávnik, ale aj okrasné kríky.'), ('Maximálne pohodlie a ergonómiu počas práce s nožnicami FERRIDA GS 8036 zaisťuje pogumovaná rukoväť. '), ('Praktický držiak kolies, ktorý urobí vo vašej garáži poriadok'),
	('V prípade, že držiak už nepotrebujete, možno ho veľmi ľahko zložiť a uskladniť aj do tej najmenšej štrbinky vašej garáže.'), ('Potrebujete spoľahlivé náradie na prezúvanie pneumatík? Predstavujeme vám montážnu páku YATO. Táto profesionálna páka vám určite bude vyhovovať. Je zhotovená z kvalitnej chróm-vanádiovej ocele. '), ('Čistič BOSCH EasyAquatak 120 vyniká veľkou mierou flexibility, a to nielen vďaka kompaktným rozmerom a úložnému priestoru, ale aj vďaka funkcii samonasávania. Pre používanie čističa nemusíte mať na dosah vodovod, ale postačí vám aj vhodný externý zdroj vody, ako je potrebná nádrž alebo sud na dažďovú vodu. V tomto prípade stačí do suda alebo nádrže ponoriť samonasávací filter a spustiť tlakový čistič ako obvykle.'), ('Vysokotlakový čistič Bosch EasyAquatak 120 je dodávaný s veľkým množstvom trysiek zahŕňajúcimi rotačnú a variabilnú vejárovú trysku, s ktorými sa ľahko vysporiadate aj s náročným umývaním, od terás a kvetináčov, cez bicykle a automobily až po záhradný nábytok'), ('Univerzálna sada konektorov a káblových koncoviek YATO'),
	('Plynový horák DREMEL VersaFlame je jediný stacionárny horák, ktorý sa môže pochváliť bezpečnou kombináciou použitia otvoreného plameňa s katalyzátorom a spájkovacou hlavou. Ide o najuniverzálnejší stacionárny horák na trhu určený najmä na svojpomocné práce. Plynový horák VersaFlame obsahuje zásobník so skvapalneným butánom, čo zaistí rýchly ohrev a jednoduché doplňovanie. Možno ho použiť na mäkké spájkovanie, zmršťovanie izolácie, zváranie atď.'), ('Všetko je zabalené a rozdelené v plastovom kufríku'), ('Celkom 347 kusov rôznych matíc, skrutiek, podložiek a samorezných skrutiek'), ('Výmena či doplnenie oleja bude s touto praktickou nádobou s lievikom ľahšia. Nádoba YATO vám umožní čisté a bezpečné nalievanie oleja a iných prevádzkových kvapalín automobilov vďaka flexibilnému lieviku. Je vyrobená z veľmi odolného PE materiálu a má skrutkovacie veko. Disponuje stupnicou a objemom 5 l.'), ('Veľký objem a odolný PE plast'),
	('Mierky špárové, ktoré sú vyrobené známou a overenou spoločnosťou Geko, si iste veľmi rýchlo zamilujete a určite by nemali chýbať vo vašej dielni či garáži. V tejto súprave je celkom 13 kusov mierok všetkých bežne používaných rozmerov v rozsahu 0,05 – 1 mm, ktoré sú navyše vyrobené z kvalitného kovu a sú prispôsobené pre ľahkú manipuláciu. Dĺžka miery je 100 mm a kompletná súprava je uložená v praktickom puzdre.'), ('Povoľte zhrdzavené súčiastky pomocou rýchlo pôsobiaceho uvoľňovača hrdze od firmy LIQUI MOLY. Je vhodný predovšetkým pre ťažko sa otáčajúce skrutky a iné zatuhnuté pohyblivé súčiastky. Vyznačuje sa rýchlym prienikom do tesných miest, kde spoľahlivo hrdzu rozpúšťa.'), ('Makita E-10883 je vysokokvalitná sada nástrojov, ktorá je ideálna pre širokú škálu využitia v záhrade, v dielni aj pri profesionálnej montáži na stavbách. Súprava ponúka 221 kusov nástrojov z odolnej chróm-vanádiovej ocele, ktorá je zárukou vysokej odolnosti a maximálnej spoľahlivosti. Gola sada Makita E-10883 je dodávaná v prepravnom kufri, ktorý zaistí bezpečnú a praktickú manipuláciu. Kufor je navyše vybavený praktickými priehradkami na prehľadné uloženie jednotlivých kusov náradia, takže budete mať všetko potrebné kedykoľvek k dispozícii. '), ('Gola sada Makita E-10883 obsahuje 221 kusov nástrojov pre praktické použitie v záhrade, v dielni aj na stavbách. Súčasťou balenia je napríklad 12 ks kombinovaných kľúčov, sada s 8 kusmi imbusových kľúčov, kvalitný skrutkovač s magnetickou špičkou alebo 48 ks nástrčných kľúčov. V gola sade Makita E-10883 nájdete aj 139 ks bitov šesťhran a nastaviteľný magnetický držiak.'), ('Dvojnožové záhradné nožnice Gardena B/M Comfort v modernom modrom vyhotovení s rôznofarebnými detailmi sú určené predovšetkým na strihanie a zastrihávanie kvetín a konárov. Nožnice Gardena majú maximálny priemer strihaného materiálu 24 mm.   Nože sú pevné, stabilné a špeciálne brúsené pre čistý a hladký strih. Nerezová úprava horného noža a antiadhézne vyhotovenie potom napomáhajú k ľahkej údržbe a čisteniu. Ergonomická rukoväť je vystužená sklenenými vláknami a zabezpečuje príjemnú prácu. Nožnice sú veľmi ľahké a výborne sa s nimi manipuluje.');

drop table if exists kategoria cascade;
create table kategoria
(
   meno varchar
);

insert into kategoria (meno)
values	('Procesory'), ('Grafické karty'), ('Hobby a záhrada'), ('Pre psy'), ('Pre mačky'),
	('PC Gaming'), ('Mobile Gaming'), ('Autodielňa'), ('Šport'), ('Knihy'),
	('E-knihy'), ('Mobilné telefóny'), ('Notebooky'), ('Mikrofóny'), ('Pršiplášte'),
	('Sladené nápoje'), ('Záznamové zariadenia'), ('Softvér'), ('Elektrotechnické súčiastky'), ('Pneumatiky');

-- tu koncia tabulky pre produkty--
drop table if exists poznamka cascade;
create table poznamka
(
   meno varchar
);

insert into poznamka (meno)
values	('Veľa šťastia'), ('Na Vianoce'), ('S tátárskou omáčkou a bez slaniny'), ('Zabaliť do bakelitu poprosím'), ('Nemusíte baliť do sáčku od rohlíku'),
	('Vyzdvihnem zo zadnej časti predajne'), ('Platba obličkou'), ('Platba šekom'), ('Pred doručením rozbaliť a skontrolovať obsah'), ('Pred doručením poliať svätenou vodou'),
	('Po zabalení zabaliť ešte raz !'), ('Bez letákov proprosím'), ('Xbox gamepass si môžete nechať'), ('Dodať pred Vianocami'), ('Zabaliť do Vianočného'),
	('Pred zabalením odfotiť'), ('POZOR KREHKÉ !'), ('Bez čokolády'), ('Vajnory vedla lidla'), ('Pneumatiky môžte aj nafúkať');


-- tu koncia tabulky pre objeobjednavky
drop table if exists kosik_meno cascade;
create table kosik_meno
(
   meno varchar
);

insert into kosik_meno (meno)
values	('Veľa šťastia'), ('Na Vianoce'), ('Andrejke'), ('NASko'), ('Mining RIG'),
	('Zoznam'), ('Obľúbené'), ('Pre Adama'), ('Na chatu'), ('Do hôr'),
	('Do auta'), ('Nemám na to ale chcem to'), ('Čo si jedného dňa kúpim'), ('Košík'), ('Vilo'),
	('GabeN plz'), ('PC'), ('Najčastejšie nakúpené'), ('Here we go again'), ('YOLO');
   --tu konci meno kosikov
drop table if exists poznamky_faktury cascade;
create table poznamky_faktury
(
   meno varchar
);

insert into poznamky_faktury (meno)
values	('Platené na dvoch faktúrach'), (''), (''), ('Zaplatené obličkou'), ('Rozdelené na štvrtinky bez navýšenia'),
	(''), (''), ('Platba kartou'), ('Platba v hotovosti nad hotovostný limit počas núdzového stavu'), ('Platba v hotovosti nad hotovostný limit počas núdzového stavu'),
	(''), (''), ('Platba obličkou'), ('Platba v hotovosti nad hotovostný limit počas núdzového stavu'), ('Platba pečeňou'),
	(''), (''), ('Platba v hotovosti nad hotovostný limit počas núdzového stavu'), ('Platba kartou'), ('Platba v hotovosti nad hotovostný limit počas núdzového stavu');
   --tu koncia tabulky pre faktury
drop table if exists dopyt_vyhladanie cascade;
create table dopyt_vyhladanie
(
   meno varchar
);

insert into dopyt_vyhladanie (meno)
values	('Hviezdny internet'), ('Na Vianoce'), ('Makeup'), ('RTX3090'), ('Mining RIG'),
	('Tesla'), ('ponožky'), ('USB'), ('Alkohol'), ('Spacáky'),
	('Podlaha čierna'), ('Fialové pneumatiky'), ('Bryndzové halušky'), ('radiator'), ('debilníček'),
	('PC hra'), ('PSko'), ('Najčastejšie nakúpené'), ('Niečo na narodeniny'), ('Pre mužov');

drop table if exists dopyt_vyhladanie2 cascade;
create table dopyt_vyhladanie2
(
   meno varchar
);

insert into dopyt_vyhladanie2 (meno)
values	(' s bielym poťahom'), (' do zoo'), (' v rúžovom'), (' s vodotryskom'), (' v červenom obale'),
	(' s doručením zdarma'), (' rúžové'), (' s kovovým špicom'), (' 40%'), (' do hory'),
	(' do školy'), (' na pohreb'), (' na okuliare'), (''), (' s batmanom'),
	(' do Postele'), (' so zlatou hviezdou'), (' v malej lyžičke'), (' na obraz'), (' do postele');

drop table if exists dopyt_vyhladanie3 cascade;
create table dopyt_vyhladanie3
(
   meno varchar
);

insert into dopyt_vyhladanie3 (meno)
values	('  bez hlavy'), (' do 14€'), ('    '), ('  pre psa'), ('   bez obalu'),
	('  bez doručenia zdarma'), ('  v strednej veľkosti'), (' s poistením proti rozbitiu'), (' na zvonček'), (' do 400€'),
	(' do 55€'), (' do Neba'), (' do zajtra'), (' na neučito'), (' na Mars'),
	(' bez dresingu'), (' na pštrosa'), (' do kina'), ('  bez kompromisov'), (' na sklade');
drop table if exists dopyt_vyhladanie4 cascade;
create table dopyt_vyhladanie4
(
   meno varchar
);

insert into dopyt_vyhladanie4 (meno)
values	('kusov'), ('kusy'), ('x'), ('krát'), ('ponožky'),
	('krat'), ('pocty'), ('počty'), ('usporiadané'), ('kroužky'),
	('dolárov'), ('topánky'), ('kraat'), ('krát banánové'), ('telefóny'),
	('bez chlórové'), ('nemecké'), ('vyrobené'), ('kupelnove'), ('nahodne usporiadané');

drop table if exists dopyt_vyhladanie5 cascade;
create table dopyt_vyhladanie5
(
   meno varchar
);

insert into dopyt_vyhladanie5 (meno)
values	('vyrobené na slovensku'), ('vyrobené šikovnými azíjskými ručičkami'), ('vyrobené šikovnými detskými ručičkami'), ('vyrobené podomácky'), ('dovezrné z japonska'),
	('obliate kakaom'), ('bez šálu'), ('so štyrmi kopčekmi zmrzliny'), ('bez topánok'), ('na 4 kroužky'),
	('pre dôchodcu'), ('na starobu'), ('na chuť'), ('na doma'), ('MADE IN CHINA'),
	(''), ('nemecké'), ('bez výroby'), ('bez kúpelne'), ('podľa gregorianskeho kalendára');
    -- tu koncia tabulky pre dopyt
    drop table if exists track_id cascade;
create table track_id
(
   meno varchar
);

insert into track_id (meno)
values	('RR123456789SK'), ('VV123456789SK'), ('CP123456789SK'), ('CV123456789SK'), ('CZ123456789SK'),
	('VZ123456789SK'), ('VZ923456789SK'), ('VZ999456789CZ'), ('RR546971235PL'), ('CP145987524SK'),
	('CP512348965CZ'), ('CP452102365SK'), ('CP10230159SK'), ('CP021365487SK'), ('CP702341569SK'),
	('CP402356879CZ'), ('CP102365789PL'), ('CP201365478SK'), ('RR569874521CZ'), ('VZ154698742SK');
    -- tu koncia tabulky pre track_id
        drop table if exists atributy_mena cascade;
create table atributy_mena
(
   meno varchar
);

insert into atributy_mena (meno)
values	('1 300 W'), ('ATX formát'), ('Plne modulárny'), ('Odpojiteľné káble'), ('Tepelná regulácia otáčok'),
	('80 PLUS Gold'), ('Bez certifikácie'), ('Ochrana proti prehriatiu'), ('Nadprúdová ochrana'), ('Ochrana proti preťaženiu'),
	('RGB ovládanie'), ('22,5 dB'), ('Bez regulácie otáčok'), ('1 800 RPM'), ('Fluid Dynamic Bearing (FDB) typ ložiska'),
	('Hmotnosť 26,5 kg'), ('Vykurovací výkon 13 000 W'), ('S poistkou proti prevrhnutiu'), ('Vykurovací výkon 13 kW'), ('Spotreba plynu 0,95 kg/hod');
    -- tu koncia vsetky tabulky (aj pre atributy)


    --miesta
    CREATE OR REPLACE FUNCTION random_between(low INT ,high INT)
   RETURNS INT AS
$$
BEGIN
   RETURN floor(random()* (high-low + 1) + low);        --cmajznute z https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-random-range/
END;
$$ language 'plpgsql' STRICT;
    CREATE OR REPLACE FUNCTION random_between_big(low BIGINT ,high BIGINT)
   RETURNS BIGINT AS
$$
BEGIN
   RETURN floor(random()* (high-low + 1) + low);        --cmajznute z https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-random-range/
END;
$$ language 'plpgsql' STRICT;
create or replace function random_adresa() returns varchar language sql as $$
select meno from adresy tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_mesta() returns varchar language sql as $$
select meno from mesta tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_staty() returns varchar language sql as $$
select meno from staty tablesample system_rows(20) order by random() limit 1 $$;

insert into miesta (adresa, mesto, psc, stat)
select	CONCAT(random_adresa(),' ',ROUND(random() * 100),'/',ROUND(random() * 100)),
        random_mesta(),
        random_between(19999,99999),
        random_staty()
from generate_series(1, 30) as seq(i);

-- Zakaznik
create or replace function random_meno() returns varchar language sql as $$
select meno from meno tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_priezvisko() returns varchar language sql as $$
select meno from priezvisko tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_email() returns varchar language sql as $$
select meno from email tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_isic() returns varchar language sql as $$
select meno from isic tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_tel() returns varchar language sql as $$
select meno from tel tablesample system_rows(20) order by random() limit 1 $$;

insert into zakaznik (ico, dic, meno, email,cislo,isic,miesto)
select	random_between(10000000,99999999),
        random_between(200000000,299999999),
        CONCAT(random_meno(),' ',random_priezvisko()),
        random_email(),
        random_tel(),
        random_isic(),
        random_between(1,30)
from generate_series(1, 100) as seq(i);
--produkty
create or replace function random_produkt() returns varchar language sql as $$
select meno from meno_produktu tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_specifikacie() returns varchar language sql as $$
select meno from specifikacie tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_popis() returns varchar language sql as $$
select meno from popis tablesample system_rows(20) order by random() limit 1 $$;
create or replace function random_kategoria() returns varchar language sql as $$
select meno from kategoria tablesample system_rows(20) order by random() limit 1 $$;
insert into produkty (meno_produktu, specifikacie, popis, cena,dostupnost,upc,kategoria,zlava,reklamovanost,pocet_predanych_kusov)
select	random_produkt(),
        random_specifikacie(),
        random_popis(),
        random_between(20,1999),
        random_between(0,2), --2 znamena na ceste
        random_between_big(1000000000000,9999999999999), -- UPC AKA EAN
        random_kategoria(),
        random_between(0,5),
        random()*10,
        random_between(1,30)
from generate_series(1, 100) as seq(i);


-- prod_atrib_specs
create or replace function random_spec_list() returns varchar language sql as $$
select meno from prod_atrib_spec tablesample system_rows(20) order by random() limit 1 $$;
insert into prod_atrib_list (meno,produkt)
select	random_spec_list(),
		random_between(1,100)
from generate_series(1, 100) as seq(i);
-- atributy
create or replace function random_atributy() returns varchar language sql as $$
select meno from atributy_mena tablesample system_rows(20) order by random() limit 1 $$;
insert into atributy (produkty_id,hodnota)
select	random_between(1,100),
		random_atributy()
from generate_series(1, 1500) as seq(i);
-- kosik
create or replace function random_kosik_meno() returns varchar language sql as $$
select meno from kosik_meno tablesample system_rows(20) order by random() limit 1 $$;
insert into kosik (zakaznik,meno)
select	random_between(1,100),
		random_kosik_meno()
from generate_series(1, 100) as seq(i);
-- obsah_kosika
insert into obsah_kosika (produkt,kosik,pocet_poloziek)
select	random_between(1,100),
		random_between(1,100),
		random_between(1,99)

from generate_series(1, 530) as seq(i);
--objednavky
create or replace function random_poznamka() returns varchar language sql as $$
select meno from poznamka tablesample system_rows(20) order by random() limit 1 $$;

insert into objednavky (zakaznik, kosik, datum, poznamka,stav)
select	random_between(1,100),
        random_between(1,100),
        now() - random() * INTERVAL '90 days', --https://stackoverflow.com/questions/45456735/update-every-row-with-a-random-datetime-between-two-dates
        random_poznamka(),
        random_between(0,2)
from generate_series(1, 100) as seq(i);
-- faktury
create or replace function random_poznamky_faktury() returns varchar language sql as $$
select meno from poznamky_faktury tablesample system_rows(20) order by random() limit 1 $$;
insert into faktury (objednavky,suma,datum_platby,datum_vystavenia,miesto_dorucenia,poznamka)
select	random_between(1,100),
		random_between(45,12345), --toto urobiť lepšie
        now() - random() * INTERVAL '10 days',
        now() - random() * INTERVAL '90 days',
        random_between(1,30),
		random_poznamky_faktury()
from generate_series(1, 100) as seq(i);
--dopyt
create or replace function random_dopyt() returns varchar language sql as $$
select meno from dopyt_vyhladanie tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_dopyt2() returns varchar language sql as $$
select meno from dopyt_vyhladanie2 tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_dopyt3() returns varchar language sql as $$
select meno from dopyt_vyhladanie3 tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_dopyt4() returns varchar language sql as $$
select meno from dopyt_vyhladanie4 tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_dopyt5() returns varchar language sql as $$
select meno from dopyt_vyhladanie5 tablesample system_rows(20) order by random() limit 1 $$;

create or replace function random_dopyt_vysledok() returns varchar language sql as $$
SELECT CONCAT(random_between(1,12)::text,' ',random_dopyt4(),' ',random_dopyt(), ' ',random_dopyt2(),' ',random_dopyt3(),' ',random_dopyt5())$$;

insert into dopyt (priame_vyhladanie,pocet_vyhladani)
select	random_dopyt_vysledok(),
		random_between_big(1,48549)

from generate_series(1, 1000) as seq(i);
-- vyhladavanie
insert into vyhladavanie (zakaznik_id,priame_vyhladanie,datum_vyhladavania,dopyt_id)
select	random_between(1,100),
		random_between(0,1),
        now() - random() * INTERVAL '432 days',
		random_between(1,1000)
from generate_series(1, 250) as seq(i);
--doprava
create or replace function random_track_id() returns varchar language sql as $$
select meno from track_id tablesample system_rows(20) order by random() limit 1 $$;
insert into doprava (zakaznik,track_id,datum,miesto_dorucenia,typ_dovozu,stav)
select	random_between(1,100),
		random_track_id(),
        now() - random() * INTERVAL '105 days',
        random_between(1,30),
        random_between(1,3),
        random_between(0,1)


from generate_series(1, 530) as seq(i);
drop table priezvisko, staty,adresy,mesta,meno,email,isic,tel,prod_atrib_spec,meno_produktu,specifikacie,popis,kategoria,poznamka,kosik_meno,poznamky_faktury,dopyt_vyhladanie,track_id,atributy_mena,dopyt_vyhladanie,dopyt_vyhladanie2,dopyt_vyhladanie3,dopyt_vyhladanie4,dopyt_vyhladanie5  cascade;
drop function random_adresa();
drop function random_atributy();
drop function random_between(integer, integer) cascade;
drop function random_between_big(bigint, bigint) cascade;
drop function random_dopyt();
drop function random_dopyt2();
drop function random_dopyt3();
drop function random_dopyt4();
drop function random_dopyt5();
drop function random_dopyt_vysledok();
drop function random_email();
drop function random_isic();
drop function random_kategoria();
drop function random_kosik_meno();
drop function random_meno();
drop function random_priezvisko();
drop function random_mesta();
drop function random_popis();
drop function random_poznamka();
drop function random_poznamky_faktury();
drop function random_produkt();
drop function random_specifikacie();
drop function random_spec_list();
drop function random_staty();
drop function random_tel();
drop function random_track_id();

--pridat index

CREATE INDEX idx_atributy ON atributy (produkty_id,hodnota);
CREATE INDEX idx_doprava ON doprava (zakaznik,miesto_dorucenia,track_id,datum,typ_dovozu);
CREATE INDEX idx_dopyt ON dopyt (pocet_vyhladani,priame_vyhladanie);
CREATE INDEX idx_faktury ON faktury (objednavky,datum_platby);
CREATE INDEX idx_kosik ON kosik (zakaznik,meno);
CREATE INDEX idx_miesta ON miesta (adresa,mesto);
CREATE INDEX idx_objednavky ON objednavky (zakaznik,kosik,datum);
CREATE INDEX idx_obsah_kosika ON obsah_kosika (produkt,kosik);
CREATE INDEX idx_atrib_list ON prod_atrib_list (produkt);
CREATE INDEX idx_produkty ON produkty (kategoria,reklamovanost,meno_produktu,dostupnost);
CREATE INDEX idx_vyhladavanie ON vyhladavanie (zakaznik_id,dopyt_id,datum_vyhladavania,priame_vyhladanie);
CREATE INDEX idx_zakaznik ON zakaznik (meno,ico,dic,miesto);
COMMIT;