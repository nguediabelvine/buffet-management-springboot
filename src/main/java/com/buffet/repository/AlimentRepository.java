package com.buffet.repository;

import com.buffet.model.Aliment;
import com.buffet.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlimentRepository extends JpaRepository<Aliment, Long> {
    
    /**
     * Trouve tous les aliments d'une catégorie
     */
    List<Aliment> findByCategorie(Categorie categorie);
    
    /**
     * Trouve tous les aliments d'une catégorie par nom
     */
    List<Aliment> findByCategorieNom(String categorieNom);
    
    /**
     * Trouve les aliments par nom (insensible à la casse)
     */
    List<Aliment> findByNomContainingIgnoreCase(String nom);
    
    /**
     * Trouve les aliments par plage de calories
     */
    List<Aliment> findByCaloriesPer100gBetween(Double minCalories, Double maxCalories);
    
    /**
     * Trouve les aliments par allergie (recherche partielle)
     */
    List<Aliment> findByAllergiesContainingIgnoreCase(String allergie);
    
    /**
     * Trouve les aliments sans allergie
     */
    List<Aliment> findByAllergiesOrAllergiesIsNull(String allergies);
    
    /**
     * Trouve tous les aliments avec leur catégorie
     */
    @Query("SELECT a FROM Aliment a LEFT JOIN FETCH a.categorie")
    List<Aliment> findAllWithCategorie();
    
    /**
     * Trouve un aliment par ID avec sa catégorie
     */
    @Query("SELECT a FROM Aliment a LEFT JOIN FETCH a.categorie WHERE a.id = :id")
    Optional<Aliment> findByIdWithCategorie(@Param("id") Long id);
    
    /**
     * Trouve les aliments les plus caloriques
     */
    @Query("SELECT a FROM Aliment a ORDER BY a.caloriesPer100g DESC")
    List<Aliment> findTopCaloriques();
    
    /**
     * Trouve les aliments les moins caloriques
     */
    @Query("SELECT a FROM Aliment a ORDER BY a.caloriesPer100g ASC")
    List<Aliment> findMoinsCaloriques();
    
    /**
     * Trouve les aliments par allergie spécifique
     */
    @Query("SELECT a FROM Aliment a WHERE LOWER(a.allergies) LIKE LOWER(CONCAT('%', :allergie, '%'))")
    List<Aliment> findByAllergieSpecifique(@Param("allergie") String allergie);
} 