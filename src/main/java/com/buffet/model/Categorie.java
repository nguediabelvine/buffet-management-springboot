package com.buffet.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Categorie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Aliment> aliments = new ArrayList<>();
    
    // Constructeurs
    public Categorie() {}
    
    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<Aliment> getAliments() {
        return aliments;
    }
    
    public void setAliments(List<Aliment> aliments) {
        this.aliments = aliments;
    }
    
    // Méthodes utilitaires
    public void addAliment(Aliment aliment) {
        aliments.add(aliment);
        aliment.setCategorie(this);
    }
    
    public void removeAliment(Aliment aliment) {
        aliments.remove(aliment);
        aliment.setCategorie(null);
    }
    
    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 