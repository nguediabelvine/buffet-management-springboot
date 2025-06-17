# 🍽️ Gestion de Buffet - Application Spring Boot

Application de gestion de buffet et de recommandations alimentaires développée avec Spring Boot 3.2.0 et PostgreSQL.

## 🚀 Fonctionnalités

- **Gestion des aliments** : CRUD complet avec informations nutritionnelles et allergies
- **Gestion des catégories** : Organisation des aliments par catégories
- **API REST** : Interface complète pour interagir avec l'application
- **Documentation Swagger** : Interface interactive pour tester les APIs
- **Architecture en couches** : DAO, Services, Controllers
- **Double implémentation** : JDBC et JPA
- **Base de données PostgreSQL** : Persistance des données

## 🛠️ Technologies utilisées

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Web MVC**
- **PostgreSQL**
- **Maven**
- **Swagger/OpenAPI**
- **Docker**

## 📋 Prérequis

### Pour le développement local
- Java 17 ou supérieur
- Maven 3.6+
- PostgreSQL 12+

### Pour Docker
- Docker
- Docker Compose

## 🐳 Déploiement avec Docker

### Option 1 : Docker Compose (Recommandé)

1. **Cloner le repository**
```bash
git clone https://github.com/nguediabelvine/buffet-management-springboot.git
cd buffet-management-springboot
```

2. **Lancer l'application avec Docker Compose**
```bash
docker-compose up -d
```

3. **Accéder à l'application**
- **Swagger UI** : http://localhost:8080/swagger-ui/index.html
- **API Base** : http://localhost:8080/api

4. **Arrêter l'application**
```bash
docker-compose down
```

### Option 2 : Docker seul

1. **Construire l'image**
```bash
docker build -t buffet-management .
```

2. **Lancer le conteneur**
```bash
docker run -p 8080:8080 buffet-management
```

## 🏃‍♂️ Développement local

### 1. Configuration de la base de données

Créer une base de données PostgreSQL :
```sql
CREATE DATABASE buffet_db;
CREATE USER buffet_user WITH PASSWORD 'buffet_password';
GRANT ALL PRIVILEGES ON DATABASE buffet_db TO buffet_user;
```

### 2. Configuration de l'application

Le fichier `application.properties` est déjà configuré pour PostgreSQL.

### 3. Lancer l'application

```bash
mvn spring-boot:run
```

### 4. Accéder à l'application

- **Swagger UI** : http://localhost:8080/swagger-ui/index.html
- **API Base** : http://localhost:8080/api

## 📚 API Endpoints

### Aliments
- `GET /api/aliments` - Liste tous les aliments
- `GET /api/aliments/{id}` - Récupère un aliment par ID
- `POST /api/aliments` - Crée un nouvel aliment
- `PUT /api/aliments/{id}` - Met à jour un aliment
- `DELETE /api/aliments/{id}` - Supprime un aliment

### Catégories
- `GET /api/categories` - Liste toutes les catégories
- `GET /api/categories/{id}` - Récupère une catégorie par ID
- `GET /api/categories/{id}/aliments` - Liste les aliments d'une catégorie

### Buffet
- `POST /api/buffet/calculer` - Calcule les recommandations de buffet

## 🗄️ Structure de la base de données

### Tables principales
- **categories** : Catégories d'aliments
- **aliments** : Aliments avec informations nutritionnelles
- **repas** : Planification des repas
- **repas_aliments** : Relation many-to-many entre repas et aliments

## 🔧 Configuration

### Variables d'environnement Docker
- `SPRING_DATASOURCE_URL` : URL de connexion PostgreSQL
- `SPRING_DATASOURCE_USERNAME` : Nom d'utilisateur PostgreSQL
- `SPRING_DATASOURCE_PASSWORD` : Mot de passe PostgreSQL
- `SPRING_JPA_HIBERNATE_DDL_AUTO` : Mode de création des tables

## 📝 Notes importantes

- L'application utilise `create-drop` pour Hibernate en mode Docker
- Les données sont persistées dans un volume Docker pour PostgreSQL
- L'application redémarre automatiquement en cas d'erreur

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👨‍💻 Auteur

**Nguedia Belvine**
- GitHub: [@nguediabelvine](https://github.com/nguediabelvine)
