/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lccf
 */
public class Vetoriais {

    private int tipo;
    private String legenda;
    private String hex;
    private Color rgb;
    private String espessura;
    //Constantes de tipo
    public static final int PONTO = 1;
    public static final int LINHA = 2;
    public static final int POLIGONO = 3;

    public Vetoriais(String legenda) {
        this(legenda, "#000000");
    }
    
    public Vetoriais(String legenda, String hex) {
        this.legenda = legenda;
        this.hex = hex;
        this.rgb = Color.decode(hex);
        this.espessura = "";
    }

    public Vetoriais(String legenda, Color rgb) {
        this.legenda = legenda;
        this.rgb = rgb;
        this.hex = toHexString(rgb);
        this.espessura = "";
    }

    public Vetoriais(String legenda, String hex, String espessura) {
        this(legenda, hex);
        this.espessura = espessura;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Color getRgb() {
        return rgb;
    }

    public void setRgb(Color rgb) {
        this.rgb = rgb;
    }

    public String getEspessura() {
        return espessura;
    }

    public void setEspessura(String espessura) {
        this.espessura = espessura;
    }

    private String toHexString(Color colour) throws NullPointerException {
        String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
        }
        return "#" + hexColour;
    }

    public static Color parse(String input) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(input);
        if (m.matches()) {
            return new Color(Integer.valueOf(m.group(1)), // r
                    Integer.valueOf(m.group(2)), // g
                    Integer.valueOf(m.group(3))); // b 
        }
        return null;
    }

}
