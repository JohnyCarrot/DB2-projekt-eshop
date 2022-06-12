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
 * Brána pre košík
 */
public class Kosik {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getZakaznik_id() {
        return zakaznik_id;
    }

    public void setZakaznik_id(int zakaznik_id) {
        this.zakaznik_id = zakaznik_id;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    private Integer id;
    private int zakaznik_id;
    private String meno;




    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Kosik (zakaznik,meno) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, zakaznik_id);
            s.setString(2, meno);


            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Kosik SET zakaznik = ?, meno = ? WHERE id = ?")) {
            s.setInt(1, zakaznik_id);
            s.setString(2, meno);
            s.setInt(2, id);


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


        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Obsah_Kosika USING kosik WHERE kosik.id = obsah_kosika.kosik AND obsah_kosika.id = ?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM kosik WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }

    }

    public String[] zoznam(int ofset,int zakaznik) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM Kosik WHERE id = ? ORDER BY ID LIMIT 10 OFFSET ?")) {
            s.setInt(1, zakaznik);
            s.setInt(2, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(3));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }

    public boolean over_kosik(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from Kosik WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
                return rs.next();
        }
    }

    public boolean over_kosik_zakaznik(int cislo,int zakaznik_id) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from Kosik WHERE id = ? AND zakaznik = ?;")) {
            s.setInt(1,cislo);
            s.setInt(2,zakaznik_id);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }

    public void stiahni_kosik(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from Kosik WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setZakaznik_id(rs.getInt(2));
                setMeno(rs.getString(3));
            }
        }
    }




}

