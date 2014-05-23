/*
 * Copyright (C) 2014 Gian Poltéra
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ch.hsr.dbseminar.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Gian Poltéra
 * @version 1.0
 * 
 */
public class DBHelper {

    private static final String TABLENAME = "testdata"; //name of the created table
    
    private static final String QUERYCREATETABLE = "DROP TABLE IF EXISTS" + TABLENAME + ";\n"
            + "CREATE TABLE" + TABLENAME + " (id INT, value INT);\n"
            + "INSERT INTO" + TABLENAME + " (id, value) VALUES (1,10), (2,20), (3,100), (4,200);\n";

    //drop the old table and create a new table
    public static void createTable(Connection connection) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(QUERYCREATETABLE);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Table creation failed");
        }
    }

    //begin the transaction
    public static void beginTransaction(Connection connection) {
        try {
            connection.createStatement().executeUpdate("BEGIN TRANSACTION;");
        } catch (SQLException ex) {
            System.out.println("Begin transaction failed");
        }
    }

    //commits a transaction
    public static void commitTransaction(Connection connection) {
        try {
            connection.createStatement().executeUpdate("COMMIT TRANSACTION;");
        } catch (SQLException ex) {
            System.out.println("Commit transaction failed");
        }
    }

    //makes a rollback of the transaction
    public static void rollbackTransaction(Connection connection) {
        try {
            connection.createStatement().executeUpdate("ROLLBACK TRANSACTION;");
        } catch (SQLException ex) {
            System.out.println("Rollback transaction failed");
        }
    }

    //sets the isolation level
    public static void setIsolationLevel(Connection connection, String isolationlevel) {
        try {
            connection.createStatement().executeUpdate("SET TRANSACTION ISOLATION LEVEL " + isolationlevel + ";");
        } catch (SQLException ex) {
            System.out.println("Set Isolation Level failed");
        }
    }

    //inserts a new value in the table
    public static void insertValue(Connection connection) {
        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("INSERT INTO " + TABLENAME + " (id, value) VALUES (5, 30);");
            st.close();
        } catch (SQLException ex) {
            System.out.println("Insert failed");
        }
    }

    //updates a value in the table
    public static void updateValue(Connection connection) {
        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("UPDATE " + TABLENAME + " SET value=1 WHERE id=2;");
            st.close();
        } catch (SQLException ex) {
            System.out.println("Update failed");
        }
    }
    
    //updates the first value in the table
    public static void updateFirst(Connection connection, int value) {
        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("UPDATE " + TABLENAME + " SET value=" + value +" WHERE id=1;");
            st.close();
        } catch (SQLException ex) {
            System.out.println("Update failed");
        }
    }

    //deletes a value from the table
    public static void deleteValue(Connection connection) {
        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("DELETE FROM " + TABLENAME + " WHERE id=1;");
            st.close();
        } catch (SQLException ex) {
            System.out.println("Delete failed");
        }
    }

    //gets the sum over all values in the table
    public static int getSum(Connection connection) {
        Statement st = null;
        ResultSet rs = null;
        int sum = 0;
        try {
            st = connection.createStatement();
            rs = st.executeQuery("SELECT SUM(value) FROM " + TABLENAME + ";");
            rs.next();
            sum = rs.getInt(1);
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.out.println("Failed to calculate the sum");
        }
        return sum;
    }

    //gets the first value from the table
    public static int getFirst(Connection connection) {
        Statement st = null;
        ResultSet rs = null;
        int first = 0;
        try {
            st = connection.createStatement();
            rs = st.executeQuery("SELECT value FROM " + TABLENAME + " WHERE id=1;");
            rs.next();
            first = rs.getInt(1);
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.out.println("Failed to get first value");
        }
        return first;
    }

    //select table gives the whole table
    private static final String QUERYSELECTTABLE = "SELECT id, value FROM " + TABLENAME + ";";
    public static void selectTable(Connection connection) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int spalten;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(QUERYSELECTTABLE);
            rsmd = rs.getMetaData();
            spalten = rsmd.getColumnCount();

            while (rs.next()) {
                int i = 1;
                while (i < spalten + 1) {
                    System.out.print(rs.getString(i) + " ");
                    i++;
                }
                System.out.print("\n");
            }

            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.out.println("Select failed");
        }
    }
}
