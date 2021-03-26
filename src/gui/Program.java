package gui;

import db.DataAccessException;
import db.DatabaseRecycle;

import java.io.File;

public class Program {

    public static void main(String[] args) {
        char s = File.separatorChar;

        String setup = String.format(".%cscripts%csetup%c", s, s, s);
        String clean = String.format(".%cscripts%ccleanup%c", s, s, s);
        String populate = String.format(".%cscripts%cpopulate%c", s, s, s);

        try {
            DatabaseRecycle.recycleDatabase(clean, setup, populate);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
