package com.buffet.service;

import com.buffet.model.Repas;
import com.buffet.model.Aliment;
import com.buffet.repository.RepasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Transactional
public class PlanificationService {
    
    private final RepasRepository repasRepository;
    private final AlimentService alimentService;
    
    public PlanificationService(RepasRepository repasRepository, AlimentService alimentService) {
        this.repasRepository = repasRepository;
        this.alimentService = alimentService;
    }
    
    /**
     * Génère une planification hebdomadaire automatique
     */
    public List<Repas> genererPlanificationHebdomadaire(LocalDate dateSemaine) {
        LocalDate debutSemaine = dateSemaine.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate finSemaine = debutSemaine.plusDays(6);
        
        List<Repas> planification = new ArrayList<>();
        
        // Générer les repas pour chaque jour de la semaine
        for (LocalDate date = debutSemaine; !date.isAfter(finSemaine); date = date.plusDays(1)) {
            // Petit déjeuner
            planification.add(creerRepasAutomatique(date, Repas.TypeRepas.PETIT_DEJEUNER, "Petit déjeuner équilibré"));
            
            // Déjeuner
            planification.add(creerRepasAutomatique(date, Repas.TypeRepas.DEJEUNER, "Déjeuner complet"));
            
            // Dîner
            planification.add(creerRepasAutomatique(date, Repas.TypeRepas.DINER, "Dîner léger"));
        }
        
        return planification;
    }
    
    /**
     * Crée un repas automatique avec des aliments variés
     */
    private Repas creerRepasAutomatique(LocalDate date, Repas.TypeRepas typeRepas, String description) {
        Repas repas = new Repas();
        repas.setNom(genererNomRepas(date, typeRepas));
        repas.setDescription(description);
        repas.setDateRepas(date);
        repas.setTypeRepas(typeRepas);
        repas.setNombrePersonnes(4); // Par défaut pour 4 personnes
        
        // Sélectionner des aliments selon le type de repas
        List<Aliment> alimentsSelectionnes = selectionnerAlimentsPourRepas(typeRepas);
        repas.setAliments(alimentsSelectionnes);
        
        return repas;
    }
    
    /**
     * Génère un nom pour le repas
     */
    private String genererNomRepas(LocalDate date, Repas.TypeRepas typeRepas) {
        String jour = date.getDayOfWeek().toString().toLowerCase();
        jour = jour.substring(0, 1).toUpperCase() + jour.substring(1);
        
        switch (typeRepas) {
            case PETIT_DEJEUNER:
                return "Petit déjeuner du " + jour;
            case DEJEUNER:
                return "Déjeuner du " + jour;
            case DINER:
                return "Dîner du " + jour;
            default:
                return "Repas du " + jour;
        }
    }
    
