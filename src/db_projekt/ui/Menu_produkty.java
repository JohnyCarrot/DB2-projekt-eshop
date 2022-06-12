package db_projekt.ui;
import db_projekt.ts.*;
import db_projekt.rdg.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
/**
 *
 * @author Roman Božik
 * Užívateľské menu obsahujúce základné operácie na prácu s produktami
 */
public class Menu_produkty {
    int aktualny_pouzivatel = 1;
    boolean exit = false;
    public Menu_produkty() throws SQLException, IOException, ParseException {
            print();
        BufferedReader bratm = new BufferedReader(new InputStreamReader(System.in));
        while (exit == false) {

            System.out.print("Tvoja volba:");
            //System.out.println("Zadaj \"1\", \"2\", \"3\" \"4\""); //Ukradnuté z https://stackoverflow.com/questions/20681616/java-creating-a-menu-loop
                var vstupika = bratm.readLine();
                switch (vstupika) {
                    case "1":
                        var zoznam = new Produkty().zoznam(0); //10 na stranu - napisat pocet stran
                        if(zoznam.length==0){
                            System.out.println("Zoznam produktov je prázdny");
                            break;
                        }

                        for(int i =0;i<zoznam.length;i+=3){
                            System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]+" | Cena: "+zoznam[i+2]);
                        }
                        int strana = 0;
                        System.out.println("Zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
                        if(new Produkty().zoznam(10).length==0) break;
                        while (true){

                            BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
                            var vstupik = bra.readLine();
                            if(vstupik.equals("l")){
                                if(!(strana ==0))  strana--;
                            }
                            else if(vstupik.equals("r")){
                                strana++;
                            }
                            else if(vstupik.equals("exit")){
                                break;
                            }
                            else {
                                System.out.println("Nerozumiem ti, odchádzam");
                                break;
                            }
                            zoznam = new Produkty().zoznam(strana * 10);
                            if(zoznam.length==0) {strana--; zoznam = new Produkty().zoznam(strana * 10);}
                            for(int i =0;i<zoznam.length;i+=3){
                                System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]+" | Cena: "+zoznam[i+2]);
                            }
                        }
                        break;
                    case "2":
                        pridaj_produkt();
                        break;
                    case "3":
                        odstran_produkt();
                        break;
                    case "4":
                        aktualizuj_produkt();
                        break;
                    case "6":
                        var prodakt = new Produkty();
                        System.out.println("Zadaj ID produktu, ktorému chceš zmeniť skladové zásoby");
                        BufferedReader bro = new BufferedReader(new InputStreamReader(System.in));
                        var vstup = bro.readLine();
                        while(true){
                            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
                            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
                            vstup = bro.readLine();
                        }
                        int AJDI = Integer.parseInt(vstup);
                        if(prodakt.over_produkt(AJDI)==false){ System.out.println("Produkt s ID: "+AJDI+" neexistuje");break;}
                        prodakt.stiahni_produkt(AJDI);
                        System.out.println("Momentálne zásoby produktu "+prodakt.getMeno_produktu()+"sú: "+prodakt.getDostupnost());
                        vstup = bro.readLine();
                        while(true){
                            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
                            else if (vstup.isEmpty()){vstup= String.valueOf(prodakt.getDostupnost());break;}
                            else System.out.println("Číslo nieje  korektné, skús znovu");
                            vstup = bro.readLine();
                        }
                        prodakt.setDostupnost(Integer.parseInt(vstup));
                        prodakt.update();
                        System.out.println("Produktu "+prodakt.getMeno_produktu()+"s ID "+AJDI+" úspešne zmenené skladové zásoby na "+prodakt.getDostupnost());
                        break;
                    case "5": // nemeniť je to prehodené naschvál
                        var pr = new Produkty();
                        System.out.println("Zadaj ID produktu, ktorého skladové zásoby chceš vedieť");
                        BufferedReader brr = new BufferedReader(new InputStreamReader(System.in));
                        var vstupiiik = brr.readLine();
                        while(true){
                            if(isNumeric(vstupiiik)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
                            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
                            vstupiiik = brr.readLine();
                        }
                        int AJDII = Integer.parseInt(vstupiiik);
                        if(pr.over_produkt(AJDII)==false){ System.out.println("Produkt s ID: "+AJDII+" neexistuje");break;}
                        pr.stiahni_produkt(AJDII);
                        if(pr.getDostupnost()>0) System.out.println("Skladové zásoby produktu s ID"+pr.getId()+" sú "+pr.getDostupnost());
                        else if(pr.getDostupnost()<0) System.out.println("Skladové zásoby produktu s ID"+pr.getId()+" sú  nastavené na 'na ceste'");
                        else if (pr.getDostupnost()==0) System.out.println("Skladové zásoby produktu s ID"+pr.getId()+" sú nastavené na 'nedostupné'");
                        break;
                    case "7":
                        //do logic
                        return;
                        //exit = true;
                        //break;
                    case "8":
                        //do logic
                        print();
                        break;
                    case "9":
                        //do logic
                        detail_produktu();



                        break;
                }

            }

        System.out.println("Dovidenia !");
    }
    public void print() {
        //pridať možnosť prepínať medzi
        //System.out.println(" Aktuálne zvolený používateľ: "+aktualny_pouzivatel);
        System.out.println("*********************************");
        System.out.println("* 1. Vypíš zoznam produktov     *");
        System.out.println("* 2. Pridaj produkt             *");
        System.out.println("* 3. Odstráň produkt            *");
        System.out.println("* 4. Aktualizuj produkt         *");
        System.out.println("* 5. Skladové zásoby produktu   *");
        System.out.println("* 6. Nastav skladové zásoby     *");
        System.out.println("* 7. exit                       *");
        System.out.println("* 8. zobraz menu                *");
        System.out.println("* 9. Detail produktu            *");
        System.out.println("*********************************");
    }

    private void detail_produktu() throws SQLException, IOException {
        var produkt = new Produkty();
        var zoznam = new Produkty().zoznam(0); //10 na stranu - napisat pocet stran
        if(zoznam.length==0){
            System.out.println("Zoznam produktov je prázdny");
            return;
        }
        //System.out.println(Arrays.toString(zoznam));
        //System.out.println(zoznam.length); - pre ziskanie jedneho zaznamu treba podelit 3jkou !!!
        for(int i =0;i<zoznam.length;i+=3){
            System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
        }
        int strana = 0;
        System.out.println("Ak zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
        var vstupik = "";
        while (true){

            BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
            vstupik = bra.readLine();
            if(vstupik.equals("l")){
                if(!(strana ==0))  strana--;
            }
            else if(vstupik.equals("r")){
                strana++;
            }
            else if(isNumeric(vstupik) && produkt.over_produkt(Integer.parseInt(vstupik))){
                break;
            }
            else if (isNumeric(vstupik)) {System.out.println("Produkt s takýmto ID neexistuje");continue;}
            else if(vstupik.equals("exit")){
                return;
            }
            else {
                System.out.println("Vstup nie je numerický, skús znovu");
                continue;
            }
            zoznam = new Produkty().zoznam(strana * 10);
            if(zoznam.length==0) {strana--; zoznam = new Produkty().zoznam(strana * 10);}
            for(int i =0;i<zoznam.length;i+=3){
                System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
            }
        }
        produkt.stiahni_produkt(Integer.parseInt(vstupik));
        System.out.println("Meno: "+produkt.getMeno_produktu());
        System.out.println("Kategoria : "+produkt.getKategoria());
        System.out.println("Cena : "+produkt.getCena());
        if(produkt.getZlava()>0 && produkt.getCena()-produkt.getZlava()>0) System.out.println("Cena po zľave: "+(produkt.getCena()-produkt.getZlava()));
        System.out.println("Unikátne označenie : "+produkt.getUpc());
        System.out.println("Reklamovanosť : "+ (int) produkt.getReklamovanost()+" %");
        System.out.println("Špecifikácie : "+produkt.getSpecifikacie());
        System.out.println("Atributy: "+Arrays.toString(produkt.zoznam_atributov(produkt.getId())));
        System.out.println("Popis : "+produkt.getPopis());


    }

    private void pridaj_produkt() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var produkt = new Produkty();

        System.out.println("Zadaj meno produktu:");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) || vstup.length()==0) System.out.println("Skús znovu");     //https://stackabuse.com/java-check-if-string-is-a-number/
            else break;
            vstup = br.readLine();
        }
        produkt.setMeno_produktu(vstup);
        System.out.println("Zadaj špecifikacie:");
        vstup = br.readLine();
        produkt.setSpecifikacie(vstup);
        System.out.println("Zadaj popis");
        vstup = br.readLine();
        produkt.setPopis(vstup);
        System.out.println("cena (v celých eurách)");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else System.out.println("vstup obsahuje nenumerické znaky, skús znovu");
            vstup = br.readLine();
        }
        produkt.setCena(Integer.parseInt(vstup));
        System.out.println("Zadaj dostupnosť (1 a viac- na sklade, 0 - nedostupný,-1 - na ceste)");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else System.out.println("Číslo nieje  korektné, skús znovu");
            vstup = br.readLine();
        }
        produkt.setDostupnost(Integer.parseInt(vstup));
        System.out.println("Zadaj UPC (Unique product code)");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 12 && isNumeric(vstup) )break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(isNumeric(vstup))  System.out.println("UPC musí mať 12 znakov. Zadal si: "+vstup.length());
            else System.out.println("Nenumerický vstup, skús znovu");
            vstup = br.readLine();
        }
        produkt.setUpc(Long.parseLong(vstup));
        System.out.println("Urči kategóriu:");
        vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("skús znova");
            vstup = br.readLine();
        }
        produkt.setKategoria(vstup);
        System.out.println("Zadaj zľavu (v celých eurách):");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("Zadaný vstup obsahuje nenumerické znaky");
            vstup = br.readLine();
        }
        produkt.setZlava(Integer.parseInt(vstup));
        System.out.println("Zadaj reklamovanosť (môžeš aj s desatinnou čiarkou[bodkou])");
        vstup = br.readLine();
        while(true){
            if(isNumeric_double(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else System.out.println("Vstup nie je numerický, skús znovu");
            vstup = br.readLine();
        }
        produkt.setReklamovanost(Double.parseDouble(vstup));
        System.out.println("Zadaj počet predaných kusov");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("Vstup nieje numerický");
            vstup = br.readLine();
        }
        produkt.setPocet_predanych_kusov(Long.parseLong(vstup));
        produkt.insert();
        System.out.println("Produkt úspešne pridaný");
        System.out.print("ID nového produktu["+produkt.getMeno_produktu()+"] je: ");
        System.out.println(produkt.getId());
    }

    private void odstran_produkt() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var produkt = new Produkty();

        System.out.println("Zadaj ID na odstranenie:");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
            vstup = br.readLine();
        }
        int ID = Integer.parseInt(vstup);
        //System.out.println(br.readLine().isEmpty()); //Kontrola, či sa dá dať aj prázdny znak - dokončiť
        if(!produkt.over_produkt(ID)) {System.out.println("ID produktu neexistuje");return;} //Treba zmazať aj všetky vyhladavania
        produkt.setId(ID);
        produkt.delete();
        System.out.println("Produkt s ID "+ID+" úspešne odstránený.");
    }
    private void aktualizuj_produkt() throws IOException, SQLException {
        //TO DO Ukázať pôvodnú volbu
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var produkt = new Produkty();

        System.out.println("Zadaj ID na aktualizaciu:");
        //System.out.println("Pre zachovanie hodnoty, len stlač enter");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        int ID = Integer.parseInt(vstup);
        //System.out.println(br.readLine().isEmpty()); //Kontrola, či sa dá dať aj prázdny znak - dokončiť
        if(!produkt.over_produkt(ID)) {System.out.println("ID zákazníka neexistuje");return;} //Treba zmazať aj všetky vyhladavania
        produkt.setId(ID);
        produkt.stiahni_produkt(ID);
        var produkt_stary = new Produkty();
        produkt_stary.stiahni_produkt(ID);


        System.out.println("Zadaj meno produktu["+produkt.getMeno_produktu()+"]:");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) System.out.println("Skús znovu");     //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup=produkt.getMeno_produktu();break;}
            else break;
            vstup = br.readLine();
        }
        produkt.setMeno_produktu(vstup);
        System.out.println("Zadaj špecifikacie:");
        vstup = br.readLine();
        if (vstup.isEmpty()){vstup=produkt.getSpecifikacie();}
        produkt.setSpecifikacie(vstup);
        System.out.println("Zadaj popis");
        vstup = br.readLine();
        if (vstup.isEmpty()){vstup=produkt.getPopis();}
        produkt.setPopis(vstup);
        System.out.println("cena (v celých eurách)["+produkt.getCena()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getCena());break;}
            else System.out.println("vstup obsahuje nenumerické znaky, skús znovu");
            vstup = br.readLine();
        }
        produkt.setCena(Integer.parseInt(vstup));
        System.out.println("Zadaj dostupnosť (1 a viac- na sklade, 0 - nedostupný,-1 - na ceste)["+produkt.getDostupnost()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getDostupnost());break;}
            else System.out.println("Číslo nieje  korektné, skús znovu");
            vstup = br.readLine();
        }
        produkt.setDostupnost(Integer.parseInt(vstup));
        System.out.println("Zadaj UPC (Unique product code)["+produkt.getUpc()+"]: ");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 12 && isNumeric(vstup) )break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getUpc());break;}
            else if(isNumeric(vstup))  System.out.println("UPC musí mať 12 znakov. Zadal si: "+vstup.length());
            else System.out.println("Nenumerický vstup, skús znovu");
            vstup = br.readLine();
        }
        produkt.setUpc(Long.parseLong(vstup));
        System.out.println("Urči kategóriu["+produkt.getKategoria()+"]: ");
        vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getKategoria());break;}
            else  System.out.println("skús znova");
            vstup = br.readLine();
        }
        produkt.setKategoria(vstup);
        System.out.println("Zadaj zľavu (v celých eurách)["+produkt.getZlava()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getZlava());break;}
            else  System.out.println("Zadaný vstup obsahuje nenumerické znaky");
            vstup = br.readLine();
        }
        produkt.setZlava(Integer.parseInt(vstup));
        System.out.println("Zadaj reklamovanosť (môžeš aj s desatinnou čiarkou[bodkou])["+produkt.getReklamovanost()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric_double(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getReklamovanost());break;}
            else System.out.println("Vstup nie je numerický, skús znovu");
            vstup = br.readLine();
        }
        produkt.setReklamovanost(Double.parseDouble(vstup));
        System.out.println("Zadaj počet predaných kusov["+produkt.getPocet_predanych_kusov()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.isEmpty()){vstup= String.valueOf(produkt.getPocet_predanych_kusov());break;}
            else  System.out.println("Vstup nieje numerický");
            vstup = br.readLine();
        }
        produkt.setPocet_predanych_kusov(Long.parseLong(vstup));

        var nezmenene = 0;
        if(!Objects.equals(produkt_stary.getMeno_produktu(), produkt.getMeno_produktu())) System.out.println(produkt_stary.getMeno_produktu()+" -> "+ produkt.getMeno_produktu());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getSpecifikacie(), produkt.getSpecifikacie())) System.out.println(produkt_stary.getSpecifikacie()+" -> "+ produkt.getSpecifikacie());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getPopis(), produkt.getPopis())) System.out.println(produkt_stary.getPopis()+" -> "+ produkt.getPopis());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getCena(), produkt.getCena())) System.out.println(produkt_stary.getCena()+" -> "+ produkt.getCena());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getDostupnost(), produkt.getDostupnost())) System.out.println(produkt_stary.getDostupnost()+" -> "+ produkt.getDostupnost());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getUpc(), produkt.getUpc())) System.out.println(produkt_stary.getUpc()+" -> "+ produkt.getUpc());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getKategoria(), produkt.getKategoria()))System.out.println(produkt_stary.getKategoria()+" -> "+ produkt.getKategoria());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getZlava(), produkt.getZlava()))System.out.println(produkt_stary.getZlava()+" -> "+ produkt.getZlava());
        else nezmenene++;
        if(produkt_stary.getReklamovanost()!=produkt.getReklamovanost())System.out.println(produkt_stary.getReklamovanost()+" -> "+ produkt.getReklamovanost());
        else nezmenene++;
        if(!Objects.equals(produkt_stary.getPocet_predanych_kusov(), produkt.getPocet_predanych_kusov()))System.out.println(produkt_stary.getPocet_predanych_kusov()+" -> "+ produkt.getPocet_predanych_kusov());
        else nezmenene++;
        if (nezmenene>9) {
            System.out.println("Neboli zadané žiadne zmeny oproti originálu. Akcia terminovaná");}
        else {
            System.out.println();
            System.out.println("Pre aplikovanie zmien zadaj 'Ano' ");
            vstup = br.readLine();
            if (vstup.equals("Ano")) {
                produkt.update();
                System.out.println("Produkt s ID " + ID + " úspešne aktualizovaný.");
            } else System.out.println("Akcia bola terminovaná");
        }
    }
    public static boolean isNumeric(String string) {
        long intValue;
        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Long.parseLong(string);
            intValue++;
            return true;
        } catch (NumberFormatException e) {
            //return false;
        }
        return false;
    }
    public static boolean isNumeric_double(String string) {
        double intValue;
        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Double.parseDouble(string);
            intValue++;
            return true;
        } catch (NumberFormatException e) {
            //return false;
        }
        return false;
    }
    public boolean isValidEmailAddress(String email) { //https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isValidPhoneNumero(String email) { //https://stackoverflow.com/questions/25763935/how-to-check-phone-number-format-is-valid-or-not-from-telephony-manager
        String ePattern = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
