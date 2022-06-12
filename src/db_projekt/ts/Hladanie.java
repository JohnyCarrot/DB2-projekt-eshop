package db_projekt.ts;

import db_projekt.DbContext;
import db_projekt.rdg.Dopyt;
import db_projekt.rdg.Produkty;
import db_projekt.rdg.Vyhladavanie;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Hladanie {
    /**
     *
     * @author Roman Božik
     * Pomocná brána pre vyhľadávanie
     */
    private Date datum_vyhladavania;
    private int priame_vyhladanie = 0;
    public String[] vysledok;
    public Hladanie(){

    }

    public Hladanie(String dopyt,int id_uzivatela)throws SQLException{
        vysledok = vyhladaj_podla(dopyt);
        //System.out.println(priame_vyhladanie);
       var zapis_do_historie_uzivatela =  new Vyhladavanie();
       zapis_do_historie_uzivatela.setDatum_vyhladavania(new Date(System.currentTimeMillis()));
       zapis_do_historie_uzivatela.setPriame_vyhladanie(priame_vyhladanie);
        zapis_do_historie_uzivatela.setZakaznik_id(id_uzivatela);
        var samotny_dopyt = new Dopyt();
        if(najdi_dopyt(dopyt)>-1) {
            zapis_do_historie_uzivatela.setDopyt_id((int) najdi_dopyt(dopyt));
            samotny_dopyt.setPocet(najdi_dopyt_pocet(dopyt)+1);
            samotny_dopyt.setDopyt(dopyt);
            samotny_dopyt.setId((int) najdi_dopyt(dopyt));
            samotny_dopyt.update();
        }
        else{
            samotny_dopyt.setDopyt(dopyt);
            samotny_dopyt.setPocet(1);
            samotny_dopyt.insert();
            zapis_do_historie_uzivatela.setDopyt_id(samotny_dopyt.getId());
        }
        zapis_do_historie_uzivatela.insert();


    }

    public String[] vyhladaj_podla(String dopyt) throws SQLException {
                String temp = dopyt;
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT meno_produktu FROM produkty where lower(meno_produktu) LIKE lower(?) ;")) {
            if(temp.startsWith("%")==false) temp = "%"+temp+"%";
            ArrayList<String> a = new ArrayList<String>();
            s.setString(1,temp);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                if (a.contains(rs.getString(1))==false)   a.add(rs.getString(1));
            }

            var k =  (String[]) a.toArray(new String[a.size()]);
            if(k.length>0) {priame_vyhladanie = 1;
            return k;}
        }
        ArrayList<String> a = new ArrayList<String>();
        for(var k: dopyt.split(" ")){
            for(var x : new Produkty().hladanie_podla_atributu(k)) if(a.contains(x)==false) a.add(x);
            if(a.contains(k)) continue;
           if(k.charAt(0)!='%') k = "%"+k+"%";
            try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT meno_produktu FROM produkty where lower(meno_produktu) LIKE lower(?) ;")) {
                s.setString(1,k);
                ResultSet rs = s.executeQuery();
                while(rs.next())
                {
                    if (a.contains(rs.getString(1))==false)   a.add(rs.getString(1));
                }

            }
        }

        return (String[]) a.toArray(new String[a.size()]);
    }


    public int najdi_dopyt(String dopyt) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from dopyt WHERE priame_vyhladanie = ?;")) {
            s.setString(1,dopyt);
            ResultSet rs = s.executeQuery();
            if (rs.next() == false) return -1;
            int count = rs.getInt(1);
            return count;
        }
    }
    public long najdi_dopyt_pocet(String dopyt) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT pocet_vyhladani from dopyt WHERE priame_vyhladanie = ?;")) {
            s.setString(1,dopyt);
            ResultSet rs = s.executeQuery();
            if (rs.next() == false) return 0;
            int count = rs.getInt(1);
            return count;
        }
    }
}
