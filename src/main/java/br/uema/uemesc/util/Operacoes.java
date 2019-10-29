/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uema.uemesc.util;

/**
 *
 * @author lccf
 */
public class Operacoes {
    
    private int tipo;
    private String operacao;
    public static final int NAO_ENCONTRADO = 0;
    public static final int ABERTO = 1;
    public static final int MENSAGEM = 2;
    public static final int SQL = 3;
    public static final int ERRO = 4;

    public Operacoes(int tipo, String operacao) {
        this.tipo = tipo;
        this.operacao = operacao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
    
}
