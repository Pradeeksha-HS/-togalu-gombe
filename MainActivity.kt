@file:OptIn(androidx.media3.common.util.UnstableApi::class)
package com.togalugombe

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

val LampGold = Color(0xFFE8B14B)
val EmberOrange = Color(0xFFD97A2B)
val ShadowBlack = Color(0xFF0B0A0A)
val ShadowDark = Color(0xFF161413)
val ShadowMid = Color(0xFF231F1D)
val ParchmentCream = Color(0xFFEFE0C2)

data class Puppet(
    val id: String, val name: String, val epithet: String,
    val desc: String, val powers: String, val symbolism: String, val image: Int
)

data class Scene(
    val num: Int, val title: String, val summary: String,
    val detail: String, val music: String = ""
)

data class Play(
    val id: String, val title: String, val subtitle: String,
    val cover: Int, val scenes: List<Scene>
)

data class Artist(val name: String, val village: String, val craft: String, val phone: String)

object Session {
    private const
    val PREF = "togalu_session"
    private const
    val KEY_USER = "logged_user"

    fun save(ctx: Context, user: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_USER, user).apply()
    }

    fun get(ctx: Context): String? =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USER, null)

    fun clear(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply()
    }
}

object Seed {
    val puppets = listOf(
        Puppet("p1", "ಶ್ರೀ ರಾಮ", "ಮರ್ಯಾದಾ ಪುರುಷೋತ್ತಮ",
            "ಅಯೋಧ್ಯೆಯ ರಾಜಕುಮಾರ, ವಿಷ್ಣುವಿನ ಏಳನೇ ಅವತಾರ.",
            "ದಿವ್ಯ ಬಿಲ್ವಿದ್ಯೆ, ಬ್ರಹ್ಮಾಸ್ತ್ರ, ಧರ್ಮ ಪಾಲನೆಯ ಶಕ್ತಿ.",
            "ನೀಲಿ ವರ್ಣ ಸಾಗರಗಂಭೀರತೆ; ಬಿಲ್ಲು ಸಂಯಮ.",
            R.drawable.rama),
        Puppet("p2", "ಸೀತಾ ಮಾತೆ", "ಜಾನಕಿ",
            "ಮಿಥಿಲೆಯ ರಾಜಕುಮಾರಿ, ರಾಮನ ಪತ್ನಿ.",
            "ಅಗ್ನಿ ಪರೀಕ್ಷೆಯಲ್ಲಿ ಶುದ್ಧತೆ; ಭೂದೇವಿಯ ಅಂಶ.",
            "ಹಳದಿ ಸೀರೆ ಸಮೃದ್ಧಿ; ಪುಷ್ಪರಂಧ್ರಗಳು ಸ್ತ್ರೀತ್ವದ ಸಂಕೇತ.",
            R.drawable.sita),
        Puppet("p3", "ಹನುಮಂತ", "ಪವನಪುತ್ರ",
            "ಅಂಜನಾದೇವಿಯ ಮಗ, ರಾಮಭಕ್ತ.",
            "ಆಕಾಶಚಲನೆ, ಪರ್ವತ ಎತ್ತುವ ಬಲ, ರೂಪಪರಿವರ್ತನೆ.",
            "ಕೆಂಪು ವರ್ಣ ಶೌರ್ಯ; ಬಾಲದ ಸುರುಳಿ ರಂಧ್ರಗಳು.",
            R.drawable.hanuman),
        Puppet("p4", "ರಾವಣ", "ಲಂಕೇಶ್ವರ",
            "ಲಂಕೆಯ ರಾಜ, ದಶಮುಖ.",
            "ಶಿವತಾಂಡವ ಸ್ತೋತ್ರಜ್ಞ; ಮಾಯಾವಿದ್ಯೆ; ಚಂದ್ರಹಾಸ ಖಡ್ಗ.",
            "ಹತ್ತು ತಲೆ ಷಡ್ರಿಪು ಮತ್ತು ನಾಲ್ಕು ವೇದ.",
            R.drawable.ravana),
        Puppet("p5", "ಶ್ರೀ ಕೃಷ್ಣ", "ಯೋಗೇಶ್ವರ",
            "ವಿಷ್ಣುವಿನ ಎಂಟನೇ ಅವತಾರ, ಭಗವದ್ಗೀತೆಯ ಉಪದೇಶಕ.",
            "ಸುದರ್ಶನ ಚಕ್ರ; ವಿಶ್ವರೂಪ ದರ್ಶನ.",
            "ನೀಲಿ ಮೇಲ್ಮೈ; ಮುರಳಿ ರಂಧ್ರಗಳಲ್ಲಿ ನಾದದ ಸಂಕೇತ.",
            R.drawable.krishna),
        Puppet("p6", "ಅರ್ಜುನ", "ಪಾರ್ಥ",
            "ಪಾಂಡವರಲ್ಲಿ ಮೂರನೆಯವ, ಶ್ರೇಷ್ಠ ಧನುರ್ಧರ.",
            "ಗಾಂಡೀವ ಧನುಸ್ಸು; ಪಾಶುಪತಾಸ್ತ್ರ; ಯೋಗಸಂಧಾನ.",
            "ಬಿಳಿ ವಸ್ತ್ರ ಶುದ್ಧತೆ; ಬಿಲ್ಲಿನ ತಂತಿ ಏಕಾಗ್ರತೆ.",
            R.drawable.arjuna)
    )


