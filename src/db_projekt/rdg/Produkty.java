package db_projekt.rdg;

import db_projekt.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 *
 * @author Roman Božik
 * Brána pre produkty
 */
public class Produkty {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeno_produktu() {
        return meno_produktu;
    }

    public void setMeno_produktu(String meno_produktu) {
        this.meno_produktu = meno_produktu;
    }

    public String getSpecifikacie() {
        return specifikacie;
    }

    public void setSpecifikacie(String specifikacie) {
        this.specifikacie = specifikacie;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(int dostupnost) {
        this.dostupnost = dostupnost;
    }

    public long getUpc() {
        return upc;
    }

    public void setUpc(long upc) {
        this.upc = upc;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public long getZlava() {
        return zlava;
    }

    public void setZlava(int zlava) {
        this.zlava = zlava;
    }

    public double getReklamovanost() {
        return reklamovanost;
    }

    public void setReklamovanost(double reklamovanost) {
        this.reklamovanost = reklamovanost;
    }

    public long getPocet_predanych_kusov() {
        return pocet_predanych_kusov;
    }

    public void setPocet_predanych_kusov(long pocet_predanych_kusov) {
        this.pocet_predanych_kusov = pocet_predanych_kusov;
    }

    private Integer id;
    private String meno_produktu;
    private String specifikacie;
    private String popis;
    private int cena;
    private int dostupnost;
    private long upc;
    private String kategoria;
    private int zlava;
    private double reklamovanost;
    private long pocet_predanych_kusov;

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Produkty (meno_produktu,specifikacie,popis,cena,dostupnost,upc,kategoria,zlava,reklamovanost,pocet_predanych_kusov) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, meno_produktu);
            s.setString(2, specifikacie);
            s.setString(3, popis);
            s.setInt(4, cena);
            s.setInt(5, dostupnost);
            s.setLong(6, upc);
            s.setString(7, kategoria);
            s.setInt(8, zlava);
            s.setDouble(9, reklamovanost);
            s.setLong(10, pocet_predanych_kusov);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Produkty SET meno_produktu=?,specifikacie=?,popis=?,cena=?,dostupnost=?,upc=?,kategoria=?,zlava=?,reklamovanost=?,pocet_predanych_kusov=? WHERE id = ?")) {
            s.setString(1, meno_produktu);
            s.setString(2, specifikacie);
            s.setString(3, popis);
            s.setInt(4, cena);
            s.setInt(5, dostupnost);
            s.setLong(6, upc);
            s.setString(7, kategoria);
            s.setInt(8, zlava);
            s.setDouble(9, reklamovanost);
            s.setLong(10, pocet_predanych_kusov);
            s.setInt(11, id);

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Produkty WHERE id = ?")) {
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM Produkty ORDER BY ID LIMIT 10 OFFSET ?")) {
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from produkty WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
    public void stiahni_produkt(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from produkty WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setMeno_produktu(rs.getString(2));
                setSpecifikacie(rs.getString(3));
                setPopis(rs.getString(4));
                setCena(rs.getInt(5));
                setDostupnost(rs.getInt(6));
                setUpc(rs.getLong(7));
                setKategoria(rs.getString(8));
                setZlava(rs.getInt(9));
                setReklamovanost(rs.getDouble(10));
                setPocet_predanych_kusov(rs.getLong(11));
            }
        }
    }
    public String[] zoznam_atributov(int produkt_id) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT DISTINCT atributy.hodnota FROM atributy JOIN produkty p on p.id = atributy.produkty_id WHERE p.id = ?;")) {
            s.setInt(1, produkt_id);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }
}
