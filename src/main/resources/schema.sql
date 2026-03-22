-- Schema migration for the Vexora application
-- Statement separator is ^^ (configured in application.yaml)

-- Add cancelado column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS cancelado BOOLEAN NOT NULL DEFAULT FALSE^^

-- Add data_cancelamento column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS data_cancelamento TIMESTAMP(6)^^

-- Add motivo_cancelamento column if it doesn't exist
ALTER TABLE tb_comanda_itens ADD COLUMN IF NOT EXISTS motivo_cancelamento VARCHAR(255)^^

-- Update existing role values before changing the constraint
UPDATE tb_users SET role = 'ADMIN' WHERE role = 'ROLE_ADMIN'^^
UPDATE tb_users SET role = 'FUNCIONARIO' WHERE role = 'ROLE_USER'^^

-- Update the role constraint to allow new enum values
ALTER TABLE tb_users DROP CONSTRAINT IF EXISTS tb_users_role_check^^
ALTER TABLE tb_users ADD CONSTRAINT tb_users_role_check CHECK (role IN ('ADMIN', 'GERENTE', 'FUNCIONARIO'))^^

-- Migrate produtos to use dynamic categories
-- Step 1: ensure tb_categoria table exists (in case schema is fresh)
CREATE TABLE IF NOT EXISTS tb_categoria (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)^^

-- Step 2: ensure default categories exist
INSERT INTO tb_categoria (id, nome, descricao, ativa, criado_em)
VALUES
    (gen_random_uuid(), 'Bebidas Alcoólicas', 'Cervejas, vinhos, destilados', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Refrigerantes', 'Refrigerantes e sucos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Água', 'Águas e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Petiscos', 'Salgadinhos e petiscos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Energéticos', 'Energéticos e isotônicos', true, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Outros', 'Outros produtos diversos', true, CURRENT_TIMESTAMP)
ON CONFLICT (nome) DO NOTHING^^

-- Step 3: add categoria_id column if it doesn't exist (nullable first)
ALTER TABLE tb_produto ADD COLUMN IF NOT EXISTS categoria_id UUID^^

-- Step 4: assign any products without a category to 'Outros'
UPDATE tb_produto SET categoria_id = (SELECT id FROM tb_categoria WHERE nome = 'Outros' LIMIT 1)
WHERE categoria_id IS NULL^^

-- Step 5: drop old enum categoria column if it still exists
ALTER TABLE tb_produto DROP COLUMN IF EXISTS categoria^^

-- Step 6: set NOT NULL on categoria_id (safe because step 4 ensures no NULLs remain)
ALTER TABLE tb_produto ALTER COLUMN categoria_id SET NOT NULL^^
