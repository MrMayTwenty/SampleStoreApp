package com.aaa.samplestore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aaa.samplestore.presentation.cart.CartScreen
import com.aaa.samplestore.presentation.cart.CartViewModel
import com.aaa.samplestore.presentation.checkout.CheckoutScreen
import com.aaa.samplestore.presentation.checkout.CheckoutViewModel
import com.aaa.samplestore.presentation.productdetail.ProductDetailScreen
import com.aaa.samplestore.presentation.productdetail.ProductDetailViewModel
import com.aaa.samplestore.presentation.productlist.ProductListScreen
import com.aaa.samplestore.presentation.productlist.ProductListViewModel
import com.aaa.samplestore.presentation.ui.theme.SampleStoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleStoreAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   NavHost(
                       navController = navController,
                       startDestination = Screen.ProductListScreen,
                       modifier = Modifier.padding(innerPadding)
                   ) {
                       composable<Screen.ProductListScreen> {
                           val viewModel = hiltViewModel<ProductListViewModel>()
                           ProductListScreen(viewModel,
                               onProductClick = { navController.navigate(Screen.ProductDetailScreen(it)) },
                               onCartClick = { navController.navigate(Screen.CartScreen) },
                           )
                       }

                       composable<Screen.ProductDetailScreen> { backStackEntry ->
                           val viewModel = hiltViewModel<ProductDetailViewModel>()
                           val productDetail: Screen.ProductDetailScreen = backStackEntry.toRoute()
                           ProductDetailScreen(viewModel,
                               productDetail.productId,
                               onBackClick = { },
                               onAddToCartClick = { },
                               onBuyNowClick = { }
                           )
                       }

                       composable<Screen.CartScreen> {
                           val viewModel = hiltViewModel<CartViewModel>()
                           CartScreen(viewModel,
                               onBackClick = { },
                               onProceedToCheckoutClick = { navController.navigate(Screen.CheckoutScreen) }
                           )
                       }

                       composable<Screen.CheckoutScreen> {
                           val viewModel = hiltViewModel<CheckoutViewModel>()
                           CheckoutScreen(viewModel,
                               onBackClick = { },
                               onCompletePurchaseClick = { }
                           )
                       }

                   }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleStoreAppTheme {
        Greeting("Android")
    }
}