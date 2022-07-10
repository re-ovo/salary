package me.rerere.salary.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.salary.ui.route.NavGraphs
import me.rerere.salary.ui.theme.SalaryTheme
import me.rerere.salary.ui.util.DefaultScreenTransition

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SalaryTheme {
                DestinationsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navGraph = NavGraphs.rootGraph,
                    engine = rememberAnimatedNavHostEngine(
                        rootDefaultAnimations = DefaultScreenTransition
                    )
                )
            }
        }
    }
}