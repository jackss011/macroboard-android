package com.jackss.ag.macroboard.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Send udp packets to a specific address and port
 */
public class UdpSender
{
    private static final String TAG = "UdpSender";
    
    private ExecutorService executorService;
    

    /** Runnable used to send strings to udp host, port */
    private class SendingTask implements Runnable
    {
        private final int port;
        private final InetAddress address;
        private final String data;

        SendingTask(InetAddress address, int port, String data)
        {
            this.address = address;
            this.port = port;
            this.data = data;
        }

        @Override
        public void run()
        {
            try (DatagramSocket socket = new DatagramSocket())
            {
                byte buff[] = data.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(buff, buff.length, address, port);
                socket.send(packet);
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public UdpSender()
    {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void sendData(InetAddress address, int port, String data)
    {
        executorService.execute(new SendingTask(address, port, data));
    }
}
