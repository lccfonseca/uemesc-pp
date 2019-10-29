/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.control;

import br.uema.uemesc.dao.ConnectionFactory;
import br.uema.uemesc.util.BufferOperacoes;
import br.uema.uemesc.util.Operacoes;
import br.uema.uemesc.util.Vetoriais;
import br.uema.uemesc.util.SimpleParser;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jamel.dbf.DbfReader;
import org.jamel.dbf.utils.DbfUtils;

/**
 *
 * @author lccf
 */
public class PaletaController extends BufferOperacoes {

    private ConnectionFactory cf;
    private Connection c;
    private String db;
    private File paletaFile;
    private SimpleParser sp;

    public PaletaController(String db, SimpleParser sp) throws SQLException {
        this.db = db;
        this.sp = sp;
        cf = new ConnectionFactory(db);
        c = cf.getConnectionFactory("UTF-8");
        paletaFile = new File(db + "PALETA.dbf");
        if (!paletaFile.exists()) {
            criarPaleta();
            addOperacao(new Operacoes(Operacoes.MENSAGEM, "Paleta criada e carregada com: "
                    + preenchePaleta()
                    + " classe(s)!"));
        }
    }

    private void criarPaleta() throws SQLException {
        String createPaleta = "create table if not exists PALETA (LEGENDA varchar, COR varchar(7), ESPESSURA varchar(5))";
        Statement s = this.c.createStatement();
        s.execute(createPaleta);
        s.close();
    }

    private int preenchePaleta() throws SQLException {
        for (Vetoriais vetorial : sp.getListVetoriais()) {
            String insertSql = "insert into PALETA (LEGENDA, COR, ESPESSURA) values (?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(insertSql);
            ps.setString(1, vetorial.getLegenda());
            ps.setString(2, vetorial.getHex());
            ps.setString(3, vetorial.getEspessura());
            ps.executeUpdate();
            ps.close();
            addOperacao(new Operacoes(Operacoes.MENSAGEM, vetorial.getLegenda() + " - "
                    + vetorial.getHex() + " - "
                    + vetorial.getEspessura()));
        }
        return sp.getListVetoriais().size();
    }

    public List<Vetoriais> carregaPaleta() {
        try (DbfReader reader = new DbfReader(paletaFile)) {
            List<Vetoriais> vetoriais = new ArrayList<>();
            Object[] row;
            while ((row = reader.nextRecord()) != null) {
                String legenda = new String(DbfUtils.trimLeftSpaces((byte[]) row[0]));
                String cor = new String(DbfUtils.trimLeftSpaces((byte[]) row[1]));
                String espessura = new String(DbfUtils.trimLeftSpaces((byte[]) row[2]));
                Vetoriais vetorial = new Vetoriais(legenda, cor, espessura);
                vetoriais.add(vetorial);
            }
            return vetoriais;
        }
    }
}
