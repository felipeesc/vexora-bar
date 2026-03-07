-- Schema migration for the Vexora application

-- Add cancelado column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS cancelado BOOLEAN NOT NULL DEFAULT FALSE;

-- Add data_cancelamento column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS data_cancelamento TIMESTAMP(6);

-- Add motivo_cancelamento column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS motivo_cancelamento VARCHAR(255);

-- Update existing role values before changing the constraint
UPDATE tb_users SET role = 'ADMIN' WHERE role = 'ROLE_ADMIN';
UPDATE tb_users SET role = 'FUNCIONARIO' WHERE role = 'ROLE_USER';

-- Update the role constraint to allow new enum values
ALTER TABLE tb_users DROP CONSTRAINT IF EXISTS tb_users_role_check;
ALTER TABLE tb_users ADD CONSTRAINT tb_users_role_check CHECK (role IN ('ADMIN', 'GERENTE', 'FUNCIONARIO'));

-- Migrate produtos to use dynamic categories
-- Step 1: ensure default categories exist
INSERT INTO tb_categoria (id, nome, descricao, ativa, criado_em)
VALUES
    (gen_random_uuid(), 'Bebidas Alcoólicas', 'Cervejas, vinhos, destilados', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Refrigerantes', 'Refrigerantes e sucos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Água', 'Águas e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Petiscos', 'Salgadinhos e petiscos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Energéticos', 'Energéticos e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Outros', 'Outros produtos diversos', true, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO NOTHING;

-- Step 2: add categoria_id column if it doesn't exist (nullable first)
ALTER TABLE tb_produto ADD COLUMN IF NOT EXISTS categoria_id UUID;

-- Step 3: map existing products to categories only if old 'categoria' column still exists
-- Uses $MIGRATION$ dollar-quoting to prevent Spring Boot from splitting the block on $$
DO $MIGRATION$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'tb_produto' AND column_name = 'categoria'
    ) THEN
        UPDATE tb_produto SET categoria_id = (
            SELECT id FROM tb_categoria WHERE nome = 'Bebidas Alcoólicas' LIMIT 1
        ) WHERE categoria_id IS NULL AND categoria IN ('CERVEJA', 'DESTILADO', 'VINHO');

        UPDATE tb_produto SET categoria_id = (
            SELECT id FROM tb_categoria WHERE nome = 'Refrigerantes' LIMIT 1
        ) WHERE categoria_id IS NULL AND categoria IN ('REFRIGERANTE', 'SUCO');

        UPDATE tb_produto SET categoria_id = (
            SELECT id FROM tb_categoria WHERE nome = 'Água' LIMIT 1
        ) WHERE categoria_id IS NULL AND categoria = 'AGUA';

        UPDATE tb_produto SET categoria_id = (
            SELECT id FROM tb_categoria WHERE nome = 'Petiscos' LIMIT 1
        ) WHERE categoria_id IS NULL AND categoria = 'PETISCO';

        UPDATE tb_produto SET categoria_id = (
            SELECT id FROM tb_categoria WHERE nome = 'Energéticos' LIMIT 1
        ) WHERE categoria_id IS NULL AND categoria = 'ENERGETICO';
    END IF;
END
$MIGRATION$;

-- Step 4: assign remaining unmapped products to 'Outros' (safe: no column reference)
UPDATE tb_produto SET categoria_id = (
    SELECT id FROM tb_categoria WHERE nome = 'Outros' LIMIT 1
) WHERE categoria_id IS NULL;

-- Step 5: drop old enum categoria column if it still exists
ALTER TABLE tb_produto DROP COLUMN IF EXISTS categoria;

-- Step 6: set NOT NULL on categoria_id now that all rows are filled
ALTER TABLE tb_produto ALTER COLUMN categoria_id SET NOT NULL;

