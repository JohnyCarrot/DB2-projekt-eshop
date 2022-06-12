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
 * Brána pre listy atribútov
 */
public class PROD_ATRIB_LIST {
    private Integer id;
    private Integer produkt;
    private String meno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProdukt() {
        return produkt;
    }

    public void setProdukt(Integer produkt) {
        this.produkt = produkt;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }
    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO PROD_ATRIB_LIST (produkt,meno) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, produkt);
            s.setString(2, meno);


            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE PROD_ATRIB_LIST SET produkt = ?, meno=? WHERE id = ?")) {
            s.setInt(1, produkt);
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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM PROD_ATRIB_LIST WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    public String[] zoznam(int ofset) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM PROD_ATRIB_LIST ORDER BY ID LIMIT 10 OFFSET ?")) {
            s.setInt(1, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(3));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }
    public boolean over_list(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from PROD_ATRIB_LIST WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            return rs.next();
        }
    }
}
