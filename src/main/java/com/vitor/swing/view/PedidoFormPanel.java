package com.vitor.swing.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.swing.model.Pedido;
import com.vitor.swing.service.PedidoPollingService;
import okhttp3.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class PedidoFormPanel extends JPanel {

    private final JTextField produtoField;
    private final JTextField quantidadeField;
    private final JButton enviarButton;

    private final PedidoStatusPanel statusPanel;
    private final PedidoPollingService pollingService;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PedidoFormPanel(PedidoStatusPanel statusPanel, PedidoPollingService pollingService) {
        this.statusPanel = statusPanel;
        this.pollingService = pollingService;

        produtoField = new JTextField(10);
        quantidadeField = new JTextField(5);
        enviarButton = new JButton("Enviar Pedido");

        setLayout(new FlowLayout());

        add(new JLabel("Produto:"));
        add(produtoField);

        add(new JLabel("Quantidade:"));
        add(quantidadeField);

        add(enviarButton);

        enviarButton.addActionListener(e -> enviarPedido());
    }

    private void enviarPedido() {
        if (!validarCampos()) return;

        Pedido pedido = criarPedido();

        // Adiciona na UI
        statusPanel.adicionarPedido(pedido.getIdPedido(), "ENVIADO, AGUARDANDO PROCESSO");
        pollingService.adicionarPedido(pedido.getIdPedido());

        enviarParaBackend(pedido);
    }

    private boolean validarCampos() {
        String produto = produtoField.getText().trim();
        String quantidade = quantidadeField.getText().trim();

        if (produto.isEmpty() || quantidade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return false;
        }

        try {
            Integer.parseInt(quantidade);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return false;
        }

        return true;
    }

    private Pedido criarPedido() {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(UUID.randomUUID());
        pedido.setProduto(produtoField.getText().trim());
        pedido.setQuantidade(Integer.parseInt(quantidadeField.getText().trim()));
        return pedido;
    }

    private void enviarParaBackend(Pedido pedido) {
        try {
            String json = objectMapper.writeValueAsString(pedido);

            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/pedidos")
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(PedidoFormPanel.this,
                                    "Erro ao enviar pedido: " + ex.getMessage())
                    );
                }

                @Override
                public void onResponse(Call call, Response response) {
                    response.close();
                    if (!response.isSuccessful()) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(PedidoFormPanel.this,
                                        "Falha ao enviar pedido: " + response.code())
                        );
                    }
                }
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao processar pedido: " + ex.getMessage());
        }
    }
}
