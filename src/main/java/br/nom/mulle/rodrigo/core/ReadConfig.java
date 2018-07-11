package br.nom.mulle.rodrigo.core;

import java.util.Properties;

public class ReadConfig {

    private String host;
    private String port;

    public ReadConfig(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public Properties getProperties() {
        Properties properties = new Properties();

        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host",host);
        properties.put("mail.imaps.port", port);

        return properties;
    }
}
