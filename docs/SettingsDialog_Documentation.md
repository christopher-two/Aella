### **Documentación del Componente: SettingsDialog**

---

### **1. Introducción**

El componente `SettingsDialog` proporciona una solución de alta productividad para crear *dialogs* de configuración en aplicaciones de **Compose Multiplatform**. Utiliza un enfoque **dirigido por datos (*data-driven*)**, donde la interfaz de usuario se genera automáticamente a partir de una lista de modelos de datos, en lugar de ser construida manualmente.

Este enfoque estandariza la apariencia, reduce drásticamente el código repetitivo y acelera el desarrollo.

---

### **2. Características Principales**

- **Generación Automática de UI:** La interfaz se renderiza a partir de una lista de modelos (`SettingItem`).  
- **Controles Predefinidos:** Soporte integrado para interruptores (`Switch`), campos de texto (`TextField`), selectores de opción única (`DropdownMenu`) y barras deslizantes (`Slider`).  
- **Totalmente Personalizable:** La apariencia (colores, formas, dimensiones, tipografía) está desacoplada de la lógica y se controla a través de un objeto de configuración (`SettingsDialogConfig`).  
- **Modular y Reutilizable:** Diseñado para ser independiente y fácil de integrar en cualquier proyecto de Compose Multiplatform.  
- **A Prueba de Errores:** El uso de interfaces selladas (*sealed interface*) garantiza que todos los tipos de configuración definidos sean manejados en tiempo de compilación.

---

### **3. API y Archivos del Componente**

El sistema se divide en tres archivos lógicos:

---

#### **3.1. `SettingModels.kt` (Modelos de Datos)**

Define la API para los datos de configuración:

- `SettingItem`: *sealed interface*  
  - La interfaz base que todos los modelos de configuración deben implementar.  
  - **Propiedad:** `title: String` — El texto descriptivo para el ajuste.  

- `ToggleSetting`: *data class*  
  - Renderiza una fila con un título y un `Switch`.  
  - **Parámetros:**  
    - `title: String`: El texto a mostrar.  
    - `state: MutableState<Boolean>`: El estado que almacena el valor `true`/`false`.  

- `TextEntrySetting`: *data class*  
  - Renderiza una fila con un título y un `OutlinedTextField`.  
  - **Parámetros:**  
    - `title: String`: El texto a mostrar.  
    - `state: MutableState<String>`: El estado que almacena el texto introducido.  

- `ChoiceSetting`: *data class*  
  - Renderiza una fila con un título y un menú desplegable.  
  - **Parámetros:**  
    - `title: String`: El texto a mostrar.  
    - `state: MutableState<String>`: El estado que almacena la opción seleccionada.  
    - `options: List<String>`: La lista de opciones a mostrar en el menú.  

- `SliderSetting`: *data class*  
  - Renderiza un control deslizante con un título.  
  - **Parámetros:**  
    - `title: String`: El texto a mostrar.  
    - `state: MutableState<Float>`: El estado que almacena el valor del slider.  
    - `valueRange: ClosedFloatingPointRange<Float>` (opcional): El rango de valores. Por defecto `0f..1f`.  
    - `steps: Int` (opcional): El número de pasos discretos. Por defecto `0` (continuo).  

---

#### **3.2. `SettingsDialog.kt` (Componente Principal)**

Contiene el composable principal que renderiza la UI:

```kotlin
@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    title: String,
    items: List<SettingItem>,
    config: SettingsDialogConfig,
    buttons: @Composable RowScope.() -> Unit
)
```

---

#### **3.3. `SettingsDialogConfig.kt` (Configuración de Estilo)**

Define la API para la personalización visual:

- `@Composable fun getDefaultSettingsDialogConfig()`:  
  Función de utilidad que retorna una instancia de `SettingsDialogConfig` con valores predeterminados basados en el `MaterialTheme` actual. Se recomienda usar esta función como punto de partida.

- `data class SettingsDialogConfig`:  
  - **Propiedades:**  
    - `colors: DialogColors`: Define los colores del contenedor, título, etc.  
    - `dimensions: DialogDimensions`: Define anchos/altos máximos y paddings.  
    - `shape: Shape`: Define la forma de las esquinas del dialog.  
    - `typography: DialogTypography`: Define el estilo del texto del título.

---

### **4. Guía de Implementación**

Sigue estos tres pasos para integrar el dialog en tu aplicación.

---

#### **Paso 1: Declarar los Estados**

Define los `MutableState` que contendrán los valores de tu configuración:

```kotlin
// En tu Composable
val notificationState = remember { mutableStateOf(true) }
val usernameState = remember { mutableStateOf("UsuarioInicial") }
val themeState = remember { mutableStateOf("Oscuro") }
```

---

#### **Paso 2: Construir la Lista de Modelos**

Crea una `List<SettingItem>` asociando cada modelo con su estado correspondiente:

```kotlin
val settingsList = remember {
    list_of(
        ToggleSetting("Habilitar notificaciones", notificationState),
        TextEntrySetting("Nombre de usuario", usernameState),
        ChoiceSetting("Tema", themeState, listOf("Claro", "Oscuro", "Sistema"))
    )
}
```

---

#### **Paso 3: Invocar `SettingsDialog`**

Llama al composable `SettingsDialog` y pásale la lista y los botones de acción:

```kotlin
if (showDialog) {
    SettingsDialog(
        onDismissRequest = { showDialog = false },
        title = "Configuración",
        items = settingsList,
        config = getDefaultSettingsDialogConfig(),
        buttons = {
            OutlinedButton(onClick = { showDialog = false }) {
                Text("Cancelar")
            }
            Button(onClick = {
                // Aquí va tu lógica para guardar los valores
                // Ejemplo: viewModel.save(usernameState.value, ...)
                showDialog = false
            }) {
                Text("Guardar")
            }
        }
    )
}
```

---

### **5. Personalización Avanzada**

Para cambiar la apariencia del dialog, crea tu propia instancia de `SettingsDialogConfig` en lugar de usar `getDefaultSettingsDialogConfig()`.

**Ejemplo: Un dialog más compacto y con colores personalizados**

```kotlin
val customConfig = SettingsDialogConfig(
    colors = DialogColors(
        containerColor = Color.DarkGray,
        titleContentColor = Color.White,
        dividerColor = Color.Gray
    ),
    dimensions = DialogDimensions(
        dialogMaxWidth = 400.dp, // Más estrecho
        contentPadding = PaddingValues(8.dp) // Menos padding
    ),
    shape = RoundedCornerShape(4.dp), // Esquinas menos redondeadas
    typography = DialogTypography(
        titleTextStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
)

// ... y luego pásalo al dialog
SettingsDialog(
    onDismissRequest = { /* ... */ },
    title = "Configuración",
    items = settingsList,
    config = customConfig,
    buttons = { /* ... */ }
)
```
