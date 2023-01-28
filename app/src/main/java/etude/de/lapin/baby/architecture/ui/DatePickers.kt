package etude.de.lapin.baby.architecture.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import etude.de.lapin.baby.architecture.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ShowDatePicker(
    date: LocalDateTime,
    onValueChanged: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
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
    }, modifier = modifier) {
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
fun ShowTimePicker(
    date: LocalDateTime,
    onValueChanged: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
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
    }, modifier = modifier) {
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

@Composable
fun ShowDialog(
    openDialog: MutableState<Boolean>,
    message: String,
    okText: String,
    cancelText: String,
    doOnOk: () -> Unit,
    doOnCancel: () -> Unit
) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                doOnCancel.invoke()
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        doOnOk.invoke()
                    }) {
                    Text(okText)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        doOnCancel.invoke()
                    }) {
                    Text(cancelText)
                }
            }
        )
    }
}