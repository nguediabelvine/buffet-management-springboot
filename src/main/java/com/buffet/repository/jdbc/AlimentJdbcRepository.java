package com.buffet.repository.jdbc;

import com.buffet.model.Aliment;
import com.buffet.model.Categorie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AlimentJdbcRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final CategorieJdbcRepository categorieRepository;
    private final RowMapper<Aliment> alimentRowMapper;
    
    public AlimentJdbcRepository(JdbcTemplate jdbcTemplate, CategorieJdbcRepository categorieRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.categorieRepository = categorieRepository;
        
        this.alimentRowMapper = (rs, rowNum) -> {
            Aliment aliment = new Aliment();
            aliment.setId(rs.getLong("id"));
            aliment.setNom(rs.getString("nom"));
            aliment.setDescription(rs.getString("description"));
            aliment.setCaloriesPer100g(rs.getBigDecimal("calories_per_100g"));
            aliment.setAllergies(rs.getString("allergies"));
            aliment.setImageUrl(rs.getString("image_url"));
            aliment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            
            // Charger la catégorie
            Long categorieId = rs.getLong("categorie_id");
            if (categorieId != null) {
                categorieRepository.findById(categorieId).ifPresent(aliment::setCategorie);
            }
            
            return aliment;
        };
    }
    
    /**
     * Sauvegarde un aliment
     */
    public Aliment save(Aliment aliment) {
        if (aliment.getId() == null) {
            return insert(aliment);
        } else {
            return update(aliment);
        }
    }
    
    /**
     * Insère un nouvel aliment
     */
    private Aliment insert(Aliment aliment) {
        String sql = "INSERT INTO aliments (nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, aliment.getNom());
            ps.setString(2, aliment.getDescription());
            ps.setBigDecimal(3, aliment.getCaloriesPer100g());
            ps.setString(4, aliment.getAllergies());
            ps.setString(5, aliment.getImageUrl());
            ps.setLong(6, aliment.getCategorie().getId());
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        
        aliment.setId(keyHolder.getKey().longValue());
        return aliment;
    }
    
    /**
     * Met à jour un aliment existant
     */
    private Aliment update(Aliment aliment) {
        String sql = "UPDATE aliments SET nom = ?, description = ?, calories_per_100g = ?, " +
                    "allergies = ?, image_url = ?, categorie_id = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
                aliment.getNom(),
                aliment.getDescription(),
                aliment.getCaloriesPer100g(),
                aliment.getAllergies(),
                aliment.getImageUrl(),
                aliment.getCategorie().getId(),
                aliment.getId());
        
        return aliment;
    }
    
    /**
     * Trouve un aliment par ID
     */
    public Optional<Aliment> findById(Long id) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE id = ?";
        
        List<Aliment> result = jdbcTemplate.query(sql, alimentRowMapper, id);
        
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    
    /**
     * Trouve tous les aliments
     */
    public List<Aliment> findAll() {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments ORDER BY nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper);
    }
    
    /**
     * Trouve les aliments d'une catégorie
     */
    public List<Aliment> findByCategorie(Categorie categorie) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE categorie_id = ? ORDER BY nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper, categorie.getId());
    }
    
    /**
     * Trouve les aliments par nom de catégorie
     */
    public List<Aliment> findByCategorieNom(String categorieNom) {
        String sql = "SELECT a.id, a.nom, a.description, a.calories_per_100g, a.allergies, a.image_url, a.categorie_id, a.created_at " +
                    "FROM aliments a " +
                    "JOIN categories c ON a.categorie_id = c.id " +
                    "WHERE c.nom = ? ORDER BY a.nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper, categorieNom);
    }
    
    /**
     * Trouve les aliments par nom (recherche partielle)
     */
    public List<Aliment> findByNomContaining(String nom) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE LOWER(nom) LIKE LOWER(?) ORDER BY nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper, "%" + nom + "%");
    }
    
    /**
     * Trouve les aliments par plage de calories
     */
    public List<Aliment> findByCaloriesBetween(BigDecimal minCalories, BigDecimal maxCalories) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE calories_per_100g BETWEEN ? AND ? ORDER BY calories_per_100g";
        
        return jdbcTemplate.query(sql, alimentRowMapper, minCalories, maxCalories);
    }
    
    /**
     * Trouve les aliments par allergie
     */
    public List<Aliment> findByAllergiesContaining(String allergie) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE LOWER(allergies) LIKE LOWER(?) ORDER BY nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper, "%" + allergie + "%");
    }
    
    /**
     * Trouve les aliments sans allergie
     */
    public List<Aliment> findSansAllergie() {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE allergies = 'Aucune allergie connue' OR allergies IS NULL ORDER BY nom";
        
        return jdbcTemplate.query(sql, alimentRowMapper);
    }
    
    /**
     * Supprime un aliment par ID
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM aliments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    /**
     * Compte le nombre total d'aliments
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM aliments";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    /**
     * Trouve les aliments les plus caloriques
     */
    public List<Aliment> findTopCaloriques(int limit) {
        String sql = "SELECT id, nom, description, calories_per_100g, allergies, image_url, categorie_id, created_at " +
                    "FROM aliments WHERE calories_per_100g IS NOT NULL " +
                    "ORDER BY calories_per_100g DESC LIMIT ?";
        
        return jdbcTemplate.query(sql, alimentRowMapper, limit);
    }
} 