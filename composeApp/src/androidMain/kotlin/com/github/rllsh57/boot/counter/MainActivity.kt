package com.github.rllsh57.boot.counter

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.rllsh57.boot.counter.boot.*
import kotlinx.datetime.*
import org.koin.android.ext.android.get
import java.util.Calendar
import kotlin.time.*

const val ACTION_BOOT_COMPLETED = "com.rllsh57.github.boot.counter.action.BOOT_COMPLETED"

class MainActivity : ComponentActivity() {

    private val storage: BootTimeStorage = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                hasNotificationPermissions = ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PERMISSION_GRANTED,
                dismissalInterval = storage.readDismissalInterval(),
                totalDimissals = storage.readTotalDismissals(),
                timestamps = storage.readAll(),
                onButtonClicked = { sendBroadcast(this) },
                onAddBootClicked = { storage.addTimestamp(Calendar.getInstance().timeInMillis) },
                onClearClicked = { storage.clear() },
                onSaveTotalDismissals = { storage.writeTotalDismissals(it) },
                onSaveDismissalInterval = { storage.writeDismissalInterval(it) }
            )
        }
    }
}

@Composable
fun App(
    hasNotificationPermissions: Boolean,
    dismissalInterval: Long,
    totalDimissals: Int,
    timestamps: List<Long>,
    onButtonClicked: () -> Unit,
    onAddBootClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onSaveTotalDismissals: (Int) -> Unit,
    onSaveDismissalInterval: (Long) -> Unit
) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onButtonClicked
            ) {
                Text("Show Notification")
            }

            Button(
                onClick = onAddBootClicked
            ) {
                Text("Add Boot")
            }

            Button(
                onClick = onClearClicked
            ) {
                Text("Clear")
            }

            Text(
                text = "Has notification permissions: $hasNotificationPermissions"
            )

            Text(
                text = "Total dismissals: $totalDimissals"
            )

            Text(
                text = "Dismissal interval: ${dismissalInterval.toDuration(DurationUnit.MILLISECONDS)}"
            )

            Column {
                timestamps
                    .map {
                        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
                    }
                    .groupBy {
                        it.date
                    }
                    .forEach {
                        val date = it.key
                        val boots = it.value
                        Text("$date - ${boots.size}")
                }
            }

            var totalDismissals by remember { mutableStateOf("$totalDimissals") }
            TextField(
                value = totalDismissals,
                onValueChange = { totalDismissals = it },
                placeholder = { "Total Dismissals" },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onGo = { onSaveTotalDismissals(totalDismissals.toInt()) }
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )

            var dismissalInterval by remember { mutableStateOf("${dismissalInterval}") }
            TextField(
                value = dismissalInterval,
                onValueChange = { dismissalInterval = it },
                placeholder = { "Dismissal interval (milliseconds)" },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onGo = { onSaveDismissalInterval(dismissalInterval.toLong()) }
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
            )
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
