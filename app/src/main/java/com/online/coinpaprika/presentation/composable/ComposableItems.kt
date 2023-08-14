package com.online.coinpaprika.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun LogoImage(imageUrl: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
fun Details(
    content: String, style: TextStyle = MaterialTheme.typography.subtitle1,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = content,
        textAlign = textAlign,
        fontWeight = FontWeight.Normal,
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )

}
@Composable
fun DetailsSession(content: String, style: TextStyle = MaterialTheme.typography.subtitle1,
                   textAlign: TextAlign = TextAlign.Start) {
    Details(content, style = style, textAlign = textAlign)
    Divider(startIndent = 2.dp, thickness = 1.dp, color = Color.LightGray)

}


@Composable
fun ShowSnackBar(snackbarHostState: SnackbarHostState, message: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(key1 = snackbarHostState) {
                snackbarHostState.showSnackbar(message)
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
@Composable
fun LoadProgressbar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}