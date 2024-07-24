package com.example.registrationwithnumber

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrationwithnumber.ui.theme.RegistrationWithNumberTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class VerifyActivity : ComponentActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var verificationOtp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificationOtp = intent.getStringExtra("verificationOtp") ?: ""

        setContent {
            val errorMessage = remember { mutableStateOf("") }
            val otpVal = remember { mutableStateOf("") }

            RegistrationWithNumberTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Enter the OTP", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        OTPTextFields(length = 6) { otp ->
                            otpVal.value = otp
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {
                                if (otpVal.value.isNotEmpty()) {
                                    otpVerification(otpVal.value, errorMessage)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(45.dp)
                        ) {
                            Text("Verify OTP", fontSize = 15.sp, color = Color.White)
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

    private fun otpVerification(otp: String, errorMessage: MutableState<String>) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, otp)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    errorMessage.value = "Verification Successful"
                    val intent = Intent(this@VerifyActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    errorMessage.value = "Code Wrong"
                }
            }
    }
}
