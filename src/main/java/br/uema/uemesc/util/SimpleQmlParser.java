/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author lccf
 */
public class SimpleQmlParser implements SimpleParser {

    private File d;
    private String atributoReferencia;
    private List<Vetoriais> llc;

    public SimpleQmlParser(File d) {
        this.d = d;
        this.llc = new ArrayList<>();
    }

    @Override
    public String getAtributoReferencia() {
        return atributoReferencia;
    }

    @Override
    public void setAtributoReferencia(String atributoReferencia) {
        this.atributoReferencia = atributoReferencia;
    }

    private void ignoreDtd(DocumentBuilder builder) {
        builder.setEntityResolver(new EntityResolver() {

            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                System.out.println("Ignoring " + publicId + ", " + systemId);
                return new InputSource(new StringReader(""));
            }
        });
    }

    @Override
    public void parse() {
        try {
            this.llc = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbd = dbf.newDocumentBuilder();

            ignoreDtd(dbd);

            Document doc = (Document) dbd.parse(d);
            doc.getDocumentElement().normalize();

            NodeList rulesList = doc.getElementsByTagName("rule");
            for (int i = 0; i < rulesList.getLength(); i++) {
                Node noR = rulesList.item(i);
                if (noR.getNodeType() == Node.ELEMENT_NODE) {
                    Element eR = (Element) noR;
                    //Recupera a legenda e o código do symbol para recuperar a cor
                    String legenda = eR.getAttribute("label");
                    String simbolo = eR.getAttribute("symbol");
                    //Recupera o filter para extrair o atributo de referencia
                    String filtro = eR.getAttribute("filter");
                    String[] arrFiltro = filtro.split(" = ", 2);
                    setAtributoReferencia(arrFiltro[0]);
                    //A cor é recuperada a partir da busca por symbol
                    System.out.println("Legenda: " + legenda + " - " + getAtributoReferencia() + " - " + simbolo);
                    String cor = "#000000";
                    NodeList symbolList = doc.getElementsByTagName("symbol");
                    for (int j = 0; j < symbolList.getLength(); j++) {
                        Node noS = symbolList.item(j);
                        Element eS = (Element) noS;
                        if (eS.getAttribute("name").equals(simbolo)) {
                            NodeList propList = eS.getElementsByTagName("prop");
                            for (int k = 0; k < propList.getLength(); k++) {
                                Node noP = propList.item(k);
                                Element eP = (Element) noP;
                                if (eP.getAttribute("k").equals("color")) {
                                    cor = eP.getAttribute("v");
                                }
                            }
                        }
                    }
                    String[] rgb = cor.split(",", 4);                    
                    Color c = Vetoriais.parse("rgb("+rgb[0]+","+rgb[1]+","+rgb[2]+")");
                    Vetoriais lc = new Vetoriais(legenda, c);
                    llc.add(lc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Vetoriais> getListVetoriais() {
        return llc;
    }

}
