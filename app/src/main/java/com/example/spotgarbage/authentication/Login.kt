package com.example.spotgarbage.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotgarbage.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(navController: NavController) {
    val emailTextState = remember { mutableStateOf("") }
    val passowrdTextState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var role = remember { mutableStateOf("") }
    val keyboardController= LocalSoftwareKeyboardController.current
    var auth= FirebaseAuth.getInstance()
    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier.wrapContentSize().padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.app_icon), contentDescription = "App icon",
                    modifier = Modifier.height(150.dp).width(200.dp).padding(10.dp)
                )
                Text(
                    text = "Spot the garbage",
                    color = colorResource(R.color.primary_light),
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                    fontSize = 24.sp
                )
                Text(
                    text = "Lets be responsible",
                    color = colorResource(R.color.black),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                Text(
                    text = "Login To Your Account",
                    color = colorResource(R.color.secondary_light),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier.wrapContentSize().padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = emailTextState.value,
                    onValueChange = { emailTextState.value = it },
                    label = {
                        Text(text = "Email", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.mail),
                            contentDescription = "mail box", modifier = Modifier.size(24.dp)
                        )

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // Hides keyboard when 'Done' is pressed
                        }
                    ),
                    modifier = Modifier.wrapContentSize()
                        .padding(start = 25.dp, end = 25.dp, top = 10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light), // focused border color
                        unfocusedBorderColor = colorResource(R.color.black), // unfocused border color
                        focusedLabelColor = colorResource(R.color.secondary_light), // focused label color
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )
                )
                OutlinedTextField(
                    value = passowrdTextState.value,
                    onValueChange = { passowrdTextState.value = it },
                    label = { Text(text = "password", color = Color.Gray) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier.wrapContentSize()
                        .padding(top = 15.dp, end = 25.dp, start = 25.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.lock),
                            contentDescription = "Lock"
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.secondary_light),
                        unfocusedLabelColor = colorResource(R.color.black),
                        focusedLabelColor = colorResource(R.color.secondary_light),
                        unfocusedBorderColor = colorResource(R.color.black),
                        focusedLeadingIconColor = colorResource(R.color.secondary_light),
                        unfocusedLeadingIconColor = colorResource(R.color.black)
                    )


                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(end = 30.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val isChecked = ToggleSwitch()
                    if (isChecked) {
                        Text(
                            text = "I am admin",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 0.dp)
                        )
                        role.value = "admin"
                    } else {
                        Text(
                            text = "I am user",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 0.dp)
                        )
                        role.value = "user"
                    }
                }
                Button(
                    onClick = {
                        if (emailTextState.value.isEmpty() || passowrdTextState.value.isEmpty()) {
                            Toast.makeText(context, "Enter email and password", Toast.LENGTH_LONG)
                                .show()

                        } else {
                            loginUser(
                                role.value,
                                emailTextState.value,
                                passowrdTextState.value
                            ) { result ->
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                                val auth = FirebaseAuth.getInstance()
                                val user = auth.currentUser
                                if (result == "success" && role.value == "user") {
                                    navController.navigate("Home") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_LONG)
                                        .show()
                                } else if (result == "success" && role.value == "admin") {
                                    navController.navigate("adminHome") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_LONG)
                                        .show()
                                } else {
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                                    Log.d("Big error", result)
                                    navController.navigate("login")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, top = 5.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.primary),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text(text = "Login")
                }
                Text(
                    text = "Don't have account?",
                    color = colorResource(R.color.black),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp).clickable(onClick = {
                        navController.navigate("signUp")
                    })
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "Powered by Manoj",
                        color = colorResource(R.color.secondary_light),
                        modifier = Modifier.padding(top=5.dp)
                    )

                }
            }

        }
    }
        }
