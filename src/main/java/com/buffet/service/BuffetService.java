package com.buffet.service;

import com.buffet.dto.BuffetResponse;
import com.buffet.model.Aliment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuffetService {
    
    private final AlimentService alimentService;
    
    // Constantes pour les calculs de buffet
    private static final double GRAMMES_PAR_PERSONNE = 150.0; // 150g par personne par aliment
    private static final double FACTEUR_VARIETE = 1.2; // 20% de plus pour la variété
    
    public BuffetService(AlimentService alimentService) {
        this.alimentService = alimentService;
    }
    
    /**
     * Calcule un buffet basé sur le nombre d'invités et les aliments sélectionnés
     */
    public BuffetResponse calculerBuffet(Integer nombreInvites, List<Long> alimentIds) {
        List<Aliment> aliments = alimentService.findByIds(alimentIds);
        
        if (aliments.isEmpty()) {
            throw new IllegalArgumentException("Aucun aliment trouvé avec les IDs fournis");
        }
        
        List<BuffetResponse.AlimentBuffet> alimentsBuffet = new ArrayList<>();
        double caloriesTotales = 0;
        List<String> allergiesList = new ArrayList<>();
        
        for (Aliment aliment : aliments) {
            // Calculer la quantité nécessaire
            double quantiteKg = calculerQuantite(nombreInvites);
            
            // Calculer les calories
            double calories = 0;
            if (aliment.getCaloriesPer100g() != null) {
                BigDecimal caloriesBigDecimal = aliment.getCaloriesPer100g()
                    .multiply(BigDecimal.valueOf(quantiteKg))
                    .multiply(BigDecimal.TEN); // *10 car calories_per_100g
                calories = caloriesBigDecimal.doubleValue();
            }
            
            // Collecter les allergies
            if (aliment.getAllergies() != null && !aliment.getAllergies().equals("Aucune allergie connue")) {
                allergiesList.add(aliment.getAllergies());
            }
            
            // Créer l'aliment du buffet
            BuffetResponse.AlimentBuffet alimentBuffet = new BuffetResponse.AlimentBuffet(
                    aliment.getId(),
                    aliment.getNom(),
                    aliment.getCategorie().getNom(),
                    quantiteKg,
                    calories,
                    aliment.getAllergies()
            );
            
            alimentsBuffet.add(alimentBuffet);
            
            // Accumuler les calories totales
            caloriesTotales += calories;
        }
        
        // Créer la liste des allergies présentes
        String allergiesPresentes = allergiesList.isEmpty() ? 
            "Aucune allergie détectée" : 
            String.join("; ", allergiesList.stream().distinct().collect(Collectors.toList()));
        
        return new BuffetResponse(
                nombreInvites,
                alimentsBuffet,
                caloriesTotales,
                allergiesPresentes
        );
    }
    
    /**
     * Calcule la quantité nécessaire par aliment
     */
    private double calculerQuantite(Integer nombreInvites) {
        return (GRAMMES_PAR_PERSONNE * nombreInvites * FACTEUR_VARIETE) / 1000.0; // Conversion en kg
    }
    
    /**
     * Génère des recommandations de buffet basées sur le nombre d'invités
     */
    public List<Aliment> genererRecommandationsBuffet(Integer nombreInvites) {
        List<Aliment> recommandations = new ArrayList<>();
        
        // Logique de recommandation basée sur le nombre d'invités
        if (nombreInvites <= 10) {
            // Petit buffet : 3-4 aliments variés
            recommandations.addAll(alimentService.findByCategorieNom("Viandes").stream().limit(1).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Légumes").stream().limit(2).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Fruits").stream().limit(1).toList());
        } else if (nombreInvites <= 30) {
            // Buffet moyen : 5-6 aliments
            recommandations.addAll(alimentService.findByCategorieNom("Viandes").stream().limit(2).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Poissons").stream().limit(1).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Légumes").stream().limit(2).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Fruits").stream().limit(1).toList());
        } else {
            // Grand buffet : 7-8 aliments
            recommandations.addAll(alimentService.findByCategorieNom("Viandes").stream().limit(2).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Poissons").stream().limit(1).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Légumes").stream().limit(3).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Fruits").stream().limit(1).toList());
            recommandations.addAll(alimentService.findByCategorieNom("Céréales").stream().limit(1).toList());
        }
        
        return recommandations;
    }
    
    /**
     * Calcule un buffet économique (moins calorique)
     */
    public BuffetResponse calculerBuffetEconomique(Integer nombreInvites) {
        List<Aliment> alimentsEconomiques = alimentService.findMoinsCaloriques();
        List<Long> alimentIds = alimentsEconomiques.stream()
                .limit(5) // Limiter à 5 aliments pour l'économie
                .map(Aliment::getId)
                .toList();
        
        return calculerBuffet(nombreInvites, alimentIds);
    }
    
    /**
     * Calcule un buffet équilibré (sans allergie)
     */
    public BuffetResponse calculerBuffetEquilibre(Integer nombreInvites) {
        List<Aliment> alimentsEquilibres = alimentService.findSansAllergie();
        List<Long> alimentIds = alimentsEquilibres.stream()
                .limit(6) // Limiter à 6 aliments équilibrés
                .map(Aliment::getId)
                .toList();
        
        return calculerBuffet(nombreInvites, alimentIds);
    }
    
    /**
     * Calcule un buffet sans allergie spécifique
     */
    public BuffetResponse calculerBuffetSansAllergie(Integer nombreInvites, String allergie) {
        List<Aliment> alimentsSansAllergie = alimentService.findSansAllergie();
        List<Long> alimentIds = alimentsSansAllergie.stream()
                .limit(5)
                .map(Aliment::getId)
                .toList();
        
        return calculerBuffet(nombreInvites, alimentIds);
    }
    
    /**
     * Obtient les statistiques de calories par personne
     */
    public Double getCaloriesParPersonne(BuffetResponse buffetResponse) {
        if (buffetResponse.getNombreInvites() <= 0) {
            return 0.0;
        }
        
        return buffetResponse.getCaloriesTotales() / buffetResponse.getNombreInvites();
    }
} 