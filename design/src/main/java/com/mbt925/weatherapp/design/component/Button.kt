package com.mbt925.weatherapp.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    val clickAndSemanticsModifier = if (onClick == null) Modifier else
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            role = Role.Button,
            enabled = true,
            onClick = onClick,
        )

    Box(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.background(
                        color = MaterialTheme.colors.primary
                            .copy(alpha = ContentAlpha.disabled),
                        shape = MaterialTheme.shapes.small,
                    )
                } else Modifier
            )
            .clip(MaterialTheme.shapes.small)
            .then(clickAndSemanticsModifier),
    ) {
        content()
    }
}
