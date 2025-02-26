package com.caixaeletronico.servidor;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private static Map<Integer, Conta> contas = new HashMap<>();

    public static void main(String[] args) {
        // Inicializa as contas pré-cadastradas
        contas.put(1001, new Conta(1001, "user1", "pass1", 5000.00));
        contas.put(1002, new Conta(1002, "user2", "pass2", 3000.00));

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("Bem-vindo ao Caixa Eletrônico!");
                out.println("Usuário:");
                String usuario = in.readLine();
                out.println("Senha:");
                String senha = in.readLine();

                Conta conta = autenticar(usuario, senha);
                if (conta != null) {
                    out.println("Autenticação bem-sucedida!");
                    boolean sair = false;
                    while (!sair) {
                        out.println("Escolha uma operação:");
                        out.println("1 - Depositar");
                        out.println("2 - Sacar");
                        out.println("3 - Consultar Saldo");
                        out.println("4 - Sair");
                        String opcao = in.readLine();

                        switch (opcao) {
                            case "1":
                                out.println("Informe o número da conta e o valor a depositar:");
                                String[] deposito = in.readLine().split(" ");
                                int contaDeposito = Integer.parseInt(deposito[0]);
                                double valorDeposito = Double.parseDouble(deposito[1]);
                                if (contas.containsKey(contaDeposito)) {
                                    contas.get(contaDeposito).depositar(valorDeposito);
                                    out.println("Depósito realizado com sucesso!");
                                } else {
                                    out.println("Conta inválida!");
                                }
                                break;
                            case "2":
                                out.println("Informe o valor a sacar:");
                                double valorSaque = Double.parseDouble(in.readLine());
                                if (conta.sacar(valorSaque)) {
                                    out.println("Saque realizado com sucesso!");
                                } else {
                                    out.println("Saldo insuficiente ou valor inválido!");
                                }
                                break;
                            case "3":
                                out.println("Saldo da conta " + conta.getNumero() + ": R$ " + conta.getSaldo());
                                break;
                            case "4":
                                sair = true;
                                out.println("Conexão encerrada. Obrigado por usar o Caixa Eletrônico!");
                                break;
                            default:
                                out.println("Opção inválida!");
                        }
                    }
                } else {
                    out.println("Autenticação falhou!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Conta autenticar(String usuario, String senha) {
            for (Conta conta : contas.values()) {
                if (conta.getUsuario().equals(usuario) && conta.getSenha().equals(senha)) {
                    return conta;
                }
            }
            return null;
        }
    }
}