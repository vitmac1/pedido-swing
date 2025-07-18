package com.vitor.swing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.swing.view.PedidoStatusPanel;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;

import okhttp3.*;

public class PedidoPollingService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<UUID> pendentes = new CopyOnWriteArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final PedidoStatusPanel statusPanel;

    public PedidoPollingService(PedidoStatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public void adicionarPedido(UUID id) {
        pendentes.add(id);
    }

    public void iniciar() {
        scheduler.scheduleAtFixedRate(() -> {
            for (UUID id : pendentes) {
                Request request = new Request.Builder()
                        .url("http://localhost:8080/api/pedidos/status/" + id)
                        .get()
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Pode logar ou mostrar erro
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String status = response.body().string().replace("\"", "");
                            if (status.equals("SUCESSO") || status.equals("FALHA")) {
                                pendentes.remove(id);
                            }

                            SwingUtilities.invokeLater(() -> {
                                statusPanel.atualizarStatusPedido(id, status);
                            });
                        }
                        response.close();
                    }
                });
            }
        }, 0, 4, TimeUnit.SECONDS);
    }
}
