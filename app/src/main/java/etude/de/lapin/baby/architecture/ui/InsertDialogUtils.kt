package etude.de.lapin.baby.architecture.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import etude.de.lapin.baby.architecture.R

@Composable
fun RowScope.DialogColumnTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.dialog_insert_column_title_width))
            .padding(start = dimensionResource(id = R.dimen.default_padding))
            .align(Alignment.CenterVertically)
    )
}

@Composable
fun DialogAddCancel(onAddAction: () -> Unit, onCancelAction: () -> Unit) {
    Row {
        Button(onClick = {
            onAddAction.invoke()
        }, modifier = Modifier.weight(1f)) {
            Text(stringResource(id = R.string.insert_done))
        }
        Button(onClick = {
            onCancelAction.invoke()
        }, modifier = Modifier.weight(1f)) {
            Text(stringResource(id = R.string.cancel))
        }
    }
}

@Composable
fun DialogModifyDeleteCancel(
    onModifyAction: () -> Unit,
    onDeleteAction: () -> Unit,
    onCancelAction: () -> Unit
) {
    Row {
        Button(onClick = {
            onModifyAction.invoke()
        }, modifier = Modifier.weight(1f)) {
            Text(stringResource(id = R.string.update_done))
        }
        Button(onClick = {
            onDeleteAction.invoke()
        }, modifier = Modifier.weight(1f)) {
            Text(stringResource(id = R.string.delete_done))
        }
        Button(onClick = {
            onCancelAction.invoke()
        }, modifier = Modifier.weight(1f)) {
            Text(stringResource(id = R.string.cancel))
        }
    }
}