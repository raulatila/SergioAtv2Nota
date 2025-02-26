package com.caixaeletronico.cliente;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {

            // Mensagem de boas-vindas
            System.out.println(in.readLine()); // Bem-vindo ao Caixa Eletrônico!

            // Autenticação
            System.out.print(in.readLine()); // Usuário:
            String usuario = consoleIn.readLine();
            out.println(usuario);
            System.out.print(in.readLine()); // Senha:
            String senha = consoleIn.readLine();
            out.println(senha);

            // Resposta da autenticação
            String resposta = in.readLine();
            System.out.println(resposta); // Autenticação bem-sucedida! ou Autenticação falhou!

            if (resposta.equals("Autenticação bem-sucedida!")) {
                boolean sair = false;
                while (!sair) {
                    // Exibe o menu de operações
                    System.out.println(in.readLine()); // Escolha uma operação:
                    System.out.println(in.readLine()); // 1 - Depositar
                    System.out.println(in.readLine()); // 2 - Sacar
                    System.out.println(in.readLine()); // 3 - Consultar Saldo
                    System.out.println(in.readLine()); // 4 - Sair

                    // Lê a opção do usuário
                    String opcao = consoleIn.readLine();
                    out.println(opcao);

                    switch (opcao) {
                        case "1": // Depositar
                            System.out.println(in.readLine()); // Informe o número da conta e o valor a depositar:
                            
                            // Lê o número da conta e o valor do depósito digitado pelo usuário
                            System.out.print("Número da conta: ");
                            String conta = consoleIn.readLine();
                            System.out.print("Valor a depositar: ");
                            String valor = consoleIn.readLine();
                            
                            // Envia o número da conta e o valor no formato esperado pelo servidor
                            out.println(conta + " " + valor);

                            // Exibe a resposta do servidor
                            System.out.println(in.readLine()); // Depósito realizado com sucesso!
                            break;
                        case "2": // Sacar
                            System.out.println(in.readLine()); // Informe o valor a sacar:
                            String saque = consoleIn.readLine();
                            out.println(saque);
                            System.out.println(in.readLine()); // Resposta do servidor (sucesso ou erro)
                            break;
                        case "3": // Consultar Saldo
                            System.out.println(in.readLine()); // Saldo da conta X: R$ Y
                            break;
                        case "4": // Sair
                            sair = true;
                            System.out.println(in.readLine()); // Conexão encerrada. Obrigado por usar o Caixa Eletrônico!
                            break;
                        default:
                            System.out.println(in.readLine()); // Opção inválida!
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}