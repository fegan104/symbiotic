package com.frankegan.symbiotic

import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.ingredient_input_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Equivalent to [launch] but return [Unit] instead of [Job].
 *
 * Mainly for usage when you want to lift [launch] to return. Example:
 *
 * ```
 * override fun loadData() = launchSilent {
 *     // code
 * }
 * ```
 */
fun CoroutineScope.launchSilent(
    context: CoroutineContext = this.coroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    this.launch(context, start, block)
}

fun LocalDateTime.format(format: String) = this.format(DateTimeFormatter.ofPattern(format))

inline fun Fragment.openTimePicker(
    hourOfDay: Int = LocalTime.now().hour,
    minute: Int = LocalTime.now().minute,
    is24HourMode: Boolean = true,
    crossinline callback: (view: TimePickerDialog, hour: Int, minute: Int) -> Unit
) {
    return TimePickerDialog.newInstance(
        { v, h, m, _ -> callback(v, h, m) },
        hourOfDay,
        minute,
        is24HourMode
    ).show(requireFragmentManager(), "TimePickerFragment")
}

inline fun Fragment.openDatePicker(
    year: Int = LocalDate.now().year,
    monthOfYear: Int = LocalDate.now().monthValue - 1,
    dayOfMonth: Int = LocalDate.now().dayOfMonth,
    crossinline callback: (view: DatePickerDialog, year: Int, month: Int, day: Int) -> Unit
) {
    return DatePickerDialog.newInstance(
        { v, y, m, d -> callback(v, y, m, d) },
        year,
        monthOfYear,
        dayOfMonth
    ).show(requireFragmentManager(), "DatePickerFragment")
}

suspend inline fun Fragment.openDateTimeDialog(): LocalDateTime = suspendCoroutine { cont ->
    openDatePicker { _, year, month, day ->
        openTimePicker { _, hour, minute ->
            cont.resume(LocalDateTime.of(year, month + 1, day, hour, minute))
        }
    }
}

suspend fun Fragment.textInputDialog() = suspendCoroutine<String> { cont ->
    // Set up the input
    val input = EditText(requireContext()).apply {
        inputType = InputType.TYPE_CLASS_TEXT
    }
    val builder = AlertDialog.Builder(requireContext())
    val dialog = builder.setTitle("Add Caption")
        .setView(input)
        .setPositiveButton("OK") { _, _ -> cont.resume(input.text.toString()) }
        .setNegativeButton("Cancel") { _, _ -> cont.resume("") }
        .create().apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }

    dialog.show()
}

/**
 * @return a triple of the ingredients name, amount, and serving.
 */
suspend fun Fragment.ingredientInputDialog() =
    suspendCoroutine<Triple<String, Double, String>> { cont ->
        val layout = LayoutInflater.from(activity).inflate(R.layout.ingredient_input_dialog, null)
        val quantity = layout.findViewById<EditText>(R.id.ingredient_quantity_input)
        val spinner = layout.findViewById<AutoCompleteTextView>(R.id.exposed_dropdown)
        spinner.apply {
            val units = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                arrayOf("tbsp", "tsp", "gal", "cup")
            )
            exposed_dropdown.apply {
                setAdapter(units)
                listSelection = 0
            }
        }
        AlertDialog.Builder(requireContext())
            .setView(layout)
            .positiveButton(R.string.save) {
                cont.resume(
                    Triple(
                        layout.findViewById<EditText>(R.id.ingredient_name_input).text.toString(),
                        quantity.text.toString().toDoubleOrNull() ?: 0.0,
                        spinner.editableText.toString()
                    )
                )
            }
            .negativeButton(R.string.cancel) { it.first.dismiss() }
            .show()
    }


val LocalDateTime.millis get() = this.toInstant(ZoneOffset.UTC).epochSecond

fun localDateTimeFromMillis(millis: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)

inline fun AlertDialog.Builder.positiveButton(
    @StringRes text: Int,
    crossinline onClick: (Pair<DialogInterface, Int>) -> Unit
): AlertDialog.Builder = this.setPositiveButton(text) { dialog, which -> onClick(dialog to which) }

inline fun AlertDialog.Builder.negativeButton(
    @StringRes text: Int,
    crossinline onClick: (Pair<DialogInterface, Int>) -> Unit
): AlertDialog.Builder = this.setNegativeButton(text) { dialog, which -> onClick(dialog to which) }
