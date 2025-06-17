package com.buffet.dto;

import java.util.List;

public class BuffetResponse {
    
    private Integer nombreInvites;
    private List<AlimentBuffet> aliments;
    private Double caloriesTotales;
    private String allergiesPresentes;
    
    // Classe interne pour les aliments du buffet
    public static class AlimentBuffet {
        private Long id;
        private String nom;
        private String categorie;
        private Double quantiteKg;
        private Double calories;
        private String allergies;
        
        // Constructeurs
        public AlimentBuffet() {}
        
        public AlimentBuffet(Long id, String nom, String categorie, Double quantiteKg, 
                           Double calories, String allergies) {
            this.id = id;
            this.nom = nom;
            this.categorie = categorie;
            this.quantiteKg = quantiteKg;
            this.calories = calories;
            this.allergies = allergies;
        }
        
        // Getters et Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        
        public String getCategorie() { return categorie; }
        public void setCategorie(String categorie) { this.categorie = categorie; }
        
        public Double getQuantiteKg() { return quantiteKg; }
        public void setQuantiteKg(Double quantiteKg) { this.quantiteKg = quantiteKg; }
        
        public Double getCalories() { return calories; }
        public void setCalories(Double calories) { this.calories = calories; }
        
        public String getAllergies() { return allergies; }
        public void setAllergies(String allergies) { this.allergies = allergies; }
    }
    
    // Constructeurs
    public BuffetResponse() {}
    
    public BuffetResponse(Integer nombreInvites, List<AlimentBuffet> aliments, 
                         Double caloriesTotales, String allergiesPresentes) {
        this.nombreInvites = nombreInvites;
        this.aliments = aliments;
        this.caloriesTotales = caloriesTotales;
        this.allergiesPresentes = allergiesPresentes;
    }
    
    // Getters et Setters
    public Integer getNombreInvites() { return nombreInvites; }
    public void setNombreInvites(Integer nombreInvites) { this.nombreInvites = nombreInvites; }
    
    public List<AlimentBuffet> getAliments() { return aliments; }
    public void setAliments(List<AlimentBuffet> aliments) { this.aliments = aliments; }
    
    public Double getCaloriesTotales() { return caloriesTotales; }
    public void setCaloriesTotales(Double caloriesTotales) { this.caloriesTotales = caloriesTotales; }
    
    public String getAllergiesPresentes() { return allergiesPresentes; }
    public void setAllergiesPresentes(String allergiesPresentes) { this.allergiesPresentes = allergiesPresentes; }
    
    @Override
    public String toString() {
        return "BuffetResponse{" +
                "nombreInvites=" + nombreInvites +
                ", aliments=" + aliments.size() + " aliments" +
                ", caloriesTotales=" + caloriesTotales +
                ", allergiesPresentes='" + allergiesPresentes + '\'' +
                '}';
    }
} 