package com.mbt925.weatherapp.design.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.mbt925.weatherapp.design.theme.IconSize
import com.mbt925.weatherapp.design.theme.SizeXXXXS


@Composable
fun IconText(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconResId: Int,
    onClick: (() -> Unit)?,
) {
    Button(
        onClick = onClick,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(IconSize),
                painter = painterResource(iconResId),
                contentDescription = null,
            )
            Spacer(Modifier.width(SizeXXXXS))
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
        }
    }
}
