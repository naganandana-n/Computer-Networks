import java.io.*;
import java.net.*;

public class tcpClientHandler implements Runnable {
    private final Socket clientSocket;

    public tcpClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.printf("Sent from the client: %s\n", line);
                String response = processRequest(line);
                out.println(response);
                out.flush();
                if (line.trim().equalsIgnoreCase("BYE")) {
                    System.out.println("Client socket closed");
                    break;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processRequest(String request) {
        String[] tokens = request.split(" ");
        if (tokens.length != 3) {
            return "Invalid input. Please use the format: <operand1> <operator> <operand2>";
        }

        try {
            double operand1 = Double.parseDouble(tokens[0]);
            double operand2 = Double.parseDouble(tokens[2]);
            String operator = tokens[1];
            double result;

            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 == 0) {
                        return "Error: Division by zero";
                    }
                    result = operand1 / operand2;
                    break;
                default:
                    return "Invalid operator. Use +, -, *, or /";
            }

            return "Result: " + result;
        } catch (NumberFormatException e) {
            return "Invalid number format. Please ensure operands are numbers.";
        }
    }
}
