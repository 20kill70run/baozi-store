
package br.com.baozistore.controller;

import br.com.baozistore.model.Pedido;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.ProdutoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {

        if (pedido.getClienteId() == null ||
            pedido.getProdutoId() == null ||
            pedido.getQuantidade() == null ||
            pedido.getQuantidade() <= 0) {

            return ResponseEntity.badRequest()
                    .body("Dados do pedido invalidos.");
        }

        if (!clienteRepository.existsById(pedido.getClienteId())) {
            return ResponseEntity.badRequest()
                    .body("Cliente nao encontrado.");
        }

        if (!produtoRepository.existsById(pedido.getProdutoId())) {
            return ResponseEntity.badRequest()
                    .body("Produto nao encontrado.");
        }

        pedido.setId(null);

        Pedido salvo = pedidoRepository.save(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> consultar(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedidoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
