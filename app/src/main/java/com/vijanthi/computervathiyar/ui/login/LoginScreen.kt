package com.vijanthi.computervathiyar.ui.login

import android.R.attr.start
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vijanthi.computervathiyar.R
import com.vijanthi.computervathiyar.ui.login.components.GoogleButton
import com.vijanthi.computervathiyar.ui.login.components.rememberFirebaseAuthLauncher
import com.vijanthi.computervathiyar.ui.theme.gradientLoginEnd
import com.vijanthi.computervathiyar.ui.theme.gradientLoginMiddle
import com.vijanthi.computervathiyar.ui.theme.gradientLoginMiddleBefore
import com.vijanthi.computervathiyar.ui.theme.gradientLoginStart
import com.vijanthi.computervathiyar.ui.theme.primaryColor
import com.vijanthi.computervathiyar.ui.theme.primaryColor2


@Composable
fun LoginScreen(navController: NavHostController) {
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val loginSplash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_splash))
    val context = LocalContext.current
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
            navController.navigateToHome()
        },
        onAuthError = {
            Log.e("launcher", "LoginScreen: Error", it)
            Toast.makeText(context, "Something went wrong, Please try after sometime.", Toast.LENGTH_LONG).show()
            user = null
        }
    )
    val token = stringResource(R.string.default_web_client_id)
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor,
                            gradientLoginStart,
                            gradientLoginMiddleBefore
                        ),
                        start = Offset(Float.POSITIVE_INFINITY, 2f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))
            Text(buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 38.sp)) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, color = gradientLoginMiddleBefore,  fontSize = 32.sp)) {
                        append("C")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp)) {
                        append("omputer ")
                    }

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = primaryColor2, fontSize = 32.sp)) {
                        append("V")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp)) {
                        append("athiyar")
                    }
                }
            })
            Spacer(modifier = Modifier.height(15.dp))
            LottieAnimation(composition = loginSplash,
                modifier = Modifier.size(350.dp),
                reverseOnRepeat = true
            )
            Column(Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                GoogleButton(onClick = {
                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .requestProfile()
                            .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                })
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(onClick = {
                    navController.navigateToHome()
                }, contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp)) {
                    Text(text = "Skip", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

fun NavController.navigateToHome() {
    navigate("home") {
        popUpTo("login") {
            inclusive = true
        }
    }
}
