package com.caixaeletronico.servidor;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

public class Servidor {
    private static Map<Integer, Conta> contas = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Servidor.class.getName());

    public static void main(String[] args) {
        // Configura o logger para escrever em um arquivo
        try {
            FileHandler fileHandler = new FileHandler("caixa_eletronico.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicializa as contas pré-cadastradas
        contas.put(1001, new Conta(1001, "user1", "pass1", 5000.00));
        contas.put(1002, new Conta(1002, "user2", "pass2", 3000.00));

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado. Aguardando conexões...");
            logger.info("Servidor iniciado. Aguardando conexões...");

            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Nova conexão recebida: " + socket.getInetAddress());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Erro no servidor: " + e.getMessage());
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
                    logger.info("Usuário " + usuario + " autenticado com sucesso.");
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
                                    logger.info("Depósito de R$ " + valorDeposito + " na conta " + contaDeposito + " realizado com sucesso.");
                                } else {
                                    out.println("Conta inválida!");
                                    logger.warning("Tentativa de depósito em conta inválida: " + contaDeposito);
                                }
                                break;
                            case "2":
                                out.println("Informe o valor a sacar:");
                                double valorSaque = Double.parseDouble(in.readLine());
                                if (conta.sacar(valorSaque)) {
                                    out.println("Saque realizado com sucesso!");
                                    logger.info("Saque de R$ " + valorSaque + " na conta " + conta.getNumero() + " realizado com sucesso.");
                                } else {
                                    out.println("Saldo insuficiente ou valor inválido!");
                                    logger.warning("Tentativa de saque de R$ " + valorSaque + " na conta " + conta.getNumero() + " falhou: saldo insuficiente ou valor inválido.");
                                }
                                break;
                            case "3":
                                out.println("Saldo da conta " + conta.getNumero() + ": R$ " + conta.getSaldo());
                                logger.info("Consulta de saldo na conta " + conta.getNumero() + ": R$ " + conta.getSaldo());
                                break;
                            case "4":
                                sair = true;
                                out.println("Conexão encerrada. Obrigado por usar o Caixa Eletrônico!");
                                logger.info("Usuário " + usuario + " encerrou a conexão.");
                                break;
                            default:
                                out.println("Opção inválida!");
                                logger.warning("Opção inválida selecionada pelo usuário " + usuario + ": " + opcao);
                        }
                    }
                } else {
                    out.println("Autenticação falhou!");
                    logger.warning("Tentativa de autenticação falhou para o usuário: " + usuario);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.severe("Erro na conexão com o cliente: " + e.getMessage());
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