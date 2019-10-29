/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author lccf
 */
public class SimpleSldParser implements SimpleParser {

    private File d;
    private String atributoReferencia;
    private List<Vetoriais> vetoriais;

    public SimpleSldParser(File d) {
        this.d = d;
        this.vetoriais = new ArrayList<>();
    }

    @Override
    public String getAtributoReferencia() {
        return atributoReferencia;
    }

    @Override
    public void setAtributoReferencia(String atributoReferencia) {
        this.atributoReferencia = atributoReferencia;
    }

    private Vetoriais getVetorial(Element eR, boolean has_legenda) {
        String legenda = "";
        if (has_legenda) {
            legenda = eR.getElementsByTagName("ogc:Literal")
                    .item(0)
                    .getTextContent();
        } else {
            legenda = eR.getElementsByTagName("sld:Title")
                    .item(0)
                    .getTextContent();
        }
        Vetoriais vetorial = new Vetoriais(legenda);
        NodeList pontoList = eR.getElementsByTagName("sld:PointSymbolizer");
        NodeList linhaList = eR.getElementsByTagName("sld:LineSymbolizer");
        NodeList poligonoList = eR.getElementsByTagName("sld:PolygonSymbolizer");
        NodeList cssList = eR.getElementsByTagName("sld:CssParameter");
        // Identifica se é um ponto, linha ou poligono
        if (pontoList.getLength() > 0) { // <-- Errado não é Atributo e sim Tag
            vetorial.setTipo(Vetoriais.PONTO);
            // Varre os parametros CSS de ponto para achar a cor de preenchimento do ponto.
            for (int i = 0; i < cssList.getLength(); i++) {
                Element eC = (Element) cssList.item(i);
                if (eC.getAttribute("name").equals("fill")) {
                    vetorial.setHex(eC.getTextContent());
                }
            }
            vetorial.setEspessura(eR.getElementsByTagName("sld:Size")
                    .item(0)
                    .getTextContent());
        } else if (linhaList.getLength() > 0) {
            vetorial.setTipo(Vetoriais.LINHA);
            // Varre os parametros CSS de linha para achar a cor da linha e espessura.
            for (int i = 0; i < cssList.getLength(); i++) {
                Element eC = (Element) cssList.item(i);
                if (eC.getAttribute("name").equals("stroke")) {
                    vetorial.setHex(eC.getTextContent());
                }
                if (eC.getAttribute("name").equals("stroke-width")) {
                    vetorial.setEspessura(eC.getTextContent());
                }
            }
        } else if (poligonoList.getLength() > 0) {
            vetorial.setTipo(Vetoriais.POLIGONO);
            // Varre os parametros CSS de poligono para achar a cor de preenchimento do polígono.
            for (int i = 0; i < cssList.getLength(); i++) {
                Element eC = (Element) cssList.item(i);
                if (eC.getAttribute("name").equals("fill")) {
                    vetorial.setHex(eC.getTextContent());
                }
            }
        }
        return vetorial;
    }

    @Override
    public void parse() {
        try {
            this.vetoriais = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbd = dbf.newDocumentBuilder();
            Document doc = (Document) dbd.parse(d);
            doc.getDocumentElement().normalize();
            NodeList ruleList = doc.getElementsByTagName("sld:Rule");
            for (int i = 0; i < ruleList.getLength(); i++) {
                Node noR = ruleList.item(i);
                if (noR.getNodeType() == Node.ELEMENT_NODE) {
                    Element eR = (Element) noR;
                    if (eR.getElementsByTagName("ogc:PropertyName").getLength() > 0) {
                        setAtributoReferencia(eR.getElementsByTagName("ogc:PropertyName")
                                .item(0)
                                .getTextContent());
                        vetoriais.add(getVetorial(eR, true));
                    } else {
                        vetoriais.add(getVetorial(eR, false));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Vetoriais> getListVetoriais() {
        return vetoriais;
    }

    @Override
    public String toString() {
        return "SimpleSldParser{" + "llc=" + vetoriais + '}';
    }

}
