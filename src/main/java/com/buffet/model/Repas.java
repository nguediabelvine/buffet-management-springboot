package com.buffet.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repas")
public class Repas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "date_repas", nullable = false)
    private LocalDate dateRepas;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_repas", nullable = false)
    private TypeRepas typeRepas;
    
    @Column(name = "nombre_personnes")
    private Integer nombrePersonnes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "repas_aliments",
        joinColumns = @JoinColumn(name = "repas_id"),
        inverseJoinColumns = @JoinColumn(name = "aliment_id")
    )
    private List<Aliment> aliments = new ArrayList<>();
    
    // Enum pour les types de repas
    public enum TypeRepas {
        PETIT_DEJEUNER,
        DEJEUNER,
        DINER
    }
    
    // Constructeurs
    public Repas() {
        this.createdAt = LocalDateTime.now();
        this.nombrePersonnes = 1;
    }
    
    public Repas(String nom, String description, LocalDate dateRepas, TypeRepas typeRepas) {
        this();
        this.nom = nom;
        this.description = description;
        this.dateRepas = dateRepas;
        this.typeRepas = typeRepas;
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
    
    public LocalDate getDateRepas() {
        return dateRepas;
    }
    
    public void setDateRepas(LocalDate dateRepas) {
        this.dateRepas = dateRepas;
    }
    
    public TypeRepas getTypeRepas() {
        return typeRepas;
    }
    
    public void setTypeRepas(TypeRepas typeRepas) {
        this.typeRepas = typeRepas;
    }
    
    public Integer getNombrePersonnes() {
        return nombrePersonnes;
    }
    
    public void setNombrePersonnes(Integer nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
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
    
    public void addAliment(Aliment aliment) {
        this.aliments.add(aliment);
    }
    
    public void removeAliment(Aliment aliment) {
        this.aliments.remove(aliment);
    }
    
    @Override
    public String toString() {
        return "Repas{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateRepas=" + dateRepas +
                ", typeRepas=" + typeRepas +
                ", nombrePersonnes=" + nombrePersonnes +
                ", createdAt=" + createdAt +
                ", aliments=" + aliments.size() + " aliments" +
                '}';
    }
} 