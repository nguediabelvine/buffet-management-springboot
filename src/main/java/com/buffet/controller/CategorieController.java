package com.buffet.controller;

import com.buffet.model.Categorie;
import com.buffet.service.CategorieService;
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
@RequestMapping("/api/categories")
@Tag(name = "Catégories", description = "API de gestion des catégories d'aliments")
public class CategorieController {
    
    private final CategorieService categorieService;
    
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les catégories", description = "Retourne la liste de toutes les catégories disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class)))
    })
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.findAll();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par ID", description = "Retourne une catégorie spécifique par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catégorie trouvée",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class))),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    public ResponseEntity<Categorie> getCategorieById(
            @Parameter(description = "ID de la catégorie à récupérer") 
            @PathVariable Long id) {
        Optional<Categorie> categorie = categorieService.findById(id);
        return categorie.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie", description = "Crée une nouvelle catégorie dans la base de données")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Catégorie créée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Categorie> createCategorie(
            @Parameter(description = "Catégorie à créer") 
            @RequestBody Categorie categorie) {
        Categorie savedCategorie = categorieService.save(categorie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategorie);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une catégorie", description = "Met à jour une catégorie existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catégorie mise à jour avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class))),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Categorie> updateCategorie(
            @Parameter(description = "ID de la catégorie à mettre à jour") 
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la catégorie") 
            @RequestBody Categorie categorie) {
        if (!categorieService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categorie.setId(id);
        Categorie updatedCategorie = categorieService.save(categorie);
        return ResponseEntity.ok(updatedCategorie);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie", description = "Supprime une catégorie de la base de données")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Catégorie supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    public ResponseEntity<Void> deleteCategorie(
            @Parameter(description = "ID de la catégorie à supprimer") 
            @PathVariable Long id) {
        if (!categorieService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categorieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/nom/{nom}")
    @Operation(summary = "Récupérer une catégorie par nom", description = "Retourne une catégorie par son nom exact")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catégorie trouvée",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class))),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    public ResponseEntity<Categorie> getCategorieByNom(
            @Parameter(description = "Nom de la catégorie") 
            @PathVariable String nom) {
        Categorie categorie = categorieService.findByNom(nom);
        if (categorie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categorie);
    }
    
    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des catégories par nom", description = "Recherche des catégories dont le nom contient le terme spécifié")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Résultats de la recherche",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class)))
    })
    public ResponseEntity<List<Categorie>> searchCategories(
            @Parameter(description = "Terme de recherche") 
            @RequestParam String nom) {
        List<Categorie> categories = categorieService.findByNomContaining(nom);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/avec-aliments")
    @Operation(summary = "Récupérer toutes les catégories avec leurs aliments", description = "Retourne toutes les catégories avec la liste de leurs aliments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des catégories récupérée avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class)))
    })
    public ResponseEntity<List<Categorie>> getAllCategoriesWithAliments() {
        List<Categorie> categories = categorieService.findAllWithAliments();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/nom/{nom}/avec-aliments")
    @Operation(summary = "Récupérer une catégorie par nom avec ses aliments", description = "Retourne une catégorie avec la liste de ses aliments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catégorie trouvée",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Categorie.class))),
        @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    public ResponseEntity<Categorie> getCategorieByNomWithAliments(
            @Parameter(description = "Nom de la catégorie") 
            @PathVariable String nom) {
        Optional<Categorie> categorie = categorieService.findByNomWithAliments(nom);
        return categorie.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/statistiques")
    @Operation(summary = "Obtenir les statistiques des catégories", description = "Retourne le nombre d'aliments par catégorie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès")
    })
    public ResponseEntity<List<Object[]>> getStatistiques() {
        List<Object[]> statistiques = categorieService.getStatistiques();
        return ResponseEntity.ok(statistiques);
    }
} 