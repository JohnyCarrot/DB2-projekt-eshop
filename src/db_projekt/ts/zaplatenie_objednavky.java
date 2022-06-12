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
 * Funkcia na základe ktorej môže užívateľ uhradiť nezaplatenú a nevyexpedovanú objednávku
 */
public class zaplatenie_objednavky {
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public zaplatenie_objednavky(int id_pouzivatela,int id_kosika) throws SQLException, IOException, InterruptedException {
        var faktury_zoznam = new Faktury().zoznam_nezaplatenych(id_pouzivatela);
        if(faktury_zoznam.length==0){
            System.out.println("Nie sú evidované žiadne neuhradené platby....");
            return;
        }
        for(int i=0;i< faktury_zoznam.length;i=i+3) System.out.println("ID: "+faktury_zoznam[i]+" | Suma: "+faktury_zoznam[i+1]+" | Dátum vystavenia: "+faktury_zoznam[i+2]);
        System.out.println("Vyber id objednávky, ktorú chceš uhradiť");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        var vstup = br.readLine();
        while (true){
            if(isNumeric(vstup) && new Faktury().over_fakturu_nezaplatena(Integer.parseInt(vstup))) break;
            else if (isNumeric(vstup)) System.out.println("Faktúra s takýmto ID neexistuje,je zaplatená, alebo nepatrí používateľovi");
            else System.out.println("Vstup nie je numerický");
            vstup = br.readLine();

        }
        var id_faktury = Integer.parseInt(vstup);
        var faktura = new Faktury();
        faktura.stiahni_fakturu(id_faktury);
        System.out.println("Zadaj číslo karty (16 miestne)");
        vstup = br.readLine();
        while (true){
            if(isNumeric(vstup) && vstup.length()==16) break;
            else if (isNumeric(vstup)) System.out.println("Vstup mal "+vstup.length()+" znakov namiesto 16");
            else System.out.println("Vstup nie je numerický");
            vstup = br.readLine();
        }
        System.out.println("Zadaj dátum expirácie karty (formát MM/yyyy)");
        vstup = br.readLine();
        while (true){
            if(validateDateFormat(vstup)) break;
            System.out.println("Vstup nie je vo valídnom formáte");
            vstup = br.readLine();
        }
        System.out.println("Zadaj CVC kod (3 miestne číslo)");
        vstup = br.readLine();
        while (true){
            if(isNumeric(vstup) && vstup.length()==3) break;
            else if (isNumeric(vstup)) System.out.println("Vstup mal "+vstup.length()+" znakov namiesto 3");
            else System.out.println("Vstup nie je numerický");
            vstup = br.readLine();
        }
        System.out.println("Uveď sumu k úhrade");
        vstup = br.readLine();
        while (true){
            if (isNumeric(vstup))break;
            System.out.println("Vstup nie je numerický");
            vstup = br.readLine();
        }
        int suma = Integer.parseInt(vstup);
        while(true) {
            System.out.println("Prosím o trpezlivosť, spájam sa s bankou...");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(450,1780));
            if(faktura.getSuma()==suma) break;
            System.out.println("Bohužiaľ, zadaná suma nesedí s požadovanou platbou, vraciam peniaze....");
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(450,1780));
            System.out.println("Peniaze úspešne vrátené, pre pokračovanie, preveďte prosím požadovanú sumu ("+faktura.getSuma()+")");
            vstup = br.readLine();
            while (true){
                if (isNumeric(vstup))break;
                System.out.println("Vstup nie je numerický");
                vstup = br.readLine();
            }
            suma = Integer.parseInt(vstup);

        }

        try {
            //connection.setTransactionIsolation(desiredIsolationLevel); //https://stackoverflow.com/questions/20748046/jdbc-transactions-and-resultsets-and-select-statements
            DbContext.getConnection().setAutoCommit(false); // starting transaction
            if(produkty_nedostupne(id_kosika)) {System.out.println("Upozornenie: Nie všetok tovar je momentálne na sklade, budeme objednávať z veľkoskladu");update_dostupnost(id_kosika);}
            var obj = new Objednavky();
            obj.setId(faktura.getObjednavky());
            obj.stiahni_produkt(faktura.getObjednavky());
            obj.setStav(1);
            obj.update();
            faktura.setDatum_platby(new Date(Calendar.getInstance().getTimeInMillis()));
            faktura.update();





            DbContext.getConnection().commit(); // committing transaction
            System.out.println("Faktúra "+faktura.getId()+" úspešne uhradená");

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE produkty as t SET dostupnost = 0 FROM kosik t1, obsah_kosika t2 WHERE t1.id = ? AND t.id = t2.produkt AND t1.id = t2.kosik AND dostupnost<0;")) {
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

    public boolean over_zaplatenie(int fasktura) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT datum_platby FROM faktury WHERE id=? AND datum_platby is null;")) {
            s.setInt(1,fasktura);
            ResultSet rs = s.executeQuery();

            while(rs.next()){
                if(rs.getDate(1) == null) return false;
            }
        }
        return true;
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
