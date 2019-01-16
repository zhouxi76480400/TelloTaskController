package org.enes.tellotaskcontroller.threads;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class CommandReceiveThread extends Thread {

    public static final int command_receive_port = 8889;

    public static final String command_send_address = "192.168.10.1";

    public static final int command_max_buffer_size = 1460;

    public CommandReceiveThread() {
        super();
    }

    @Override
    public void run() {
        super.run();
        try {
            DatagramSocket datagramSocket = new DatagramSocket(command_receive_port);
            byte[] buff = new byte[command_max_buffer_size];
            DatagramPacket datagramPacket = new DatagramPacket(buff,command_max_buffer_size);
            while (true) {
                datagramSocket.receive(datagramPacket);
                Log.e("test","test");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
