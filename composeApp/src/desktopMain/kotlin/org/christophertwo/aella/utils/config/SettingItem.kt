package org.christophertwo.aella.utils.config

import androidx.compose.runtime.MutableState

/**
 * Interfaz sellada que representa un único elemento de configuración en un dialog.
 * Al ser sellada, nos permite usar un `when` exhaustivo para asegurarnos de que
 * manejamos todos los tipos de configuración posibles al renderizar la UI.
 *
 * @property title El texto descriptivo para esta configuración.
 */
sealed interface SettingItem {
    val title: String

    /**
     * Modelo para una configuración de tipo interruptor (On/Off).
     * Se renderizará como una fila con un texto y un componente `Switch`.
     *
     * @param title El texto que describe la opción.
     * @param state El estado mutable que contiene el valor booleano actual (checked/unchecked).
     */
    data class ToggleSetting(
        override val title: String,
        val state: MutableState<Boolean>
    ) : SettingItem

    /**
     * Modelo para una configuración de entrada de texto.
     * Se renderizará como una fila con un texto y un `OutlinedTextField`.
     *
     * @param title El texto que describe el campo.
     * @param state El estado mutable que contiene el valor de texto actual.
     */
    data class TextEntrySetting(
        override val title: String,
        val state: MutableState<String>
    ) : SettingItem

    /**
     * Modelo para una configuración de selección única entre varias opciones.
     * Se renderizará como una fila con un texto y un menú desplegable.
     *
     * @param title El texto que describe la selección.
     * @param state El estado mutable que contiene la opción actualmente seleccionada.
     * @param options La lista de todas las opciones de texto disponibles para elegir.
     */
    data class ChoiceSetting(
        override val title: String,
        val state: MutableState<String>,
        val options: List<String>,
        val onStateChange: (String) -> Unit
    ) : SettingItem

    /**
     * Modelo para una configuración de tipo barra deslizante (slider).
     * Se renderizará como un grupo con un texto (título y valor) y un `Slider`.
     *
     * @param title El texto que describe el valor.
     * @param state El estado mutable que contiene el valor flotante actual.
     * @param valueRange El rango de valores permitidos para el slider (ej. 0f..1f).
     * @param steps El número de pasos discretos en el slider. 0 para un slider continuo.
     */
    data class SliderSetting(
        override val title: String,
        val state: MutableState<Float>,
        val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        val steps: Int = 0
    ) : SettingItem

}