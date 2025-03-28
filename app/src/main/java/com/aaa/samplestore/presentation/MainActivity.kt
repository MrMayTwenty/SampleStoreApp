package com.aaa.samplestore.presentation

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.invoke

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var authState: String? = null
    private lateinit var navigationViewModel: NavigationViewModel
    private val checkoutViewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            LaunchedEffect(checkoutViewModel.capturePaymentState.value) {
                checkoutViewModel.capturePaymentState.value.data?.let {
                    navigationViewModel.navController?.navigate(Screen.ProductListScreen) {
                        popUpTo(Screen.ProductListScreen) { inclusive = true }
                    }
                }
            }

            SampleStoreAppTheme {
                navigationViewModel = hiltViewModel<NavigationViewModel>()
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val navController = rememberNavController()
                navigationViewModel.navController = navController

                val snackBarHostState = remember {
                    SnackbarHostState()
                }
                val scope = rememberCoroutineScope()
                ObserverAsEvents(flow = SnackbarController.events, snackBarHostState) { event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        var result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Long
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackBarHostState
                        )
                    },) { innerPadding ->
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
                                onProfileClick = { navController.navigate(Screen.ProfileScreen) },
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
                                onLogoutSuccess = { navController.navigate(Screen.ProductListScreen)})
                        }

                        composable<Screen.WishlistScreen> {
                            val viewModel = hiltViewModel<WishlistViewModel>()
                            WishlistScreen(viewModel,
                                onProductClick = { productId -> navController.navigate(Screen.ProductDetailScreen(productId))})
                        }

                        composable<Screen.CardScreen>{ backStackEntry ->
                            val cardScreen: Screen.CardScreen = backStackEntry.toRoute()
                            val viewModel = hiltViewModel<CardViewModel>()
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
                is CardApproveOrderResult.Failure -> Toast.makeText(this, callback.error.message, Toast.LENGTH_LONG).show()
                is CardApproveOrderResult.Success ->
                    navigationViewModel.navController?.navigate(Screen.ProductListScreen) {
                        popUpTo(Screen.ProductListScreen) { inclusive = true }
                    }

            }
        }
    }

    fun presentAuthChallenge(authChallenge: CardAuthChallenge){
        val paypalConfig = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
        val cardClient = CardClient(this, paypalConfig)
        when(val result = cardClient.presentAuthChallenge(this,authChallenge)){
            is CardPresentAuthChallengeResult.Failure -> Toast.makeText(this, result.error.message, Toast.LENGTH_LONG).show()
            is CardPresentAuthChallengeResult.Success -> {
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
                checkoutViewModel.capturePayment(approveOrderResult.orderId)
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