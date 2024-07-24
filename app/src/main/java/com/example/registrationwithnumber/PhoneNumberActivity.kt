package com.example.registrationwithnumber

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrationwithnumber.ui.theme.RegistrationWithNumberTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneNumberActivity : ComponentActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val errorMessage = remember { mutableStateOf("") }
            val phoneNumber = remember { mutableStateOf("") }

            RegistrationWithNumberTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PhoneNumberField(phoneNumber)
                        Spacer(modifier = Modifier.height(20.dp))
                        SendOtpButton {
                            if (phoneNumber.value.isNotEmpty()) {
                                send(phoneNumber.value, errorMessage)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        if (errorMessage.value.isNotEmpty()) {
                            Text(
                                text = errorMessage.value,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

    private fun send(mobileNumber: String, errorMessage: MutableState<String>) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+998$mobileNumber")
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(errorM: FirebaseException) {
                    errorMessage.value = errorM.message ?: "Verification failed"
                }

                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    Toast.makeText(applicationContext,"Code sent",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@PhoneNumberActivity, VerifyActivity::class.java)
                    intent.putExtra("verificationOtp", otp)
                    intent.putExtra("mobileNumber", mobileNumber)
                    startActivity(intent)
                    finish()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

@Composable
fun PhoneNumberField(phoneNumber: MutableState<String>) {
    OutlinedTextField(
        value = phoneNumber.value,
        onValueChange = { phoneNumber.value = it },
        label = { Text(text = "Phone Number") },
        placeholder = { Text(text = "Phone Number") },
        leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Number") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun SendOtpButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(45.dp)
    ) {
        Text("Send OTP", fontSize = 15.sp, color = Color.White)
    }
}