    val plays = listOf(
        Play("ramayana", "ರಾಮಾಯಣ", "ಆದಿಕವಿ ವಾಲ್ಮೀಕಿ ರಚಿತ ಮಹಾಕಾವ್ಯ",
            R.drawable.ramayana,
            listOf(
                Scene(1, "ಶ್ರೀ ರಾಮನ ಜನನ",
                    "ರಾಮ-ಲಕ್ಷ್ಮಣ-ಭರತ-ಶತ್ರುಘ್ನರ ಜನನ.",
                    "ಅಯೋಧ್ಯೆಯ ಮಹಾರಾಜ ದಶರಥ ಸಂತಾನಹೀನನಾಗಿ ದುಃಖಿತನಾಗಿರಲು, ಋಷ್ಯಶೃಂಗ ಮುನಿಗಳ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ಪುತ್ರಕಾಮೇಷ್ಟಿ ಯಾಗ ನಡೆಸುತ್ತಾನೆ. ಯಜ್ಞಪುರುಷನು ದಿವ್ಯ ಪಾಯಸವನ್ನು ನೀಡಲು, ಅದನ್ನು ಕೌಸಲ್ಯೆ-ಕೈಕೇಯಿ-ಸುಮಿತ್ರೆಯರು ಸೇವಿಸುತ್ತಾರೆ. ಚೈತ್ರ ಮಾಸದ ನವಮಿಯಂದು ಪುನರ್ವಸು ನಕ್ಷತ್ರದಲ್ಲಿ ಶ್ರೀರಾಮನ ಜನನ — ಇಡೀ ಅಯೋಧ್ಯೆ ದೀಪಾಲಂಕಾರ, ಸಂಗೀತ, ನೃತ್ಯಗಳಿಂದ ಸಂಭ್ರಮಿಸುತ್ತದೆ. ಲಕ್ಷ್ಮಣ ಶತ್ರುಘ್ನರು ಸುಮಿತ್ರೆಗೆ, ಭರತ ಕೈಕೇಯಿಗೆ ಜನಿಸುತ್ತಾರೆ.",
                    "ಮಂಗಳ ಶಂಖನಾದ"),

                Scene(2, "ಸೀತಾ ಸ್ವಯಂವರ",
                    "ಶಿವಧನುಸ್ಸನ್ನು ಎತ್ತಿದ ರಾಮ.",
                    "ವಿಶ್ವಾಮಿತ್ರ ಮುನಿಗಳೊಂದಿಗೆ ಮಿಥಿಲೆಗೆ ಬಂದ ರಾಮ-ಲಕ್ಷ್ಮಣರು ಜನಕರಾಜನ ಆಸ್ಥಾನದಲ್ಲಿ ಶಿವಧನುಸ್ಸನ್ನು ಕಾಣುತ್ತಾರೆ. ಭೂಮಿಯಿಂದ ಜನಿಸಿದ ಸೀತೆಯ ವಿವಾಹಕ್ಕಾಗಿ ಜನಕನು ಪಣವಿಟ್ಟಿದ್ದ — ಯಾರು ಶಿವಧನುಸ್ಸನ್ನು ಎತ್ತುತ್ತಾರೋ ಅವರಿಗೇ ಸೀತೆಯ ಕೈಕೊಡುವೆ ಎಂದು. ಅನೇಕ ರಾಜರು ಸೋತ ಮೇಲೆ ರಾಮ ಸುಲಭವಾಗಿ ಧನುಸ್ಸನ್ನು ಎತ್ತಿ ಠೋಂಕಿಸುವಾಗ ಅದು ಮುರಿದು ಹೋಗುತ್ತದೆ. ಮಿಥಿಲೆಯಲ್ಲಿ ರಾಮ-ಸೀತಾ, ಲಕ್ಷ್ಮಣ-ಊರ್ಮಿಳಾ, ಭರತ-ಮಾಂಡವಿ, ಶತ್ರುಘ್ನ-ಶ್ರುತಕೀರ್ತಿ — ನಾಲ್ವರ ವಿವಾಹ ವೈಭವಯುತವಾಗಿ ನೆರವೇರುತ್ತದೆ.",
                    "ಶಹನಾಯಿ"),

                Scene(3, "ವನವಾಸದ ಆದೇಶ",
                    "ಕೈಕೇಯಿಯ ವರಗಳಿಂದ 14 ವರ್ಷ ವನವಾಸ.",
                    "ರಾಮನ ಪಟ್ಟಾಭಿಷೇಕದ ಮುನ್ನಾದಿನ, ಮಂಥರೆಯ ದುಷ್ಪ್ರೇರಣೆಯಿಂದ ಕೈಕೇಯಿ ತನ್ನ ಹಿಂದಿನ ಎರಡು ವರಗಳನ್ನು ದಶರಥನಿಂದ ಬೇಡುತ್ತಾಳೆ — ಭರತನಿಗೆ ಪಟ್ಟಾಭಿಷೇಕ ಮತ್ತು ರಾಮನಿಗೆ 14 ವರ್ಷ ವನವಾಸ. ಸತ್ಯವ್ರತಿ ರಾಮ ಯಾವ ದುಃಖವೂ ಇಲ್ಲದೆ ತಂದೆಯ ಮಾತಿಗೆ ಬದ್ಧನಾಗಿ ನಗುಮುಖದಿಂದ ಒಪ್ಪುತ್ತಾನೆ. ಸೀತೆ ಭರ್ತೃಭಕ್ತಿಯಿಂದ, ಲಕ್ಷ್ಮಣ ಭ್ರಾತೃಪ್ರೇಮದಿಂದ ಜೊತೆಗೆ ಹೊರಡುತ್ತಾರೆ. ದಶರಥ ಪುತ್ರಶೋಕದಿಂದ ಪ್ರಾಣಬಿಡುತ್ತಾನೆ.",
                    "ಕರುಣ ರಾಗ"),

                Scene(4, "ಸುವರ್ಣ ಮೃಗ",
                    "ಮಾರೀಚ ಮಾಯಾಮೃಗ.",
                    "ಪಂಚವಟಿಯಲ್ಲಿ ವಾಸಿಸುತ್ತಿದ್ದ ರಾಮ-ಸೀತಾ-ಲಕ್ಷ್ಮಣರ ಬಳಿ ರಾವಣನ ಆಜ್ಞೆಯಂತೆ ಮಾರೀಚನು ಬಂಗಾರದ ಜಿಂಕೆಯ ರೂಪ ತಾಳಿ ಬರುತ್ತಾನೆ. ಅದನ್ನು ನೋಡಿ ಮೋಹಗೊಂಡ ಸೀತೆ ಅದನ್ನು ಹಿಡಿದು ತರಲು ರಾಮನನ್ನು ಕೇಳುತ್ತಾಳೆ. ರಾಮ ಬಾಣ ಪ್ರಯೋಗಿಸುವಾಗ ಮಾರೀಚ ಸಾಯುವ ಮೊದಲು 'ಹಾ ಲಕ್ಷ್ಮಣಾ! ಹಾ ಸೀತೆ!' ಎಂದು ರಾಮನ ಧ್ವನಿಯಲ್ಲಿ ಕೂಗುತ್ತಾನೆ. ಆ ಧ್ವನಿಯಿಂದ ಭಯಗೊಂಡ ಸೀತೆ ಲಕ್ಷ್ಮಣನನ್ನು ಬಲವಂತದಿಂದ ಕಳಿಸುತ್ತಾಳೆ. ಲಕ್ಷ್ಮಣ ಆಶ್ರಮದ ಸುತ್ತ ರೇಖೆ ಎಳೆದು ಹೋಗುತ್ತಾನೆ.",
                    "ತಾಳ ಲಯ"),

                Scene(5, "ಸೀತಾಪಹರಣ",
                    "ರಾವಣ ಸೀತೆಯನ್ನು ಲಂಕೆಗೆ ಒಯ್ಯುತ್ತಾನೆ.",
                    "ಸಾಧುವೇಷಧಾರಿಯಾಗಿ ಬಂದ ರಾವಣನು ಭಿಕ್ಷೆ ಬೇಡುತ್ತಾನೆ. ಸೀತೆ ಲಕ್ಷ್ಮಣರೇಖೆಯನ್ನು ದಾಟಿ ಭಿಕ್ಷೆ ಕೊಡಲು ಹೋಗುವಾಗ ರಾವಣ ತನ್ನ ನಿಜರೂಪ ತಾಳಿ ಸೀತೆಯನ್ನು ಪುಷ್ಪಕ ವಿಮಾನದಲ್ಲಿ ಎತ್ತಿಕೊಳ್ಳುತ್ತಾನೆ. ಆಕಾಶದಲ್ಲಿ ಜಟಾಯು ಪಕ್ಷಿರಾಜ ಧೈರ್ಯವಾಗಿ ರಾವಣನನ್ನು ಎದುರಿಸುತ್ತಾನೆ. ರಾವಣ ಚಂದ್ರಹಾಸ ಖಡ್ಗದಿಂದ ಜಟಾಯುವಿನ ರೆಕ್ಕೆಗಳನ್ನು ಕಡಿಯುತ್ತಾನೆ. ಸೀತೆ ತನ್ನ ಆಭರಣಗಳನ್ನು ಭೂಮಿಗೆ ಬಿಸಾಡಿ ಗುರುತು ಬಿಡುತ್ತಾಳೆ. ಲಂಕೆಯ ಅಶೋಕವನದಲ್ಲಿ ಬಂಧಿಯಾಗುತ್ತಾಳೆ.",
                    "ರೌದ್ರ ಮೃದಂಗ"),

                Scene(6, "ಹನುಮಂತ ಭೇಟಿ",
                    "ಸುಗ್ರೀವ-ರಾಮ ಸ್ನೇಹ.",
                    "ಸೀತೆಯನ್ನು ಹುಡುಕುತ್ತಾ ರಾಮ-ಲಕ್ಷ್ಮಣರು ಋಷ್ಯಮೂಕ ಪರ್ವತಕ್ಕೆ ಬರುತ್ತಾರೆ. ಅಲ್ಲಿ ವಾಲಿಯಿಂದ ಭಯಭೀತನಾಗಿ ಅಡಗಿದ್ದ ಸುಗ್ರೀವ ಹನುಮಂತನನ್ನು ಬ್ರಾಹ್ಮಣ ವೇಷದಲ್ಲಿ ರಾಮನ ಬಳಿ ಕಳುಹಿಸುತ್ತಾನೆ. ಹನುಮಂತನ ಮೃದು ಸಂಸ್ಕೃತ ಭಾಷೆಯಿಂದ ಪ್ರಭಾವಿತನಾದ ರಾಮ ಆತನನ್ನು ಮೆಚ್ಚುತ್ತಾನೆ. ಅಗ್ನಿಸಾಕ್ಷಿಯಾಗಿ ರಾಮ-ಸುಗ್ರೀವ ಸ್ನೇಹ. ರಾಮ ವಾಲಿಯನ್ನು ಸಂಹರಿಸಿ ಸುಗ್ರೀವನಿಗೆ ಕಿಷ್ಕಿಂಧೆಯ ರಾಜ್ಯ ದೊರಕಿಸಿಕೊಡುತ್ತಾನೆ.",
                    "ವೀರ ರಸ"),

                Scene(7, "ಲಂಕಾ ದಹನ",
                    "ಹನುಮಂತನಿಂದ ಲಂಕೆ ದಹನ.",
                    "ಸೀತೆಯನ್ನು ಹುಡುಕಲು ಸಮುದ್ರ ಜಿಗಿದ ಹನುಮಂತ 100 ಯೋಜನ ಸಮುದ್ರ ದಾಟಿ ಲಂಕೆ ತಲುಪುತ್ತಾನೆ. ದಾರಿಯಲ್ಲಿ ಮೈನಾಕ, ಸುರಸಾ, ಸಿಂಹಿಕೆಯರೆಂಬ ಪರೀಕ್ಷೆಗಳನ್ನು ಗೆಲ್ಲುತ್ತಾನೆ. ಅಶೋಕವನದಲ್ಲಿ ಸೀತೆಯನ್ನು ಕಂಡು ರಾಮನ ಮುದ್ರೆಯುಂಗುರ ನೀಡುತ್ತಾನೆ. ರಾವಣಸಭೆಯಲ್ಲಿ ಬಂಧಿಯಾದಾಗ ಬಾಲಕ್ಕೆ ಬೆಂಕಿ ಹಚ್ಚಿದರೆ, ಆ ಬೆಂಕಿಯಿಂದಲೇ ಇಡೀ ಸುವರ್ಣಲಂಕೆಯನ್ನು ಸುಟ್ಟು ರಾವಣನ ದರ್ಪವನ್ನು ಚೂರ್ಣ ಮಾಡುತ್ತಾನೆ.",
                    "ಅಗ್ನಿ ತಾಳ"),

                Scene(8, "ಸೇತು ಬಂಧನ",
                    "ರಾಮಸೇತು ನಿರ್ಮಾಣ.",
                    "ರಾಮನ ವಾನರ ಸೈನ್ಯ ಸಮುದ್ರ ತೀರಕ್ಕೆ ತಲುಪಿದ ಮೇಲೆ, ರಾಮ ಸಮುದ್ರ ರಾಜನನ್ನು 3 ದಿನ ಧ್ಯಾನಿಸುತ್ತಾನೆ. ಸಮುದ್ರ ಪ್ರಸನ್ನನಾಗಿ ನಳ-ನೀಲರಿಗೆ ಕಲ್ಲುಗಳನ್ನು ತೇಲಿಸುವ ವರವಿದೆಯೆಂದು ಹೇಳುತ್ತಾನೆ. ವಿಶ್ವಕರ್ಮನ ಪುತ್ರ ನಳನ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ವಾನರಸೇನೆ 5 ದಿನಗಳಲ್ಲಿ 100 ಯೋಜನ ಉದ್ದದ ಸೇತುವೆ ನಿರ್ಮಿಸುತ್ತದೆ. ಪ್ರತಿ ಕಲ್ಲಿನ ಮೇಲೆ 'ಶ್ರೀರಾಮ' ಎಂದು ಬರೆದ ಕಲ್ಲುಗಳು ತೇಲುತ್ತವೆ — ಭಕ್ತಿಯ ಶಕ್ತಿಯ ಸಂಕೇತ.",
                    "ಗಂಭೀರ ತಾಳ"),

                Scene(9, "ರಾವಣ ವಧೆ",
                    "ಬ್ರಹ್ಮಾಸ್ತ್ರದಿಂದ ಸಂಹಾರ.",
                    "ಲಂಕೆಯಲ್ಲಿ ಮಹಾಯುದ್ಧ. ಕುಂಭಕರ್ಣ, ಇಂದ್ರಜಿತು, ಅತಿಕಾಯ ಮುಂತಾದ ರಾಕ್ಷಸ ವೀರರು ಸಂಹಾರ. ವಿಭೀಷಣ ರಾಮನ ಪಕ್ಷ ಸೇರಿ ರಾವಣನ ಪ್ರಾಣವು ಅವನ ನಾಭಿಯಲ್ಲಿನ ಅಮೃತಕುಂಭದಲ್ಲಿದೆ ಎಂಬ ರಹಸ್ಯ ಬಿಚ್ಚುತ್ತಾನೆ. ರಾಮ ಅಗಸ್ತ್ಯ ಮುನಿಯ ಆದಿತ್ಯಹೃದಯ ಸ್ತೋತ್ರ ಪಠಿಸಿ ಬ್ರಹ್ಮಾಸ್ತ್ರ ಪ್ರಯೋಗಿಸಿ ರಾವಣನನ್ನು ಸಂಹರಿಸುತ್ತಾನೆ. ಸಾಯುವ ಮೊದಲು ರಾವಣ ರಾಮನಿಂದ ರಾಜನೀತಿಯ ಪಾಠ ಪಡೆಯುತ್ತಾನೆ.",
                    "ಜಯಘೋಷ"),

                Scene(10, "ಪಟ್ಟಾಭಿಷೇಕ",
                    "ಅಯೋಧ್ಯೆಗೆ ಮರಳಿಕೆ.",
                    "ಸೀತೆಯ ಅಗ್ನಿಪರೀಕ್ಷೆಯ ನಂತರ, ರಾಮ-ಸೀತಾ-ಲಕ್ಷ್ಮಣ-ಹನುಮಂತ-ವಿಭೀಷಣ-ಸುಗ್ರೀವರು ಪುಷ್ಪಕ ವಿಮಾನದಲ್ಲಿ ಅಯೋಧ್ಯೆಗೆ ಮರಳುತ್ತಾರೆ. 14 ವರ್ಷ ಭರತ ಪಾದುಕೆಗಳನ್ನೇ ಪೂಜಿಸಿ ರಾಜ್ಯಭಾರ ಮಾಡಿದ್ದನು. ಹನುಮಂತ ಮೊದಲೇ ಬಂದು ಭರತನಿಗೆ ಶುಭವಾರ್ತೆ ತಿಳಿಸುತ್ತಾನೆ. ವಸಿಷ್ಠ ಮುನಿಗಳ ಆಧ್ವರ್ಯದಲ್ಲಿ ರಾಮನ ಪಟ್ಟಾಭಿಷೇಕ ವೈಭವಯುತವಾಗಿ ನಡೆಯುತ್ತದೆ. ರಾಮರಾಜ್ಯದಲ್ಲಿ ಸತ್ಯ-ಧರ್ಮ-ನ್ಯಾಯ ರಾಜ್ಯವಾಳುತ್ತವೆ.",
                    "ಮಂಗಳ ವಾದ್ಯ")
            )),

        Play("mahabharata", "ಮಹಾಭಾರತ", "ವ್ಯಾಸ ಮಹರ್ಷಿ ರಚಿತ ಪಂಚಮ ವೇದ",
            R.drawable.mahabharatha,
            listOf(
                Scene(1, "ಪಾಂಡವ-ಕೌರವರ ಜನನ",
                    "ಹಸ್ತಿನಾಪುರದಲ್ಲಿ ಸಂತಾನ.",
                    "ಹಸ್ತಿನಾಪುರದ ಧೃತರಾಷ್ಟ್ರನಿಗೆ ಗಾಂಧಾರಿಯಿಂದ 100 ಪುತ್ರರು (ಕೌರವರು) ಮತ್ತು ಪಾಂಡುವಿಗೆ ಕುಂತಿ-ಮಾದ್ರಿಯರಿಂದ ಪಂಚ ಪಾಂಡವರು ಜನಿಸುತ್ತಾರೆ. ಯುಧಿಷ್ಠಿರ (ಧರ್ಮ), ಭೀಮ (ವಾಯು), ಅರ್ಜುನ (ಇಂದ್ರ), ನಕುಲ-ಸಹದೇವ (ಅಶ್ವಿನಿ ಕುಮಾರರು) — ಎಲ್ಲರೂ ದೇವಮಂತ್ರದಿಂದ ಜನಿಸಿದವರು. ದುರ್ಯೋಧನ ಹುಟ್ಟಿದ ಕ್ಷಣ ಅಶುಭ ಶಕುನಗಳು — ವಿದುರ ಆತನನ್ನು ತ್ಯಜಿಸುವಂತೆ ಸಲಹೆ ನೀಡುತ್ತಾನೆ ಆದರೆ ಧೃತರಾಷ್ಟ್ರ ಒಪ್ಪುವುದಿಲ್ಲ. ಬಾಲ್ಯದಿಂದಲೇ ಭೀಮನ ಶಕ್ತಿಗೆ ದುರ್ಯೋಧನ ಹೊಟ್ಟೆಕಿಚ್ಚು ಪಡುತ್ತಾನೆ.",
                    "ಶಾಂತ ರಾಗ"),

                Scene(2, "ಲಾಕ್ಷಾಗೃಹ",
                    "ದುರ್ಯೋಧನನ ಯೋಜನೆ.",
                    "ಪಾಂಡವರ ಗುಣಗಳಿಂದ ಪ್ರಜೆಗಳು ಪ್ರೀತಿಸಲು, ದುರ್ಯೋಧನ ಶಕುನಿಯ ಸಲಹೆಯಿಂದ ವಾರಣಾವತದಲ್ಲಿ ಲಾಖೆಯ ಮನೆ ಕಟ್ಟಿಸಿ ಪಾಂಡವರನ್ನು ಅಲ್ಲಿಗೆ ಕಳುಹಿಸುತ್ತಾನೆ. ಪುರೋಚನನು ಮಧ್ಯರಾತ್ರಿ ಬೆಂಕಿ ಹಚ್ಚಲು ಯೋಜಿಸಿರುತ್ತಾನೆ. ವಿದುರ ಸಂಕೇತ ಭಾಷೆಯಲ್ಲಿ ಯುಧಿಷ್ಠಿರನಿಗೆ ಎಚ್ಚರಿಕೆ ನೀಡುತ್ತಾನೆ. ಭೀಮ ಭೂಗತ ಸುರಂಗ ತೋಡುತ್ತಾನೆ. ಪಾಂಡವರು ಒಬ್ಬ ಭಿಕ್ಷುಕಿ ಮತ್ತು ಆಕೆಯ 5 ಮಕ್ಕಳನ್ನು ಬಿಟ್ಟು ಸುರಂಗದಿಂದ ತಪ್ಪಿಸಿಕೊಳ್ಳುತ್ತಾರೆ — ಎಲ್ಲರೂ ಸುಟ್ಟು ಹೋದರೆಂದು ಭಾವಿಸಲಾಗುತ್ತದೆ.",
                    "ಭಯಾನಕ ತಾಳ"),

                Scene(3, "ದ್ರೌಪದಿ ಸ್ವಯಂವರ",
                    "ಮತ್ಸ್ಯಯಂತ್ರ ಭೇದನ.",
                    "ಪಾಂಚಾಲ ರಾಜ ದ್ರುಪದನು ತನ್ನ ಮಗಳ ಸ್ವಯಂವರಕ್ಕಾಗಿ ಮತ್ಸ್ಯಯಂತ್ರವನ್ನು ನಿರ್ಮಿಸುತ್ತಾನೆ — ಎತ್ತರದಲ್ಲಿ ತಿರುಗುವ ಮೀನನ್ನು ಕೆಳಗಿನ ತೈಲದಲ್ಲಿ ನೋಡಿ ಭೇದಿಸಬೇಕು. ಬ್ರಾಹ್ಮಣ ವೇಷದಲ್ಲಿದ್ದ ಪಾಂಡವರೂ ಭಾಗವಹಿಸುತ್ತಾರೆ. ಕರ್ಣ ಬಿಲ್ಲು ಹೆದೆಗೇರಿಸುವಾಗ ದ್ರೌಪದಿ 'ಸೂತಪುತ್ರನನ್ನು ವರಿಸುವುದಿಲ್ಲ' ಎಂದು ತಿರಸ್ಕರಿಸುತ್ತಾಳೆ. ಎಲ್ಲರೂ ವಿಫಲರಾದಾಗ ಅರ್ಜುನ ಸುಲಭವಾಗಿ ಯಂತ್ರ ಭೇದಿಸಿ ದ್ರೌಪದಿಯನ್ನು ಗೆಲ್ಲುತ್ತಾನೆ. ಕುಂತಿಯ ಅಚಾನಕ ಮಾತಿನಂತೆ ಪಂಚಪಾಂಡವರ ಸಾಮಾನ್ಯ ಪತ್ನಿಯಾಗುತ್ತಾಳೆ.",
                    "ಶೌರ್ಯ ರಸ"),

                Scene(4, "ದ್ಯೂತಸಭೆ",
                    "ಶಕುನಿಯ ಮೋಸ.",
                    "ಇಂದ್ರಪ್ರಸ್ಥದ ರಾಜಸೂಯ ಯಜ್ಞದ ವೈಭವ ನೋಡಿ ಅಸೂಯೆಗೊಂಡ ದುರ್ಯೋಧನ, ಶಕುನಿಯ ಮಾಯಾ ಪಗಡೆ ಆಟಕ್ಕೆ ಯುಧಿಷ್ಠಿರನನ್ನು ಆಹ್ವಾನಿಸುತ್ತಾನೆ. ಶಕುನಿಯ ಪಗಡೆ ಯಾವಾಗಲೂ ಆತ ಬಯಸಿದ ಸಂಖ್ಯೆಗೇ ಬೀಳುತ್ತಿತ್ತು. ಯುಧಿಷ್ಠಿರ ಒಂದೊಂದಾಗಿ ಸಂಪತ್ತು, ರಾಜ್ಯ, ತಮ್ಮಂದಿರನ್ನು, ತನ್ನನ್ನು ಮತ್ತು ಕೊನೆಗೆ ದ್ರೌಪದಿಯನ್ನೂ ಪಣವಿಟ್ಟು ಸೋಲುತ್ತಾನೆ. ಭೀಷ್ಮ-ದ್ರೋಣಾದಿಗಳು ಮೌನವಾಗಿಯೇ ಇರುತ್ತಾರೆ — ಇದು ಮಹಾಭಾರತದ ಬೀಜಬಿಂದು.",
                    "ಕರುಣ ರಾಗ"),

                Scene(5, "ವಸ್ತ್ರಾಪಹರಣ",
                    "ಅಕ್ಷಯ ಸೀರೆ.",
                    "ದುಶ್ಶಾಸನನು ಋತುಮತಿಯಾದ ದ್ರೌಪದಿಯನ್ನು ಕೂದಲು ಹಿಡಿದು ಸಭೆಗೆ ಎಳೆದು ತಂದು ಆಕೆಯ ಸೀರೆ ಸುಲಿಯಲು ಪ್ರಯತ್ನಿಸುತ್ತಾನೆ. ದ್ರೌಪದಿ ಎಲ್ಲರ ಕಡೆ ಸಹಾಯಕ್ಕಾಗಿ ನೋಡುತ್ತಾಳೆ — ಯಾರೂ ಸಹಾಯ ಮಾಡುವುದಿಲ್ಲ. ಕೊನೆಗೆ ಎರಡೂ ಕೈಗಳನ್ನು ಮೇಲೆತ್ತಿ 'ಗೋವಿಂದಾ! ದ್ವಾರಕಾವಾಸಿ!' ಎಂದು ಕೃಷ್ಣನನ್ನು ಪ್ರಾರ್ಥಿಸುತ್ತಾಳೆ. ಕೃಷ್ಣ ಅಕ್ಷಯ ವಸ್ತ್ರ ನೀಡಿ ಆಕೆಯ ಮಾನ ರಕ್ಷಿಸುತ್ತಾನೆ. ದುಶ್ಶಾಸನ ದಣಿದು ಬೀಳುತ್ತಾನೆ. ಭೀಮ ಪ್ರತಿಜ್ಞೆ — ದುಶ್ಶಾಸನನ ಎದೆ ಸೀಳಿ ರಕ್ತ ಕುಡಿದು, ಆ ರಕ್ತದಿಂದ ದ್ರೌಪದಿಯ ಕೂದಲು ಕಟ್ಟುವೆ.",
                    "ರೌದ್ರ ಮೃದಂಗ"),

                Scene(6, "ವನವಾಸ-ಅಜ್ಞಾತವಾಸ",
                    "13 ವರ್ಷ.",
                    "ಎರಡನೆಯ ದ್ಯೂತದ ನಂತರ 12 ವರ್ಷ ವನವಾಸ + 1 ವರ್ಷ ಅಜ್ಞಾತವಾಸ. ವನದಲ್ಲಿ ಪಾಂಡವರು ಅನೇಕ ಋಷಿಗಳಿಂದ ಜ್ಞಾನ ಪಡೆಯುತ್ತಾರೆ. ಅರ್ಜುನ ಶಿವನಿಂದ ಪಾಶುಪತಾಸ್ತ್ರ ಪಡೆಯುತ್ತಾನೆ. ಯುಧಿಷ್ಠಿರ ಯಕ್ಷ ಪ್ರಶ್ನೆಗಳಿಗೆ ಉತ್ತರಿಸಿ ತಮ್ಮಂದಿರ ಜೀವ ಉಳಿಸುತ್ತಾನೆ. ಅಜ್ಞಾತವಾಸದಲ್ಲಿ ವಿರಾಟರಾಜನ ಆಸ್ಥಾನದಲ್ಲಿ ವೇಷ — ಯುಧಿಷ್ಠಿರ ಕಂಕ, ಭೀಮ ವಲಲ ಪಾಚಕ, ಅರ್ಜುನ ಬೃಹನ್ನಳಾ ನೃತ್ಯಶಿಕ್ಷಕ, ದ್ರೌಪದಿ ಸೈರಂಧ್ರಿ. ಕೀಚಕನು ದ್ರೌಪದಿಗೆ ಕಿರುಕುಳ ನೀಡಲು, ಭೀಮ ಆತನನ್ನು ಸಂಹರಿಸುತ್ತಾನೆ.",
                    "ಶೋಕ ರಾಗ"),

                Scene(7, "ಭಗವದ್ಗೀತೋಪದೇಶ",
                    "ಕೃಷ್ಣನ ದಿವ್ಯ ಉಪದೇಶ.",
                    "ಕುರುಕ್ಷೇತ್ರದಲ್ಲಿ ಎರಡೂ ಸೈನ್ಯಗಳ ಮಧ್ಯೆ ರಥ ನಿಲ್ಲಿಸಿ, ಪಿತಾಮಹ-ಗುರು-ಬಂಧುಗಳನ್ನು ನೋಡಿ ಅರ್ಜುನ 'ನಾನು ಯುದ್ಧ ಮಾಡಲಾರೆ' ಎಂದು ಗಾಂಡೀವವನ್ನು ಕೆಳಗಿಡುತ್ತಾನೆ. ಆಗ ಕೃಷ್ಣ 18 ಅಧ್ಯಾಯಗಳ 700 ಶ್ಲೋಕಗಳ ಭಗವದ್ಗೀತೆ ಉಪದೇಶಿಸುತ್ತಾನೆ — ಕರ್ಮಯೋಗ, ಜ್ಞಾನಯೋಗ, ಭಕ್ತಿಯೋಗ. 'ನಿಷ್ಕಾಮ ಕರ್ಮ ಮಾಡು, ಫಲ ನನಗೆ ಬಿಡು' ಎಂಬ ಸಂದೇಶ. ಅಂತಿಮವಾಗಿ ವಿಶ್ವರೂಪ ದರ್ಶನ ತೋರಿಸಿ — ಸಾವಿರಾರು ಸೂರ್ಯರ ಕಾಂತಿ, ಎಲ್ಲ ಯೋಧರೂ ಆತನ ಬಾಯಿಯಲ್ಲಿ ಪ್ರವೇಶಿಸುತ್ತಿರುವ ದೃಶ್ಯ. ಅರ್ಜುನ ಗಾಂಡೀವ ಎತ್ತಿಕೊಳ್ಳುತ್ತಾನೆ.",
                    "ದಿವ್ಯ ಶಂಖನಾದ"),

                Scene(8, "ಭೀಷ್ಮ ಪತನ",
                    "ಶರಶಯ್ಯೆ.",
                    "ಇಚ್ಛಾಮರಣಿ ಭೀಷ್ಮ ಪಿತಾಮಹ 10 ದಿನ ಕೌರವ ಸೈನ್ಯದ ಸೇನಾಧಿಪತಿ. ಆತನನ್ನು ಸೋಲಿಸುವುದು ಯಾರಿಗೂ ಸಾಧ್ಯವಿರಲಿಲ್ಲ. ಕೃಷ್ಣನ ಸಲಹೆಯಂತೆ ಪಾಂಡವರು ಶಿಖಂಡಿಯನ್ನು ಮುಂದಿಡುತ್ತಾರೆ — ಶಿಖಂಡಿ ಪೂರ್ವಜನ್ಮದಲ್ಲಿ ಸ್ತ್ರೀ ಆಗಿದ್ದರಿಂದ ಭೀಷ್ಮ ಆಕೆಯ ಮೇಲೆ ಬಾಣ ಪ್ರಯೋಗಿಸುವುದಿಲ್ಲ. ಶಿಖಂಡಿಯ ಮರೆಯಿಂದ ಅರ್ಜುನ ಬಾಣ ಪ್ರಯೋಗಿಸಿ ಭೀಷ್ಮನನ್ನು ಶರಶಯ್ಯೆಗೆ ಒಪ್ಪಿಸುತ್ತಾನೆ. ಭೀಷ್ಮ ಉತ್ತರಾಯಣದವರೆಗೆ ಕಾಯ್ದು ಸ್ವೇಚ್ಛೆಯಿಂದ ಪ್ರಾಣಬಿಡುತ್ತಾನೆ. ಆ ಶಯ್ಯೆಯಿಂದಲೇ ಯುಧಿಷ್ಠಿರನಿಗೆ ರಾಜಧರ್ಮ ಬೋಧಿಸುತ್ತಾನೆ.",
                    "ಗಂಭೀರ ತಾಳ"),

                Scene(9, "ದುರ್ಯೋಧನ ವಧೆ",
                    "ಗದಾಯುದ್ಧ.",
                    "18 ದಿನಗಳ ಮಹಾಯುದ್ಧದ ಕೊನೆಯಲ್ಲಿ ದುರ್ಯೋಧನ ದ್ವೈಪಾಯನ ಸರೋವರದಲ್ಲಿ ಜಲಸ್ತಂಭ ವಿದ್ಯೆಯಿಂದ ಅಡಗಿರುತ್ತಾನೆ. ಪಾಂಡವರು ಸವಾಲು ಹಾಕಿದಾಗ ಗದಾಯುದ್ಧಕ್ಕೆ ಬರುತ್ತಾನೆ. ಭೀಮ-ದುರ್ಯೋಧನರ ಭಯಾನಕ ಗದಾಯುದ್ಧ. ಕೃಷ್ಣ ಸಂಜ್ಞೆ ಮಾಡಲು, ಭೀಮ ತನ್ನ ಪ್ರತಿಜ್ಞೆಯಂತೆ ಗದೆಯಿಂದ ದುರ್ಯೋಧನನ ತೊಡೆ ಮುರಿಯುತ್ತಾನೆ — ಇದು ಯುದ್ಧಧರ್ಮಕ್ಕೆ ವಿರುದ್ಧ. ಆದರೆ ಭೀಮನ ಪ್ರತಿಜ್ಞೆಯ ಪೂರೈಕೆ. ಸಾಯುವಾಗ ದುರ್ಯೋಧನ 'ನಾನು ಕ್ಷತ್ರಿಯ ಧರ್ಮದಂತೆ ಸ್ವರ್ಗಕ್ಕೆ ಹೋಗುತ್ತಿದ್ದೇನೆ' ಎಂದು ಗರ್ವದಿಂದ ಸಾಯುತ್ತಾನೆ.",
                    "ವೀರ ತಾಳ"),

                Scene(10, "ಸ್ವರ್ಗಾರೋಹಣ",
                    "ಪಾಂಡವರ ಯಾತ್ರೆ.",
                    "36 ವರ್ಷ ಧರ್ಮರಾಜ್ಯ ನಡೆಸಿದ ಮೇಲೆ, ಯಾದವ ಕುಲ ನಾಶ ಮತ್ತು ಕೃಷ್ಣನ ಅವತಾರ ಸಮಾಪ್ತಿ ಕೇಳಿ ಪಾಂಡವರು ರಾಜ್ಯವನ್ನು ಪರೀಕ್ಷಿತನಿಗೆ ಬಿಟ್ಟು ಮಹಾಪ್ರಸ್ಥಾನಕ್ಕೆ ಹೊರಡುತ್ತಾರೆ. ದ್ರೌಪದಿ-ನಕುಲ-ಸಹದೇವ-ಅರ್ಜುನ-ಭೀಮ ಕ್ರಮವಾಗಿ ಪತಿತರಾಗುತ್ತಾರೆ. ಕೊನೆಗೆ ಯುಧಿಷ್ಠಿರ ಮತ್ತು ಒಂದು ನಾಯಿ (ಯಮಧರ್ಮನ ಅವತಾರ) ಮಾತ್ರ ಉಳಿಯುತ್ತಾರೆ. ಇಂದ್ರ ಸ್ವರ್ಗಕ್ಕೆ ಆಹ್ವಾನಿಸಿದರೂ ನಾಯಿಯನ್ನು ಬಿಡದೆ ಹೋಗಲಾರೆ ಎನ್ನುವ ಯುಧಿಷ್ಠಿರನ ಧರ್ಮನಿಷ್ಠೆಗೆ ಮೆಚ್ಚಿ ಎಲ್ಲರೂ ಸ್ವರ್ಗ ಸೇರುತ್ತಾರೆ — ಮಹಾಭಾರತ ಸಮಾಪ್ತ.",
                    "ಮಂಗಳ ರಾಗ")
            ))
    )

