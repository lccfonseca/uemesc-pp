/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lccf
 */
public class StringUtil {

    public static final String removerAcentos(String acentuada) {
        CharSequence cs = new StringBuilder(acentuada);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static final String removerEspeciais(String especial) {
        Pattern p = Pattern.compile("{cntrl}");
        Matcher m = p.matcher("");
        m.reset(especial);
        return m.replaceAll("");
    }

}
