package com.caixaeletronico.servidor;

public class Conta {
    private int numero;
    private String usuario;
    private String senha;
    private double saldo;

    public Conta(int numero, String usuario, String senha, double saldo) {
        this.numero = numero;
        this.usuario = usuario;
        this.senha = senha;
        this.saldo = saldo;
    }

    public int getNumero() {
        return numero;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
            return true;
        }
        return false;
    }
}