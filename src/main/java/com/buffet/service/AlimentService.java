package com.buffet.service;

import com.buffet.model.Aliment;
import com.buffet.model.Categorie;
import com.buffet.repository.AlimentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlimentService {
    
    private final AlimentRepository alimentRepository;
    private final CategorieService categorieService;
    
    public AlimentService(AlimentRepository alimentRepository, CategorieService categorieService) {
        this.alimentRepository = alimentRepository;
        this.categorieService = categorieService;
    }
    
    /**
     * Sauvegarde un aliment
     */
    public Aliment save(Aliment aliment) {
        return alimentRepository.save(aliment);
    }
    
    /**
     * Trouve un aliment par ID
     */
    @Transactional(readOnly = true)
    public Optional<Aliment> findById(Long id) {
        return alimentRepository.findById(id);
    }
    
    /**
     * Trouve un aliment par ID avec sa catégorie
     */
    @Transactional(readOnly = true)
    public Optional<Aliment> findByIdWithCategorie(Long id) {
        return alimentRepository.findByIdWithCategorie(id);
    }
    
    /**
     * Trouve tous les aliments
     */
    @Transactional(readOnly = true)
    public List<Aliment> findAll() {
        return alimentRepository.findAll();
    }
    
    /**
     * Trouve tous les aliments avec leur catégorie
     */
    @Transactional(readOnly = true)
    public List<Aliment> findAllWithCategorie() {
        return alimentRepository.findAllWithCategorie();
    }
    
    /**
     * Trouve les aliments d'une catégorie
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByCategorie(Categorie categorie) {
        return alimentRepository.findByCategorie(categorie);
    }
    
    /**
     * Trouve les aliments par nom de catégorie
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByCategorieNom(String categorieNom) {
        return alimentRepository.findByCategorieNom(categorieNom);
    }
    
    /**
     * Trouve les aliments par nom (recherche partielle)
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByNomContaining(String nom) {
        return alimentRepository.findByNomContainingIgnoreCase(nom);
    }
    
    /**
     * Trouve les aliments par plage de calories
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByCaloriesBetween(Double minCalories, Double maxCalories) {
        return alimentRepository.findByCaloriesPer100gBetween(minCalories, maxCalories);
    }
    
    /**
     * Trouve les aliments par allergie
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByAllergiesContaining(String allergie) {
        return alimentRepository.findByAllergiesContainingIgnoreCase(allergie);
    }
    
    /**
     * Trouve les aliments sans allergie
     */
    @Transactional(readOnly = true)
    public List<Aliment> findSansAllergie() {
        return alimentRepository.findByAllergiesOrAllergiesIsNull("Aucune allergie connue");
    }
    
    /**
     * Trouve les aliments les plus caloriques
     */
    @Transactional(readOnly = true)
    public List<Aliment> findTopCaloriques() {
        return alimentRepository.findTopCaloriques();
    }
    
    /**
     * Trouve les aliments les moins caloriques
     */
    @Transactional(readOnly = true)
    public List<Aliment> findMoinsCaloriques() {
        return alimentRepository.findMoinsCaloriques();
    }
    
    /**
     * Trouve les aliments par allergie spécifique
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByAllergieSpecifique(String allergie) {
        return alimentRepository.findByAllergieSpecifique(allergie);
    }
    
    /**
     * Supprime un aliment par ID
     */
    public void deleteById(Long id) {
        alimentRepository.deleteById(id);
    }
    
    /**
     * Vérifie si un aliment existe par ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return alimentRepository.existsById(id);
    }
    
    /**
     * Compte le nombre total d'aliments
     */
    @Transactional(readOnly = true)
    public long count() {
        return alimentRepository.count();
    }
    
    /**
     * Trouve plusieurs aliments par leurs IDs
     */
    @Transactional(readOnly = true)
    public List<Aliment> findByIds(List<Long> ids) {
        return alimentRepository.findAllById(ids);
    }
} 