/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package biblioteca_spring.biblioteca_spring.controllers;

import biblioteca_spring.biblioteca_spring.models.Libro;
import biblioteca_spring.biblioteca_spring.repositories.LibroRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Syzer
 */
@RestController
@RequestMapping("/biblioteca")
public class LibroController {

    @Autowired
    LibroRepository repo;

    @GetMapping()
    public List<Libro> list() {
        return repo.findAll();
    }

    //Listar todos los libros (solo el id y titulo de cada uno)
    @GetMapping("/libros")
    public ResponseEntity<List<HashMap<String, Object>>> getLibros() {
        List<Libro> libros = repo.findAll();
        if (!libros.isEmpty()) {
            List<HashMap<String, Object>> librosResumen = new ArrayList<>();
            for (Libro libro : libros) {
                HashMap<String, Object> libroResumen = new HashMap<>();
                libroResumen.put("id", libro.getId());
                libroResumen.put("titulo", libro.getTitulo());
                librosResumen.add(libroResumen);
            }
            return new ResponseEntity<List<HashMap<String, Object>>>(librosResumen, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Listar el detalle de un libro concreto​
    @GetMapping("/{id}")

    public ResponseEntity<Libro> get(@PathVariable Long id) {
        if (repo.existsById(id)) {
            return new ResponseEntity<Libro>(repo.findById(id).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Listar todos los libros de una categoría concreta que se le pasa por la url
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Libro>> getLibrosCategoria(@PathVariable String categoria) {
        List<Libro> libros = repo.findAll();
        List<Libro> librosCategoria = new ArrayList<>();
        if (!libros.isEmpty()) {
            
            for (Libro libro : libros) {
                if (libro.getCategoria().toLowerCase().equals(categoria.toLowerCase())) {

                    
                    librosCategoria.add(libro);
                }
            }
            return new ResponseEntity<List<Libro>>(librosCategoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Listar todos los libros de un autor concreto que se le pasa por la url​
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> getLibrosAutor(@PathVariable String autor) {
        List<Libro> libros = repo.findAll();
        List<Libro> librosAutor = new ArrayList<>();
        if (!libros.isEmpty()) {
        
            for (Libro libro : libros) {
                if (libro.getAutor().toLowerCase().equals(autor.toLowerCase())) {

                    librosAutor.add(libro);
                }
            }
            return new ResponseEntity<List<Libro>>(librosAutor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Permita crear un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro input) {

        repo.save(input);
        System.out.println(input);

        return new ResponseEntity<>(input, HttpStatus.CREATED);
    }

    //Permita actualizar un nuevo libro, se actualizan solo los campos introducidos
    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable Long id, @RequestBody Libro input) {
        ResponseEntity<Libro> salida;
        System.out.println(input);

        if (repo.existsById(id)) {
            Libro libro = repo.getById(id);
            input.setId(id);

            if (input.getTitulo() == null) {
                input.setTitulo(libro.getTitulo());
            }
            if (input.getAutor() == null) {
                input.setAutor(libro.getAutor());
            }
            if (input.getCategoria() == null) {
                input.setCategoria(libro.getCategoria());
            }
            if (input.getIsbn() == null) {
                input.setIsbn(libro.getIsbn());
            }
            if (input.getEdicion() == null) {
                input.setEdicion(libro.getEdicion());
            }

            repo.save(input);
            salida = new ResponseEntity<Libro>(repo.findById(id).get(), HttpStatus.OK);
        } else {
            salida = new ResponseEntity<Libro>(HttpStatus.NOT_FOUND);
        }

        return salida;
    }

    //Permitir solicitudes POST para eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Libro> delete(@PathVariable Long id) {

        ResponseEntity<Libro> salida;

        if (repo.existsById(id)) {
            salida = new ResponseEntity<Libro>(repo.findById(id).get(), HttpStatus.OK);
            repo.deleteById(id);
        } else {
            salida = new ResponseEntity<Libro>(HttpStatus.NOT_FOUND);
        }

        return salida;
    }

    //EXTRA: listar un libro por id, muestra id y titulo
    @GetMapping("/titulo/{id}")
    public ResponseEntity<HashMap<String, Object>> getTitulo(@PathVariable Long id) {
        if (repo.existsById(id)) {

            Libro libro = repo.findById(id).get();
            HashMap<String, Object> libroResumen = new HashMap<>();

            libroResumen.put("id", libro.getId());
            libroResumen.put("titulo", libro.getTitulo());
            return new ResponseEntity<HashMap<String, Object>>(libroResumen, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
