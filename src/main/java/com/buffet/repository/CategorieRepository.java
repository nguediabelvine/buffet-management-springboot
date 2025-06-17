package com.buffet.repository;

import com.buffet.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    
    /**
     * Trouve une catégorie par son nom
     */
    Optional<Categorie> findByNom(String nom);
    
    /**
     * Trouve toutes les catégories contenant le nom spécifié (insensible à la casse)
     */
    List<Categorie> findByNomContainingIgnoreCase(String nom);
    
    /**
     * Trouve toutes les catégories avec leurs aliments
     */
    @Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.aliments")
    List<Categorie> findAllWithAliments();
    
    /**
     * Trouve une catégorie par nom avec ses aliments
     */
    @Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.aliments WHERE c.nom = :nom")
    Optional<Categorie> findByNomWithAliments(@Param("nom") String nom);
    
    /**
     * Compte le nombre d'aliments par catégorie
     */
    @Query("SELECT c.nom, COUNT(a) FROM Categorie c LEFT JOIN c.aliments a GROUP BY c.id, c.nom")
    List<Object[]> countAlimentsByCategorie();
} 