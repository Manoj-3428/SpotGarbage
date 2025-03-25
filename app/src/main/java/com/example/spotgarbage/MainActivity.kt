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
import com.example.spotgarbage.authentication.signUp
import com.example.spotgarbage.dataclasses.Complaint
import com.example.spotgarbage.dataclasses.Profiles
import com.example.spotgarbage.sample.SimpleLoadingBar
import com.example.spotgarbage.starter.splash1
import com.example.spotgarbage.starter.splash2
import com.example.spotgarbage.view.DetailScreen
import com.example.spotgarbage.view.Home
import com.example.spotgarbage.view.MyDetailScreen
import com.example.spotgarbage.view.MyPosts
import com.example.spotgarbage.view.WorkDoneScreen
import com.example.spotgarbage.view.addComplaint
import com.example.spotgarbage.view.adminHome
import com.example.spotgarbage.view.profile
import com.example.spotgarbage.viewmodel.ComplaintViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.spotgarbage.authentication.login
import com.example.spotgarbage.onboardingscreens.OnboardingScreen
import com.example.spotgarbage.sample.LottieAnimation
import com.example.spotgarbage.ui.MapsScreen
import com.example.spotgarbage.view.TrackComplaints
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val complaintViewModel: ComplaintViewModel = viewModel()
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            var startDestination by remember { mutableStateOf<String?>(null) }
            val context = LocalContext.current

            LaunchedEffect(userId) {
                if (userId != null) {
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            val user = document.toObject(Profiles::class.java)
                            startDestination = if (user?.role == "admin") "adminHome" else "Home"
                        }
                        .addOnFailureListener {
                            startDestination = "Home"
                        }
                } else {
                    startDestination = "onBoarding"
                }
            }
            if (startDestination != null) {
                NavHost(navController = navController, startDestination = startDestination!!) {
                    composable("signUp") { signUp(navController) }
                    composable("login") { login(navController) }
                    composable("splash1") { splash1(navController) }
                    composable("splash2") { splash2(navController) }
                    composable("Home") { Home(navController, complaintViewModel) }
                    composable("profile") {
                        profile(navController)
                    }
                    composable("addComplaint") { addComplaint(navController) }
                    composable("DetailScreen") {
                        val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                        DetailScreen(complaint, navController)
                    }
                    composable("WorkDoneScreen") {
                        val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                        WorkDoneScreen(complaint, navController)
                    }
                    composable("MyDetailScreen") {
                        val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                        MyDetailScreen(complaint, navController)
                    }
                    composable("TrackComplaint") {
                        val complaint = navController.previousBackStackEntry?.savedStateHandle?.get<Complaint>("complaint")
                        TrackComplaints(navController, complaintViewModel)
                    }
                    composable("MyPosts") { MyPosts(navController, complaintViewModel) }
                    composable("SimpleLoading") { SimpleLoadingBar() }
                    composable("adminHome") { adminHome(navController, complaintViewModel) }
                    composable("onBoarding") {
                        OnboardingScreen(navController)
                    }
                    composable("MapScreen"){
                        MapsScreen()
                    }
                    composable("LottieAnimation") {
                        LottieAnimation()
                    }
                }
            }
        }

    }
}
