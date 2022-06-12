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
 * Užívateľské menu obsahujúce základné operácie na prácu s košíkom
 */
public class Menu_kosik {
    int aktualny_pouzivatel = 1;

    public int getAktualny_kosik() {
        return aktualny_kosik;
    }

    int aktualny_kosik = 1;
    boolean exit = false;
    public Menu_kosik(int pouzivatel) throws SQLException, IOException, ParseException {
        var llpe = new Kosik();
        var poceta = 0;
        while(poceta<1100){
            if(llpe.over_kosik_zakaznik(poceta,pouzivatel)){
                aktualny_kosik = poceta;
                break;
            }
            poceta++;
        }
        if (poceta>= 1098) {System.out.println("Pre správny chod aplikácie bol vytvorený 'Sample Košík");
            var llch = new Kosik();
            llch.setMeno("Sample Košík");
            llch.setZakaznik_id(aktualny_pouzivatel);
            llch.insert();
            aktualny_kosik = llch.getId();
        }
        aktualny_pouzivatel = pouzivatel;
            print();
        BufferedReader bratm = new BufferedReader(new InputStreamReader(System.in));
        while (exit == false) {

            System.out.print("Tvoja volba:");

                var vstupika = bratm.readLine();
                switch (vstupika) {
                    case "1":
                        var zoznam = new Kosik().zoznam(0,aktualny_kosik); //10 na stranu - napisat pocet stran
                        if(zoznam.length==0){
                            System.out.println("Chyba: Nenašiel som košík !");
                            break;
                        }

                        for(int i =0;i<zoznam.length;i+=2){
                            System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
                        }
                        int strana = 0;
                        System.out.println("Zadaj ID košíku, ktorého obsah chceš zobraziť");
                        System.out.println("Ak zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
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
                            else if(isNumeric(vstupik)){
                                if(new Kosik().over_kosik(Integer.parseInt(vstupik))==false){
                                    System.out.println("Košík s týmto ID neexistuje, skús znovu");
                                    continue;
                                }
                                obsah_koska(Integer.parseInt(vstupik));
                                break;
                            }
                            else {
                                System.out.println("Nerozumiem ti, odchádzam");
                                break;
                            }
                            zoznam = new Kosik().zoznam(strana * 10,aktualny_pouzivatel);
                            if(zoznam.length==0) {strana--; zoznam = new Kosik().zoznam(strana * 10,aktualny_pouzivatel);}
                            for(int i =0;i<zoznam.length;i+=2){
                                System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]);
                            }

                        }
                        break;
                    case "2":
                        pridaj_do_kosiku();
                        break;
                    case "3":
                        odstran_z_kosiku();
                        break;
                    case "4":
                        aktualizuj_produkt();
                        break;

                    case "5": // nemeniť je to prehodené naschvál
                                    pridaj_kosik();
                        break;
                    case "7":
                        return;
                    case "8":

                        print();
                        break;
                    case "9":

                        var llp = new Kosik();
                        llp.stiahni_kosik(aktualny_kosik);
                        var fgu = new Obsah_Kosika();
                        fgu.setKosik_id(aktualny_kosik);
                        fgu.delete_cely();
                        System.out.println("Košík "+llp.getMeno()+" s ID "+aktualny_kosik+" úspešne premazaný");
                        break;
                    case "0":
                        var zakaznik = new Kosik();
                        System.out.println("Zadaj ID košíku na ktory sa chceš prepnúť");
                        BufferedReader brou = new BufferedReader(new InputStreamReader(System.in));
                        var vstupp = brou.readLine();
                        while(true){
                            if(isNumeric(vstupp)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
                            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
                            vstupp = brou.readLine();
                        }
                        int AJDA = Integer.parseInt(vstupp);
                        if(zakaznik.over_kosik(AJDA)==false){ System.out.println("Kosik s ID: "+AJDA+" neexistuje");break;}
                        else aktualny_kosik = AJDA;
                        System.out.println("Úspešne prepnuté na Košík s ID"+AJDA);
                    case "6":


                        var llpa = new Kosik();
                        if(new Kosik().over_kosik(aktualny_kosik)==false){
                            System.out.println("Nastala chyba, odporúčam pridať košík");
                            break;
                        }
                        llpa.stiahni_kosik(aktualny_kosik);
                        var fgua = new Obsah_Kosika();
                        fgua.setKosik_id(aktualny_kosik);
                        fgua.delete_cely();
                        llpa.delete();
                        System.out.println("Košík "+llpa.getMeno()+" s ID "+aktualny_kosik+" zmazaný");
                        var pocet = 0;
                        while(pocet<1100){
                            if(llpa.over_kosik_zakaznik(pocet,aktualny_pouzivatel)){
                                aktualny_kosik = pocet;
                                break;
                            }
                            pocet ++;
                        }
                        if (pocet>= 1099) {System.out.println("Pre správny chod aplikácie bol vytvorený 'Sample Košík'");
                            var llch = new Kosik();
                            llch.setMeno("Sample Košík");
                            llch.setZakaznik_id(aktualny_pouzivatel);
                            llch.insert();
                            aktualny_kosik = llch.getId();
                        }
                        else System.out.println("Bol vybraný košík s ID: "+aktualny_kosik);
                        break;


                }



            }

