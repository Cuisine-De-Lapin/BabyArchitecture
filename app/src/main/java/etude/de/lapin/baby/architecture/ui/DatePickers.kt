package etude.de.lapin.baby.architecture

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowDatePicker(date: LocalDateTime, onValueChanged: (LocalDateTime) -> Unit) {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year: Int, month: Int, day: Int ->
            onValueChanged(
                date.withMonth(month.plus(1))
                    .withDayOfMonth(day)
                    .withYear(year)
            )

        }, date.year, date.month.value.minus(1), date.dayOfMonth
    )

    Button(onClick = {
        datePickerDialog.show()
    }) {
        DateText(date)
    }
}

@Composable
fun DateText(date: LocalDateTime) {
    val format: DateTimeFormatter =
        DateTimeFormatter.ofPattern(stringResource(id = R.string.date_format))
    Text(text = date.format(format))
}


@Composable
fun ShowTimePicker(date: LocalDateTime, onValueChanged: (LocalDateTime) -> Unit) {
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, minute: Int ->
            onValueChanged(
                date
                    .withHour(hour)
                    .withMinute(minute)
            )
        }, date.hour, date.minute, true
    )
    Button(onClick = {
        timePickerDialog.show()
    }) {
        HourText(date)
    }
}

@Composable
fun HourText(date: LocalDateTime) {
    val format: DateTimeFormatter =
        DateTimeFormatter.ofPattern(stringResource(id = R.string.hour_format))
    Text(text = date.format(format))
}

@Composable
fun ShowDialog(openDialog: MutableState<Boolean>, message: String, closeText: String) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(closeText)
                }
            }
        )
    }
}