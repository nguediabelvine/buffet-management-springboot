package com.buffet.controller;

import com.buffet.dto.BuffetRequest;
import com.buffet.dto.BuffetResponse;
import com.buffet.model.Aliment;
import com.buffet.service.BuffetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buffet")
@Tag(name = "Buffet", description = "API de calcul et gestion des buffets")
public class BuffetController {
    
    private final BuffetService buffetService;
    
    public BuffetController(BuffetService buffetService) {
        this.buffetService = buffetService;
    }
    
    @PostMapping("/calculer")
    @Operation(summary = "Calculer un buffet personnalisé", description = "Calcule un buffet basé sur le nombre d'invités et les aliments sélectionnés")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buffet calculé avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = BuffetResponse.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<BuffetResponse> calculerBuffet(
            @Parameter(description = "Paramètres du buffet") 
            @RequestBody BuffetRequest request) {
        BuffetResponse response = buffetService.calculerBuffet(request.getInvites(), request.getAliments());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recommandations/{nombreInvites}")
    @Operation(summary = "Obtenir des recommandations de buffet", description = "Génère des recommandations d'aliments pour un buffet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recommandations générées avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = Aliment.class)))
    })
    public ResponseEntity<List<Aliment>> getRecommandations(
            @Parameter(description = "Nombre d'invités") 
            @PathVariable Integer nombreInvites) {
        List<Aliment> recommandations = buffetService.genererRecommandationsBuffet(nombreInvites);
        return ResponseEntity.ok(recommandations);
    }
    
    @GetMapping("/economique/{nombreInvites}")
    @Operation(summary = "Calculer un buffet économique", description = "Calcule un buffet avec des aliments moins caloriques")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buffet économique calculé avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = BuffetResponse.class)))
    })
    public ResponseEntity<BuffetResponse> calculerBuffetEconomique(
            @Parameter(description = "Nombre d'invités") 
            @PathVariable Integer nombreInvites) {
        BuffetResponse response = buffetService.calculerBuffetEconomique(nombreInvites);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/equilibre/{nombreInvites}")
    @Operation(summary = "Calculer un buffet équilibré", description = "Calcule un buffet avec des aliments sans allergie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buffet équilibré calculé avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = BuffetResponse.class)))
    })
    public ResponseEntity<BuffetResponse> calculerBuffetEquilibre(
            @Parameter(description = "Nombre d'invités") 
            @PathVariable Integer nombreInvites) {
        BuffetResponse response = buffetService.calculerBuffetEquilibre(nombreInvites);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/sans-allergie/{nombreInvites}")
    @Operation(summary = "Calculer un buffet sans allergie", description = "Calcule un buffet avec des aliments sans allergie spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Buffet sans allergie calculé avec succès",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = BuffetResponse.class)))
    })
    public ResponseEntity<BuffetResponse> calculerBuffetSansAllergie(
            @Parameter(description = "Nombre d'invités") 
            @PathVariable Integer nombreInvites,
            @Parameter(description = "Allergie à éviter") 
            @RequestParam String allergie) {
        BuffetResponse response = buffetService.calculerBuffetSansAllergie(nombreInvites, allergie);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/statistiques")
    @Operation(summary = "Obtenir les statistiques d'un buffet", description = "Calcule les statistiques nutritionnelles d'un buffet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistiques calculées avec succès")
    })
    public ResponseEntity<Object> getStatistiquesBuffet(
            @Parameter(description = "Réponse du buffet") 
            @RequestBody BuffetResponse buffetResponse) {
        Double caloriesParPersonne = buffetService.getCaloriesParPersonne(buffetResponse);
        
        return ResponseEntity.ok(Map.of(
            "caloriesParPersonne", caloriesParPersonne,
            "caloriesTotales", buffetResponse.getCaloriesTotales(),
            "nombreInvites", buffetResponse.getNombreInvites(),
            "allergiesPresentes", buffetResponse.getAllergiesPresentes()
        ));
    }
} 