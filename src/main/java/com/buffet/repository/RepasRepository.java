package com.buffet.repository;

import com.buffet.model.Repas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {
    
    /**
     * Trouve les repas d'une date spécifique
     */
    List<Repas> findByDateRepas(LocalDate dateRepas);
    
    /**
     * Trouve les repas entre deux dates
     */
    List<Repas> findByDateRepasBetween(LocalDate dateDebut, LocalDate dateFin);
    
    /**
     * Trouve les repas par type
     */
    List<Repas> findByTypeRepas(Repas.TypeRepas typeRepas);
    
    /**
     * Trouve les repas par type et date
     */
    List<Repas> findByTypeRepasAndDateRepas(Repas.TypeRepas typeRepas, LocalDate dateRepas);
    
    /**
     * Trouve les repas avec leurs aliments
     */
    @Query("SELECT r FROM Repas r LEFT JOIN FETCH r.aliments")
    List<Repas> findAllWithAliments();
    
    /**
     * Trouve les repas d'une date avec leurs aliments
     */
    @Query("SELECT r FROM Repas r LEFT JOIN FETCH r.aliments WHERE r.dateRepas = :date")
    List<Repas> findByDateRepasWithAliments(@Param("date") LocalDate dateRepas);
    
    /**
     * Trouve les repas d'une période avec leurs aliments
     */
    @Query("SELECT r FROM Repas r LEFT JOIN FETCH r.aliments WHERE r.dateRepas BETWEEN :debut AND :fin")
    List<Repas> findByDateRepasBetweenWithAliments(@Param("debut") LocalDate dateDebut, @Param("fin") LocalDate dateFin);
} 