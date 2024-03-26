package seminar.client;

import seminar.server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Client {

  public static void main(String[] args) {
    try {
      Socket serverSocket = new Socket("localhost", Server.PORT);
      System.out.println("Подключились к серверу: tcp://localhost:" + Server.PORT);

      // Читаем с сервера приветственное сообщение
      Scanner serverIn = new Scanner(serverSocket.getInputStream());
      String input = serverIn.nextLine();
      System.out.println("Сообщение от сервера: " + input);

      // Отправили идентфиикатор на сервер
      new PrintWriter(serverSocket.getOutputStream(), true).println(UUID.randomUUID());

      new Thread(new ServerReader(serverSocket)).start();
      new Thread(new ServerWriter(serverSocket)).start();
    } catch (IOException e) {
      throw new RuntimeException("Не удалось подключиться к серверу: " + e.getMessage(), e);
    }
  }

}

class ServerWriter implements Runnable {
  private final Socket serverSocket;

  public ServerWriter(Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    Scanner consoleReader = new Scanner(System.in);
    try (PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true)) {
      while (true) {
        String msgFromConsole = consoleReader.nextLine();
        out.println(msgFromConsole);

        // uuid msg

        if (Objects.equals("/exit", msgFromConsole)) {
          System.out.println("Отключаемся...");
          break;
        }
      }
    } catch (IOException e) {
      System.err.println("Ошибка при отправке на сервер: " + e.getMessage());
    }


    try {
      serverSocket.close();
    } catch (IOException e) {
      System.err.println("Ошибка при отключении от сервера: " + e.getMessage());
    }
  }

}

class ServerReader implements Runnable {
  private final Socket serverSocket;

  public ServerReader(Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    try (Scanner in = new Scanner(serverSocket.getInputStream())) {
      while (in.hasNext()) {
        String input = in.nextLine();
//        System.out.println("Сообщение от сервера: " + input);
        System.out.println(input);
      }
    } catch (IOException e) {
      System.err.println("Ошибка при отключении чтении с сервера: " + e.getMessage());
    }

    try {
      serverSocket.close();
    } catch (IOException e) {
      System.err.println("Ошибка при отключении от сервера: " + e.getMessage());
    }
  }

}
