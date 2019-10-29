/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.io.File;
import java.util.List;

/**
 *
 * @author lccf
 */
public interface SimpleParser {

    public String getAtributoReferencia();
    public void setAtributoReferencia(String atributoReferencia);
    public void parse();
    public List<Vetoriais> getListVetoriais();

}
