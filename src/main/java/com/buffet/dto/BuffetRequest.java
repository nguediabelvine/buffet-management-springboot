package com.buffet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BuffetRequest {
    
    @NotNull(message = "Le nombre d'invités est obligatoire")
    @Min(value = 1, message = "Le nombre d'invités doit être au moins 1")
    private Integer invites;
    
    @NotEmpty(message = "La liste des aliments est obligatoire")
    private List<Long> aliments;
    
    // Constructeurs
    public BuffetRequest() {}
    
    public BuffetRequest(Integer invites, List<Long> aliments) {
        this.invites = invites;
        this.aliments = aliments;
    }
    
    // Getters et Setters
    public Integer getInvites() {
        return invites;
    }
    
    public void setInvites(Integer invites) {
        this.invites = invites;
    }
    
    public List<Long> getAliments() {
        return aliments;
    }
    
    public void setAliments(List<Long> aliments) {
        this.aliments = aliments;
    }
    
    @Override
    public String toString() {
        return "BuffetRequest{" +
                "invites=" + invites +
                ", aliments=" + aliments +
                '}';
    }
} 