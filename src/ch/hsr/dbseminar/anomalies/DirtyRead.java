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

package ch.hsr.dbseminar.anomalies;

import ch.hsr.dbseminar.db.DBConnection;
import ch.hsr.dbseminar.db.DBHelper;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author Gian Poltéra
 * @version 1.0
 * 
 */
public class DirtyRead {
    
    public void dirtyRead(String isolationlevel) throws SQLException {
        //Dirty Read
        Connection connection = new DBConnection().connect();
        DBHelper.createTable(connection);
        connection.close();

        //T1 begin
        System.out.println("T1 begin work");
        Connection T1 = new DBConnection().connect();
        DBHelper.beginTransaction(T1);
        DBHelper.setIsolationLevel(T1, isolationlevel);

        //T1 read
        int sum1 = DBHelper.getSum(T1);
        System.out.println("T1 read the sum -> " + sum1);

        //T2 begin
        System.out.println("                                      T2 begin work");
        Connection T2 = new DBConnection().connect();
        DBHelper.beginTransaction(T2);
        DBHelper.setIsolationLevel(T2, isolationlevel);

        //T2 update
        System.out.print("                                      T2 update a value, sum -> ");
        DBHelper.updateValue(T2);
        System.out.println(DBHelper.getSum(T2));

        //T1 read
        int sum2 = DBHelper.getSum(T1);
        System.out.println("T1 read the sum -> " + sum2);
        T1.close();

        //T2 rollback
        System.out.println("                                      T2 makes a rollback");
        DBHelper.rollbackTransaction(T2);
        T2.close();

        //Check
        if (sum1 == sum2) {
            System.out.println("OK: No Dirty Read");
        } else {
            System.out.println("FAILURE: Dirty Read");
        }
    }
}
