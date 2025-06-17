package com.buffet.service;

import com.buffet.model.Categorie;
import com.buffet.repository.CategorieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategorieService {
    
    private final CategorieRepository categorieRepository;
    
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }
    
    /**
     * Sauvegarde une catégorie
     */
    public Categorie save(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
    
    /**
     * Trouve une catégorie par ID
     */
    @Transactional(readOnly = true)
    public Optional<Categorie> findById(Long id) {
        return categorieRepository.findById(id);
    }
    
    /**
     * Trouve une catégorie par nom
     */
    @Transactional(readOnly = true)
    public Categorie findByNom(String nom) {
        return categorieRepository.findByNom(nom).orElse(null);
    }
    
    /**
     * Trouve toutes les catégories
     */
    @Transactional(readOnly = true)
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }
    
    /**
     * Trouve les catégories par nom (recherche partielle)
     */
    @Transactional(readOnly = true)
    public List<Categorie> findByNomContaining(String nom) {
        return categorieRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Trouve toutes les catégories avec leurs aliments
     */
    @Transactional(readOnly = true)
    public List<Categorie> findAllWithAliments() {
        return categorieRepository.findAllWithAliments();
    }
    
    /**
     * Trouve une catégorie par nom avec ses aliments
     */
    @Transactional(readOnly = true)
    public Optional<Categorie> findByNomWithAliments(String nom) {
        return categorieRepository.findByNomWithAliments(nom);
    }
    
    /**
     * Supprime une catégorie par ID
     */
    public void deleteById(Long id) {
        categorieRepository.deleteById(id);
    }
    
    /**
     * Vérifie si une catégorie existe par ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return categorieRepository.existsById(id);
    }
    
    /**
     * Compte le nombre total de catégories
     */
    @Transactional(readOnly = true)
    public long count() {
        return categorieRepository.count();
    }
    
    /**
     * Obtient les statistiques des catégories (nombre d'aliments par catégorie)
     */
    @Transactional(readOnly = true)
    public List<Object[]> getStatistiques() {
        return categorieRepository.countAlimentsByCategorie();
    }
} 