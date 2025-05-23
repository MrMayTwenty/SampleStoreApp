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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.presentation.components.ErrorView
import com.aaa.samplestore.presentation.components.HeaderView
import com.aaa.samplestore.presentation.components.LoadingView
import com.aaa.samplestore.presentation.components.SaleBadge
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
    val addToCartState = viewModel.addToCartState.value
    val currentUserId = viewModel.getCurrentUserId()
    val shouldShowPurchaseSuccessDialog = viewModel.purchaseSuccessState.value


    LaunchedEffect(Unit) {
        if(productsState.data.isNullOrEmpty()){
            viewModel.getAllProducts(1)
        }
    }

    if(shouldShowPurchaseSuccessDialog){
        AlertDialog(
            onDismissRequest = {
                viewModel.disablePurchaseSuccessState()
            },
            title = {
                Text(text = stringResource(R.string.thank_you))
            },
            text = {
                Text(text = stringResource(R.string.product_successfully_purchased))
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.disablePurchaseSuccessState()
                }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            HeaderView(
                onSearch = { query ->  },
                onCartClick = onCartClick,
                onProfileClick = onProfileClick,
                shouldShowSearch = false,
                shouldShowProfile = true,
                shouldShowCart = true
            )
        },
        bottomBar = { BottomFilter(
            selectedFilter = selectedFilter.value,
            onFilterSelected = {
                when(it) {
                    Constants.ProductFilter.ALL -> {
                        viewModel.showAllProducts()
                    }
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

                categories.forEach { (category, resId) ->
                    PillButton(
                        text = stringResource(resId),
                        selected = selectedCategory.value == category.value,
                        onClick = { viewModel.showProductsByCategory(category)},
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
                    onAddToCart = { product -> viewModel.addToCart(product,currentUserId) }
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
fun ProductGrid(
    products: List<Product>,
    onProductClick: (Int) -> Unit,
    onAddToCart: (Product) -> Unit
) {

    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        state = listState
    ) {
        items(products) { product ->
            ProductItem(product = product,
                onClick = { onProductClick(product.id) },
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
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

            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(3.dp))
                product.discount?.let {
                    SaleBadge(product.discount)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { onAddToCart(product) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_shopping_cart_24),
                    contentDescription = "Add to cart",
                )
            }
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
                        Constants.ProductFilter.ALL -> Icon(painterResource(R.drawable.baseline_shopping_bag_24), contentDescription = "All")
                        Constants.ProductFilter.POPULAR -> Icon(imageVector = Icons.Default.Star, contentDescription = "Popular")
                        Constants.ProductFilter.SALE -> Icon(painterResource(R.drawable.baseline_money_off_24), contentDescription = "Sale")
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