package com.socketChat.app.presentation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.socketChat.app.presentation.screens.ChatScreen
import com.socketChat.app.presentation.screens.ConversationScreen

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "conversation") {

        composable("conversation") {
            ConversationScreen(
                onBotClick = { botName ->
                    navController.navigate("chat/$botName")
                }
            )
        }

        composable(
            route = "chat/{botName}",
            arguments = listOf(navArgument("botName") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val botName = backStackEntry.arguments?.getString("botName") ?: "UnknownBot"
            ChatScreen(
                botName = botName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
