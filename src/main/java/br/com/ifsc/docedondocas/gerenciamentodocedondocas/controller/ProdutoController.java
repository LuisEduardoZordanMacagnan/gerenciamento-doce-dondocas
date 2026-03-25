package br.com.ifsc.docedondocas.gerenciamentodocedondocas.controller;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.Produto;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.ProdutoCategoria;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.ProdutoMarca;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.ProdutoCategoriaRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.ProdutoMarcaRepository;
import br.com.ifsc.docedondocas.gerenciamentodocedondocas.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ProdutoCategoriaRepository produtoCategoriaRepository;
    @Autowired
    private ProdutoMarcaRepository produtoMarcaRepository;

    @GetMapping("/marca")
    public ResponseEntity findAllMarcas() { return ResponseEntity.ok(produtoMarcaRepository.findAll()); }

    @GetMapping("/marca/{id}")
    public ResponseEntity findMarcasById(@PathVariable long id) {
        ProdutoMarca pm = produtoMarcaRepository.findById(id);
        if ( pm == null ) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pm);
    }

    @PostMapping("/marca")
    public ResponseEntity cadastrarMarca(@Valid @RequestBody ProdutoMarca produtoMarca) {
        produtoMarcaRepository.save(produtoMarca);
        URI location = URI.create("/produto/marca/"+produtoMarca.getId());
        return ResponseEntity.created(location).body(produtoMarca);
    }

    @DeleteMapping("/marca/{id}")
    public ResponseEntity deletarMarca(@PathVariable long id) {
        ProdutoMarca pm = produtoMarcaRepository.findById(id);
        if ( pm == null ) return ResponseEntity.notFound().build();
        produtoMarcaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/marca")
    public ResponseEntity editarMarca(@Valid @RequestBody ProdutoMarca data) {
        ProdutoMarca produtoMarca = produtoMarcaRepository.findById(data.getId()).orElse(null);
        if ( produtoMarca == null ) {
            return ResponseEntity.notFound().build();
        }

        if ( data.getMarca() != null ) { produtoMarca.setMarca(data.getMarca()); }
        produtoMarcaRepository.save(produtoMarca);
        return ResponseEntity.ok(produtoMarca);
    }

    @GetMapping("/categoria")
    public ResponseEntity findAllCategorias() {
        return ResponseEntity.ok(produtoCategoriaRepository.findAll());
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity findCategoriasById(@PathVariable long id) {
        ProdutoCategoria pc = produtoCategoriaRepository.findById(id);
        if (pc == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pc);
    }

    @PostMapping("/categoria")
    public ResponseEntity cadastrarCategoria(@Valid @RequestBody ProdutoCategoria produtoCategoria) {
        produtoCategoriaRepository.save(produtoCategoria);
        URI location = URI.create("/produto/categoria/"+produtoCategoria.getId());
        return ResponseEntity.created(location).body(produtoCategoria);
    }

    @DeleteMapping("/categoria/{id}")
    public ResponseEntity deletarCategoria(@PathVariable long id) {
        ProdutoCategoria pc = produtoCategoriaRepository.findById(id);
        if (pc == null) return ResponseEntity.notFound().build();
        produtoCategoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/categoria")
    public ResponseEntity editarCategoria(@Valid @RequestBody ProdutoCategoria data) {
        ProdutoCategoria produtoCategoria = produtoCategoriaRepository.findById(data.getId());
        if ( data.getCategoria() != null ) { produtoCategoria.setCategoria(data.getCategoria()); }
        produtoCategoriaRepository.save(produtoCategoria);
        return ResponseEntity.ok(produtoCategoria);
    }

    @GetMapping()
    public ResponseEntity findAllProdutos() { return ResponseEntity.ok(produtoRepository.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity findProdutoById(@PathVariable long id) {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(produto);
    }

    @PostMapping()
    public ResponseEntity cadastrar(@Valid @RequestBody Produto data) {
        /*Produto produto = Produto.builder()
                        .valor(data.getValor())
                                .marca(data.getMarca())
                                        .titulo(data.getTitulo())
                                                .categoria(data.getCategoria())
                                                        .build();*/
        produtoRepository.save(data);
        URI location = URI.create("/produto/"+data.getId());
        data = produtoRepository.findById(data.getId());
        return ResponseEntity.created(location).body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable long id) {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) return ResponseEntity.notFound().build();
        produtoRepository.delete(produto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity editar(@Valid @RequestBody Produto data) {
        Produto produto = produtoRepository.findById(data.getId());

        if ( produto == null ) return ResponseEntity.notFound().build();

        if (data.getCategoria() != null) {
            ProdutoCategoria categoria = produtoCategoriaRepository.findById(data.getCategoria().getId());
            if (categoria != null) {
                produto.setCategoria(categoria);
            } else {
                // Possivel autocriacao
            }
        }
        else{
            produto.setCategoria(null);
        }

        if (data.getMarca() != null) {
            ProdutoMarca marca = produtoMarcaRepository.findById(data.getMarca().getId()).orElse(null);
            if (marca != null) {
                produto.setMarca(marca);
            } else {
                // Possivel autocriacao
            }
        }
        else{
            produto.setMarca(null);
        }

        produto.setValor(data.getValor());
        produto.setTitulo(data.getTitulo());

        produtoRepository.save(produto);
        return ResponseEntity.ok(produto);
    }
}
