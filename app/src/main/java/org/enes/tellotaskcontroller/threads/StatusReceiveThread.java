package org.enes.tellotaskcontroller.threads;

import android.content.Intent;

import org.enes.tellotaskcontroller.MyApplication;
import org.enes.tellotaskcontroller.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class StatusReceiveThread extends Thread {

    public static final String status_broadcast_action_name = "TELLO_STATUS_BROADCAST";

    public static final String status_broadcast_extra_name = "data";

    private long last_broadcast_time;

    public static final long min_broadcast_update_interval = 1000;

    public static final int status_receive_port = 8890;

    public static final int status_max_buffer_size = 1460;

    public StatusReceiveThread() {
        super();
    }

    @Override
    public void run() {
        super.run();
        try {
            DatagramSocket datagramSocket = new DatagramSocket(status_receive_port);
            byte[] buff = new byte[status_max_buffer_size];
            DatagramPacket datagramPacket = new DatagramPacket(buff,status_max_buffer_size);
            while (true) {
                datagramSocket.receive(datagramPacket);
                String str = new String(datagramPacket.getData(),datagramPacket.getOffset(),datagramPacket.getLength());
                HashMap<String,String> status_data = Utils.statusDataToHashMap(str);
                long now_time = System.currentTimeMillis();
                status_data.put("update_time", String.valueOf(now_time));
                if(now_time - last_broadcast_time >= min_broadcast_update_interval) {
                    Intent intent = new Intent();
                    intent.putExtra(status_broadcast_extra_name,status_data);
                    intent.setAction(status_broadcast_action_name);
                    MyApplication.getInstance().sendBroadcast(intent);
                    last_broadcast_time = now_time;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
