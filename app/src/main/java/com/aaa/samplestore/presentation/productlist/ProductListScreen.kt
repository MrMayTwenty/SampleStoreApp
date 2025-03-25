package com.aaa.samplestore.presentation.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.presentation.components.ErrorView
import com.aaa.samplestore.presentation.components.LoadingView

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val productsState = viewModel.productsState.value
    val selectedFilter = viewModel.selectedFilter.value
    val selectedCategory = viewModel.selectedCategory.value


    LaunchedEffect(Unit) {
        if(productsState.data.isNullOrEmpty()){
            viewModel.getAllProducts(1)
        }
    }

    Scaffold(
        topBar = { TopBar(
            onSearch = { query ->  },
            onCartClick = onCartClick,
            onProfileClick = onProfileClick
        ) },
        bottomBar = { BottomFilter(
            selectedFilter = selectedFilter.value,
            onFilterSelected = {
                when(it) {
                    Constants.ProductFilter.ALL -> viewModel.showAllProducts()
                    Constants.ProductFilter.POPULAR -> viewModel.showPopularProducts()
                    Constants.ProductFilter.SALE -> viewModel.showSaleProducts()
                }
            }
        )}
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val categories = listOf(
                    Constants.ProductCategory.ALL to R.string.all,
                    Constants.ProductCategory.TV to R.string.tv,
                    Constants.ProductCategory.AUDIO to R.string.audio,
                    Constants.ProductCategory.LAPTOP to R.string.laptop,
                    Constants.ProductCategory.GAMING to R.string.gaming,
                    Constants.ProductCategory.MOBILE to R.string.mobile,
                    Constants.ProductCategory.APPLIANCES to R.string.appliances)

                categories.forEach { (filter, resId) ->
                    PillButton(
                        text = stringResource(resId),
                        selected = selectedFilter.value == filter.value,
                        onClick = { viewModel.showProductsByCategory(filter)},
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

            }


            if(productsState.isLoading) {
                LoadingView()
            }

            if(productsState.error != null) {
                ErrorView(productsState.error)
            }

            if(!productsState.data.isNullOrEmpty()) {
                ProductGrid(
                    products = productsState.data,
                    onProductClick = onProductClick,
                )
            }


//            BottomNavBar(
//                selectedCategory = selectedCategory,
//                onCategorySelected = { selectedCategory = it }
//            )
        }
    }


}

@Composable
fun PillButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun TopBar(
    onSearch: (String) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            placeholder = { Text("Search...") },
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onCartClick) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
        }

        IconButton(onClick = onProfileClick) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Account")
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    onProductClick: (Int) -> Unit
) {

    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        state = listState
    ) {
        items(products) { product ->
            ProductItem(product = product, onClick = { onProductClick(product.id) })
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}


@Composable
fun BottomFilter(
    selectedFilter: String,
    onFilterSelected: (Constants.ProductFilter) -> Unit
) {
    val filters = listOf(Constants.ProductFilter.ALL to R.string.all, Constants.ProductFilter.POPULAR to R.string.popular, Constants.ProductFilter.SALE to R.string.sale)

    Row {
        filters.forEach { (filter, resId) ->
            FilterItem(
                icon = {
                    when (filter) {
                        Constants.ProductFilter.ALL -> Icon(imageVector = Icons.Default.Home, contentDescription = "All")
                        Constants.ProductFilter.POPULAR -> Icon(imageVector = Icons.Default.Star, contentDescription = "Popular")
                        Constants.ProductFilter.SALE -> Icon(imageVector = Icons.Default.DateRange, contentDescription = "Sale")
                    }
                },
                label = { Text(stringResource(resId)) },
                selected = selectedFilter == filter.value,
                onClick = { onFilterSelected(filter) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FilterItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        label()
    }
}