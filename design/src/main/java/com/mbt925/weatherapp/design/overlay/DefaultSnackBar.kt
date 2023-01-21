package com.mbt925.weatherapp.design.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mbt925.weatherapp.design.component.Button
import com.mbt925.weatherapp.design.theme.SizeS
import com.mbt925.weatherapp.design.theme.SizeXS

@Composable
fun DefaultSnackBar(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    onAction: () -> Unit,
) {
    SnackbarHost(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom),
        hostState = hostState,
    ) { data ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondary)
                .then(contentModifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = SizeS),
                text = data.message,
                style = MaterialTheme.typography.body2,
            )

            data.actionLabel?.let {
                Button(onClick = onAction) {
                    Text(
                        modifier = Modifier.padding(SizeXS),
                        text = it,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}
