[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/PIusfa98)
# Programación Orientada a Objetos
## Práctica 5 - Librería Java para definir reglas de base de datos a partir de DTD

**Problema:**   

Se requiere desarrollar una librería en Java que permita establecer las reglas de una base de datos a partir de un DTD (Declaración de Tipo de Documento) definido por el usuario. La librería debe ser capaz de:

  1. **Parsear y procesar el DTD:** Leer y analizar el DTD para extraer información sobre las entidades, sus atributos, tipos de datos, claves primarias, claves foráneas, claves únicas y restricciones de verificación (check constraints).
  2. **Validar archivos CSV:** Utilizar la información extraída del DTD para validar la estructura y los datos de archivos CSV que representan las tablas de la base de datos.
  3. **Generar sentencias SQL:** Crear sentencias SQL para crear las tablas de la base de datos, incluyendo las definiciones de las entidades, atributos, tipos de datos, claves, restricciones y relaciones.
  4. **Integrarse con una GUI existente:** Interactuar con una interfaz gráfica de usuario (GUI) previamente desarrollada para proporcionar una experiencia de usuario amigable para la definición y validación de las reglas de la base de datos.
  5. **Multi-Hilo:** La librería deberá ser capaz de analizar y validar la estructura de varios archivos a la vez.
  6. **Validar sentencias SQL On-The-Fly:** La librería deberá ser capaz de validar las sentencias "sobre la marcha" para que, junto a la GUI, muestre los errores de tipo de datos, de errores en relaciones (llaves foráneas, primarias, únicas y checks) mientras el usuario digita la sentencia SQL.

**Entregables:**

1. **Librería Java:** Se deberá entregar el código fuente de la librería Java, incluyendo:

    * Clases para parsear y procesar el DTD.
    * Clases para validar archivos CSV.
    * Clases para generar sentencias SQL.
    * Documentación completa de la API de la librería.
    * Archivo JAR.

2. **Proyecto Maven:** Se debe entregar un proyecto Maven completo que incluya:

    * Estructura de directorios estándar de Maven.
    * Archivo pom.xml con las dependencias necesarias.
    * Configuración para la compilación y empaquetado de la librería.

3. **Pruebas unitarias:** Se deben implementar pruebas unitarias que cubran la funcionalidad principal de la librería, incluyendo:

    * Pruebas para el parseo y procesamiento del DTD.
    * Pruebas para la validación de archivos CSV.
    * Pruebas para la generación de sentencias SQL.

4. **Pruebas de integración:** Se deben implementar pruebas de integración que verifiquen la interacción de la librería con la GUI existente, incluyendo:

    * Pruebas para cargar y analizar el DTD en la GUI.
    * Pruebas para validar archivos CSV y generar sentencias SQL a través de la GUI.
    * Pruebas para la integración de la librería con las funcionalidades de la GUI.

5. **Documentación:** Se deberá entregar documentación completa en JavaDoc que incluya:

    * Una descripción detallada del problema y la solución propuesta.
    * Instrucciones para la instalación y uso de la librería.
    * Ejemplos de uso de la librería.
    * Explicación de las pruebas unitarias y de integración.
