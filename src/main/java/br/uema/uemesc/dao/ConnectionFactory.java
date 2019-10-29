/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author lccf
 */
public class ConnectionFactory {
    
    private String url;

    public ConnectionFactory(String url) {
        this.url = "jdbc:DBF:///" + url;
    }
    
    public Connection getConnectionFactory(String charset) throws SQLException {
        try {
            Class.forName("com.hxtt.sql.dbf.DBFDriver");
            Properties properties=new Properties();
            properties.setProperty("charSet",charset);
            return DriverManager.getConnection(this.url, properties);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
            throw new SQLException(e.getMessage());
        }
    }

}