    /**
     * Sélectionne des aliments appropriés selon le type de repas
     */
    private List<Aliment> selectionnerAlimentsPourRepas(Repas.TypeRepas typeRepas) {
        List<Aliment> tousAliments = alimentService.findAll();
        List<Aliment> selection = new ArrayList<>();
        
        switch (typeRepas) {
            case PETIT_DEJEUNER:
                // Petit déjeuner : céréales, fruits, produits laitiers
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Céréales"))
                    .limit(1)
                    .collect(Collectors.toList()));
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Fruits"))
                    .limit(1)
                    .collect(Collectors.toList()));
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Produits laitiers"))
                    .limit(1)
                    .collect(Collectors.toList()));
                break;
                
            case DEJEUNER:
                // Déjeuner : protéines, légumes, céréales
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Viandes") || 
                                a.getCategorie().getNom().equals("Poissons"))
                    .limit(1)
                    .collect(Collectors.toList()));
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Légumes"))
                    .limit(2)
                    .collect(Collectors.toList()));
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Céréales"))
                    .limit(1)
                    .collect(Collectors.toList()));
                break;
                
            case DINER:
                // Dîner : légumes, protéines légères
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Légumes"))
                    .limit(2)
                    .collect(Collectors.toList()));
                selection.addAll(tousAliments.stream()
                    .filter(a -> a.getCategorie().getNom().equals("Poissons"))
                    .limit(1)
                    .collect(Collectors.toList()));
                break;
        }
        
        return selection;
    }
    
    /**
     * Sauvegarde une planification
     */
    public Repas sauvegarderRepas(Repas repas) {
        return repasRepository.save(repas);
    }
    
    /**
     * Trouve les repas d'une semaine
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRepasSemaine(LocalDate dateSemaine) {
        // Si aucun repas n'existe, générer une planification automatique
        LocalDate debutSemaine = dateSemaine.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate finSemaine = debutSemaine.plusDays(6);
        
        List<Repas> repasExistant = repasRepository.findByDateRepasBetween(debutSemaine, finSemaine);
        
        if (repasExistant.isEmpty()) {
            // Générer une planification automatique
            List<Repas> planification = genererPlanificationHebdomadaire(dateSemaine);
            return convertirRepasEnMap(planification);
        }
        
        return convertirRepasEnMap(repasExistant);
    }
    
    /**
     * Convertit une liste de Repas en liste de Map pour éviter les problèmes de sérialisation
     */
    private List<Map<String, Object>> convertirRepasEnMap(List<Repas> repas) {
        List<Map<String, Object>> resultat = new ArrayList<>();
        
        for (Repas r : repas) {
            Map<String, Object> repasMap = new HashMap<>();
            repasMap.put("id", r.getId());
            repasMap.put("nom", r.getNom());
            repasMap.put("description", r.getDescription());
            repasMap.put("dateRepas", r.getDateRepas());
            repasMap.put("typeRepas", r.getTypeRepas());
            repasMap.put("nombrePersonnes", r.getNombrePersonnes());
            repasMap.put("createdAt", r.getCreatedAt());
            
            // Convertir les aliments en Map aussi
            List<Map<String, Object>> alimentsMap = new ArrayList<>();
            for (Aliment a : r.getAliments()) {
                Map<String, Object> alimentMap = new HashMap<>();
                alimentMap.put("id", a.getId());
                alimentMap.put("nom", a.getNom());
                alimentMap.put("description", a.getDescription());
                alimentMap.put("caloriesPer100g", a.getCaloriesPer100g());
                alimentMap.put("allergies", a.getAllergies());
                alimentMap.put("imageUrl", a.getImageUrl());
                alimentsMap.add(alimentMap);
            }
            repasMap.put("aliments", alimentsMap);
            
            resultat.add(repasMap);
        }
        
        return resultat;
    }
    
    /**
     * Trouve les repas d'un jour spécifique
     */
    @Transactional(readOnly = true)
    public List<Repas> getRepasJour(LocalDate date) {
        return repasRepository.findByDateRepas(date);
    }
    
    /**
     * Calcule les statistiques nutritionnelles d'une semaine
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesSemaine(LocalDate dateSemaine) {
        List<Map<String, Object>> repasSemaine = getRepasSemaine(dateSemaine);
        
        BigDecimal caloriesTotales = BigDecimal.ZERO;
        List<String> allergiesList = new ArrayList<>();
        
        for (Map<String, Object> repasMap : repasSemaine) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> aliments = (List<Map<String, Object>>) repasMap.get("aliments");
            Integer nombrePersonnes = (Integer) repasMap.get("nombrePersonnes");
            
            for (Map<String, Object> alimentMap : aliments) {
                // Calcul basé sur 100g par personne par repas
                BigDecimal quantite = BigDecimal.valueOf(nombrePersonnes * 0.1); // 100g = 0.1kg
                
                Object caloriesObj = alimentMap.get("caloriesPer100g");
                if (caloriesObj instanceof BigDecimal) {
                    BigDecimal calories = (BigDecimal) caloriesObj;
                    caloriesTotales = caloriesTotales.add(calories.multiply(quantite).multiply(BigDecimal.TEN));
                }
                
                // Collecter les allergies
                String allergies = (String) alimentMap.get("allergies");
                if (allergies != null && !allergies.equals("Aucune allergie connue")) {
                    allergiesList.add(allergies);
                }
            }
        }
        
        Map<String, Object> statistiques = new HashMap<>();
        statistiques.put("calories", caloriesTotales.doubleValue());
        statistiques.put("allergies", allergiesList.stream().distinct().collect(Collectors.toList()));
        statistiques.put("nombreRepas", repasSemaine.size());
        
        return statistiques;
    }
} 