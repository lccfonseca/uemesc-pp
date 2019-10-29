/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc;

import br.uema.uemesc.util.SimpleQmlParser;
import java.io.File;

/**
 *
 * @author lccf
 */
public class Testes {

    public static void main(String args[]) {
        
        File fDbf = new File("/home/lccf/imesc/solos/solos_05_09_2019_semrebiogurupi.dbf");
        File fSld = new File("/home/lccf/imesc/solos/solos_05_09_2019_semrebiogurupi.sld");
        File fQml = new File("/home/lccf/imesc/GEOMORFOLOGIA_said/GEOMO_AREA.qml");
        
        SimpleQmlParser sqp = new SimpleQmlParser(fQml);
        if (fQml.exists())
            sqp.parse();
        else System.err.println("Arquivo não exite!!!");
        
    }

}
