# Hibernate Standalone

Proyecto Java que demuestra el uso de Hibernate en modo standalone (sin contenedor de aplicaciones).


## 1. Clonar el repositorio

1. Abre una terminal (cmd o PowerShell) en la carpeta deseada.  
2. Clona el repositorio:
   ```bash
   git clone https://github.com/giuliano-simone-dev/hibernate-standalone.git
   cd hibernate-standalone
   ```
3. (Opcional, recomendado) Elimina la conexión al remoto para evitar problemas de permisos:
   ```bash
   git remote remove origin
   ```
4. (Opcional) Descarga las dependencias de Maven:
   ```bash
   mvn install
   ```

## 2. Configurar el proyecto

1. Abre el proyecto en tu IDE preferido (NetBeans, IntelliJ IDEA, Eclipse, etc.).  
2. Si no ejecutaste `mvn install` (paso 1.4), descarga las dependencias desde el IDE (por ejemplo, en NetBeans haz clic derecho en **Dependencies** → **Download Declared Dependencies**).  
3. Editar `src/main/resources/hibernate.cfg.xml`:
   - Actualizar `hibernate.connection.url` con la URL de tu base de datos.  
   - Sustituir `hibernate.connection.username` e `hibernate.connection.password` por las credenciales.  

## 3. Uso

1. El punto de entrada (método main) está en `src/main/java/com/hibernatestandalone/HibernateStandalone/App.java`.  
2. Las entidades se encuentran en el paquete:
   ```
   com.hibernatestandalone.entity
   ```
   Para cada nueva tabla, crea una clase anotada con Jakarta Persistence (`@Entity`, `@Id`, `@ManyToOne`, etc.).  
3. Para cada entidad, crea un servicio en:
   ```
   com.hibernatestandalone.services
   ```
   Extender `AbstractService` y ajustat los métodos según la lógica (priorizar el menor cambio posible). Si se lo desea, se puede utilziar JPAController en lugar de los servicios.  
4. Agrega el mapeo de cada entidad en `hibernate.cfg.xml`:
   ```xml
   <mapping class="com.hibernatestandalone.entity.NombreDeTuEntidad"/>
   ```

---