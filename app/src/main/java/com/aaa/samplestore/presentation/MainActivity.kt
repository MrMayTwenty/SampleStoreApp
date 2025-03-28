package com.aaa.samplestore.presentation

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
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
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.presentation.card.CardScreen
import com.aaa.samplestore.presentation.card.CardViewModel
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
import com.aaa.samplestore.presentation.wishlist.WishlistScreen
import com.aaa.samplestore.presentation.wishlist.WishlistViewModel
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardAuthChallenge
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardFinishApproveOrderResult
import com.paypal.android.cardpayments.CardPresentAuthChallengeResult
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.Address
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var authState: String? = null
    private lateinit var navigationViewModel: NavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleStoreAppTheme {
                navigationViewModel = hiltViewModel<NavigationViewModel>()
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val navController = rememberNavController()
                navigationViewModel.navController = navController
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ProductListScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.ProductListScreen> {
                            val viewModel = hiltViewModel<ProductListViewModel>()
                            ProductListScreen(
                                viewModel,
                                onProductClick = {
                                    navController.navigate(
                                        Screen.ProductDetailScreen(
                                            it
                                        )
                                    )
                                },
                                onCartClick = { navController.navigate(Screen.CartScreen) },
                                onProfileClick = { navController.navigate(Screen.ProfileScreen) }
                            )
                        }

                        composable<Screen.ProductDetailScreen> { backStackEntry ->
                            val viewModel = hiltViewModel<ProductDetailViewModel>()
                            val productDetail: Screen.ProductDetailScreen = backStackEntry.toRoute()
                            ProductDetailScreen(
                                viewModel,
                                productDetail.productId,
                                onBackClick = { },
                                onAddToCartClick = { },
                                onBuyNowClick = { navController.navigate(Screen.CartScreen) },
                                onProfileClick = { navController.navigate(Screen.ProfileScreen) },
                                onCartClick = { navController.navigate(Screen.CartScreen) }
                            )
                        }

                        composable<Screen.CartScreen> {
                            val viewModel = hiltViewModel<CartViewModel>()
                            CartScreen(
                                viewModel,
                                onBackClick = { },
                                onProceedToCheckoutClick = {
                                    if (loginViewModel.isLoggedIn()) {
                                        navController.navigate(Screen.CheckoutScreen)
                                    } else {
                                        navController.navigate(Screen.LoginScreen)
                                    }
                                }
                            )
                        }

                        composable<Screen.CheckoutScreen> {
                            val viewModel = hiltViewModel<CheckoutViewModel>()
                            CheckoutScreen(
                                viewModel,
                                onGetPayPalOrderSuccess = { orderId ->
//                                   checkOut(orderId = orderId)
                                    navController.navigate(Screen.CardScreen(orderId))
                                }
                            )
                        }

                        composable<Screen.LoginScreen> {
                            val viewModel = loginViewModel
                            LoginScreen(
                                viewModel,
                                onLoginSuccess = { navController.navigate(Screen.ProductListScreen) },
                                onRegisterClick = { navController.navigate(Screen.RegisterScreen) }
                            )
                        }

                        composable<Screen.RegisterScreen> {
                            val viewModel = hiltViewModel<RegisterViewModel>()
                            RegisterScreen(
                                viewModel,
                                onRegisterSuccess = { navController.navigate(Screen.ProductListScreen) }
                            )
                        }

                        composable<Screen.ProfileScreen> {
                            val viewModel = hiltViewModel<ProfileViewModel>()
                            ProfileScreen(
                                viewModel,
                                onLoginClick = { navController.navigate(Screen.LoginScreen) },
                                onRegisterClick = { navController.navigate(Screen.RegisterScreen)},
                                onWishListClick = { navController.navigate(Screen.WishlistScreen)},
                                onLogoutSuccess = {})
                        }

                        composable<Screen.WishlistScreen> {
                            val viewModel = hiltViewModel<WishlistViewModel>()
                            WishlistScreen(viewModel,
                                onProductClick = { productId -> navController.navigate(Screen.ProductDetailScreen(productId))})
                        }

                        composable<Screen.CardScreen>{ backStackEntry ->
                            val viewModel = hiltViewModel<CardViewModel>()
                            val cardScreen: Screen.CardScreen = backStackEntry.toRoute()
                            CardScreen(viewModel,cardScreen.orderId, onRequestApproveOrder = { cardRequest ->
                                checkOut(cardRequest)
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkForCardAuthCompletion(intent)
    }

    override fun onResume() {
        super.onResume()
        checkForCardAuthCompletion(intent)
    }

    fun checkOut(cardRequest: CardRequest){
        val paypalConfig = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
        val cardClient = CardClient(this, paypalConfig)
        cardClient.approveOrder(cardRequest) { callback ->
            when(callback){
                is CardApproveOrderResult.AuthorizationRequired -> presentAuthChallenge(authChallenge = callback.authChallenge)
                is CardApproveOrderResult.Failure -> TODO("Handle approve order failure")
                is CardApproveOrderResult.Success -> TODO("Capture or authorize order on server")
            }
        }
    }

    fun presentAuthChallenge(authChallenge: CardAuthChallenge){
        val paypalConfig = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
        val cardClient = CardClient(this, paypalConfig)
        when(val result = cardClient.presentAuthChallenge(this,authChallenge)){
            is CardPresentAuthChallengeResult.Failure -> TODO("Handle Present Auth Challenge Failure")
            is CardPresentAuthChallengeResult.Success -> {
                // Capture auth state for balancing call to finishApproveOrder()/finishVault() when
                // the merchant application re-enters the foreground
                authState = result.authState
            }
        }
    }

    fun checkForCardAuthCompletion(intent: Intent) = authState?.let { state ->
        val paypalConfig = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
        val cardClient = CardClient(this, paypalConfig)
        when (val approveOrderResult = cardClient.finishApproveOrder(intent,state)){
            is CardFinishApproveOrderResult.Canceled -> authState = null
            is CardFinishApproveOrderResult.Failure -> authState = null
            is CardFinishApproveOrderResult.NoResult -> authState = null
            is CardFinishApproveOrderResult.Success -> {
                //Authorize Order
                navigationViewModel.navController?.navigate(Screen.ProductListScreen) {
                    popUpTo(Screen.ProductListScreen) { inclusive = true }
                }
                authState = null
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