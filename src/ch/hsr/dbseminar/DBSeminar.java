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

package ch.hsr.dbseminar;

import ch.hsr.dbseminar.anomalies.DirtyRead;
import ch.hsr.dbseminar.anomalies.NonRepeateableRead;
import ch.hsr.dbseminar.anomalies.LostUpdate;
import ch.hsr.dbseminar.anomalies.PhantomRead;
import java.sql.SQLException;

/**
 * 
 * @author Gian Poltéra
 * @version 1.0
 * 
 */
public class DBSeminar {

    private static final String LEVEL1 = "READ UNCOMMITTED";
    private static final String LEVEL2 = "READ COMMITTED";
    private static final String LEVEL3 = "REPEATABLE READ";
    private static final String LEVEL4 = "SERIALIZABLE";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        startDirtyRead(); //starts the DirtyRead Demo
        startNonRepeateableRead(); //starts the startNonRepeateableRead Demo
        startPhantomRead(); //starts the PhantomRead Demo
        startLostUpdate(); //starts the LostUpdate Demo
    }

    private static void startDirtyRead() throws SQLException {
        //Dirty Read
        System.out.println("Start DirtyRead with " + LEVEL1);
        System.out.println("--------------------------------------------");
        new DirtyRead().dirtyRead(LEVEL1);
        System.out.println("\n--------------------------------------------\n");
    }

    private static void startNonRepeateableRead() throws SQLException {
        //Nonrepeatable Read 1
        System.out.println("Start Nonrepeatable Read with " + LEVEL2);
        System.out.println("--------------------------------------------");
        new NonRepeateableRead().nonRepeateableRead(LEVEL2);
        System.out.println("\n--------------------------------------------\n");

        //Nonrepeatable Read 2
        System.out.println("Start Nonrepeatable Read with " + LEVEL3);
        System.out.println("--------------------------------------------");
        new NonRepeateableRead().nonRepeateableRead(LEVEL3);
        System.out.println("\n--------------------------------------------\n");
    }

    private static void startPhantomRead() throws SQLException {
        //PhantomRead 1
        System.out.println("Start PhantomRead with " + LEVEL2);
        System.out.println("--------------------------------------------");
        new PhantomRead().phantomRead(LEVEL2);
        System.out.println("\n--------------------------------------------\n");

        //PhantomRead 2
        System.out.println("Start PhantomRead with " + LEVEL3);
        System.out.println("--------------------------------------------");
        new PhantomRead().phantomRead(LEVEL3);
        System.out.println("\n--------------------------------------------\n");
    }

    private static void startLostUpdate() throws SQLException {
        //LostUpdate 1
        System.out.println("Start LostUpdate with " + LEVEL2);
        System.out.println("--------------------------------------------");
        new LostUpdate().lostUpdate(LEVEL2);
        System.out.println("\n--------------------------------------------\n");

        //LostUpdate 2
        System.out.println("Start LostUpdate with " + LEVEL3);
        System.out.println("--------------------------------------------");
        new LostUpdate().lostUpdate(LEVEL3);
        System.out.println("\n--------------------------------------------\n");
    }
}
