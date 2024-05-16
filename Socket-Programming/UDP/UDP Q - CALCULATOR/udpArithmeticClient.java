import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class udpArithmeticClient {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        DatagramSocket ds = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        byte buf[] = null;
        while (true) {
            System.out.print("Enter expression (e.g., 2 + 3): ");
            String inp = sc.nextLine();
            buf = inp.getBytes();
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, 1234);
            ds.send(DpSend);
            if (inp.equalsIgnoreCase("bye")) {
                break;
            }
            byte[] receive = new byte[65535];
            DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
            ds.receive(DpReceive);
            System.out.println("Server: " + new String(receive));
        }
    }
}
