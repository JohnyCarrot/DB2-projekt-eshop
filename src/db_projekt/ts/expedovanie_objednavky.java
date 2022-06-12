package db_projekt.ts;

import db_projekt.DbContext;
import db_projekt.rdg.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author Roman Božik
 * Užívateľ zadá ID objednávky, ktorú chce vyexpedovať. Pokiaľ je objednávka uhradená a na sklade je dostatok skladových zásob, objednávka bude vyexpedovaná
 */
public class expedovanie_objednavky {
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public expedovanie_objednavky(int id_pouzivatela,int id_kosika) throws SQLException, IOException, InterruptedException {
        var faktury_zoznam = new Faktury().zoznam_na_expedovanie(0);
        if(faktury_zoznam.length==0){
            System.out.println("Nie sú evidované žiadne objednávky na expedovanie....");
            return;
        }
        for(int i=0;i< faktury_zoznam.length;i=i+3) System.out.println("ID: "+faktury_zoznam[i]+" | Suma: "+faktury_zoznam[i+1]+" | Dátum vystavenia: "+faktury_zoznam[i+2]);

        int strana = 0;
        System.out.println("Zadaj ID faktúry, ktorej objednávku chceš vyexpedovať");
        System.out.println("Ak zoznam obsahuje viac strán, l-s5;r dopredu;exit - odchod");
        BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
        var vstupik = "";
        while (true){
            vstupik = bra.readLine();
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
                if(over_objednavku_expedovanie(Integer.parseInt(vstupik))==false){
                    System.out.println("Objednávka s týmto ID neexistuje, alebo nie je pripravená na expedovanie, skús znovu");
                    continue;
                }
                break;
            }
            else {
                System.out.println("Nerozumiem ti, odchádzam");
                break;
            }
            var zoznam = new Faktury().zoznam_na_expedovanie(strana * 10);
            if(zoznam.length==0) {strana--; zoznam = new Faktury().zoznam_na_expedovanie(strana * 10);}
            for(int i =0;i<zoznam.length;i+=3){
                System.out.println("ID: "+zoznam[i]+" | Suma: "+zoznam[i+1]+" | Dátum vystavenia: "+zoznam[i+2]);
            }

        }
        try {
            //connection.setTransactionIsolation(desiredIsolationLevel); //https://stackoverflow.com/questions/20748046/jdbc-transactions-and-resultsets-and-select-statements
            DbContext.getConnection().setAutoCommit(false); // starting transaction
            var faktura = new Faktury();
            faktura.setId(Integer.valueOf(vstupik));
            faktura.stiahni_fakturu(Integer.parseInt(vstupik));
            var objednavka = new Objednavky();
            objednavka.stiahni_produkt(faktura.getObjednavky());
            boolean uspech = false;
            if (produkty_nedostupne(objednavka.getKosik())) System.out.println("Ešte nie je dostatok skladových zásob");
            else  {objednavka.setStav(2);uspech = true;objednavka.update();}


            DbContext.getConnection().commit(); // committing transaction
             if(uspech) System.out.println("Objednavka úspešne vyexpedovaná");

        } catch (final Throwable ex) {
            DbContext.getConnection().rollback(); // rolling back the transaction upon any exception
            // doing exception-handling stuff
            ex.printStackTrace();
        } finally {
            DbContext.getConnection().setAutoCommit(true); // setting auto-commit mode back to true
            //connection.setTransactionIsolation(previousIsolationLevel); // setting transaction isolation to previous value
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

    public int suma_kosik(int cislo) throws SQLException{
        var vysledok = -1;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT sum(produkty.cena * obsah_kosika.pocet_poloziek) FROM obsah_kosika JOIN kosik ON obsah_kosika.kosik = kosik.id JOIN produkty ON obsah_kosika.produkt = produkty.id WHERE kosik.id=?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();

            while(rs.next()){
            vysledok = rs.getInt(1);
            }
        }
        return vysledok;
    }

    public void update_dostupnost(int id_kosika) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE produkty as t SET dostupnost = dostupnost + t2.pocet_poloziek FROM kosik t1, obsah_kosika t2 WHERE t1.id = ? AND t.id = t2.produkt AND t1.id = t2.kosik;")) {
            s.setInt(1,id_kosika);
            s.executeUpdate();

        }
    }
    public boolean produkty_nedostupne(int kosik) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT count(*) FROM obsah_kosika JOIN kosik ON obsah_kosika.kosik = kosik.id JOIN produkty ON obsah_kosika.produkt = produkty.id WHERE kosik.id=? AND dostupnost<0;")) {
            s.setInt(1,kosik);
            ResultSet rs = s.executeQuery();

            while(rs.next()){
                if(rs.getInt(1)>0) return true;
            }
        }
        return false;
    }
    public boolean validateDateFormat(String dateToValdate) {
        try {
            DateFormat df = new SimpleDateFormat("MM/yyyy"); //Čmajznuté z https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
            df.setLenient(false);
            df.parse(dateToValdate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public boolean over_objednavku_expedovanie(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT faktury.id FROM faktury JOIN objednavky o on faktury.objednavky = o.id WHERE o.stav = 1 AND faktury.id = ? ORDER BY faktury.ID;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
}
