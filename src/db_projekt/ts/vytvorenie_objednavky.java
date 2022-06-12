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
import java.util.*;
/**
 *
 * @author Roman Božik
 * Funkcia na základe ktorej môže užívateľ vytvoriť objednávku
 */
public class vytvorenie_objednavky {
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public vytvorenie_objednavky(int id_pouzivatela,int id_kosika) throws SQLException, IOException {
        if(new Kosik().zoznam(0,id_kosika).length==0){
            System.out.println("Košík: Som tak prázdny....");
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Miesta bydlisko = new Miesta();
        System.out.println("Zadaj adresu (ulica,cislo domu v kope) kam chceš poslať objednávku");
        var vstup = br.readLine();
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
        try {
            //connection.setTransactionIsolation(desiredIsolationLevel); //https://stackoverflow.com/questions/20748046/jdbc-transactions-and-resultsets-and-select-statements
            DbContext.getConnection().setAutoCommit(false); // starting transaction
            System.out.println("Suma za tovar: "+suma_kosik(id_kosika));
            var suma_za_dovoz = new Random().nextInt(5,15);
            System.out.println("Suma za dovoz: "+suma_za_dovoz);
            System.out.println();
            var objednavka = new Objednavky();
            objednavka.setZakaznik(id_pouzivatela);
            objednavka.setKosik(id_kosika);
            objednavka.setDatum(new Date(Calendar.getInstance().getTimeInMillis()));
            objednavka.setStav(0); //vytvorená
            System.out.println("Vlož poznámku k objednávke");
            vstup = br.readLine();
            objednavka.setPoznamka(vstup);
            update_dostupnost(id_kosika);
            if(produkty_nedostupne(id_kosika)) System.out.println("Upozornenie: Nie všetok tovar je momentálne na sklade");
            objednavka.insert();
            var faktura = new Faktury();
            faktura.setObjednavky(objednavka.getId());
            faktura.setSuma(suma_za_dovoz+suma_kosik(id_kosika));
            faktura.setDatum_vystavenia(new Date(Calendar.getInstance().getTimeInMillis()));
            faktura.setMiesto_dorucenia(bydlisko.getId());
            faktura.insert();
            System.out.println("Objednávka úspešne vytvorená a faktúra bola vystavená"); //Doplniť vystavenie faktúry





            DbContext.getConnection().commit(); // committing transaction


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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE produkty as t SET dostupnost = dostupnost - t2.pocet_poloziek FROM kosik t1, obsah_kosika t2 WHERE t1.id = ? AND t.id = t2.produkt AND t1.id = t2.kosik;")) {
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
}
