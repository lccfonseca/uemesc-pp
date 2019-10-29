/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lccf
 */
public abstract class BufferOperacoes {
    
    private static List<Operacoes> ops = new ArrayList<>();;
    
    public void resetOps(){
        BufferOperacoes.ops = new ArrayList<>();
    }
    
    public void addOperacao(Operacoes op) {
        BufferOperacoes.ops.add(op);
    }

    public List<Operacoes> getOps() {
        return BufferOperacoes.ops;
    }
    
}
