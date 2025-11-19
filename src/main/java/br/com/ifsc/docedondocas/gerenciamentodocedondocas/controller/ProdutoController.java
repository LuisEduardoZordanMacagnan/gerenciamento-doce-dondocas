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

    @GetMapping("/marcas")
    public ResponseEntity findAllMarcas() { return ResponseEntity.ok(produtoMarcaRepository.findAll()); }

    @GetMapping("/marcas/{id}")
    public ResponseEntity findMarcasById(@PathVariable long id) { return ResponseEntity.ok(produtoMarcaRepository.findById(id)); }

    @PostMapping("/cadastrarMarca")
    public ResponseEntity cadastrarMarca(@Valid @RequestBody ProdutoMarca produtoMarca) {
        produtoMarcaRepository.save(produtoMarca);
        return ResponseEntity.ok(produtoMarca);
    }

    @PostMapping("/deletarMarca/{id}")
    public ResponseEntity deletarMarca(@PathVariable long id) {
        produtoMarcaRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/editarMarca")
    public ResponseEntity editarMarca(@Valid @RequestBody ProdutoMarca data) {
        ProdutoMarca produtoMarca = produtoMarcaRepository.findById(data.getId()).orElse(null);
        if ( produtoMarca == null ) {
            return ResponseEntity.notFound().build();
        }

        if ( data.getMarca() != null ) { produtoMarca.setMarca(data.getMarca()); }
        produtoMarcaRepository.save(produtoMarca);
        return ResponseEntity.ok(produtoMarca);
    }

    @GetMapping("/categorias")
    public ResponseEntity findAllCategorias() {
        return ResponseEntity.ok(produtoCategoriaRepository.findAll());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity findCategoriasById(@PathVariable long id) {
        return ResponseEntity.ok(produtoCategoriaRepository.findById(id));
    }

    @PostMapping("/cadastrarCategoria")
    public ResponseEntity cadastrarCategoria(@Valid @RequestBody ProdutoCategoria produtoCategoria) {
        produtoCategoriaRepository.save(produtoCategoria);
        return ResponseEntity.ok(produtoCategoria);
    }

    @PostMapping("/deletarCategoria/{id}")
    public ResponseEntity deletarCategoria(@PathVariable long id) {
        produtoCategoriaRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/editarCategoria")
    public ResponseEntity editarCategoria(@Valid @RequestBody ProdutoCategoria data) {
        ProdutoCategoria produtoCategoria = produtoCategoriaRepository.findById(data.getId());
        if ( data.getCategoria() != null ) { produtoCategoria.setCategoria(data.getCategoria()); }
        produtoCategoriaRepository.save(produtoCategoria);
        return ResponseEntity.ok(produtoCategoria);
    }

    @GetMapping("/produtos")
    public ResponseEntity findAllProdutos() { return ResponseEntity.ok(produtoRepository.findAll()); }

    @GetMapping("/produtos/{id}")
    public ResponseEntity findProdutoById(@PathVariable long id) { return ResponseEntity.ok(produtoRepository.findById(id)); }

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@Valid @RequestBody Produto data) {
        /*Produto produto = Produto.builder()
                        .valor(data.getValor())
                                .marca(data.getMarca())
                                        .titulo(data.getTitulo())
                                                .categoria(data.getCategoria())
                                                        .build();*/
        produtoRepository.save(data);
        data = produtoRepository.findById(data.getId());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/deletar/{id}")
    public ResponseEntity deletar(@PathVariable long id) {
        produtoRepository.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/editar")
    public ResponseEntity editar(@Valid @RequestBody Produto data) {
        Produto produto = produtoRepository.findById(data.getId());

        if ( data.getCategoria() != null ) { produto.setCategoria(data.getCategoria()); }
        if ( data.getMarca() != null ) { produto.setMarca(data.getMarca()); }

        produtoRepository.save(produto);
        return ResponseEntity.ok(produto);
    }
}
