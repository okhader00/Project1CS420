package chatSystem;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private String clientName;	//Name of primary client of the server
    private Map<String, ChatServerInterface> connectedClients;	//Map of the connected clients (besides main client)

    protected ChatServer(String clientName) throws RemoteException {
        this.clientName = clientName;
        this.connectedClients = new HashMap<>();
    }

    @Override
    public void sendMessage(String message, String sender) throws RemoteException {
        System.out.println(sender + ": " + message);
    }

    @Override
    public void registerClient(String clientName, ChatServerInterface clientStub) throws RemoteException {
        connectedClients.put(clientName, clientStub);	//Adds new client to connected clients map
        System.out.println(clientName + " has joined the chat.");
    }
    
    public void receiveMessage(String message) {
    	System.out.println("Received: " + message);
    }
     
    public void sendToAll(String message) throws RemoteException {
        for (ChatServerInterface client : connectedClients.values()) {
            try {	//Message will be received by every client connected to the sending client
                client.receiveMessage(message);
            } catch (Exception e) {
                System.err.println("Failed to send message to a client: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public void listClients() {
    	System.out.println("Connected clients" + connectedClients.keySet());
    }
}
