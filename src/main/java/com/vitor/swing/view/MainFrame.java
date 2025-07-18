package com.vitor.swing.view;

import com.vitor.swing.service.PedidoPollingService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Criar painel de status
        PedidoStatusPanel statusPanel = new PedidoStatusPanel();

        // Criar serviço de polling
        PedidoPollingService pollingService = new PedidoPollingService(statusPanel);
        pollingService.iniciar();

        // Criar formulário e injetar statusPanel e pollingService
        PedidoFormPanel formPanel = new PedidoFormPanel(statusPanel, pollingService);

        add(formPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
    }
}
