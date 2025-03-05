package com.example.spotgarbage
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotgarbage.authentication.login
import com.example.spotgarbage.authentication.signUp
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.sample.SimpleLoadingBar
import com.example.spotgarbage.starter.splash1
import com.example.spotgarbage.starter.splash2
import com.example.spotgarbage.view.DetailScreen
import com.example.spotgarbage.view.Home
import com.example.spotgarbage.view.MyDetailScreen
import com.example.spotgarbage.view.MyPosts
import com.example.spotgarbage.view.addComplaint
import com.example.spotgarbage.view.profile
import com.example.spotgarbage.viewmodel.ComplaintViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val complaintViewModel: ComplaintViewModel = viewModel()

            NavHost(navController = navController, startDestination = "Home") {
                composable("signUp") {
                    signUp(navController)
                }
                composable("login") {
                    login(navController)
                }
                composable("splash1"){
                    splash1(navController)
                }
                composable("splash2"){
                    splash2(navController)
                }
                composable("Home"){
                    Home(navController,complaintViewModel)
                }
                composable("profile"){
                    profile()
                }
                composable("addComplaint"){
                    addComplaint(navController)
                }
                composable("DetailScreen") { backStackEntry ->
                    val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                    DetailScreen(complaint,navController)
                }
                composable("MyDetailScreen"){
                    val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                    MyDetailScreen(complaint,navController)
                }
                composable("MyPosts"){
                    MyPosts(navController,complaintViewModel)
                }
                composable("SimpleLoading"){
                    SimpleLoadingBar()
                }
            }

        }
    }
}
