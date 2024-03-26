package seminar.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

    // http -> 8080
    // https -> 443
    // smtp -> 25
    // ...

    public static final int PORT = 8181;

    public static void main(String[] args) {
        final Map<String, ClientHandler> clients = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Подключился новый клиент: " + clientSocket);

                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientOut.println("Подключение успешно. Пришлите свой идентификатор");

                    Scanner clientIn = new Scanner(clientSocket.getInputStream());
                    String clientId = clientIn.nextLine();
                    System.out.println("Идентификатор клиента " + clientSocket + ": " + clientId);

                    String allClients = clients.entrySet().stream()
                            .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
                            .collect(Collectors.joining("\n"));
                    clientOut.println("Список доступных клиентов: \n" + allClients);

                    ClientHandler clientHandler = new ClientHandler(clientSocket, clients, clientId);
                    new Thread(clientHandler).start();

                    for (ClientHandler client : clients.values()) {
                        client.send("Подключился новый клиент: " + clientSocket + ", id = " + clientId);
                    }
                    clients.put(clientId, clientHandler);
                } catch (IOException e) {
                    System.err.println("Произошла ошибка при взаимодействии с клиентом: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось начать прослушивать порт " + PORT, e);
        }
    }

}

class ClientHandler implements Runnable {

    private final Socket clientSocket;

    private final String uuid;
    private final PrintWriter out;
    private final Map<String, ClientHandler> clients;

    public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients, String uuid) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clients = clients;
        this.uuid = uuid;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(clientSocket.getInputStream())) {
            while (true) {
                if (clientSocket.isClosed()) {
                    System.out.println("Клиент " + uuid + " " + clientSocket + " отключился");
                    break;
                }

                String input = in.nextLine();
                System.out.println("Получено сообщение от клиента " + clientSocket + ": " + input);

                String toClientId = null;
                if (input.startsWith("@")) {
                    String[] parts = input.split("\\s+");
                    if (parts.length > 0) {
                        toClientId = parts[0].substring(1);
                    }
                }

                if (input.startsWith("/")) {
                    switch (input) {
                        case "/all" -> {
                            StringBuilder msg = new StringBuilder();
                            msg.append("\nСписок текущих участников (кроме вас):\n");
                            for (String clientId : clients.keySet()) {
                                if (clientId.equals(this.uuid)) continue;
                                msg.append("\t" + clientId + "\n");
                            }
                            this.send(msg.toString());
                            continue;
                        }
                        case "/exit" -> {
                            ClientHandler client = clients.remove(this.uuid);
                            try {
                                if (client != null) this.clientSocket.close();
                            } catch (IOException e) {
                                System.err.println("Ошибка при отключении клиента " + this.clientSocket
                                        + ": " + e.getMessage());
                            }
                            clients.values()
                                    .forEach(it -> it.send("Пользователь " + uuid + " отключился"));
//                            System.out.println("Клиент " + uuid + " отключился");
                            continue;
                        }
                        default -> {
                            break;
                        }
                    }
                }

                if (toClientId == null) {
                    for (String key: clients.keySet()) {
                        if (key.equals(uuid)) clients.get(key).send("Я: " + input);
                        else  clients.get(key).send(uuid + ": " + input);
                    }
                } else {
                    ClientHandler toClient = clients.get(toClientId);
                    if (toClient != null) {
                        toClient.send(input.replace("@" + toClientId + " ", ""));
                    } else {
                        System.err.println("Не найден клиент с идентфиикатором: " + toClientId);
                    }
                }
            }
        } catch (
                IOException e) {
            System.err.println("Произошла ошибка при взаимодействии с клиентом " + clientSocket + ": " + e.getMessage());
        }
    }

    public void send(String msg) {
        out.println(msg);
    }
}