    val artists = listOf(
        Artist("ಭೀಮವ್ವ ಶಿಳ್ಳೆಕ್ಯಾತ", "ಚಿಲ್ಲಂಗಿ, ಹಾವೇರಿ", "ಪದ್ಮಶ್ರೀ ಪುರಸ್ಕೃತೆ.", "+91-90000-00001"),
        Artist("ಶ್ರೀ ಬೆಳಗಲ್ಲು ವೀರಣ್ಣ", "ಬಳ್ಳಾರಿ", "ಮೂರು ತಲೆಮಾರುಗಳ ಕಲಾವಿದರು.", "+91-90000-00002"),
        Artist("ಗುಂಡೂರಾವ್ ಕಮ್ಮಾರ", "ಧಾರವಾಡ", "ಮಿನಿಯೇಚರ್ ಗೊಂಬೆ.", "+91-90000-00003")
    )

    const val MUSIC_URL = "https://upload.wikimedia.org/wikipedia/commons/4/47/Saraswathi_Veena_-_Sankarabharanam_-_S._Balachander.ogg"
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = LampGold, onPrimary = ShadowBlack,
            background = ShadowBlack, onBackground = ParchmentCream,
            surface = ShadowDark, onSurface = ParchmentCream,
            surfaceVariant = ShadowMid
        ),
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        try {
            val db = FirebaseFirestore.getInstance()
            val story1 = hashMapOf("title" to "Mahabharata", "description" to "Traditional puppet story")
            val story2 = hashMapOf("title" to "Ramayana", "description" to "Puppet story")
            db.collection("stories").document("Mahabharata").set(story1)
            db.collection("stories").document("Ramayana").set(story2)
        } catch (_: Exception) {  }

        setContent {
            AppTheme {
                Surface(Modifier.fillMaxSize()) {
                    val nav = rememberNavController()
                    val ctx = LocalContext.current
                    val startDestination =
                        if (Session.get(ctx) != null) "home" else "login"

                    NavHost(
                        navController = nav,
                        startDestination = startDestination
                    ) {
                        composable("login") {
                        LoginScreen(onLogin = {
                            nav.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                        }
                        composable("home") {
                            HomeScreen(nav, onLogout = {
                                Session.clear(ctx)
                                nav.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                             })
                        }
                        composable("play/{id}") { backStackEntry ->
                            LiveAssistScreen(
                        backStackEntry.arguments?.getString("id") ?: ""
                            ) { nav.popBackStack() }
                        }
                        composable("gallery") {
                            GalleryScreen(
                            onBack = { nav.popBackStack() },
                            onClick = { id -> nav.navigate("puppet/$id") }
                        )
                    }
                        composable("puppet/{id}") { backStackEntry ->
                            PuppetDetailScreen(
                            backStackEntry.arguments?.getString("id") ?: ""
                            ) { nav.popBackStack() }
                        }
                        composable("artist") {
                            ArtistScreen { nav.popBackStack() }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLogin: () -> Unit) {
    val ctx = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().background(ShadowBlack)
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        Surface(
            shape = RoundedCornerShape(50), color = ShadowDark,
            border = androidx.compose.foundation.BorderStroke(2.dp, LampGold),
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("🪔", fontSize = 48.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("ತೊಗಲು ಗೊಂಬೆ", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = LampGold)
        Text("ನೆರಳು ರಂಗಮಂಚಕ್ಕೆ ಸ್ವಾಗತ", fontSize = 14.sp,
            color = ParchmentCream.copy(alpha = 0.75f),
            modifier = Modifier.padding(top = 6.dp), textAlign = TextAlign.Center)
        Spacer(Modifier.height(48.dp))

        Surface(shape = RoundedCornerShape(20.dp), color = ShadowDark, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp)) {
                Text(if (isSignUp) "ಹೊಸ ಖಾತೆ" else "ಲಾಗಿನ್",
                    fontSize = 22.sp, color = LampGold, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = username, onValueChange = { username = it; error = null },
                    label = { Text("ಬಳಕೆದಾರ ಹೆಸರು") },
                    leadingIcon = { Icon(Icons.Outlined.Person, null, tint = LampGold) },
                    singleLine = true, modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LampGold, unfocusedBorderColor = ShadowMid,
                        focusedLabelColor = LampGold, cursorColor = LampGold
                    )
                )
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = password, onValueChange = { password = it; error = null },
                    label = { Text("ಪಾಸ್‌ವರ್ಡ್") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = LampGold) },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                null, tint = ParchmentCream.copy(alpha = 0.7f))
                        }
                                   },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LampGold, unfocusedBorderColor = ShadowMid,
                        focusedLabelColor = LampGold, cursorColor = LampGold
                    )
                )
                if (error != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(error!!, color = EmberOrange, fontSize = 13.sp)
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        when {
                            username.isBlank() -> error = "ಬಳಕೆದಾರ ಹೆಸರು ನಮೂದಿಸಿ"
                            password.length < 4 -> error = "ಪಾಸ್‌ವರ್ಡ್ ಕನಿಷ್ಠ 4 ಅಕ್ಷರ"
                            else -> {
                                loading = true
                                Session.save(ctx, username.trim())
                                Toast.makeText(ctx,
                                    if (isSignUp) "ಖಾತೆ ರಚನೆಯಾಗಿದೆ" else "ಸ್ವಾಗತ, ${username.trim()}!",
                                    Toast.LENGTH_SHORT).show()
                                onLogin()
                            }
                        }
                              },
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LampGold, contentColor = ShadowBlack,
                        disabledContainerColor = ShadowMid),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (loading) CircularProgressIndicator(color = ShadowBlack,
                        strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                    else Text(if (isSignUp) "ಖಾತೆ ರಚಿಸಿ" else "ಲಾಗಿನ್ ಆಗಿ",
                        fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = { isSignUp = !isSignUp; error = null },
                    modifier = Modifier.fillMaxWidth()) {
                    Text(if (isSignUp) "ಈಗಾಗಲೇ ಖಾತೆ ಇದೆಯೇ? ಲಾಗಿನ್ ಆಗಿ"
                    else "ಹೊಸ ಬಳಕೆದಾರರೇ? ಖಾತೆ ರಚಿಸಿ",
                        color = EmberOrange, fontSize = 14.sp)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { Session.save(ctx, "ಅತಿಥಿ"); onLogin() }) {
            Text("ಅತಿಥಿಯಾಗಿ ಮುಂದುವರಿಯಿರಿ →", color = ParchmentCream.copy(alpha = 0.7f))
        }
        Spacer(Modifier.height(40.dp))
    }
}
@Composable fun HomeScreen(nav: androidx.navigation.NavController, onLogout: () -> Unit) {
                    val ctx = LocalContext.current
    val user = Session.get(ctx)
    Column(
        Modifier
            .fillMaxSize()
            .background(ShadowBlack)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("ತೊಗಲು ಗೊಂಬೆ", fontSize = 36.sp, color = LampGold, fontWeight = FontWeight.Bold)
                Text("ನಮಸ್ಕಾರ, ${user ?: "ಬಳಕೆದಾರ"}",
                    fontSize = 14.sp,
                    color = EmberOrange
                )
                Text(
                    "ಡಿಜಿಟಲ್ ನೆರಳು ರಂಗಮಂಚದ ಸಂಗಾತಿ",
                    fontSize = 13.sp,
                    color = ParchmentCream.copy(alpha = 0.75f)
                )
            }
            IconButton(onClick = onLogout) {
                Icon(Icons.Outlined.Logout, contentDescription = "Logout", tint = LampGold)
            }
        }
        Spacer(Modifier.height(24.dp))


        Text("ನಾಟಕವನ್ನು ಆಯ್ಕಿ ಮಾಡಿ",
            fontSize = 18.sp, color = LampGold, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))

        Seed.plays.forEach { play ->
            Card(
                onClick = { nav.navigate("play/${play.id}") },
                colors = CardDefaults.cardColors(containerColor = ShadowDark),
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = play.cover,
                        contentDescription = play.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 88.dp, height = 64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ShadowMid)
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(play.title, color = LampGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(play.subtitle, color = ParchmentCream.copy(alpha = 0.85f), fontSize = 13.sp)
                        Text("${play.scenes.size} ದೃಶ್ಯಗಳು",
                            color = EmberOrange, fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp))
                    }
                    Icon(Icons.Outlined.PlayArrow, contentDescription = null, tint = LampGold)
                }
            }
        }
        Spacer(Modifier.height(28.dp))

        Text("ಅನ್ವೇಷಣೆ ಮಾಡಿ",
            fontSize = 18.sp, color = LampGold, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tile(
                modifier = Modifier.weight(1f),
                title = "ಗೊಂಬೆ ಗ್ಯಾಲರಿ",
                subtitle = "ಪಾತ್ರಗಳನ್ನು ತಿಳಿಯಿರಿ",
                icon = Icons.Outlined.Collections,
                onClick = { nav.navigate("gallery") }
            )
            Tile(
                modifier = Modifier.weight(1f),
                title = "ಕಲಾವಿದ ಸಂಪರ್ಕ",
                subtitle = "ಕಾರ್ಯಾಗಾರ ಬುಕ್",
                icon = Icons.Outlined.Handshake,
                onClick = { nav.navigate("artist") }
            )
        }
        Spacer(Modifier.height(40.dp))
    }
}
@Composable
fun Tile(modifier: Modifier, title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .heightIn(min = 130.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = ShadowMid
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = LampGold, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 16.sp, color = ParchmentCream, fontWeight = FontWeight.SemiBold)
            Text(subtitle, fontSize = 13.sp, color = ParchmentCream.copy(alpha = 0.7f))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveAssistScreen(playId: String, onBack: () -> Unit) {
    val play = Seed.plays.firstOrNull { it.id == playId } ?: Seed.plays.first()
    val pager = rememberPagerState(pageCount = { play.scenes.size })
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember {
        MediaPlayer().apply {
            try {
                setDataSource(Seed.MUSIC_URL)
                isLooping = true
                setVolume(0.6f, 0.6f)
                prepareAsync()
            } catch (_: Exception) {}
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            try { mediaPlayer.stop() } catch (_: Exception) {}
            mediaPlayer.release()
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(play.title, color = LampGold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = LampGold)
                }
                             },
            actions = {
                IconButton(onClick = {
                    try {
                        if (isPlaying) mediaPlayer.pause() else mediaPlayer.start()
                        isPlaying = !isPlaying
                    } catch (_: Exception) {
                        Toast.makeText(ctx, "ಸಂಗೀತ load ಆಗುತ್ತಿದೆ...", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(if (isPlaying) Icons.AutoMirrored.Outlined.VolumeUp else Icons.AutoMirrored.Outlined.VolumeOff,
                        contentDescription = null, tint = LampGold)
                }
                      },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ShadowBlack)
        )
    }) { p ->
        Column(Modifier.fillMaxSize().padding(p).background(ShadowBlack)) {
            LinearProgressIndicator(
                progress = { (pager.currentPage + 1f) / play.scenes.size },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = LampGold, trackColor = ShadowMid
            )
            Text("ದೃಶ್ಯ ${pager.currentPage + 1}  ${play.scenes.size}",
                fontSize = 13.sp, color = EmberOrange,
                modifier = Modifier.padding(start = 20.dp, top = 12.dp))
            HorizontalPager(state = pager, modifier = Modifier.weight(1f)) { i ->
                val s = play.scenes[i]
                var expanded by remember(s.num) { mutableStateOf(false) }
                Column(Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())) {
                    Surface(color = ShadowDark, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp)) {
                            Text("ದೃಶ್ಯ ${s.num}", fontSize = 13.sp, color = EmberOrange, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(6.dp))
                            Text(s.title, fontSize = 24.sp, color = LampGold, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            Text(s.summary, fontSize = 16.sp, color = ParchmentCream, lineHeight = 24.sp)
                            Spacer(Modifier.height(12.dp))
                            TextButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Outlined.AutoStories, contentDescription = null, tint = LampGold, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(if (expanded) "ಸಂಕ್ಷಿಪ್ತಗೊಳಿಸಿ" else "ಇನ್ನಷ್ಟು ತಿಳಿಯಿರಿ", color = LampGold)
                            }
                            if (expanded) {
                                Spacer(Modifier.height(8.dp))
                                Text(s.detail, fontSize = 15.sp, color = ParchmentCream.copy(alpha = 0.95f), lineHeight = 23.sp)
                            }
                            if (s.music.isNotEmpty()) {
                                Spacer(Modifier.height(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.MusicNote, contentDescription = null, tint = EmberOrange, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(s.music, fontSize = 13.sp, color = ParchmentCream.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = {
                    scope.launch { if (pager.currentPage > 0) pager.animateScrollToPage(pager.currentPage - 1) }
                                         }, enabled = pager.currentPage > 0) { Text("ಹಿಂದಿನ") }
                Button(onClick = {
                    scope.launch {
                        if (pager.currentPage < play.scenes.size - 1)
                            pager.animateScrollToPage(pager.currentPage + 1)
                    }
                                 }, enabled = pager.currentPage < play.scenes.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = LampGold, contentColor = ShadowBlack)) {
                    Text("ಮುಂದಿನ ದೃಶ್ಯ")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun GalleryScreen(onBack: () -> Unit, onClick: (String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("ಗೊಂಬೆ ಗ್ಯಾಲರಿ", color = LampGold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = LampGold)
                }
                             },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ShadowBlack)
        )
    }) { p ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(p).background(ShadowBlack),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                PuppetCraftVideo()
            }
            items(Seed.puppets) { pp ->
                Card(onClick = { onClick(pp.id) }, colors = CardDefaults.cardColors(containerColor = ShadowDark)) {
                    Column {
                        AsyncImage(model = pp.image, contentDescription = null, contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(160.dp).background(ShadowMid))
                        Column(Modifier.padding(12.dp)) {
                            Text(pp.name, fontSize = 16.sp, color = LampGold, fontWeight = FontWeight.SemiBold)
                            Text(pp.epithet, fontSize = 12.sp, color = EmberOrange)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetCraftVideo() {
    val ctx = LocalContext.current
    var muted by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val exoPlayer = remember {
        ExoPlayer.Builder(ctx).build().apply {
            try {
                val uri = Uri.parse(
                    "android.resource://${ctx.packageName}/${R.raw.puppet}")
                setMediaItem(MediaItem.fromUri(uri))
                setHandleAudioBecomingNoisy(true)
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                playWhenReady = true
                volume = 1f
                setAudioAttributes(AudioAttributes.DEFAULT, true)
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                prepare()
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        errorMsg = "ವೀಡಿಯೊ load ಆಗಲಿಲ್ಲ"
                    }
                })
            } catch (e: Exception) { errorMsg = "ದೋಷ: ${e.message}" }
        }
    }
    LaunchedEffect(muted) { exoPlayer.volume = if (muted) 0f else 1.0f }
    DisposableEffect(Unit) { onDispose { exoPlayer.release() } }
     Surface(shape = RoundedCornerShape(18.dp), color = ShadowDark,
         modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
         Column(Modifier.padding(14.dp)) {
             Row(verticalAlignment = Alignment.CenterVertically) {
                 Text("ತೊಗಲು ಗೊಂಬೆ ಪ್ರದರ್ಶನ",
                     color = LampGold, fontSize = 20.sp,
                     fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                 IconButton(onClick = { muted = !muted }) {
                     Icon(
                         if (muted) Icons.Outlined.VolumeOff else Icons.Outlined.VolumeUp,
                         contentDescription = null, tint = LampGold
                     )
                 }
             }
             Spacer(Modifier.height(8.dp))
             Text("ಪಾರಂಪರಿಕ ಗೊಂಬೆಯಾಟದ ವೀಡಿಯೊ", color = ParchmentCream.copy(alpha = 0.8f))
             Spacer(Modifier.height(12.dp))
             AndroidView(
                 factory = { c ->
                     PlayerView(c).apply {
                         player = exoPlayer
                         useController = true
                     }
                           },
                 modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(12.dp))
             )
             if (errorMsg != null) {
                 Spacer(Modifier.height(8.dp))
                 Text(errorMsg!!, color = EmberOrange, fontSize = 12.sp)
             }
         }
     }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun PuppetDetailScreen(id: String, onBack: () -> Unit) {
    val pp = Seed.puppets.firstOrNull { it.id == id } ?: Seed.puppets.first()
    var zoom by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(pp.name, color = LampGold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = LampGold)
                }
                             },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ShadowBlack)
        )
    }) { p ->
        Column(Modifier.fillMaxSize().padding(p).background(ShadowBlack).verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(if (zoom) 560.dp else 360.dp).background(ShadowMid).clickable { zoom = !zoom }) {
                AsyncImage(model = pp.image, contentDescription = pp.name, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
                Surface(color = ShadowDark.copy(alpha = 0.85f), shape = RoundedCornerShape(50),
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ZoomIn, contentDescription = null, tint = LampGold, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (zoom) "ಕಿರಿದುಗೊಳಿಸಿ" else "ಜೂಮ್ ಮಾಡಲು ಟ್ಯಾಪ್", fontSize = 12.sp, color = LampGold)
                    }
                }
            }
            Column(Modifier.padding(20.dp)) {
                Text(pp.epithet, fontSize = 16.sp, color = EmberOrange, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(16.dp))
                Section("ಪರಿಚಯ", pp.desc); Spacer(Modifier.height(20.dp))
                Section("ಶಕ್ತಿಗಳು", pp.powers); Spacer(Modifier.height(20.dp))
                Section("ಸಂಕೇತಾರ್ಥ", pp.symbolism); Spacer(Modifier.height(40.dp))
            }
        }
    }
}
@Composable fun Section(title: String, body: String) {
    Text(title, fontSize = 20.sp, color = LampGold, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 6.dp))
    Text(body, fontSize = 16.sp, color = ParchmentCream, lineHeight = 24.sp)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun ArtistScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("ಕಾರ್ಯಾಗಾರ ಬುಕ್") }
    val ctx = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("ಕಲಾವಿದ ಸಂಪರ್ಕ", color = LampGold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = LampGold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = ShadowBlack)
        )
    }) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).background(ShadowBlack)
                .verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Text(
                "ಪರಂಪರೆ ಕಲಾವಿದರು",
                fontSize = 20.sp,
                color = LampGold,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            Seed.artists.forEach { a ->
                Surface(
                    shape = RoundedCornerShape(16.dp), color = ShadowDark,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = LampGold,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                a.name,
                                fontSize = 16.sp,
                                color = LampGold,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = EmberOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    a.village,
                                    fontSize = 13.sp,
                                    color = ParchmentCream.copy(alpha = 0.8f)
                                )
                            }
                            Text(
                                a.craft,
                                fontSize = 12.sp,
                                color = ParchmentCream.copy(alpha = 0.7f)
                            )
                        }
                        Icon(Icons.Outlined.Phone, contentDescription = null, tint = LampGold)
                    }
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                "ವಿನಂತಿ ಕಳುಹಿಸಿ",
                fontSize = 20.sp,
                color = LampGold,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ಕಾರ್ಯಾಗಾರ ಬುಕ್", "ಗೊಂಬೆ ಖರೀದಿ").forEach { opt ->
                    FilterChip(
                        selected = type == opt,
                        onClick = { type = opt },
                        label = { Text(opt) })
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ನಿಮ್ಮ ಹೆಸರು") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("ಫೋನ್ ಸಂಖ್ಯೆ") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = msg, onValueChange = { msg = it }, label = { Text("ಸಂದೇಶ") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp), maxLines = 5
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    if (name.isBlank() || phone.isBlank())
                        Toast.makeText(ctx, "ಹೆಸರು ಮತ್ತು ಫೋನ್ ನಮೂದಿಸಿ", Toast.LENGTH_SHORT).show()
                    else {
                        Toast.makeText(
                            ctx,
                            "ವಿನಂತಿ ಸ್ವೀಕರಿಸಲಾಯಿತು. ಶೀಘ್ರದಲ್ಲೇ ಕರೆ ಬರುತ್ತದೆ.",
                            Toast.LENGTH_LONG
                        ).show()
                        name = ""; phone = ""; msg = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LampGold,
                    contentColor = ShadowBlack
                ),
                modifier = Modifier.fillMaxWidth()
            ) { Text("ವಿನಂತಿ ಕಳುಹಿಸಿ") }
            Spacer(Modifier.height(40.dp))
        }
    }
}