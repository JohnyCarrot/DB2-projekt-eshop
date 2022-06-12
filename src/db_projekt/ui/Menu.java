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
 * Užívateľské menu obsahujúce základné operácie na prácu s užívateľom
 */
public class Menu {

    int aktualny_pouzivatel = 1;
    int aktualny_kosik = -1;
    boolean exit = false;
    public Menu() throws SQLException, IOException, ParseException, InterruptedException {
            print();
        BufferedReader bratm = new BufferedReader(new InputStreamReader(System.in));
        while (exit == false) {

            System.out.print("Tvoja volba:");

                var vstupika = bratm.readLine();
                switch (vstupika) {
                    case "1":
                        var zoznam = new Zakaznik().zoznam(0); //10 na stranu - napisat pocet stran
                        if(zoznam.length==0){
                            System.out.println("Zoznam zákazníkov je prázdny");
                            break;
                        }

                        for(int i =0;i<zoznam.length;i+=2){
                            System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
                        }
                        int strana = 0;
                        System.out.println("Zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
                        if(new Zakaznik().zoznam(10).length==0) break;
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
                            zoznam = new Zakaznik().zoznam(strana * 10);
                            if(zoznam.length==0) {strana--; zoznam = new Zakaznik().zoznam(strana * 10);}
                            for(int i =0;i<zoznam.length;i+=2){
                                System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
                            }
                        }
                        break;
                    case "2":
                        pridaj_zakaznika();
                        break;
                    case "3":
                        odstran_zakaznika();
                        break;
                    case "4":
                        aktualizuj_zakaznika();
                        break;
                    case "5":
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



                        System.out.println("Zadaj Dopyt:");
                        Hladanie hladanie = new Hladanie(br.readLine(),aktualny_pouzivatel);
                        System.out.println(Arrays.toString(hladanie.vysledok));
                        break;
                    case "6":
                        var zakaznik = new Zakaznik();
                        System.out.println("Zadaj ID zakaznika na ktoreho sa chceš prepnúť");
                        BufferedReader bro = new BufferedReader(new InputStreamReader(System.in));
                        var vstup = bro.readLine();
                        while(true){
                            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
                            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
                            vstup = bro.readLine();
                        }
                        int AJDI = Integer.parseInt(vstup);
                        if(zakaznik.over_zakaznika(AJDI)==false){ System.out.println("Zákazník s ID: "+AJDI+" neexistuje");break;}
                        else aktualny_pouzivatel = AJDI;
                        System.out.println("Úspešne prepnuté na zákazníka s ID"+AJDI);
                        break;
                    case "7":

                        return;

                    case "8":

                        print();
                        break;
                    case "9":

                        BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));



                        System.out.println("Zadaj dátum OD (formát: YYYY-MM-DD:");
                        var jedna = bra.readLine();
                        System.out.println("Zadaj dátum DO (formát: YYYY-MM-DD:");
                        var dva = bra.readLine();
                        new Hladanie_statistics(jedna,dva);
                        break;
                    case "10":
                        var hul = new Menu_kosik(aktualny_pouzivatel);
                        aktualny_kosik = hul.getAktualny_kosik();
                        print();
                        break;
                    case "11":
                        new Menu_objednavky(aktualny_pouzivatel,aktualny_kosik);
                        print();
                        break;
                }

            }

        System.out.println("Dovidenia !");
    }
    public void print() {

        System.out.println(" Aktuálne zvolený používateľ: "+aktualny_pouzivatel);
        System.out.println("*********************************");
        System.out.println("* 1. Vypíš zoznam zákazníkov    *");
        System.out.println("* 2. Pridaj zákazníka           *");
        System.out.println("* 3. Odstráň zákazníka          *");
        System.out.println("* 4. Aktualizuj zákazníka       *");
        System.out.println("* 5. Hľadanie produktu          *");
        System.out.println("* 6. Prepni používateľa         *");
        System.out.println("* 7. exit                       *");
        System.out.println("* 8. zobraz menu                *");
        System.out.println("* 9. Štatistika vyhľadávania    *");
        System.out.println("* 10. Nákupný košík             *");
        System.out.println("* 11. Objednávky                *");
        System.out.println("*********************************");
    }
    private void pridaj_zakaznika() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Zakaznik zakaznik = new Zakaznik();

        System.out.println("Zadaj ICO(0 pre prazdne):");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && vstup.length()==8) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.equals("0")) break;
            else if (isNumeric(vstup)) System.out.println("ICO musí mať 8 číslic, zadal si: "+vstup.length());
            else System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        zakaznik.setICO(Integer.parseInt(vstup));
        System.out.println("Zadaj DIC (0 pre prazdne):");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && vstup.length()==10) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.equals("0")) break;
            else if (isNumeric(vstup)) System.out.println("DICO musí mať 10 číslic, zadal si: "+vstup.length());
            else System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        zakaznik.setDIC(Integer.parseInt(vstup));
        System.out.println("Zadaj meno");
        vstup = br.readLine();
        while(true){
           if (isNumeric(vstup) ||vstup.isEmpty()) System.out.println("Skús znovu");
            else break;vstup = br.readLine();
        }
        zakaznik.setMeno(vstup);
        System.out.println("Zadaj email");
        vstup = br.readLine();
        while(true){
            if(isValidEmailAddress(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else System.out.println("Email nieje korektný, skús znovu");
            vstup = br.readLine();
        }
        zakaznik.setEmail(vstup);
        System.out.println("Zadaj tel. číslo");
        vstup = br.readLine();
        while(true){
            if(isValidPhoneNumero(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else System.out.println("Číslo nieje  korektné, skús znovu");
            vstup = br.readLine();
        }
        zakaznik.setCislo(vstup);
        System.out.println("Zadaj ISIC (0 pre prádzne)");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 14 && vstup.charAt(0)=='S') break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (vstup.equals("0")) break;
            else  System.out.println("ISIC číslo nieje valídne, skús znova");
            vstup = br.readLine();
        }
        zakaznik.setISIC(vstup);
        Miesta bydlisko = new Miesta();
        System.out.println("Zadaj adresu (ulica,cislo domu v kope)");
        vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("skús znova");
            vstup = br.readLine();
        }
        bydlisko.setAdresa(vstup);
        System.out.println("Zadaj mesto:");
        vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("skús znova");
            vstup = br.readLine();
        }
        bydlisko.setMesto(vstup);
        System.out.println("Zadaj psc");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 5 && isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if (isNumeric(vstup)) System.out.println("PSC musí mať 5 číslic, zadal si: "+vstup.length());
            else  System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        bydlisko.setPsc(Integer.parseInt(vstup));
        System.out.println("Zadaj krajinu");
        vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("skús znova");
            vstup = br.readLine();
        }
        bydlisko.setStat(vstup);
        bydlisko.insert();
        zakaznik.setMiesto(bydlisko.getId());
        zakaznik.insert();
        System.out.println("Zákazník úspešne pridaný");
        System.out.print("ID zákazníka je: ");
        System.out.println(zakaznik.getId());
    }

    private void odstran_zakaznika() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Zakaznik zakaznik = new Zakaznik();

        System.out.println("Zadaj ID na odstranenie:");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
            vstup = br.readLine();
        }
        int ID = Integer.parseInt(vstup);
        //System.out.println(br.readLine().isEmpty()); //Kontrola, či sa dá dať aj prázdny znak - dokončiť
        if(!zakaznik.over_zakaznika(ID)) {System.out.println("ID zákazníka neexistuje");return;} //Treba zmazať aj všetky vyhladavania
        zakaznik.setId(ID);
        zakaznik.delete();
        System.out.println("Zákazník s ID "+ID+" úspešne odstránený.");
    }
    private void aktualizuj_zakaznika() throws IOException, SQLException {
        //TO DO Ukázať pôvodnú volbu
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Zakaznik zakaznik = new Zakaznik();

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
        if(!zakaznik.over_zakaznika(ID)) {System.out.println("ID zákazníka neexistuje");return;} //Treba zmazať aj všetky vyhladavania
        zakaznik.setId(ID);
        zakaznik.stiahni_zakaznika(ID);
        Zakaznik zakaznik_stary = new Zakaznik();
        zakaznik_stary.stiahni_zakaznika(ID);
        System.out.println("Zadaj ICO(0 pre prazdne)["+zakaznik.getICO()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && vstup.length()==8) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.equals("0")) break;
            else if(vstup.isEmpty()) {vstup = String.valueOf(zakaznik.getICO());break;}
            else if (isNumeric(vstup)) System.out.println("ICO musí mať 8 číslic, zadal si: "+vstup.length());
            else System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        zakaznik.setICO(Integer.parseInt(vstup));
        System.out.println("Zadaj DIC(0 pre prazdne)["+zakaznik.getDIC()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && vstup.length()==10) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.equals("0")) break;
            else if(vstup.isEmpty()) {vstup = String.valueOf(zakaznik.getICO());break;}
            else if (isNumeric(vstup)) System.out.println("DICO musí mať 10 číslic, zadal si: "+vstup.length());
           else  System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        zakaznik.setDIC(Integer.parseInt(vstup));
        System.out.println("Zadaj meno["+zakaznik.getMeno()+"]: ");
        vstup = br.readLine();
        if(vstup.isEmpty()) vstup = zakaznik.getMeno();
        zakaznik.setMeno(vstup);
        System.out.println("Zadaj email["+zakaznik.getEmail()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isValidEmailAddress(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.isEmpty()) {vstup = String.valueOf(zakaznik.getEmail());break;}
            else  System.out.println("Email nieje valídny, skús znovu");
            vstup = br.readLine();
        }
        zakaznik.setEmail(vstup);
        System.out.println("Zadaj tel. číslo["+zakaznik.getCislo()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isValidPhoneNumero(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.isEmpty()) {vstup = String.valueOf(zakaznik.getCislo());break;}
            else  System.out.println("Tel. číslo nie je valídne, skús znovu");
            vstup = br.readLine();
        }
        zakaznik.setCislo(vstup);
        System.out.println("Zadaj ISIC (0 pre prádzne)["+zakaznik.getISIC()+"]: ");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 14 && vstup.charAt(0)=='S') break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.isEmpty()) {vstup = String.valueOf(zakaznik.getISIC());break;}
            else if (vstup.equals("0")) break;
            else  System.out.println("ISIC číslo nieje valídne, skús znova");
            vstup = br.readLine();
        }
        zakaznik.setISIC(vstup);
        Miesta bydlisko = new Miesta();
        Miesta bydlisko_stary = new Miesta();
        bydlisko.stiahni(zakaznik.getMiesto());
        bydlisko_stary.stiahni(zakaznik.getMiesto());
        System.out.println("Zadaj adresu (ulica,cislo domu v kope)["+bydlisko.getAdresa()+"]: ");
        vstup = br.readLine();
        if(vstup.isEmpty()) {vstup = String.valueOf(bydlisko.getAdresa());}
        bydlisko.setAdresa(vstup);
        System.out.println("Zadaj mesto["+bydlisko.getMesto()+"]: ");
        vstup = br.readLine();
        if(vstup.isEmpty()) {vstup = String.valueOf(bydlisko.getMesto());}
        bydlisko.setMesto(vstup);
        System.out.println("Zadaj psc["+bydlisko.getPsc()+"]: ");
        vstup = br.readLine();
        while(true){
            if(vstup.length() == 5 && isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(vstup.isEmpty()) {vstup = String.valueOf(bydlisko.getPsc());break;}
            else if (isNumeric(vstup)) System.out.println("PSC musí mať 5 číslic, zadal si: "+vstup.length());
            else  System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        bydlisko.setPsc(Integer.parseInt(vstup));
        System.out.println("Zadaj krajinu["+bydlisko.getStat()+"]: ");
        vstup = br.readLine();
        if(vstup.isEmpty()) {vstup = String.valueOf(bydlisko.getStat());}
        bydlisko.setStat(vstup);
        var nezmenene = 0;
        if(zakaznik_stary.getICO()!=zakaznik.getICO()) System.out.println(zakaznik_stary.getICO()+" -> "+ zakaznik.getICO());
        else nezmenene++;
        if(zakaznik_stary.getDIC()!=zakaznik.getDIC()) System.out.println(zakaznik_stary.getDIC()+" -> "+ zakaznik.getDIC());
        else nezmenene++;
        if(!Objects.equals(zakaznik_stary.getMeno(), zakaznik.getMeno())) System.out.println(zakaznik_stary.getMeno()+" -> "+ zakaznik.getMeno());
        else nezmenene++;
        if(!Objects.equals(zakaznik_stary.getEmail(), zakaznik.getEmail())) System.out.println(zakaznik_stary.getEmail()+" -> "+ zakaznik.getEmail());
        else nezmenene++;
        if(!Objects.equals(zakaznik_stary.getCislo(), zakaznik.getCislo())) System.out.println(zakaznik_stary.getCislo()+" -> "+ zakaznik.getCislo());
        else nezmenene++;
        if(!Objects.equals(zakaznik_stary.getISIC(), zakaznik.getISIC())) System.out.println(zakaznik_stary.getISIC()+" -> "+ zakaznik.getISIC());
        else nezmenene++;
        if(!Objects.equals(bydlisko_stary.getAdresa(), bydlisko.getAdresa()))System.out.println(bydlisko_stary.getAdresa()+" -> "+ bydlisko.getAdresa());
        else nezmenene++;
        if(!Objects.equals(bydlisko_stary.getMesto(), bydlisko.getMesto()))System.out.println(bydlisko_stary.getMesto()+" -> "+ bydlisko.getMesto());
        else nezmenene++;
        if(bydlisko_stary.getPsc()!=bydlisko.getPsc())System.out.println(bydlisko_stary.getPsc()+" -> "+ bydlisko.getPsc());
        else nezmenene++;
        if(!Objects.equals(bydlisko_stary.getStat(), bydlisko.getStat()))System.out.println(bydlisko_stary.getStat()+" -> "+ bydlisko.getStat());
        else nezmenene++;
        if (nezmenene>9) {
            System.out.println("Neboli zadané žiadne zmeny oproti originálu. Akcia terminovaná");}
        else {
            System.out.println();
            System.out.println("Pre aplikovanie zmien zadaj 'Ano' ");
            vstup = br.readLine();
            if (vstup.equals("Ano")) {
                bydlisko.insert();
                zakaznik.setMiesto(bydlisko.getId());
                zakaznik.update();
                System.out.println("Zákazník s ID " + ID + " úspešne aktualizovaný.");
            } else System.out.println("Akcia bola terminovaná");
        }
    }
    public static boolean isNumeric(String string) {
        int intValue;
        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
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
