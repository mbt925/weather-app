package com.mbt925.weatherapp.design.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    group = "Form factors",
    showBackground = true,
    device = Devices.PHONE,
)
@Preview(
    name = "Foldable",
    group = "Form factors",
    showBackground = true,
    device = Devices.FOLDABLE,
)
@Preview(
    name = "Tablet",
    group = "Form factors",
    showBackground = true,
    device = Devices.TABLET,
)
annotation class FormFactorsPreview
