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
 * Užívateľ zadá počet atribútov a aplikácia na výstup vráti celkový počet predaných produktov so zadaným počtom atribútov
 */
public class predajnost_produktov {
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public predajnost_produktov(int id_pouzivatela,int id_kosika) throws SQLException, IOException, InterruptedException {
        var faktury_zoznam = new PROD_ATRIB_LIST().zoznam(0);
        if(faktury_zoznam.length==0){
            System.out.println("Nie sú evidované žiadne zhluky atribútov....");
            return;
        }
        System.out.println("Zadaj počet atribútov ");
        BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
        var vstupik = "";
        while (true){
            vstupik = bra.readLine();
            if(isNumeric(vstupik)) break;
            else System.out.println("Vstup nie je numerický, skús znovu");

        }
        var idcka = new ArrayList<Integer>();
        for(int i= 1;i<=max_pocet_produktov();i++){
            var zoznamik = new Produkty().zoznam_atributov(i);
            if(zoznamik.length==Integer.parseInt(vstupik)) idcka.add(i);
        }
        long pocet_predanych = 0;
        for(var k: idcka){
            var produkt = new Produkty();
            produkt.stiahni_produkt(k);
            pocet_predanych+=produkt.getPocet_predanych_kusov();
        }
        System.out.println("Počet predaných produktov s počtom atribútov "+vstupik+" je: "+pocet_predanych);

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

    public boolean over_list(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from PROD_ATRIB_LIST WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
    public int max_pocet_produktov() throws SQLException{
        var vysledok = -1;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT MAX(id) from produkty;")) {
            //s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();

            while(rs.next()){
                vysledok = rs.getInt(1);
            }
        }
        return vysledok;
    }
}
