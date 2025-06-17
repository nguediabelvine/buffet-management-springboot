-- Création de la table categories
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Création de la table aliments
CREATE TABLE IF NOT EXISTS aliments (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    description TEXT,
    calories_per_100g DECIMAL(8,2),
    allergies TEXT,
    image_url VARCHAR(500),
    categorie_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categorie_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Création de la table repas
CREATE TABLE IF NOT EXISTS repas (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    description TEXT,
    date_repas DATE NOT NULL,
    type_repas VARCHAR(50) NOT NULL, -- 'petit_dejeuner', 'dejeuner', 'diner'
    nombre_personnes INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table de liaison repas_aliments (Many-to-Many)
CREATE TABLE IF NOT EXISTS repas_aliments (
    repas_id INTEGER NOT NULL,
    aliment_id INTEGER NOT NULL,
    quantite_grammes DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (repas_id, aliment_id),
    FOREIGN KEY (repas_id) REFERENCES repas(id) ON DELETE CASCADE,
    FOREIGN KEY (aliment_id) REFERENCES aliments(id) ON DELETE CASCADE
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_aliments_categorie ON aliments(categorie_id);
CREATE INDEX IF NOT EXISTS idx_repas_date ON repas(date_repas);
CREATE INDEX IF NOT EXISTS idx_repas_aliments_repas ON repas_aliments(repas_id);
CREATE INDEX IF NOT EXISTS idx_repas_aliments_aliment ON repas_aliments(aliment_id); 