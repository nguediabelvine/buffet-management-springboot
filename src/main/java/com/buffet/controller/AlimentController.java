package com.buffet.controller;

import com.buffet.model.Aliment;
import com.buffet.service.AlimentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aliments")
@Tag(name = "Aliments", description = "API de gestion des aliments")
public class AlimentController {
    
    private final AlimentService alimentService;
    
    public AlimentController(AlimentService alimentService) {
        this.alimentService = alimentService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer tous les aliments", description = "Retourne la liste de tous les aliments disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getAllAliments() {
        List<Aliment> aliments = alimentService.findAllWithCategorie();
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un aliment par ID", description = "Retourne un aliment spécifique par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aliment trouvé",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class))),
        @ApiResponse(responseCode = "404", description = "Aliment non trouvé")
    })
    public ResponseEntity<Aliment> getAlimentById(
            @Parameter(description = "ID de l'aliment à récupérer") 
            @PathVariable Long id) {
        Optional<Aliment> aliment = alimentService.findByIdWithCategorie(id);
        return aliment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouvel aliment", description = "Crée un nouvel aliment dans la base de données")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aliment créé avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Aliment> createAliment(
            @Parameter(description = "Aliment à créer") 
            @RequestBody Aliment aliment) {
        Aliment savedAliment = alimentService.save(aliment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAliment);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un aliment", description = "Met à jour un aliment existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aliment mis à jour avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class))),
        @ApiResponse(responseCode = "404", description = "Aliment non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Aliment> updateAliment(
            @Parameter(description = "ID de l'aliment à mettre à jour") 
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de l'aliment") 
            @RequestBody Aliment aliment) {
        if (!alimentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        aliment.setId(id);
        Aliment updatedAliment = alimentService.save(aliment);
        return ResponseEntity.ok(updatedAliment);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un aliment", description = "Supprime un aliment de la base de données")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aliment supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Aliment non trouvé")
    })
    public ResponseEntity<Void> deleteAliment(
            @Parameter(description = "ID de l'aliment à supprimer") 
            @PathVariable Long id) {
        if (!alimentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alimentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categorie/{categorieNom}")
    @Operation(summary = "Récupérer les aliments par catégorie", description = "Retourne tous les aliments d'une catégorie spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getAlimentsByCategorie(
            @Parameter(description = "Nom de la catégorie") 
            @PathVariable String categorieNom) {
        List<Aliment> aliments = alimentService.findByCategorieNom(categorieNom);
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des aliments par nom", description = "Recherche des aliments dont le nom contient le terme spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Résultats de la recherche",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> searchAliments(
            @Parameter(description = "Terme de recherche") 
            @RequestParam String nom) {
        List<Aliment> aliments = alimentService.findByNomContaining(nom);
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/calories")
    @Operation(summary = "Récupérer les aliments par plage de calories", description = "Retourne les aliments dans une plage de calories spécifiée")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getAlimentsByCalories(
            @Parameter(description = "Calories minimum") 
            @RequestParam Double minCalories,
            @Parameter(description = "Calories maximum") 
            @RequestParam Double maxCalories) {
        List<Aliment> aliments = alimentService.findByCaloriesBetween(minCalories, maxCalories);
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/allergies")
    @Operation(summary = "Récupérer les aliments par allergie", description = "Retourne les aliments contenant une allergie spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getAlimentsByAllergies(
            @Parameter(description = "Allergie à rechercher") 
            @RequestParam String allergie) {
        List<Aliment> aliments = alimentService.findByAllergiesContaining(allergie);
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/sans-allergie")
    @Operation(summary = "Récupérer les aliments sans allergie", description = "Retourne les aliments sans allergie connue")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getAlimentsSansAllergie() {
        List<Aliment> aliments = alimentService.findSansAllergie();
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/top-caloriques")
    @Operation(summary = "Récupérer les aliments les plus caloriques", description = "Retourne les aliments avec le plus de calories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getTopCaloriques() {
        List<Aliment> aliments = alimentService.findTopCaloriques();
        return ResponseEntity.ok(aliments);
    }
    
    @GetMapping("/moins-caloriques")
    @Operation(summary = "Récupérer les aliments les moins caloriques", description = "Retourne les aliments avec le moins de calories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des aliments récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getMoinsCaloriques() {
        List<Aliment> aliments = alimentService.findMoinsCaloriques();
        return ResponseEntity.ok(aliments);
    }
} 