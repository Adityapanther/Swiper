package moe.tlaster.swiper.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.swiper.Direction
import moe.tlaster.swiper.Swiper
import moe.tlaster.swiper.rememberSwiperState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold {
                  Surface (
                      modifier = Modifier.padding(it)
                  ){
                      var show by remember { mutableStateOf(false) }
                      Column(
                          modifier = Modifier.fillMaxSize(),
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center,
                      ) {
                          Button(onClick = { show = true }) {
                              Text(text = "ClickMe")
                          }
                          Text(text = "show: $show")
                      }
                      if (show) {
                          val state = rememberSwiperState(
                              onDismiss = {
                                  show = false
                              }
                          )
                          Box(
                              modifier = Modifier
                                  .fillMaxSize()
                                  .background(Color.Black.copy(alpha = 1 - state.progress))
                                  .alpha(1 - state.progress),
                          ) {
                              Swiper(
                                  state = state,
                                  orientation = Orientation.Horizontal,
                                  direction = Direction.Left
                              ) {
                                  Box(
                                      modifier = Modifier
                                          .fillMaxWidth()
                                          .height(400.dp)
                                          .background(Color.Gray)
                                  ){
                                      Column (
                                          modifier = Modifier.fillMaxSize(),
                                          verticalArrangement = Arrangement.Bottom,
                                          horizontalAlignment = Alignment.CenterHorizontally
                                      ){
                                          Row(
                                              modifier = Modifier.fillMaxWidth(),
                                              verticalAlignment = Alignment.CenterVertically,
                                              horizontalArrangement = Arrangement.Center
                                          ) {
                                              val scope = rememberCoroutineScope()
                                              IconButton(
                                                  onClick = {
                                                      scope.launch(Dispatchers.Default){
                                                          state.dismissIt()
                                                      }
                                              }) {
                                                  Icon(
                                                      imageVector = Icons.Default.Close,
                                                      contentDescription = "Close it",
                                                      tint = Color.White
                                                  )
                                              }
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }

                }
            }
        }
    }
}