        System.out.println("Dovidenia !");
    }
    public void print() {

        System.out.println(" Aktuálne zvolený košík: "+aktualny_kosik);
        System.out.println("*********************************");
        System.out.println("* 0. Zvoľ košík                 *");
        System.out.println("* 1. Vypíš produkty v košíku    *");
        System.out.println("* 2. Pridaj produkt  do košíka  *");
        System.out.println("* 3. Odstráň produkt z košíka   *");
        System.out.println("* 4. Aktualizuj produkt v košíku*");
        System.out.println("* 5. Pridaj košík               *");
        System.out.println("* 6. Zmaž aktuálny košík        *");
        System.out.println("* 7. exit                       *");
        System.out.println("* 8. zobraz menu                *");
        System.out.println("* 9. Vyprazdni košík            *");
        System.out.println("*********************************");
    }
    private void pridaj_do_kosiku() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var obsah = new Obsah_Kosika();

        System.out.println("Zadaj ID produktu, ktorý chceš pridať:"); //Ak bude čas pridať zoznam s produktami
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && new Produkty().over_produkt(Integer.parseInt(vstup))) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(isNumeric(vstup)) System.out.println("Produkt s týmto ID neexistuje");
            else System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        obsah.setProdukt_id(Integer.parseInt(vstup));
        obsah.setKosik_id(aktualny_kosik);

        System.out.println("Zadaj množstvo");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("Vstup nieje numerický");
            vstup = br.readLine();
        }
        obsah.setPocet_poloziek(Integer.parseInt(vstup));
        obsah.insert();
        var trtst = new Produkty();
        trtst.stiahni_produkt(obsah.getProdukt_id());
        var kosicek = new Kosik();
        kosicek.stiahni_kosik(aktualny_kosik);
        System.out.println("Produkt "+trtst.getMeno_produktu()+" úspešne pridaný do košíka "+kosicek.getMeno()+" v množstve: "+obsah.getPocet_poloziek()+" kusov");
    }

    private void pridaj_kosik() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var obsah = new Kosik();

        System.out.println("Zadaj meno košíka:"); //Ak bude čas pridať zoznam s produktami
        var vstup = br.readLine();
        while(true){
            if(!vstup.isEmpty()) break;
            System.out.println("Názov nemôže byť prázdny, skús znovu");
            vstup = br.readLine();
        }
        obsah.setMeno(vstup);
        obsah.setZakaznik_id(aktualny_pouzivatel);
        obsah.insert();
        System.out.println("Košík "+obsah.getMeno()+"s ID"+obsah.getId()+" úspešne vytvorený");
    }

    private void odstran_z_kosiku() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var obsah = new Obsah_Kosika();

        System.out.println("Zadaj ID položky na odstranenie:");
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            System.out.println("Vstup nie je numerický, zadaj prosím len číslo");
            vstup = br.readLine();
        }
        int ID = Integer.parseInt(vstup);
        //System.out.println(br.readLine().isEmpty()); //Kontrola, či sa dá dať aj prázdny znak - dokončiť
        if(!obsah.over_obsah(ID)) {System.out.println("ID položky neexistuje");return;} //Treba zmazať aj všetky vyhladavania
        obsah.setId(ID);
        obsah.stiahni_kosik(ID);
        var cislo = obsah.getProdukt_id();
        var ll = new Produkty();
        ll.stiahni_produkt(cislo);

        obsah.delete();
        System.out.println("Položka("+ll.getMeno_produktu()+" "+obsah.getPocet_poloziek()+" /ks"+") s ID "+ID+" úspešne odstránená.");
    }
    private void aktualizuj_produkt() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        var obsah = new Obsah_Kosika();

        System.out.println("Zadaj ID položky, ktorú chceš upraviť:"); //Ak bude čas pridať zoznam s produktami
        var vstup = br.readLine();
        while(true){
            if(isNumeric(vstup) && new Obsah_Kosika().over_obsah(Integer.parseInt(vstup))) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else if(isNumeric(vstup)) System.out.println("Záznam s týmto ID neexistuje");
            else System.out.println("Vstup nie je numerický, zadaj prosím len čísla");
            vstup = br.readLine();
        }
        obsah.setId(Integer.parseInt(vstup));
        obsah.stiahni_kosik(obsah.getId());

        System.out.println("Zadaj množstvo["+obsah.getPocet_poloziek()+"]: ");
        vstup = br.readLine();
        while(true){
            if(isNumeric(vstup)) break;      //https://stackabuse.com/java-check-if-string-is-a-number/
            else  System.out.println("Vstup nieje numerický");
            vstup = br.readLine();
        }
        obsah.setPocet_poloziek(Integer.parseInt(vstup));
        obsah.update();
        var trtst = new Produkty();
        trtst.stiahni_produkt(obsah.getProdukt_id());
        var kosicek = new Kosik();
        kosicek.stiahni_kosik(aktualny_kosik);
        System.out.println("Produkt "+trtst.getMeno_produktu()+" v košíku "+kosicek.getMeno()+" úspešne aktualizovaný. Nové množstvo: "+obsah.getPocet_poloziek()+" kusov");

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
    public void obsah_koska(int id_kosiku) throws SQLException, IOException {
        var zoznam = new Obsah_Kosika().zoznam(0,id_kosiku); //10 na stranu - napisat pocet stran
        if(zoznam.length==0){
            System.out.println("Košík je prázdny");
            return;
        }
        //System.out.println(Arrays.toString(zoznam));
        //System.out.println(zoznam.length); - pre ziskanie jedneho zaznamu treba podelit 3jkou !!!
        for(int i =0;i<zoznam.length;i+=3){
            System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]+" | Počet: "+zoznam[i+2]);
        }
        int strana = 0;
        //System.out.println("Zadaj ID košíku, ktorého obsah chceš zobraziť");
        if(new Obsah_Kosika().zoznam(10,id_kosiku).length==0) return;
        System.out.println("Zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
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
            zoznam = new Obsah_Kosika().zoznam(strana * 10,id_kosiku);
            if(zoznam.length==0) {strana--; zoznam = new Obsah_Kosika().zoznam(strana * 10,id_kosiku);}
            for(int i =0;i<zoznam.length;i+=3){
                System.out.println("ID: "+zoznam[i]+" | Meno: "+zoznam[i+1]+" | Počet: "+zoznam[i+2]);
            }

        }
    }
}
