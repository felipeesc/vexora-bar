-- =============================================================================
-- Script  : MANTBANK_binrange_parametrizacao.sql
-- Objetivo: Parametrizar os BINs da bandeira MANTBANK para funcionamento
--           no modelo VAN.
-- Tabelas : ADQ.MC_BINRANGE
-- Autor   : Script gerado via tarefa de parametrização
-- =============================================================================

DECLARE
    v_id_bandeira        ADQ.MC_BANDEIRA.BANDEIRA%TYPE;

    v_binrange_pf        NUMBER;
    v_binrange_convenios NUMBER;
    v_binrange_alim      NUMBER;

BEGIN

    -- -------------------------------------------------------------------------
    -- 1. Recuperar o código da bandeira MANTBANK
    -- -------------------------------------------------------------------------
    SELECT BANDEIRA
      INTO v_id_bandeira
      FROM ADQ.MC_BANDEIRA
     WHERE UPPER(NOME) = 'MANTBANK'
       AND ROWNUM = 1;

    -- -------------------------------------------------------------------------
    -- 2. Gerar IDs únicos via sequence
    -- -------------------------------------------------------------------------
    SELECT ADQ.SEQ_MC_BINRANGE.NEXTVAL INTO v_binrange_pf        FROM DUAL;
    SELECT ADQ.SEQ_MC_BINRANGE.NEXTVAL INTO v_binrange_convenios FROM DUAL;
    SELECT ADQ.SEQ_MC_BINRANGE.NEXTVAL INTO v_binrange_alim      FROM DUAL;

    -- -------------------------------------------------------------------------
    -- 3. Inserir BINs na tabela ADQ.MC_BINRANGE
    -- -------------------------------------------------------------------------

    -- Produto: Pessoa Física
    INSERT INTO ADQ.MC_BINRANGE (
        ID_BINRANGE, BIN_INICIAL, BIN_FINAL, DESCRICAO,
        COD_PAIS, OPER_OFFLINE, OPER_INTER,
        DATA_INCLUSAO, DATA_BAIXA, ESTADO,
        BANDEIRA, NACIONALIDADE, PRODUTO_BANDEIRA,
        TIPO_MENSAGEM, ST_PREPAID
    ) VALUES (
        v_binrange_pf, '603637000000', '603637999999', 'MANTBANK - Pessoa Fisica',
        'BR', 0, 0,
        SYSDATE, NULL, 1,
        v_id_bandeira, 'N', 'MBK',
        2, 0
    );

    -- Produto: Convênios
    INSERT INTO ADQ.MC_BINRANGE (
        ID_BINRANGE, BIN_INICIAL, BIN_FINAL, DESCRICAO,
        COD_PAIS, OPER_OFFLINE, OPER_INTER,
        DATA_INCLUSAO, DATA_BAIXA, ESTADO,
        BANDEIRA, NACIONALIDADE, PRODUTO_BANDEIRA,
        TIPO_MENSAGEM, ST_PREPAID
    ) VALUES (
        v_binrange_convenios, '603638000000', '603638999999', 'MANTBANK - Convenios',
        'BR', 0, 0,
        SYSDATE, NULL, 1,
        v_id_bandeira, 'N', 'MBK',
        2, 0
    );

    -- Produto: Alimentação
    INSERT INTO ADQ.MC_BINRANGE (
        ID_BINRANGE, BIN_INICIAL, BIN_FINAL, DESCRICAO,
        COD_PAIS, OPER_OFFLINE, OPER_INTER,
        DATA_INCLUSAO, DATA_BAIXA, ESTADO,
        BANDEIRA, NACIONALIDADE, PRODUTO_BANDEIRA,
        TIPO_MENSAGEM, ST_PREPAID
    ) VALUES (
        v_binrange_alim, '603639000000', '603639999999', 'MANTBANK - Alimentacao',
        'BR', 0, 0,
        SYSDATE, NULL, 1,
        v_id_bandeira, 'N', 'MBK',
        2, 0
    );

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('BINs MANTBANK inseridos com sucesso.');
    DBMS_OUTPUT.PUT_LINE('  Pessoa Fisica  -> ID_BINRANGE: ' || v_binrange_pf);
    DBMS_OUTPUT.PUT_LINE('  Convenios      -> ID_BINRANGE: ' || v_binrange_convenios);
    DBMS_OUTPUT.PUT_LINE('  Alimentacao    -> ID_BINRANGE: ' || v_binrange_alim);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20001,
            'Bandeira MANTBANK nao encontrada em ADQ.MC_BANDEIRA. ' ||
            'Verifique o cadastro antes de executar este script.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
