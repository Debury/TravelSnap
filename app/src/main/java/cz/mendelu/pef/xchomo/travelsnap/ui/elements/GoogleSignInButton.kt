package cz.mendelu.pef.xchomo.travelsnap.ui.elements


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.CustomTypography
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.halfMargin

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    val googleIcon: Painter = painterResource(id = R.drawable.icons8_google) // Replace with your Google logo resource
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Sign in with Google", color = AppColors.PlaceholderTextColor ,style = CustomTypography.bodySmall)
        Spacer(modifier = Modifier.height(halfMargin()))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.LightPrimaryColor)
        ) {
            Image(
                painter = googleIcon,
                contentDescription = "Google sign-in",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}