package com.example.android_practice.content

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.Button
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.android_practice.ClassStartNotificationReceiver
import com.example.android_practice.components.GlideImage
import com.example.android_practice.data.Profile
import com.example.android_practice.scheduleNotification
import com.example.android_practice.viewmodel.ProfileViewModel
import com.example.android_practice.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.compose.koinInject
import org.koin.core.Koin
import java.io.File
import java.io.FileOutputStream
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    val profile by viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf(profile.fullName) }
    var resumeUrl by remember { mutableStateOf(profile.resumeUrl) }
    var favoriteClassTime by rememberSaveable { mutableStateOf(profile.favoriteClassTime) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    fun isValidTimeFormat(time: String): Boolean {
        return time.matches(Regex("^\\d{2}:\\d{2}$"))
    }

    var timeValidationError by remember {
        mutableStateOf(!isValidTimeFormat(favoriteClassTime) && favoriteClassTime.isNotEmpty())
    }

    fun parseTime(time: String): Pair<Int, Int> {
        return if (time.isNotEmpty() && isValidTimeFormat(time)) {
            val parts = time.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            val calendar = Calendar.getInstance()
            calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
        }
    }

    val initialTime = parseTime(profile.favoriteClassTime)

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var hasExactAlarmPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val requestExactAlarmPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasExactAlarmPermission) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            requestExactAlarmPermissionLauncher.launch(intent)
        }
    }

    val requestNotificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Разрешение на уведомления получено",
                    duration = SnackbarDuration.Short
                )
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Для работы будильника необходимо разрешение на уведомления",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            viewModel.updateAvatar(it.toString())
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            (result.data?.extras?.get("data") as? Bitmap)?.let { bitmap ->
                val uri = saveBitmapToCache(bitmap, context)
                viewModel.updateAvatar(uri)
            }
        }
    }

    fun selectImage(fromGallery: Boolean) {
        if (fromGallery) {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        } else {
            selectImage(requestCode == GALLERY_REQUEST)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                actions = {
                    TextButton(
                        onClick = {
                            if (favoriteClassTime.isEmpty() || !isValidTimeFormat(favoriteClassTime)) {
                                timeValidationError = true
                                CoroutineScope(Dispatchers.Main).launch {
                                    snackbarHostState.showSnackbar(
                                        "Некорректный формат времени (HH:mm)",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                timeValidationError = false
                                viewModel.clearScheduledNotification(context)
                                viewModel.updateProfile(
                                    Profile(
                                        fullName,
                                        profile.avatarUri,
                                        resumeUrl,
                                        favoriteClassTime
                                    )
                                )
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                    Log.d("EditProfile", "Запрашиваем разрешение на уведомления перед планированием")
                                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    context.scheduleNotification(fullName, favoriteClassTime)
                                    navController.popBackStack()
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasExactAlarmPermission) {
                                    requestExactAlarmPermission()
                                }
                            }
                        },
                        enabled = !timeValidationError,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Готово")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { showImageSourceDialog = true }
            ) {
                val avatarUri = profile.avatarUri
                if (avatarUri != null) {
                    GlideImage(
                        url = avatarUri,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Аватар",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (URL)") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = favoriteClassTime,
                    onValueChange = { newTime ->
                        favoriteClassTime = newTime
                        timeValidationError = newTime.isNotEmpty() && !isValidTimeFormat(newTime)
                    },
                    label = { Text("Время любимой пары (HH:mm)") },
                    modifier = Modifier.weight(1f),
                    isError = timeValidationError,
                    supportingText = {
                        if (timeValidationError) {
                            Text(
                                text = "Некорректный формат времени (HH:mm)",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showTimePickerDialog = true }) {
                    Icon(Icons.Filled.Timer, contentDescription = "Выбрать время")
                }
            }

            if (showTimePickerDialog) {
                val initialTime = parseTime(favoriteClassTime)
                TimePickerDialog(
                    context,
                    { _, hourOfDay: Int, minute: Int ->
                        favoriteClassTime = String.format(Locale.US, "%02d:%02d", hourOfDay, minute)
                        showTimePickerDialog = false
                        timeValidationError = false
                    },
                    initialTime.first,
                    initialTime.second,
                    true
                ).show()
            }
        }
        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Выберите источник") },
                text = { Text("Откуда взять изображение?") },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                showImageSourceDialog = false
                                requestPermission(Manifest.permission.READ_MEDIA_IMAGES, GALLERY_REQUEST)
                            }
                        ) {
                            Text("Галерея")
                        }
                        TextButton(
                            onClick = {
                                showImageSourceDialog = false
                                requestPermission(Manifest.permission.CAMERA, CAMERA_REQUEST)
                            }
                        ) {
                            Text("Камера")
                        }
                    }
                }
            )
        }
    }
}

private const val GALLERY_REQUEST = 1
private const val CAMERA_REQUEST = 2

private fun saveBitmapToCache(bitmap: Bitmap, context: android.content.Context): String {
    val file = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return Uri.fromFile(file).toString()
}