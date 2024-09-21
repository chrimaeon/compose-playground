/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.webkit.WebView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.cmgapps.android.compose.BuildConfig
import com.cmgapps.android.compose.R
import org.intellij.lang.annotations.Language
import java.io.File
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    backButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    buildYear: String = BuildConfig.BUILD_YEAR,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) }, navigationIcon = backButton)
        },
    ) { contentPadding ->
        var showOssDialog by rememberSaveable(key = "showOssDialog") { mutableStateOf(false) }
        var showOflDialog by rememberSaveable(key = "showOflDialog") { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier.padding(contentPadding),
        ) {
            item {
                Header(title = stringResource(R.string.about))
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.cmg_mobile_apps),
                    description = stringResource(R.string.copyright, buildYear),
                )
            }
            item {
                Header(title = stringResource(R.string.licenses))
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.open_source_licenses_title),
                    description = stringResource(R.string.open_source_licencses_desc),
                    onClick = { showOssDialog = true },
                )
            }
            item {
                PreferenceItem(
                    title = stringResource(R.string.open_font_licenses_title),
                    description = stringResource(R.string.open_font_licenses_desc),
                    onClick = { showOflDialog = true },
                )
            }
        }

        if (showOssDialog) {
            OssSheet(onDismissRequest = { showOssDialog = false })
        }

        if (showOflDialog) {
            OflSheet(onDismissRequest = { showOflDialog = false })
        }
    }
}

@Composable
private fun PreferenceItem(
    title: String,
    description: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier =
            onClick?.let {
                Modifier.clickable(onClick = onClick)
            } ?: Modifier,
        headlineContent = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        supportingContent =
            description?.let {
                {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
        trailingContent = trailingContent,
        leadingContent = { Box(modifier = Modifier.width(32.dp)) },
    )
}

@Composable
private fun Header(title: String) {
    Text(
        title,
        modifier = Modifier.padding(start = 62.dp, top = 16.dp, bottom = 8.dp),
        style =
            MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewSheet(
    onDismissRequest: () -> Unit,
    title: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        modifier = modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        containerColor = BottomSheetDefaults.ContainerColor,
    ) {
        val backgroundColor = BottomSheetDefaults.ContainerColor
        val licenseBackgroundColor = MaterialTheme.colorScheme.surface
        val licenseShape = MaterialTheme.shapes.medium
        val contentColor = LocalContentColor.current

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            item {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            item {
                AndroidView(
                    factory = ::WebView,
                    update = { webView ->
                        webView.webViewClient =
                            CssInjectWebViewClient(
                                bodyBackground = backgroundColor,
                                licenseBackgroundColor = licenseBackgroundColor,
                                contentColor = contentColor,
                                licenseShape = licenseShape,
                            )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            webView.enableDarkMode()
                        }
                        webView.loadUrl(url)
                    },
                )
            }
        }
    }
}

fun File.asAssetUrl(): String = "file:///android_asset/${this.path}"

@Composable
private fun OssSheet(onDismissRequest: () -> Unit) {
    WebViewSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.open_source_licenses_title),
        url = File("licenses.html").asAssetUrl(),
    )
}

@Composable
private fun OflSheet(onDismissRequest: () -> Unit) {
    WebViewSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(id = R.string.open_font_licenses_title),
        url = File("ofl.html").asAssetUrl(),
    )
}

@Suppress("DEPRECATION")
private fun WebView.enableDarkMode() {
    val nightModeFlag: Int = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(
                this.settings,
                WebSettingsCompat.FORCE_DARK_ON,
            )
        }
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
            WebSettingsCompat.setForceDarkStrategy(
                this.settings,
                WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING,
            )
        }
    }
}

private class CssInjectWebViewClient(
    private val bodyBackground: Color,
    private val licenseBackgroundColor: Color,
    private val contentColor: Color,
    private val licenseShape: CornerBasedShape,
) : WebViewClientCompat() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onPageFinished(
        view: WebView,
        url: String?,
    ) {
        view.settings.javaScriptEnabled = true

        @Language("css")
        val css =
            """
            :root {
              --license-background-color: ${licenseBackgroundColor.toCssColor()};
              --body-background-color: ${bodyBackground.toCssColor()};
              --content-color: ${contentColor.toCssColor()};
              --border-radius: ${licenseShape.toCssBorderRadius()};
            }
            """.trimIndent()

        @Language("javascript")
        val injectCssCode =
            """
            (function() {
               var head = document.getElementsByTagName('head').item(0);
               var style = document.createElement('style');
               style.type = 'text/css';
               style.innerHTML = '$css';
               head.insertBefore(style, head.firstChild);
               var link = document.createElement('link');
               link.rel = 'stylesheet';
               link.href = 'style.css';
               head.insertBefore(link, head.firstChild);
            })()
            """.trimIndent()

        view.loadUrl("javascript:$injectCssCode")

        super.onPageFinished(view, url)
    }
}

private fun Color.toCssColor(): String =
    "rgba(%d,%d,%d,%.2f)".format(
        locale = Locale.US,
        (255 * this.red).roundToInt(),
        (255 * this.green).roundToInt(),
        (255 * this.blue).roundToInt(),
        this.alpha,
    )

private fun CornerBasedShape.toCssBorderRadius(): String {
    val density = Density(1F)
    val size = Size(0F, 0F)
    val topLeft = topStart.toPx(size, density).roundToInt()
    val topRight = topEnd.toPx(size, density).roundToInt()
    val bottomLeft = bottomStart.toPx(size, density).roundToInt()
    val bottomRight = bottomEnd.toPx(size, density).roundToInt()

    return "${topLeft}px ${topRight}px ${bottomRight}px ${bottomLeft}px"
}
