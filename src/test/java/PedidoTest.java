import org.example.DescontoService;
import org.example.ItemPedido;
import org.example.Pedido;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class PedidoTest {

    @Mock
    private DescontoService descontoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCalcularValorTotalComDesconto() {
        // Criando uma lista de itens de pedido
        ItemPedido item1 = new ItemPedido("Produto 1", 100.0, 2); // 2 itens de "Produto 1" a 100.0 cada
        ItemPedido item2 = new ItemPedido("Produto 2", 50.0, 3); // 3 itens de "Produto 2" a 50.0 cada
        List<ItemPedido> itens = Arrays.asList(item1, item2);

        // Configura o comportamento do mock 'descontoService' para retornar um desconto de 15.0
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(15.0);

        // Cria uma instância da classe 'Pedido' com os itens de pedido e o mock 'descontoService'
        Pedido pedido = new Pedido(itens, descontoService);

        // Chama o método 'calcularValorTotal' para calcular o valor total com desconto
        double valorTotalComDesconto = pedido.calcularValorTotal();

        // Usa 'assertEquals' para verificar se o valor total com desconto é igual a 335.0 (cálculo: 200.0 + 150.0 - 15.0)
        assertEquals(335.0, valorTotalComDesconto, 0.01);
}


    @Test
    public void testCalcularValorTotalSemDesconto() {
        // Configurar um mock para o DescontoService
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(0.0); // Sem desconto

        // Criar uma lista de itens com subtotal de 100
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("Produto", 100, 1));

        Pedido pedido = new Pedido(itens, descontoService);
        double valorTotalSemDesconto = pedido.calcularValorTotal();

        // Valor total sem desconto deve ser 100
        assertEquals(100.0, valorTotalSemDesconto, 0.01);
    }
 
    @Test
    public void testCalcularValorTotalComDescontoMaiorQueTotal() {
        // Criando uma lista de itens de pedido
        ItemPedido item1 = new ItemPedido("Produto 1", 100.0, 2);
        ItemPedido item2 = new ItemPedido("Produto 2", 50.0, 3);
        List<ItemPedido> itens = Arrays.asList(item1, item2);

        // Configura o comportamento do mock 'descontoService' para retornar um desconto de 600.0
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(600.0);

        // Cria uma instância da classe 'Pedido' com os itens de pedido e o mock 'descontoService'
        Pedido pedido = new Pedido(itens, descontoService);
        
        // Usa 'assertThrows' para verificar se o método 'calcularValorTotal' lança uma exceção do tipo IllegalArgumentException
        assertThrows(IllegalArgumentException.class, pedido::calcularValorTotal);
    }
 
    @Test
    public void testCalcularValorTotalComExcecao() {
        // Configurar um mock para o DescontoService
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(20.0); // 20 de desconto

        // Criar uma lista de itens com subtotal de 10
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("Produto", 10, 1));

        Pedido pedido = new Pedido(itens, descontoService);

        // Configurar o subtotal para ser menor que o desconto
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(30.0);

        // Deve lançar uma exceção
        assertThrows(IllegalArgumentException.class, pedido::calcularValorTotal);
    }

    @Test
    public void testCalcularValorTotalSemItens() {
        // Configurar um mock para o DescontoService
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(0.0);

        // Criar uma lista vazia de itens
        List<ItemPedido> itens = new ArrayList<>();

        Pedido pedido = new Pedido(itens, descontoService);
        double valorTotalSemItens = pedido.calcularValorTotal();

        // Valor total sem itens deve ser 0.0
        assertEquals(0.0, valorTotalSemItens, 0.01);
    }

    @Test
    public void testCalcularValorTotalChamaCalcularDescontoUmaVez() {
        // Configurar um mock para o DescontoService
        when(descontoService.calcularDesconto(Mockito.anyDouble())).thenReturn(0.1); // 10% de desconto

        // Criar uma lista de itens com subtotal de 100
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido("Produto", 100, 1));

        Pedido pedido = new Pedido(itens, descontoService);
        double valorTotalComDesconto = pedido.calcularValorTotal();

        // Verificar se o método calcularDesconto foi chamado exatamente uma vez
        Mockito.verify(descontoService, Mockito.times(1)).calcularDesconto(Mockito.anyDouble());
    }
}
