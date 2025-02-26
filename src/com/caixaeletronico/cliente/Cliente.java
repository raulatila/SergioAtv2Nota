package com.caixaeletronico.cliente;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {

            
            System.out.println(in.readLine()); 

            
            System.out.print(in.readLine()); 
            String usuario = consoleIn.readLine();
            out.println(usuario);
            System.out.print(in.readLine()); 
            String senha = consoleIn.readLine();
            out.println(senha);

            
            String resposta = in.readLine();
            System.out.println(resposta); 

            if (resposta.equals("Autenticação bem-sucedida!")) {
                boolean sair = false;
                while (!sair) {
                    
                    System.out.println(in.readLine()); 
                    System.out.println(in.readLine()); 
                    System.out.println(in.readLine()); 
                    System.out.println(in.readLine()); 
                    System.out.println(in.readLine()); 

                    
                    String opcao = consoleIn.readLine();
                    out.println(opcao);

                    switch (opcao) {
                        case "1": 
                            System.out.println(in.readLine()); 
                            
                            
                            System.out.print("Número da conta: ");
                            String conta = consoleIn.readLine();
                            System.out.print("Valor a depositar: ");
                            String valor = consoleIn.readLine();
                            
                            
                            out.println(conta + " " + valor);

                            
                            System.out.println(in.readLine()); 
                            break;
                        case "2":
                            System.out.println(in.readLine()); 
                            String saque = consoleIn.readLine();
                            out.println(saque);
                            System.out.println(in.readLine()); 
                            break;
                        case "3": 
                            System.out.println(in.readLine()); 
                            break;
                        case "4": 
                            sair = true;
                            System.out.println(in.readLine()); 
                            break;
                        default:
                            System.out.println(in.readLine()); 
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}