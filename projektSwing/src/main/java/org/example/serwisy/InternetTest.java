package org.example.serwisy;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class InternetTest {


    public static boolean isInternetAvailable() {
        try {

            InetAddress inetAddress = InetAddress.getByName("8.8.8.8");
            return inetAddress.isReachable(5000);
        } catch (Exception e) {
            return false;
        }
    }

    public static void getError(CardLayout cardLayout, JPanel mainPanel){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicInteger x = new AtomicInteger(0);
        executor.submit(() -> {

            while (true) {
                if (!InternetTest.isInternetAvailable()) {
                    if (x.get() == 0 && !InternetTest.isInternetAvailable()) {
                        JOptionPane.showMessageDialog(null, "Brak dostępu do internetu. Przechodzimy do logowania.", "Brak internetu", JOptionPane.WARNING_MESSAGE);
                        SwingUtilities.invokeLater(() -> cardLayout.show(mainPanel, "Login"));
                        x.set(1);
                    }
                } else {
                    if (x.get() == 1) {
                        JOptionPane.showMessageDialog(null, "Przywrócono połączenie z internetem", "Połączenie odzyskane", JOptionPane.INFORMATION_MESSAGE);
                    }
                    x.set(0);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
