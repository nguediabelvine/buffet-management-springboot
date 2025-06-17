package com.buffet;

import com.buffet.model.Categorie;
import com.buffet.model.Aliment;
import com.buffet.service.CategorieService;
import com.buffet.service.AlimentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BuffetManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuffetManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(CategorieService categorieService, AlimentService alimentService) {
        return args -> {
            System.out.println("=== Import des données initiales ===");
            
            try {
                // Charger le fichier JSON
                ObjectMapper mapper = new ObjectMapper();
                ClassPathResource resource = new ClassPathResource("data.json");
                InputStream inputStream = resource.getInputStream();
                
                Map<String, Object> data = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
                
                // Importer les catégories
                List<Map<String, Object>> categoriesData = (List<Map<String, Object>>) data.get("categories");
                for (Map<String, Object> catData : categoriesData) {
                    Categorie categorie = new Categorie();
                    categorie.setNom((String) catData.get("nom"));
                    categorie.setDescription((String) catData.get("description"));
                    categorieService.save(categorie);
                    System.out.println("Catégorie importée: " + categorie.getNom());
                }
                
                // Importer les aliments
                List<Map<String, Object>> alimentsData = (List<Map<String, Object>>) data.get("aliments");
                for (Map<String, Object> aliData : alimentsData) {
                    Aliment aliment = new Aliment();
                    aliment.setNom((String) aliData.get("nom"));
                    aliment.setDescription((String) aliData.get("description"));
                    aliment.setAllergies((String) aliData.get("allergies"));
                    aliment.setImageUrl((String) aliData.get("image_url"));
                    
                    // Convertir les calories en BigDecimal
                    Object caloriesObj = aliData.get("calories_per_100g");
                    if (caloriesObj != null) {
                        BigDecimal calories = new BigDecimal(caloriesObj.toString());
                        aliment.setCaloriesPer100g(calories);
                    }
                    
                    // Trouver la catégorie correspondante
                    String categorieNom = (String) aliData.get("categorie_nom");
                    Categorie categorie = categorieService.findByNom(categorieNom);
                    if (categorie != null) {
                        aliment.setCategorie(categorie);
                        alimentService.save(aliment);
                        System.out.println("Aliment importé: " + aliment.getNom() + " (Catégorie: " + categorie.getNom() + ")");
                    } else {
                        System.err.println("Catégorie non trouvée pour l'aliment: " + aliment.getNom());
                    }
                }
                
                System.out.println("=== Import terminé avec succès ===");
                System.out.println("Nombre de catégories importées: " + categoriesData.size());
                System.out.println("Nombre d'aliments importés: " + alimentsData.size());
                
            } catch (Exception e) {
                System.err.println("Erreur lors de l'import des données: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
} 