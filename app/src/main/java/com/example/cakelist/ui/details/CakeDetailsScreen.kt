package com.example.cakelist.ui.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cakelist.R
import com.example.cakelist.models.Cake

@Composable
fun CakeDetailsScreen(navController: NavHostController) {
    val viewModel: CakeDetailsViewModel = hiltViewModel()
    val cake = viewModel.cakeState.value!!
    CakeDetailsScreenContent(cake, navController)
}

@Composable
fun CakeDetailsScreenContent(
    cake: Cake,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .padding(bottom = 28.dp)
    ) {
        Column {
            TopAppBar(
                backgroundColor = Color.Transparent,
                contentColor = Color.Black,
                elevation = 0.dp
            ) {
                Row {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "backIcon",
                                tint = Color.Black
                            )
                        }
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "closeIcon",
                                tint = Color.Black
                            )
                        }
                    )
                }
            }
            Divider(
                color = Color.Black,
                thickness = 4.dp
            )
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                CakeDetailsHeader(cake = cake)
            }
        }
    }
}

@Composable
private fun CakeDetailsHeader(cake: Cake) {
    Row(
        modifier = Modifier
            .animateContentSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.35f)
        ) {
            cake.image?.let { ImageWithBorderAndFourRoundedCorners(imageString = it) }
        }
        Column(
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.CenterVertically)
                .weight(0.65f)
                .padding(start = 20.dp)
        ) {
            Text(
                text = cake.title,
                color = Color.Black,
                style = MaterialTheme.typography.h2,
            )
            cake.desc?.let { description ->
                Text(
                    text = description,
                    color = colorResource(id = R.color.subtitle_gray),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@Composable
private fun ImageWithBorderAndFourRoundedCorners(imageString: String) {
    imageString.let {
        Card(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(4.dp, Color.Black),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = stringResource(R.string.cake_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }
}
