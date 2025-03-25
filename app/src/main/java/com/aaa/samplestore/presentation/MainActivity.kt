package com.aaa.samplestore.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aaa.samplestore.presentation.cart.CartScreen
import com.aaa.samplestore.presentation.cart.CartViewModel
import com.aaa.samplestore.presentation.checkout.CheckoutScreen
import com.aaa.samplestore.presentation.checkout.CheckoutViewModel
import com.aaa.samplestore.presentation.login.LoginScreen
import com.aaa.samplestore.presentation.login.LoginViewModel
import com.aaa.samplestore.presentation.productdetail.ProductDetailScreen
import com.aaa.samplestore.presentation.productdetail.ProductDetailViewModel
import com.aaa.samplestore.presentation.productlist.ProductListScreen
import com.aaa.samplestore.presentation.productlist.ProductListViewModel
import com.aaa.samplestore.presentation.profile.ProfileScreen
import com.aaa.samplestore.presentation.profile.ProfileViewModel
import com.aaa.samplestore.presentation.register.RegisterScreen
import com.aaa.samplestore.presentation.register.RegisterViewModel
import com.aaa.samplestore.presentation.ui.theme.SampleStoreAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleStoreAppTheme {
                val loginViewModel = hiltViewModel<LoginViewModel>()
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
                               onProfileClick = { navController.navigate(Screen.ProfileScreen) }
                           )
                       }

                       composable<Screen.ProductDetailScreen> { backStackEntry ->
                           val viewModel = hiltViewModel<ProductDetailViewModel>()
                           val productDetail: Screen.ProductDetailScreen = backStackEntry.toRoute()
                           ProductDetailScreen(viewModel,
                               productDetail.productId,
                               onBackClick = { },
                               onAddToCartClick = { },
                               onBuyNowClick = { navController.navigate(Screen.CartScreen) }
                           )
                       }

                       composable<Screen.CartScreen> {
                           val viewModel = hiltViewModel<CartViewModel>()
                               CartScreen(viewModel,
                                   onBackClick = { },
                                   onProceedToCheckoutClick = {
                                       if(loginViewModel.isLoggedIn()){
                                           navController.navigate(Screen.CheckoutScreen)
                                       }else{
                                           navController.navigate(Screen.LoginScreen)
                                       }
                                   }
                               )
                       }

                       composable<Screen.CheckoutScreen> {
                           val viewModel = hiltViewModel<CheckoutViewModel>()
                           CheckoutScreen(viewModel,
                               onPaymentSuccess = { navController.navigate(Screen.ProductListScreen) }
                           )
                       }

                       composable<Screen.LoginScreen>{
                           val viewModel = loginViewModel
                           LoginScreen(viewModel,
                               onLoginSuccess = { navController.navigate(Screen.ProductListScreen) },
                               onRegisterClick = { }
                           )
                       }

                       composable<Screen.RegisterScreen> {
                           val viewModel = hiltViewModel<RegisterViewModel>()
                           RegisterScreen(viewModel,
                               onRegisterSuccess = { navController.navigate(Screen.LoginScreen) }
                           )
                       }

                       composable<Screen.ProfileScreen>{
                            val viewModel = hiltViewModel<ProfileViewModel>()
                           ProfileScreen(viewModel,
                               onLoginClick = {},
                               onRegisterClick = {},
                               onWishListClick = {},
                               onLogoutSuccess = {})
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