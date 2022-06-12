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
 * Brána pre zákazníka
 */
public class Zakaznik {

    private Integer id;
    private int ICO;
    private int DIC;
    private Integer miesto;
    private String meno;
    private String email;
    private String cislo;
    private String ISIC;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getICO() {
        return ICO;
    }

    public void setICO(int ICO) {
        this.ICO = ICO;
    }

    public int getDIC() {
        return DIC;
    }

    public void setDIC(int DIC) {
        this.DIC = DIC;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public String getISIC() {
        return ISIC;
    }

    public void setISIC(String ISIC) {
        this.ISIC = ISIC;
    }

    public int getMiesto() {
        return miesto;
    }
    public void setMiesto(Integer miesto) {
        this.miesto = miesto;
    }

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("INSERT INTO Zakaznik (ICO, DIC, meno, email, cislo, ISIC,miesto) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, ICO);
            s.setInt(2, DIC);
            s.setString(3, meno);
            s.setString(4, email);
            s.setString(5, cislo);
            s.setString(6, ISIC);
            s.setInt(7, miesto);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("UPDATE Zakaznik SET ISIC = ?, ICO = ?, DIC = ?, meno = ?, email = ?, cislo = ?, miesto = ? WHERE id = ?")) {
            s.setString(1, ISIC);
            s.setInt(2, ICO);
            s.setInt(3, DIC);
            s.setString(4, meno);
            s.setString(5, email);
            s.setString(6, cislo);
            s.setInt(7, miesto);
            s.setInt(8, id);

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
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM DOPRAVA WHERE zakaznik = ?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM faktury USING objednavky WHERE objednavky.id = faktury.objednavky AND objednavky.zakaznik = ?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM faktury USING objednavky WHERE objednavky.id = faktury.objednavky AND objednavky.zakaznik = ?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM objednavky WHERE zakaznik = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Obsah_Kosika USING kosik WHERE kosik.id = obsah_kosika.kosik AND kosik.zakaznik = ?;")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM kosik WHERE zakaznik = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM vyhladavanie WHERE zakaznik_id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM Zakaznik WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }

    }

    public String[] zoznam(int ofset) throws SQLException {
        ArrayList<String> a = new ArrayList<String>();
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM Zakaznik ORDER BY ID LIMIT 10 OFFSET ?")) {
            s.setInt(1, ofset);
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                a.add(rs.getString(1));
                a.add(rs.getString(4));
            }

            return (String[]) a.toArray(new String[a.size()]);
        }


    }

    public boolean over_zakaznika(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT id from zakaznik WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
                return rs.next();
        }
    }

    public void stiahni_zakaznika(int cislo) throws SQLException{
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * from zakaznik WHERE id = ?;")) {
            s.setInt(1,cislo);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                setId(rs.getInt(1));
                setICO(rs.getInt(2));
                setDIC(rs.getInt(3));
                setMeno(rs.getString(4));
                setEmail(rs.getString(5));
                setCislo(rs.getString(6));
                setISIC(rs.getString(7));
                setMiesto(rs.getInt(8));
            }
        }
    }

    @Override
    public String toString() {
        return "Zakaznik{" +
                "id=" + id +
                ", ICO=" + ICO +
                ", DIC=" + DIC +
                ", miesto=" + miesto +
                ", meno='" + meno + '\'' +
                ", email='" + email + '\'' +
                ", cislo='" + cislo + '\'' +
                ", ISIC='" + ISIC + '\'' +
                '}';
    }


}

