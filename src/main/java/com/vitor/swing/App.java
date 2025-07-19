package com.vitor.swing;

import com.vitor.swing.view.MainFrame;

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
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
