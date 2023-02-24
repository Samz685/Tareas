/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package biblioteca_spring.biblioteca_spring.repositories;

import biblioteca_spring.biblioteca_spring.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Syzer
 */
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
}
