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
import org.springframework.stereotype.Controller;

/**
 *
 * @author Syzer
 */
@Controller
@RequestMapping("/biblioteca")
public class LibroControllerHtml {

    @Autowired
    LibroRepository repo;

    //Mostrar datos en una tabla en html en lugar de json
    @GetMapping("/tabla")
    public String mostrarLibros(Model modelo) {
        List<Libro> libros = repo.findAll();
        modelo.addAttribute("libros", libros);
        return "tabla_libros";
    }
}
