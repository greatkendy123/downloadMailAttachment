package br.nom.mulle.rodrigo.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException {
        String user = args[0];
        String psswd = args[1];
        String pathToSave = args[2];

        ReadConfig readConfig = new ReadConfig("imap-mail.outlook.com", "993");

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date initialDate = formato.parse("01/01/2018");
        Date finalDate = formato.parse("11/07/2018");

        DownloadMail mail1 = new DownloadMail.DownloadMailBuilder(user, psswd, pathToSave)
                .onlySeen(Boolean.TRUE)
                .configs(readConfig)
                .initialDate(initialDate)
                .finalDate(finalDate)
                .build();

        mail1.run();
    }
}
