package db_projekt.rdg;
/**
 *
 * @author Roman Božik
 * Brána pre Bydliská
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import db_projekt.DbContext;

public class Miesta {
    private Integer id;
    private Integer miesto; //V DB ako "miesto" - preklep
    private String adresa;
    private String mesto;
    private int psc;
    private String stat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMiesto() {
        return miesto;
    }

    public void setMiesto(Integer miesto) {
        this.miesto = miesto;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public int getPsc() {
        return psc;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Miesta (adresa, mesto, psc, stat) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, adresa);
            s.setString(2, mesto);
            s.setInt(3, psc);
            s.setString(4, stat);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Miesta SET adresa = ?, mesto = ?, psc = ?, stat = ? WHERE id = ?")) {
            s.setString(1, adresa);
            s.setString(2, mesto);
            s.setInt(3, psc);
            s.setString(4, stat);
            s.setInt(5, id);

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Miesta WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
    public void stiahni(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from Miesta WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setAdresa(rs.getString(2));
                setMesto(rs.getString(3));
                setPsc(rs.getInt(4));
                setStat(rs.getString(5));
            }
        }
    }
}
