import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udpArithmeticServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(1234);
        byte[] receive = new byte[65535];
        DatagramPacket DpReceive = null;
        while (true) {
            DpReceive = new DatagramPacket(receive, receive.length);
            ds.receive(DpReceive);
            String expression = data(receive).toString().trim();
            System.out.println("Client: " + expression);
            if (expression.equalsIgnoreCase("bye")) {
                System.out.println("Client sent bye... Exiting.");
                break;
            }
            String[] tokens = expression.split(" ");
            if (tokens.length != 3) {
                sendResponse(ds, DpReceive.getAddress(), DpReceive.getPort(), "Invalid expression format.");
            }
			else {
                try {
                    double operand1 = Double.parseDouble(tokens[0]);
                    double operand2 = Double.parseDouble(tokens[2]);
                    double result = 0;
                    if (tokens[1].equals("+")) {
                        result = operand1 + operand2;
                    }
					else if (tokens[1].equals("-")) {
                        result = operand1 - operand2;
                    }
					else if (tokens[1].equals("*")) {
                        result = operand1 * operand2;
                    }
					else if (tokens[1].equals("/")) {
                        if (operand2 == 0) {
                            sendResponse(ds, DpReceive.getAddress(), DpReceive.getPort(), "Division by zero error.");
                            receive = new byte[65535];
                            continue;
                        }
                        result = operand1 / operand2;
                    }
					else {
                        sendResponse(ds, DpReceive.getAddress(), DpReceive.getPort(), "Invalid operator.");
                        receive = new byte[65535];
                        continue;
                    }
                    sendResponse(ds, DpReceive.getAddress(), DpReceive.getPort(), String.valueOf(result));
                }
				catch (NumberFormatException e) {
                    sendResponse(ds, DpReceive.getAddress(), DpReceive.getPort(), "Invalid operands.");
                }
            }
            receive = new byte[65535];
        }
    }

    public static StringBuilder data(byte[] a) {
        if (a == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    public static void sendResponse(DatagramSocket ds, InetAddress address, int port, String response) throws IOException {
        byte[] buffer = response.getBytes();
        DatagramPacket DpSend = new DatagramPacket(buffer, buffer.length, address, port);
        ds.send(DpSend);
    }
}
