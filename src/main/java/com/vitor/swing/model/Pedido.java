package com.vitor.swing.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Pedido {
    private UUID idPedido;
    private String produto;
    private int quantidade;
    private LocalDateTime dataCriacao;
}

