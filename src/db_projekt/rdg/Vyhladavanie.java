package db_projekt.rdg;
import db_projekt.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
/**
 *
 * @author Roman Božik
 * Brána pre dopyty
 */
public class Vyhladavanie {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZakaznik_id() {
        return zakaznik_id;
    }

    public void setZakaznik_id(Integer zakaznik_id) {
        this.zakaznik_id = zakaznik_id;
    }

    public int getPriame_vyhladanie() {
        return priame_vyhladanie;
    }

    public void setPriame_vyhladanie(int priame_vyhladanie) {
        this.priame_vyhladanie = priame_vyhladanie;
    }

    public Date getDatum_vyhladavania() {
        return datum_vyhladavania;
    }

    public void setDatum_vyhladavania(Date datum_vyhladavania) {
        this.datum_vyhladavania = datum_vyhladavania;
    }

    private Integer id;
    private Integer zakaznik_id;
    private int priame_vyhladanie;

    public int getDopyt_id() {
        return dopyt_id;
    }

    public void setDopyt_id(int dopyt_id) {
        this.dopyt_id = dopyt_id;
    }

    private int dopyt_id;
    private Date datum_vyhladavania;
    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Vyhladavanie (zakaznik_id, priame_vyhladanie, datum_vyhladavania,dopyt_id) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, zakaznik_id);
            s.setInt(2, priame_vyhladanie);
            s.setDate(3, datum_vyhladavania);
            s.setInt(4, dopyt_id);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Vyhladavanie SET zakaznik_id = ?, priame_vyhladanie = ?, datum_vyhladavania = ?,dopyt_id=? WHERE id = ?")) {
            s.setInt(1, zakaznik_id);
            s.setInt(2, priame_vyhladanie);
            s.setDate(3, datum_vyhladavania);
            s.setInt(4, dopyt_id);
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Vyhladavanie WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
