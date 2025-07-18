package com.vitor.swing.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.UUID;

public class PedidoStatusPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable table;

    public PedidoStatusPanel() {

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID do Pedido", "Status"}, 0);

        table = new JTable(tableModel);

        table.setEnabled(false); // Desabilita edição

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void adicionarPedido(UUID id, String status) {
        tableModel.addRow(new Object[]{id.toString(), status});
    }

    // Método para atualizar o status de um pedido pelo ID
    public void atualizarStatusPedido(UUID id, String novoStatus) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String idTabela = (String) tableModel.getValueAt(i, 0);
            if (idTabela.equals(id.toString())) {
                tableModel.setValueAt(novoStatus, i, 1);
                break;
            }
        }
    }
}
