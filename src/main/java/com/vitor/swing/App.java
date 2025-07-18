package com.vitor.swing;

import javax.swing.*;

/**
 * App
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pedido App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
        });

    }
}
