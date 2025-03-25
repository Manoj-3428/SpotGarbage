package com.example.spotgarbage.authentication

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotgarbage.R
import com.example.spotgarbage.dataclasses.Profiles
import com.example.spotgarbage.ui.theme.primary_dark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signUp(navController: NavController) {
    var password = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var context = LocalContext.current
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var role = remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxSize()) {it->


        Column(
            modifier = Modifier.fillMaxSize().imePadding().verticalScroll(scrollState).imePadding().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.4f))
            Image(
                painter = painterResource(R.drawable.app_icon), contentDescription = "App icon",
                modifier = Modifier.height(150.dp).width(200.dp).padding(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Spot the garbage",
                color = colorResource(R.color.primary_light),
                modifier = Modifier.padding(bottom = 6.dp, top = 5.dp),
                fontSize = 24.sp
            )
            Text(
                text = "Lets be responsible",
                color = colorResource(R.color.black),
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(modifier = Modifier.weight(0.25f))
            Text(
                text = "Create Your Account",
                color = colorResource(R.color.secondary_light),
                modifier = Modifier.padding(top = 8.dp, bottom = 6.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.weight(0.25f))
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = {
                    Text(text = "name", color = Color.Gray)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.plus_black),
                        contentDescription = "mail box"
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
                modifier = Modifier.wrapContentSize().fillMaxWidth()
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
                value = email.value,
                onValueChange = { email.value = it },
                singleLine = true,
                label = { Text(text = "Email", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mail),
                        contentDescription = "mail box", modifier = Modifier.size(24.dp)
                    )

                },
                modifier = Modifier.wrapContentSize().fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp, top = 10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.secondary_light),
                    unfocusedBorderColor = colorResource(R.color.black),
                    focusedLabelColor = colorResource(R.color.secondary_light),
                    unfocusedLabelColor = colorResource(R.color.black),
                    focusedLeadingIconColor = colorResource(R.color.secondary_light),
                    unfocusedLeadingIconColor = colorResource(R.color.black)
                )
            )
            var passwordVisible = remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password", color = Color.Gray) },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp, top = 10.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.lock),
                        contentDescription = "Lock"
                    )
                },
                trailingIcon = {
                    val icon =
                        if (passwordVisible.value) R.drawable.showpass else R.drawable.hidepass
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                        )
                    }
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
            Spacer(modifier = Modifier.height(10.dp))
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
            Spacer(modifier = Modifier.weight(1f))
            var isLoading by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    if (email.value.isEmpty() || password.value.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        createUser(
                            context,
                            role.value, name.value, email.value, password.value
                        ) { result ->
                            isLoading = false
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            if (result == "success" && role.value == "user") {
                                navController.navigate("Home") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else if (result == "success" && role.value == "admin") {
                                navController.navigate("adminHome") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                                if (password.value.length < 7) {
                                    Toast.makeText(
                                        context,
                                        "Password length must be at least 7",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, top = 5.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = colorResource(R.color.white)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = primary_dark,
                        modifier = Modifier.size(44.dp)
                    )
                } else {
                    Text(text = "Create Account")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Already have account?",
                color = colorResource(R.color.black),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 10.dp).clickable(onClick = {
                    navController.navigate("login")
                })
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Powered by Manoj",
                color = colorResource(R.color.secondary_light),
                modifier = Modifier.padding(bottom = 20.dp)
            )

        }
    }
}

@Composable
fun ToggleSwitch() : Boolean{
    var isChecked = remember { mutableStateOf(false) }

    Switch(
        checked = isChecked.value,
        onCheckedChange = { isChecked.value = it },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = primary_dark,
            uncheckedThumbColor = Color.Black,
            uncheckedTrackColor = Color.LightGray

        ),
            )
    return isChecked.value
}

