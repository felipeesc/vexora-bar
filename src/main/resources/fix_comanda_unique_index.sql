
ALTER TABLE tb_comanda
    DROP CONSTRAINT IF EXISTS uk5n46cenbfdxx2ocxdi730exvh;



CREATE UNIQUE INDEX IF NOT EXISTS uq_comanda_mesa_identificador_aberta
    ON tb_comanda (mesa, identificador)
    WHERE aberta = true;
