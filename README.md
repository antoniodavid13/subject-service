# Subject Service — IlernaSmart

Microservicio de gestión de asignaturas, clases y matrículas de la plataforma educativa IlernaSmart.

## Tecnologías

- Java 17
- Spring Boot 3
- Spring Security (validación JWT)
- MySQL
- Maven

## Funcionalidad

Gestiona las clases académicas, las asignaturas que pertenecen a cada clase, y las matrículas de los estudiantes en dichas asignaturas. Permite a los profesores crear y administrar sus asignaturas, y a los estudiantes matricularse en ellas.

## Endpoints principales

### Clases

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/classes` | Listar todas las clases |
| GET | `/api/classes/my` | Clases del usuario autenticado |
| GET | `/api/classes/{id}` | Obtener clase por ID |
| POST | `/api/classes` | Crear clase (admin) |
| PUT | `/api/classes/{id}` | Actualizar clase |
| DELETE | `/api/classes/{id}` | Eliminar clase |
| GET | `/api/classes/{id}/members` | Miembros de una clase |
| POST | `/api/classes/{id}/members` | Añadir miembro a clase |
| DELETE | `/api/classes/{id}/members/{userId}` | Eliminar miembro de clase |

### Asignaturas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/subjects` | Listar todas las asignaturas |
| GET | `/api/subjects/{id}` | Obtener asignatura por ID |
| GET | `/api/subjects/class/{classId}` | Asignaturas de una clase |
| GET | `/api/subjects/teacher/{teacherId}` | Asignaturas de un profesor |
| POST | `/api/subjects` | Crear asignatura |
| PUT | `/api/subjects/{id}` | Actualizar asignatura |
| DELETE | `/api/subjects/{id}` | Eliminar asignatura |

### Matrículas

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/enrollments` | Matricularse en una asignatura |
| DELETE | `/api/enrollments/{subjectId}` | Cancelar matrícula |
| GET | `/api/enrollments/my` | Mis matrículas |
| GET | `/api/enrollments/subject/{subjectId}` | Alumnos matriculados en una asignatura |

## Configuración

```yaml
# application.yml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ilernasmart_subjects
    username: root
    password: tu_password

jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

## Requisitos previos

- Java 17+
- MySQL 8+
- Maven 3.8+
- Auth Service en marcha en el puerto 8081
- API Gateway en marcha en el puerto 8080

## Instalación y arranque

```bash
# Clonar el repositorio
git clone https://github.com/antoniodavid13/subject-service.git
cd subject-service

# Crear la base de datos
mysql -u root -p -e "CREATE DATABASE ilernasmart_subjects;"

# Compilar y arrancar
mvn spring-boot:run
```

El servicio arranca en el puerto **8082**.

## Estructura del proyecto

```
src/main/java/
├── controller/        # ClassController, SubjectController, EnrollmentController
├── service/           # ClassService, SubjectService, EnrollmentService
├── repository/        # ClassRepository, SubjectRepository, EnrollmentRepository
├── model/             # Class, Subject, Enrollment, ClassMember
├── dto/               # SubjectResponse, EnrollmentResponse, ClassResponse
└── security/          # JwtFilter, SecurityConfig
```

## Modelo de datos

- Una **clase** agrupa a profesores y estudiantes
- Una **asignatura** pertenece a una clase y tiene un profesor asignado
- Una **matrícula** vincula a un estudiante con una asignatura
