package org.enes.tellotaskcontroller.command;

import org.enes.tellotaskcontroller.threads.CommandReceiveThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Command {

    private static final String COMMAND_ENTER_COMMAND = "command";

    public static void send_command_to_tello_without_response() {
        send_data_to_tello_no_response(COMMAND_ENTER_COMMAND);
    }

    public static void send_data_to_tello_no_response(String command) {
        try {
            DatagramPacket datagramPacket =
                    new DatagramPacket(command.getBytes(),command.length());
            datagramPacket.setPort(CommandReceiveThread.command_receive_port);
            datagramPacket.setAddress(InetAddress.getByName(CommandReceiveThread.command_send_address));
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
