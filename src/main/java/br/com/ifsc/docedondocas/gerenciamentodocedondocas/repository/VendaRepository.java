package br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para gerenciar operações de banco de dados de Vendas
 * Estende JpaRepository para herdar operações CRUD padrão
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    /**
     * Buscar vendas entre duas datas/horas
     * Usado para relatórios diários, semanais, mensais e customizados
     */
    @Query("SELECT v FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim ORDER BY v.data DESC")
    List<Venda> findByDataBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Buscar todas as vendas de um cliente específico
     */
    @Query("SELECT v FROM Venda v WHERE v.cliente.id = :clienteId ORDER BY v.data DESC")
    List<Venda> findByClienteId(@Param("clienteId") Long clienteId);

    /**
     * Buscar todas as vendas de um produto específico
     */
    @Query("SELECT v FROM Venda v WHERE v.produto.id = :produtoId ORDER BY v.data DESC")
    List<Venda> findByProdutoId(@Param("produtoId") Long produtoId);

    /**
     * Buscar vendas por mês e ano
     * Exemplo: mes=1, ano=2024 retorna vendas de janeiro de 2024
     */
    @Query("SELECT v FROM Venda v WHERE MONTH(v.data) = :mes AND YEAR(v.data) = :ano ORDER BY v.data DESC")
    List<Venda> findByMesAndAno(
            @Param("mes") Integer mes,
            @Param("ano") Integer ano
    );

    /**
     * Buscar vendas de um dia específico
     * Exemplo: 2024-01-15
     */
    @Query("SELECT v FROM Venda v WHERE CAST(v.data AS date) = :data ORDER BY v.data DESC")
    List<Venda> findByData(@Param("data") java.time.LocalDate data);

    /**
     * Contar total de vendas em um período
     */
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim")
    Long countByDataBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Calcular valor total de vendas em um período
     */
    @Query("SELECT SUM(v.valor) FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim")
    Double sumValorByDataBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Calcular quantidade total de itens vendidos em um período
     */
    @Query("SELECT SUM(v.quantidade) FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim")
    Integer sumQuantidadeByDataBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Buscar top 10 produtos mais vendidos
     */
    @Query("SELECT v.produto.id, v.produto.titulo, SUM(v.quantidade) as totalQuantidade, SUM(v.valor) as totalValor " +
           "FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY v.produto.id, v.produto.titulo " +
           "ORDER BY totalQuantidade DESC LIMIT 10")
    List<Object[]> findTopProdutosVendidos(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Buscar top 10 clientes com mais compras
     */
    @Query("SELECT v.cliente.id, v.cliente.nome, COUNT(v) as totalCompras, SUM(v.valor) as totalGasto " +
           "FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY v.cliente.id, v.cliente.nome " +
           "ORDER BY totalGasto DESC LIMIT 10")
    List<Object[]> findTopClientesComMaisCompras(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Buscar vendas por intervalo de valores
     * Exemplo: valor mínimo 100 e máximo 500
     */
    @Query("SELECT v FROM Venda v WHERE v.valor BETWEEN :valorMinimo AND :valorMaximo ORDER BY v.data DESC")
    List<Venda> findByValorBetween(
            @Param("valorMinimo") Double valorMinimo,
            @Param("valorMaximo") Double valorMaximo
    );

    /**
     * Verificar se existe venda para um cliente em um período
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Venda v " +
           "WHERE v.cliente.id = :clienteId AND v.data BETWEEN :dataInicio AND :dataFim")
    Boolean existeVendaParaClienteEmPeriodo(
            @Param("clienteId") Long clienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
