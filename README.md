# Atelier Tickets

Petite application de ticketing avec une interface Vue et une API Spring Boot persistée dans H2.

## Lancer le backend

Pré-requis : Java 17+ et Maven 3.9+.

```powershell
cd backend
mvn spring-boot:run
```

L'API est disponible sur `http://localhost:8080/api/tickets`. Les données sont conservées dans `backend/data/ticketing`.

## Lancer le frontend

Pré-requis : Node.js 20+.

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

Ouvrir `http://localhost:5173`.
