package db_projekt.rdg;

import db_projekt.DbContext;

import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Roman Božik
 * Brána pre objednávky
 */
public class Objednavky {
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

    public int getKosik() {
        return kosik;
    }

    public void setKosik(int kosik) {
        this.kosik = kosik;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public int getStav() {
        return stav;
    }

    public void setStav(int stav) {
        this.stav = stav;
    }

    private Integer id;
    private int zakaznik;
    private int kosik;
    private Date datum;
    private String poznamka;
    private int stav;

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO objednavky (zakaznik,kosik,datum,poznamka,stav) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, zakaznik);
            s.setInt(2, kosik   );
            s.setDate(3, datum);
            s.setString(4, poznamka);
            s.setInt(5, stav);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE objednavky SET zakaznik=?,kosik=?,datum=?,poznamka=?,stav=? WHERE id = ?")) {
            s.setInt(1, zakaznik);
            s.setInt(2, kosik   );
            s.setDate(3, datum);
            s.setString(4, poznamka);
            s.setInt(5, stav);
            s.setInt(6, id);

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM objednavky WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
    public String[] hladanie_podla_atributu(String atribut) throws SQLException {
        if(atribut.charAt(0)!='%'){
            atribut = "%"+atribut+"%";
        }
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT meno_produktu FROM Produkty INNER JOIN PROD_ATRIB_LIST ON PROD_ATRIB_LIST.produkt=Produkty.ID LEFT JOIN Atributy A on PROD_ATRIB_LIST.ID = A.produkty_id WHERE lower(A.hodnota) LIKE lower(?) ;")) {
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM objednavky ORDER BY ID LIMIT 10 OFFSET ?")) {
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
    public boolean over_objednavku_expedovanie(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT faktury.id FROM faktury JOIN objednavky o on faktury.objednavky = o.id WHERE o.stav = 1 ORDER BY faktury.ID;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
    public void stiahni_produkt(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from objednavky WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setZakaznik(rs.getInt(2));
                setKosik(rs.getInt(3));
                setDatum(rs.getDate(4));
                setPoznamka(rs.getString(5));
                setStav(rs.getInt(6));
            }
        }
    }
}
