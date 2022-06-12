package db_projekt;
//import db_projekt.rdg.*;
import db_projekt.ts.*;
import db_projekt.ui.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner;

import db_projekt.rdg.Produkty;
import db_projekt.rdg.Zakaznik;
import org.postgresql.ds.PGSimpleDataSource;
//import sk.uniba.fmph.simko.db2.application.ui.MainMenu;

/**
 *
 * @author Roman Božik
 * Hlavná funkcia
 */

public class Main {
    public static void print() {
        //pridať možnosť prepínať medzi
        System.out.println("*********************************");
        System.out.println("* 1. Práca so zákazníkom        *");
        System.out.println("* 2. Práca s produktmi          *");
        System.out.println("*********************************");
    }

    public static void main(String[] args) throws SQLException, IOException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerName("db.dai.fmph.uniba.sk");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("playground");
        dataSource.setUser("bozik16@uniba.sk");
        dataSource.setPassword("xxxxxxxx");

        try (Connection connection = dataSource.getConnection()) {
            DbContext.setConnection(connection);

            print();
            BufferedReader bra = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.print("Tvoja volba:");
                var vstup = bra.readLine();
                    switch (vstup) {
                        case "1":
                            new Menu();
                            print();
                            break;
                        case "2":
                            new Menu_produkty();
                            print();
                            break;
                        case "3":
                            new Menu_produkty();
                            print();
                            break;
                    }
                }


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            DbContext.clear();
        }
    }
}
