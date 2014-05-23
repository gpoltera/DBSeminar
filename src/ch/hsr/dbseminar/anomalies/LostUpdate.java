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
public class LostUpdate {

    private Connection T1 = new DBConnection().connect();
    private Connection T2 = new DBConnection().connect();

    public void lostUpdate(String isolationlevel) throws SQLException {
        //LostUpdate
        Connection connection = new DBConnection().connect();
        DBHelper.createTable(connection);
        connection.close();

        //T1 begin
        System.out.println("T1 begin work");
        DBHelper.beginTransaction(T1);
        DBHelper.setIsolationLevel(T1, isolationlevel);

        //T2 begin      
        System.out.println("                                      T2 begin work");
        DBHelper.beginTransaction(T2);
        DBHelper.setIsolationLevel(T2, isolationlevel);

        //T1 read
        System.out.print("T1 read the first value -> ");
        int before1 = DBHelper.getFirst(T1);
        System.out.println(before1);

        //T2 read
        System.out.print("                                      T2 read the first value -> ");
        int before2 = DBHelper.getFirst(T1);
        System.out.println(before2);

        //T1 update
        int after1 = before1 + 100;
        System.out.println("T1 update first value, from " + before1 + " to " + after1);
        DBHelper.updateFirst(T1, after1);

        //T2 update
        final int after2 = before2 - 5;
        System.out.println("                                      T2 update first value, from " + before2 + " to " + after2);
        //open the second update in a new Thread for waiting of the commit from T1
        new Thread() {
            public void run() {
                try {
                    DBHelper.updateFirst(T2, after2);
                } catch (Exception ex) {
                    System.out.println("Failure in Thread");
                }
            }
        }.start();
        //sleep for two seconds to be sure that the thread is started
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        //T1 commit
        System.out.println("T1 commit");
        DBHelper.commitTransaction(T1);

        //T2 commit
        System.out.println("                                      T2 commit");
        DBHelper.commitTransaction(T2);

        //check
        int dbvalueafter = DBHelper.getFirst(T1);
        if (dbvalueafter == after1) {
            System.out.println("OK: No LostUpdate, Value in DB -> " + dbvalueafter);
        } else {
            System.out.println("FAILURE: LostUpdate, Value in DB -> " + dbvalueafter);
        }

        T1.close();
        T2.close();
    }
}
