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
 * Funkcia slúžiaca na zrušenie objednávky na základe vstupného ID objednávky
 */
public class zrusenie_objednavky {
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public zrusenie_objednavky(int id_pouzivatela,int id_kosika) throws SQLException, IOException, InterruptedException {
        var faktury_zoznam = new Faktury().zoznam_nezaplatenych(id_pouzivatela);
        if(faktury_zoznam.length==0){
            System.out.println("Nie sú evidované žiadne neuhradené platby....");
            return;
        }
        for(int i=0;i< faktury_zoznam.length;i=i+3) System.out.println("ID: "+faktury_zoznam[i]+" | Suma: "+faktury_zoznam[i+1]+" | Dátum vystavenia: "+faktury_zoznam[i+2]);
        System.out.println("Vyber id objednávky, ktorú chceš zrušiť");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        var vstup = br.readLine();
        while (true){
            if(isNumeric(vstup) && new Faktury().over_fakturu_nezaplatena(Integer.parseInt(vstup))) break;
            else if (isNumeric(vstup)) System.out.println("Faktúra s takýmto ID neexistuje, alebo nepatrí používateľovi");
            else System.out.println("Vstup nie je numerický");
            vstup = br.readLine();

        }



        try {
            //connection.setTransactionIsolation(desiredIsolationLevel); //https://stackoverflow.com/questions/20748046/jdbc-transactions-and-resultsets-and-select-statements
            DbContext.getConnection().setAutoCommit(false); // starting transaction
            var faktura = new Faktury();
            faktura.setId(Integer.valueOf(vstup));
            faktura.delete();
            update_dostupnost(id_kosika);





            DbContext.getConnection().commit(); // committing transaction
            System.out.println("Faktúra s ID "+faktura.getId()+" úspešne zmazaná");

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
}
