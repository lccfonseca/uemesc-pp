/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.control;

import br.uema.uemesc.dao.ConnectionFactory;
import br.uema.uemesc.util.BufferOperacoes;
import br.uema.uemesc.util.Vetoriais;
import br.uema.uemesc.util.Operacoes;
import br.uema.uemesc.util.SimpleParser;
import br.uema.uemesc.util.SimpleQmlParser;
import br.uema.uemesc.util.SimpleSldParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author lccf
 */
public class TabelaAtributosController extends BufferOperacoes {

    private final ConnectionFactory cf;
    private final Connection c;
    private final String db;
    private final String nomeTabela;
    private File dbfFile;
    private File dbfFileBkp;
    private SimpleParser sp;
    private PaletaController pc;
    private static final String COL_CLS = "GJSON_CLS";
    private static final String COL_COR = "GJSON_COR";
    private static final String COL_ESP = "GJSON_ESP";

    public TabelaAtributosController(File dbfFile, File xmlFile, int parser) throws SQLException, IOException {
        this.dbfFile = dbfFile;
        resetOps();
        switch (parser) {
            case 1:
                this.sp = new SimpleSldParser(xmlFile);
                break;
            case 2:
                this.sp = new SimpleQmlParser(xmlFile);
                break;
        }
        sp.parse();
        this.nomeTabela = this.dbfFile
                .getName()
                .replace(".dbf", "");
        this.db = this.dbfFile
                .getAbsolutePath()
                .replace(dbfFile.getName(), "");
        this.cf = new ConnectionFactory(db);
        //c = cf.getConnectionFactory("ISO-8859-1");
        c = cf.getConnectionFactory("UTF-8");
        this.pc = new PaletaController(db, sp);
        if (backupOriginalDbf()) {
            criaColunasTabelaDados();
            addOperacao(new Operacoes(Operacoes.MENSAGEM, "Colunas ["
                    + COL_CLS + ", "
                    + COL_COR + ", "
                    + COL_ESP + "] "
                    + "criadas na Tabela de Atributos!"));
        }
    }

    private boolean backupOriginalDbf() throws IOException {
        String bkp = db + "bkp_" + nomeTabela + ".dbf";
        dbfFileBkp = new File(bkp);
        if (!dbfFileBkp.exists()) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(dbfFile);
                os = new FileOutputStream(dbfFileBkp);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                is.close();
                os.close();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean criaColunasTabelaDados() throws SQLException {
        String alterTabelaDados = "alter table " + nomeTabela
                + " add " + COL_CLS + " varchar,"
                + " add " + COL_COR + " varchar(7),"
                + " add " + COL_ESP + " varchar(5)";
        Statement s = c.createStatement();
        s.execute(alterTabelaDados);
        s.close();
        return true;
    }

    public void joinTabelaDados() throws SQLException {
        List<Vetoriais> vetoriais = pc.carregaPaleta();
        for (Vetoriais vetorial : vetoriais) {
            String updateSql = "UPDATE " + nomeTabela + " SET "
                    + COL_CLS + " = '" + vetorial.getLegenda() + "', "
                    + COL_COR + " = '" + vetorial.getHex() + "', "
                    + COL_ESP + " = '" + vetorial.getEspessura() + "' ";
            if (vetoriais.size() > 1) {
                updateSql = updateSql
                        + "WHERE " + nomeTabela + "." + sp.getAtributoReferencia() + " like '"
                        + vetorial.getLegenda() + "'";
            }
            addOperacao(new Operacoes(Operacoes.SQL, updateSql));
            PreparedStatement ps = this.c.prepareStatement(updateSql);
            ps.executeLargeUpdate();
            ps.close();
        }
    }

    public void imprimeTabelaAtributos() throws SQLException {
        String sql = "select * from " + nomeTabela;
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int iNumCols = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= iNumCols; i++) {
            System.out.println(resultSetMetaData.getColumnLabel(i) + "  "
                    + resultSetMetaData.getColumnTypeName(i));
        }
        Object colval;
        while (rs.next()) {
            for (int i = 1; i <= iNumCols; i++) {
                colval = rs.getObject(i);
                System.out.print(colval + "  ");
            }
            System.out.println();
        }

        rs.close();
        s.close();
    }

    public void close() throws SQLException {
        c.close();
    }

    public PaletaController getPc() {
        return pc;
    }

    public void setPc(PaletaController pc) {
        this.pc = pc;
    }

}
