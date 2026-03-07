-- Script de migração para converter produtos de enum para categorias dinâmicas

-- Primeiro, adicionar coluna temporária para categoria_id
ALTER TABLE tb_produto ADD COLUMN categoria_id UUID;

-- Criar mapeamento das categorias existentes para os enums
DO $$
DECLARE
    categoria_bebidas_id UUID;
    categoria_refrigerantes_id UUID;
    categoria_agua_id UUID;
    categoria_petiscos_id UUID;
    categoria_energeticos_id UUID;
    categoria_outros_id UUID;
BEGIN
    -- Buscar IDs das categorias criadas
    SELECT id INTO categoria_bebidas_id FROM tb_categoria WHERE nome = 'Bebidas Alcoólicas';
    SELECT id INTO categoria_refrigerantes_id FROM tb_categoria WHERE nome = 'Refrigerantes';
    SELECT id INTO categoria_agua_id FROM tb_categoria WHERE nome = 'Água';
    SELECT id INTO categoria_petiscos_id FROM tb_categoria WHERE nome = 'Petiscos';
    SELECT id INTO categoria_energeticos_id FROM tb_categoria WHERE nome = 'Energéticos';

    -- Criar categoria "Outros" se não existir
    INSERT INTO tb_categoria (id, nome, descricao, ativa, criado_em)
    VALUES (gen_random_uuid(), 'Outros', 'Outros produtos diversos', true, CURRENT_TIMESTAMP)
    ON CONFLICT (nome) DO NOTHING;

    SELECT id INTO categoria_outros_id FROM tb_categoria WHERE nome = 'Outros';

    -- Mapear produtos existentes para as novas categorias
    UPDATE tb_produto SET categoria_id = categoria_bebidas_id WHERE categoria IN ('CERVEJA', 'DESTILADO', 'VINHO');
    UPDATE tb_produto SET categoria_id = categoria_refrigerantes_id WHERE categoria IN ('REFRIGERANTE', 'SUCO');
    UPDATE tb_produto SET categoria_id = categoria_agua_id WHERE categoria = 'AGUA';
    UPDATE tb_produto SET categoria_id = categoria_petiscos_id WHERE categoria = 'PETISCO';
    UPDATE tb_produto SET categoria_id = categoria_energeticos_id WHERE categoria = 'ENERGETICO';
    UPDATE tb_produto SET categoria_id = categoria_outros_id WHERE categoria IN ('GELO', 'OUTROS') OR categoria_id IS NULL;
END $$;

-- Remover coluna antiga de categoria enum
ALTER TABLE tb_produto DROP COLUMN categoria;

-- Renomear coluna temporária
ALTER TABLE tb_produto RENAME COLUMN categoria_id TO categoria_id;

-- Adicionar constraint de foreign key
ALTER TABLE tb_produto ADD CONSTRAINT fk_produto_categoria
    FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id);

-- Adicionar constraint not null
ALTER TABLE tb_produto ALTER COLUMN categoria_id SET NOT NULL;
