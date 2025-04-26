package dev.astler.unlib_test.fragments.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.data.IFlexibleItem
import dev.astler.ui.interfaces.IComposeItem
import dev.astler.ui.fragments.CatComposeFragment
import com.ao.subscribeme.R
import dev.astler.catlib.signin.SignInManager
import dev.astler.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.ui.compose.items.BaseCard
import dev.astler.ui.extensions.thenIf
import dev.astler.ui.theme.CatComposeTheme
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
abstract class TestsMenuFragment : CatComposeFragment() {

    @Inject
    lateinit var singInTool: SignInManager

    open val menuItems = listOf<IComposeItem<IFlexibleItem, String>>(
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3, true)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 4, false)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
    )

    open fun menuItemClicked(uid: String) {

    }

    @Composable
    override fun ScreenContent() {
        Box(Modifier.systemBarsPadding()) {
            Box(Modifier.imePadding()) {
                val signedIn by singInTool.signedInObservable.observeAsState(false)
                val profileImageUrl by singInTool.photoObservable.observeAsState(null)

                Column(modifier = Modifier) {
                    UserCard(
                        name = singInTool.user?.displayName ?: "John Doe",
                        isSignedIn = signedIn,
                        profileImageUrl = profileImageUrl, onSignOutClicked = {
                            singInTool.signOut()
                        }, onSignInClicked = {
                            singInTool.tryCredentialSignIn()
                        }
                    )

                    FlexibleGrid(items = menuItems, onItemClick = ::menuItemClicked)
                }
            }
        }
    }

    @Composable
    fun UserCard(
        name: String,
        isSignedIn: Boolean,
        profileImageUrl: String?,
        onSignInClicked: () -> Unit,
        onSignOutClicked: () -> Unit,
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .thenIf(!isSignedIn, Modifier.clickable { onSignInClicked() }),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = profileImageUrl ?: R.mipmap.ic_launcher,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isSignedIn) {
                    Button(
                        onClick = onSignOutClicked,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(text = "Sign Out")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun UserCardPreview() {
        CatComposeTheme {
            UserCard(
                name = "John Doe",
                isSignedIn = true,
                profileImageUrl = null,
                onSignOutClicked = {},
                onSignInClicked = {}
            )
        }
    }
}