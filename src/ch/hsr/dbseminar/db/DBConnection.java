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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Gian Poltéra
 * @version 1.0
 * 
 */
public class DBConnection {
    
    private static final String DB_POSTGRES_DRIVER = "org.postgresql.Driver"; //Postgres Driver
    private static final String DB_URL_POSTGRES_PREFIX = "jdbc:postgresql://virtualpc:5432/Practices"; //URL to the Postgres Server (Localhost or Remote)
    private static final String DB_POSTGRES_USER = "postgres"; //DB username
    private static final String DB_POSTGRES_PASSWORD = "postgres"; //DB password
    private static final boolean LOG_ON = false; //Acitvates the extended Logging
    
    public Connection connect() {
        
        //Extended log-output if enabled
        if(LOG_ON) { DriverManager.setLogWriter( new PrintWriter( System.out ) ); }
        
        //Verify that the postgres driver ist inlucded in the library
        try { 
            Class.forName(DB_POSTGRES_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found, include in your library");
        }
        
        Connection connection = null;
        
        //Connecetion to the database
        try {
            connection = DriverManager.getConnection(DB_URL_POSTGRES_PREFIX, DB_POSTGRES_USER, DB_POSTGRES_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed, check Firewall, Adress, Port, Username and Password");
        }
        
        if (connection != null) {
            //System.out.println("Connected to the database");
        } else {
            System.out.println("Connection failed");
        }
                
        return connection;
    }
}
