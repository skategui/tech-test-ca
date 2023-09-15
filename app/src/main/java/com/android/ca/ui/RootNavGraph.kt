import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.account.ui.AccountDetailScreen
import com.android.account.vm.AccountDetailViewModel
import com.android.banks.ui.BanksScreen
import com.android.common.ui.CreditAgricoleSplashAnimation
import com.android.common.ui.screens.Screens
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf


object Graph {
    const val ROOT = "root_graph"
}

/**
 * Nav graph, used for the navigation between composables
 */
@Composable
fun RootNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            CreditAgricoleSplashAnimation(navController = navController, modifier = Modifier)
        }

        composable(route = Screens.BanksScreen.route) {
            BanksScreen(navController = navController, modifier = Modifier)
        }
        composable(
            Screens.AccountDetailScreen.route + "/{accountId}",
            arguments = listOf(navArgument("accountId") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->

            val accountId = backStackEntry.arguments?.getString("accountId")
            val viewModel = getViewModel<AccountDetailViewModel>(parameters = {
                parametersOf(
                    accountId
                )
            })
            AccountDetailScreen(
                navController = navController, modifier = Modifier, viewModel = viewModel
            )
        }
    }
}