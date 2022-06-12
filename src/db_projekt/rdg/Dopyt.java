package db_projekt.rdg;
/**
 *
 * @author Roman Božik
 * Brána pre dopyty
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import db_projekt.DbContext;

public class Dopyt {
    private Integer id;
    private String dopyt;
    private long pocet=0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDopyt() {
        return dopyt;
    }

    public void setDopyt(String dopyt) {
        this.dopyt = dopyt;
    }

    public long getPocet() {
        return pocet;
    }

    public void setPocet(long pocet) {
        this.pocet = pocet;
    }

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Dopyt (priame_vyhladanie, pocet_vyhladani) VALUES (?,?) ON CONFLICT(priame_vyhladanie) DO UPDATE SET pocet_vyhladani=?", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, dopyt);
            s.setLong(2, pocet);
            s.setLong(3, pocet);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Dopyt SET  pocet_vyhladani = ? WHERE id = ?")) {
            s.setLong(1, pocet);
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Dopyt WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

}
