package db_projekt.rdg;

import db_projekt.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Atributy {
    private Integer id;
    private Integer produkty_id; //Odkaz na Atrib list
    private String meno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdukty_id() {
        return produkty_id;
    }

    public void setProdukty_id(Integer produkty_id) {
        this.produkty_id = produkty_id;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }
    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Atributy (produkty_id,meno) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, produkty_id);
            s.setString(2, meno);


            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Atributy SET produkty_id = ?, meno=? WHERE id = ?")) {
            s.setInt(1, produkty_id);
            s.setString(2, meno);
            s.setInt(3, id);

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Atributy WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

}
