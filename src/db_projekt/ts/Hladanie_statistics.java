package db_projekt.ts;

import db_projekt.DbContext;

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
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
/**
 *
 * @author Roman Božik
 * Užívateľ zadá začiatočný a konečný dátum, pričom aplikácia na výstupe zadá pomer priamych / nepriamych dopytov pre každý mesiac v rozsahu dátumov
 */
public class Hladanie_statistics {
    public Hladanie_statistics(String Datum1,String Datum2) throws SQLException, IOException, ParseException {
        while(true){

            if(validateDateFormat(Datum1)==false){
                System.out.println("Datum od je vadný, napíš ho znovu (formát YYYY-MM-DD");
                BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
                Datum1 = bra.readLine();

            }

            else if(validateDateFormat(Datum2)==false) {

                System.out.println("Datum do je vadný, napíš ho znovu (formát YYYY-MM-DD");
                BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
                Datum2 = bra.readLine();
            }
            else {
                java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(Datum1);
                java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(Datum2); // zdroj: https://stackoverflow.com/questions/19109960/how-to-check-if-a-date-is-greater-than-another-in-java

                if (date1.compareTo(date2) >= 0) {
                    System.out.println("Datum od je väčší ako dátum do, napíš ho (dátum od) znovu (formát YYYY-MM-DD)");
                    BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
                    Datum1 = bra.readLine();
                    continue;
                }

                if (validateDateFormat(Datum1) && validateDateFormat(Datum2)) break;
            }
        }
        int mesiace_medzi = (int) ChronoUnit.MONTHS.between(
                LocalDate.parse(Datum1).withDayOfMonth(1),
                LocalDate.parse(Datum2).withDayOfMonth(1));
        var pociatok = java.sql.Date.valueOf(Datum1);
        for(int i=0;i<mesiace_medzi+1;i++){
            System.out.print(pociatok.toString()+": ");
            var priame = pocet(pociatok.toString(),true);
            var nepriame = pocet(pociatok.toString(),false);
            if(priame <1 || nepriame<1) System.out.print("Null");
            else System.out.print(priame/nepriame);
            System.out.println();
            pociatok = Date.valueOf(pociatok.toLocalDate().plusMonths(1));
        }

    }


    public double pocet(String Datum1,boolean priame_vyhladanie) throws SQLException, ParseException {
        int priame_vyhladanye = 0;
        if(priame_vyhladanie) priame_vyhladanye = 1;

        DateTimeFormatter f = DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ;
        LocalDate ld = LocalDate.parse( Datum1 , f ) ;
        YearMonth ym = YearMonth.from( ld ) ;
        LocalDate endOfMonth = ym.atEndOfMonth() ;
        String Datum2 = endOfMonth.toString() ;         //https://stackoverflow.com/questions/13624442/getting-last-day-of-the-month-in-a-given-string-date

        Datum1 = Datum1.substring(0, Datum1.length() - 3);
        Datum1 = Datum1 +"-01";

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT  count(*) FROM DOPYT INNER JOIN vyhladavanie v on Dopyt.id = v.dopyt_id WHERE v.priame_vyhladanie = ? AND datum_vyhladavania >=? AND datum_vyhladavania <=?;")) {
            s.setInt(1,priame_vyhladanye);
            s.setDate(2,java.sql.Date.valueOf(Datum1));
            s.setDate(3,java.sql.Date.valueOf(Datum2));
            ResultSet rs = s.executeQuery();
            if (rs.next() == false) return -1;
            double count = rs.getInt(1);
            return count;
        }
    }





    public boolean validateDateFormat(String dateToValdate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); //Čmajznuté z https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
            df.setLenient(false);
            df.parse(dateToValdate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
