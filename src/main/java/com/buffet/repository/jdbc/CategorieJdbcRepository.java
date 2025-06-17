package com.buffet.repository.jdbc;

import com.buffet.model.Categorie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CategorieJdbcRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Categorie> categorieRowMapper = (rs, rowNum) -> {
        Categorie categorie = new Categorie();
        categorie.setId(rs.getLong("id"));
        categorie.setNom(rs.getString("nom"));
        categorie.setDescription(rs.getString("description"));
        categorie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return categorie;
    };
    
    public CategorieJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * Sauvegarde une catégorie
     */
    public Categorie save(Categorie categorie) {
        if (categorie.getId() == null) {
            return insert(categorie);
        } else {
            return update(categorie);
        }
    }
    
    /**
     * Insère une nouvelle catégorie
     */
    private Categorie insert(Categorie categorie) {
        String sql = "INSERT INTO categories (nom, description, created_at) VALUES (?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categorie.getNom());
            ps.setString(2, categorie.getDescription());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);
        
        categorie.setId(keyHolder.getKey().longValue());
        return categorie;
    }
    
    /**
     * Met à jour une catégorie existante
     */
    private Categorie update(Categorie categorie) {
        String sql = "UPDATE categories SET nom = ?, description = ? WHERE id = ?";
        
        jdbcTemplate.update(sql,
                categorie.getNom(),
                categorie.getDescription(),
                categorie.getId());
        
        return categorie;
    }
    
    /**
     * Trouve une catégorie par ID
     */
    public Optional<Categorie> findById(Long id) {
        String sql = "SELECT id, nom, description, created_at FROM categories WHERE id = ?";
        
        List<Categorie> result = jdbcTemplate.query(sql, categorieRowMapper, id);
        
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    
    /**
     * Trouve une catégorie par nom
     */
    public Optional<Categorie> findByNom(String nom) {
        String sql = "SELECT id, nom, description, created_at FROM categories WHERE nom = ?";
        
        List<Categorie> result = jdbcTemplate.query(sql, categorieRowMapper, nom);
        
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    
    /**
     * Trouve toutes les catégories
     */
    public List<Categorie> findAll() {
        String sql = "SELECT id, nom, description, created_at FROM categories ORDER BY nom";
        
        return jdbcTemplate.query(sql, categorieRowMapper);
    }
    
    /**
     * Trouve les catégories par nom (recherche partielle)
     */
    public List<Categorie> findByNomContaining(String nom) {
        String sql = "SELECT id, nom, description, created_at FROM categories WHERE LOWER(nom) LIKE LOWER(?) ORDER BY nom";
        
        return jdbcTemplate.query(sql, categorieRowMapper, "%" + nom + "%");
    }
    
    /**
     * Supprime une catégorie par ID
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    /**
     * Compte le nombre total de catégories
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM categories";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    /**
     * Vérifie si une catégorie existe par nom
     */
    public boolean existsByNom(String nom) {
        String sql = "SELECT COUNT(*) FROM categories WHERE nom = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, nom);
        return count != null && count > 0;
    }
} 