package db_projekt.rdg;

import db_projekt.DbContext;

import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Roman Božik
 * Brána pre faktúry
 */
public class Faktury {


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getObjednavky() {
        return objednavky;
    }

    public void setObjednavky(int objednavky) {
        this.objednavky = objednavky;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

    public Date getDatum_platby() {
        return datum_platby;
    }

    public void setDatum_platby(Date datum_platby) {
        this.datum_platby = datum_platby;
    }

    public Date getDatum_vystavenia() {
        return datum_vystavenia;
    }

    public void setDatum_vystavenia(Date datum_vystavenia) {
        this.datum_vystavenia = datum_vystavenia;
    }

    public int getMiesto_dorucenia() {
        return miesto_dorucenia;
    }

    public void setMiesto_dorucenia(int miesto_dorucenia) {
        this.miesto_dorucenia = miesto_dorucenia;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    private Integer id;
    private int objednavky;
    private int suma;
    private Date datum_platby;
    private Date datum_vystavenia;
    private int miesto_dorucenia;
    private String poznamka;

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO faktury (objednavky,suma,datum_platby,datum_vystavenia,miesto_dorucenia,poznamka) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, objednavky);
            s.setInt(2, suma);
            s.setDate(3, datum_platby);
            s.setDate(4, datum_vystavenia);
            s.setInt(5, miesto_dorucenia);
            s.setString(6, poznamka);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE faktury SET objednavky=?,suma=?,datum_platby=?,datum_vystavenia=?,miesto_dorucenia=?,poznamka=? WHERE id = ?")) {
            s.setInt(1, objednavky);
            s.setInt(2, suma);
            s.setDate(3, datum_platby);
            s.setDate(4, datum_vystavenia);
            s.setInt(5, miesto_dorucenia);
            s.setString(6, poznamka);
            s.setInt(7, id);
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM faktury WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
    public String[] hladanie_podla_atributu(String atribut) throws SQLException {
        if(atribut.charAt(0)!='%'){
            atribut = "%"+atribut+"%";
        }
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT meno_produktu FROM faktury INNER JOIN PROD_ATRIB_LIST ON PROD_ATRIB_LIST.produkt=Produkty.ID LEFT JOIN Atributy A on PROD_ATRIB_LIST.ID = A.produkty_id WHERE lower(A.hodnota) LIKE lower(?) ;")) {
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM faktury ORDER BY ID LIMIT 10 OFFSET ?")) {
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
    public String[] zoznam_nezaplatenych(int id_zakaznika) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM faktury JOIN objednavky o on faktury.objednavky = o.id WHERE o.zakaznik = ? AND datum_platby is null;")) {
            s.setInt(1, id_zakaznika);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(3));
                a.add(rs.getString(5));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }
    public String[] zoznam_na_expedovanie(int ofset) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM faktury JOIN objednavky o on faktury.objednavky = o.id WHERE o.stav = 1 ORDER BY faktury.ID LIMIT 10 OFFSET ?;")) {

            s.setInt(1, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(3));
                a.add(rs.getString(5));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }
    public boolean over_fakturu_nezaplatena(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from faktury WHERE id = ? AND datum_platby is null;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
    public void stiahni_fakturu(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from faktury WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setObjednavky(rs.getInt(2));
                setSuma(rs.getInt(3));
                setDatum_platby(rs.getDate(4));
                setDatum_vystavenia(rs.getDate(5));
                setMiesto_dorucenia(rs.getInt(6));
                setPoznamka(rs.getString(7));
            }
        }
    }
}
