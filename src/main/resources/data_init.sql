-- Data initialization for Vexora application
-- Statement separator is ^^ (configured in application.yaml)

-- Update existing records to have default values
UPDATE tb_comanda_itens SET cancelado = FALSE WHERE cancelado IS NULL^^

-- Insert default categories if they don't exist
INSERT INTO tb_categoria (id, nome, descricao, ativa, criado_em)
VALUES
    (gen_random_uuid(), 'Bebidas Alcoólicas', 'Cervejas, vinhos, destilados', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Refrigerantes', 'Refrigerantes e sucos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Água', 'Águas e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Petiscos', 'Salgadinhos e petiscos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Energéticos', 'Energéticos e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Outros', 'Outros produtos diversos', true, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO NOTHING^^
