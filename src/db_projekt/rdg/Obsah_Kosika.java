package db_projekt.rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import db_projekt.DbContext;

/**
 *
 * @author Roman Božik
 * Brána pre obsah v košíku
 */
public class Obsah_Kosika {


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProdukt_id() {
        return produkt_id;
    }

    public void setProdukt_id(int produkt_id) {
        this.produkt_id = produkt_id;
    }

    public int getKosik_id() {
        return kosik_id;
    }

    public void setKosik_id(int kosik_id) {
        this.kosik_id = kosik_id;
    }

    public int getPocet_poloziek() {
        return pocet_poloziek;
    }

    public void setPocet_poloziek(int pocet_poloziek) {
        this.pocet_poloziek = pocet_poloziek;
    }

    private Integer id;
    private int produkt_id;
    private int kosik_id;
    private int pocet_poloziek;




    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Obsah_Kosika (produkt,kosik,pocet_poloziek) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, produkt_id);
            s.setInt(2, kosik_id);
            s.setInt(3, pocet_poloziek);


            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Obsah_Kosika SET produkt = ?, kosik = ?,pocet_poloziek = ? WHERE id = ?")) {
            s.setInt(1, produkt_id);
            s.setInt(2, kosik_id);
            s.setInt(3, pocet_poloziek);
            s.setInt(4, id);


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


        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Obsah_Kosika WHERE id =?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }




    }

    public void delete_cely() throws SQLException {


        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Obsah_Kosika WHERE kosik =?;")) {
            s.setInt(1, kosik_id);

            s.executeUpdate();
        }




    }

    public String[] zoznam(int ofset,int id_kosika) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM Obsah_Kosika WHERE kosik = ? ORDER BY ID LIMIT 10 OFFSET ?")) {
            s.setInt(1,id_kosika);
            s.setInt(2, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                var test = new Produkty();
                //test.setId(rs.getInt(2));
                test.stiahni_produkt(rs.getInt(2));
                a.add(test.getMeno_produktu());
                a.add(rs.getString(4));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }



    public boolean over_obsah(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from Obsah_Kosika WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
                return rs.next();
        }
    }

    public void stiahni_kosik(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from Obsah_Kosika WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setProdukt_id(rs.getInt(2));
                setKosik_id(rs.getInt(3));
                setPocet_poloziek(rs.getInt(4));
            }
        }
    }




}

