package db_projekt.rdg;

import db_projekt.DbContext;

import java.sql.*;
import java.util.ArrayList;

public class Doprava {


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getZakaznik() {
        return zakaznik;
    }

    public void setZakaznik(int zakaznik) {
        this.zakaznik = zakaznik;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getMiesto_dorucenia() {
        return miesto_dorucenia;
    }

    public void setMiesto_dorucenia(int miesto_dorucenia) {
        this.miesto_dorucenia = miesto_dorucenia;
    }

    public int getTyp_dovozu() {
        return typ_dovozu;
    }

    public void setTyp_dovozu(int typ_dovozu) {
        this.typ_dovozu = typ_dovozu;
    }

    public int getStav() {
        return stav;
    }

    public void setStav(int stav) {
        this.stav = stav;
    }

    private Integer id;
    private int zakaznik;
    private String track_id;
    private Date datum;
    private int miesto_dorucenia;
    private int typ_dovozu;
    private int stav;

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO doprava (zakaznik,track_id,datum,miesto_dorucenia,typ_dovozu,stav) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, zakaznik);
            s.setString(2, track_id   );
            s.setDate(3, datum);
            s.setInt(4, miesto_dorucenia);
            s.setInt(5, typ_dovozu);
            s.setInt(6, stav);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE doprava SET zakaznik=?,track_id=?,datum=?,miesto_dorucenia=?,typ_dovozu=?,stav=? WHERE id = ?")) {
            s.setInt(1, zakaznik);
            s.setString(2, track_id   );
            s.setDate(3, datum);
            s.setInt(4, miesto_dorucenia);
            s.setInt(5, typ_dovozu);
            s.setInt(6, stav);

            s.executeUpdate();
        }
    }

    public void upsert() throws SQLException {
        if (id == null) {
            insert();
        } else {
            update();
        }
    }

    public void delete() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM doprava WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
    public String[] hladanie_podla_atributu(String atribut) throws SQLException {
        if(atribut.charAt(0)!='%'){
            atribut = "%"+atribut+"%";
        }
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT meno_produktu FROM doprava INNER JOIN PROD_ATRIB_LIST ON PROD_ATRIB_LIST.produkt=Produkty.ID LEFT JOIN Atributy A on PROD_ATRIB_LIST.ID = A.produkty_id WHERE lower(A.hodnota) LIKE lower(?) ;")) {
            s.setString(1,atribut);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }

    }
    public String[] zoznam(int ofset) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM doprava ORDER BY ID LIMIT 10 OFFSET ?")) {
            s.setInt(1, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(2));
                a.add(rs.getString(5));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }
    public boolean over_produkt(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from doprava WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
    public void stiahni_produkt(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from doprava WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setZakaznik(rs.getInt(2));
                setTrack_id(rs.getString(3));
                setDatum(rs.getDate(4));
                setTyp_dovozu(rs.getInt(5));
                setStav(rs.getInt(6));
            }
        }
    }
}
