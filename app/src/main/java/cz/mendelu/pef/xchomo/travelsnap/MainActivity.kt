package cz.mendelu.pef.xchomo.travelsnap

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import cz.mendelu.pef.xchomo.travelsnap.location.LocationManager
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.TravelSnapTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelSnapTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }



    }

}
