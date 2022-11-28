package com.example.cakelist.ui.cakes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cakelist.R
import com.example.cakelist.models.Cake
import com.example.cakelist.sealed.DataState

@Composable
fun CakesScreen(
    viewModel: CakesViewModel = hiltViewModel(),
    navigationToDetailsScreen: (String) -> Unit
) {
    val cakes by viewModel.cakesState.collectAsState()

    val listState = rememberLazyListState()

    CakesScreenContent(
        dataState = cakes,
        query = viewModel.query.value,
        onQueryChanged =
        { value: String -> viewModel.onQueryChanged(value) },
        changeSelection =
        { viewModel.changeSelection() },
        isButtonSelected = viewModel.selected.value,
        countQuery =
        { list: List<Cake> -> viewModel.countQuery(list) },
        amountResults = viewModel.results.value,
        changePositionListState =
        { viewModel.changePositionListState(firstItem = listState.firstVisibleItemIndex) },
        positionListState = viewModel.positionListState.value,
        navigationToDetailsScreen =
        {
            navigationToDetailsScreen(it.title)
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Searchbar(
    query: String,
    onQueryChanged: (String) -> Unit,
    changePositionListState: (Int) -> Unit,
    positionListState: LazyListState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = { newValue ->
            onQueryChanged(newValue.trimStart())
            changePositionListState(positionListState.firstVisibleItemIndex)
        },
        leadingIcon = { Icon(Icons.Filled.Search, stringResource(R.string.search_icon)) },
        trailingIcon = {
            if (query.isNotEmpty())
                Icon(
                    Icons.Filled.Clear, "Clear icon", modifier = Modifier
                        .clickable {
                            onQueryChanged("")
                            changePositionListState(positionListState.firstVisibleItemIndex)
                        })
        },
        singleLine = true,
        label = { Text(stringResource(R.string.find_a_cake)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onQueryChanged(query.trim())
                changePositionListState(positionListState.firstVisibleItemIndex)
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = colorResource(id = R.color.subtitle_gray),
            unfocusedLabelColor = Color.Black,
            leadingIconColor = Color.Black,
            trailingIconColor = Color.Black,
            focusedIndicatorColor = colorResource(id = R.color.subtitle_gray)
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
    )
}

@Composable
fun CakesScreenContent(
    dataState: DataState<List<Cake>, String>,
    query: String,
    onQueryChanged: (String) -> Unit,
    changeSelection: () -> Unit,
    isButtonSelected: Boolean,
    countQuery: (List<Cake>) -> Unit,
    amountResults: Int,
    changePositionListState: (Int) -> Unit,
    positionListState: LazyListState,
    navigationToDetailsScreen: (Cake) -> Unit,
) {
    when (dataState) {
        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DataState.Success<List<Cake>> -> {
            CakesLazyList(
                cakes = dataState.data,
                query = query,
                onQueryChanged = onQueryChanged,
                changeSelection = changeSelection,
                isButtonSelected = isButtonSelected,
                countQuery = countQuery,
                amountResults = amountResults,
                changePositionListState = changePositionListState,
                positionListState = positionListState,
                navigationToDetailsScreen = navigationToDetailsScreen
            )
        }
        is DataState.Failure<String> -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dataState.message,
                    fontSize = MaterialTheme.typography.h5.fontSize
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.error_fetching_data),
                    fontSize = MaterialTheme.typography.h5.fontSize
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CakesLazyList(
    cakes: List<Cake>,
    query: String,
    onQueryChanged: (String) -> Unit,
    changeSelection: () -> Unit,
    isButtonSelected: Boolean,
    countQuery: (List<Cake>) -> Unit,
    amountResults: Int,
    changePositionListState: (Int) -> Unit,
    positionListState: LazyListState,
    navigationToDetailsScreen: (Cake) -> Unit
) {
    countQuery(cakes)

    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 30.dp)
        ) {
            val cakesSorted = if (isButtonSelected) cakes.reversed() else cakes
            Text(
                text = stringResource(id = R.string.results, amountResults),
                color = Color.Black,
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 24.dp, start = 12.dp)
                    .align(alignment = Alignment.Start)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val color = if (isButtonSelected) Color.Blue else Color.Red
                val text =
                    if (isButtonSelected) stringResource(R.string.sort_z_a)
                    else stringResource(R.string.sort_a_z)
                val textColor =
                    if (isButtonSelected) Color.White
                    else Color.Black

                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                Box(
                    modifier = Modifier.weight(8f)
                ) {
                    Searchbar(
                        query, onQueryChanged, changePositionListState, positionListState
                    )
                }
                Button(
                    onClick = {
                        changeSelection()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        changePositionListState(positionListState.firstVisibleItemIndex)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = color),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 4.dp)
                        .weight(2f)
                ) {
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }
            }
            LazyColumn(
                state = positionListState,
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                items(items = cakesSorted) { cake ->
                    if (cake.title.startsWith(query)) {
                        CakeCard(
                            cake = cake,
                            navigationToDetailsScreen = navigationToDetailsScreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CakeCard(
    cake: Cake,
    navigationToDetailsScreen: (Cake) -> Unit
) {
    Card(
        shape = RectangleShape,
        border = BorderStroke(4.dp, Color.Black),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.animateContentSize()) {
            Box(
                modifier = Modifier
                    .weight(0.35f)
            ) {
                cake.image?.let { ImageWithBorder(it) }
            }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .align(Alignment.CenterVertically)
                    .weight(0.65f)
                    .padding(16.dp)
            ) {
                cake.title.uppercase().let {
                    Text(
                        text = it,
                        color = Color.Black,
                        style = MaterialTheme.typography.h2
                    )
                }
                Text(
                    text = stringResource(R.string.read_more),
                    color = colorResource(id = R.color.read_more_orange),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.clickable {
                        navigationToDetailsScreen(cake)
                    }
                )
            }
        }
    }
}

@Composable
private fun ImageWithBorder(imageString: String) {
    imageString.let {
        Card(
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
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
