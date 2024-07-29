package dev.astler.ui.compose.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.sendEmail

@Composable
fun PrivacyScreen(appConfig: AppConfig) {
    val scroll: ScrollState = rememberScrollState(0)

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        PrivacyNoticeText(appConfig)
    }
}

@Composable
fun PrivacyNoticeText(appConfig: AppConfig) {
    val context = LocalContext.current

    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_intro_title),
        stringResource(gg.pressf.resources.R.string.privacy_intro_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_data_collection_title),
        stringResource(gg.pressf.resources.R.string.privacy_data_collection_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_information_usage_title),
        stringResource(gg.pressf.resources.R.string.privacy_information_usage_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_data_security_title),
        stringResource(gg.pressf.resources.R.string.privacy_data_security_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_third_party_services_title),
        stringResource(gg.pressf.resources.R.string.privacy_third_party_services_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_children_privacy_title),
        stringResource(gg.pressf.resources.R.string.privacy_children_privacy_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_changes_notice_title),
        stringResource(gg.pressf.resources.R.string.privacy_changes_notice_content, stringResource(gg.pressf.resources.R.string.app_name))
    )
    SectionText(
        stringResource(gg.pressf.resources.R.string.privacy_contact_us_title),
        stringResource(gg.pressf.resources.R.string.privacy_contact_us_content, "support@pressf.gg")
    )

    Button(onClick = {
        context.sendEmail("support@pressf.gg", "About ${context.getString(gg.pressf.resources.R.string.app_name)}")
    }) {
        Text(text = stringResource(gg.pressf.resources.R.string.contact))
    }

    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appConfig.policyLink))
        context.startActivity(intent)
    }) {
        Text(text = stringResource(gg.pressf.resources.R.string.pressf_policy))
    }
}

@Composable
fun SectionText(title: String, content: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.primary,

        )
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 16.dp),
        color = MaterialTheme.colorScheme.onSurface,
    )
}