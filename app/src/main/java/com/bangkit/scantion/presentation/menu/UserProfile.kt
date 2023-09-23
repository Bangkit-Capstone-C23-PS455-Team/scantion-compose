package com.bangkit.scantion.presentation.menu

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.ui.component.TextFieldQuestion
import com.bangkit.scantion.util.DialogDate
import com.bangkit.scantion.util.dateToMilliseconds
import com.bangkit.scantion.viewmodel.AuthViewModel
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween) {
        TopAppBar(
            title = {
                Text(
                    text = "User Profile",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back"
                    )
                }
            },
        )

        var name by rememberSaveable { mutableStateOf("") }
        var city by rememberSaveable { mutableStateOf("") }
        var born by rememberSaveable { mutableStateOf("") }

        var hasRead by rememberSaveable { mutableStateOf(false) }
        var inEditing by rememberSaveable { mutableStateOf(false) }

        val user = viewModel.currentUser

        val db = Firebase.firestore
        val docRef = user?.let { db.collection("users").document(it.uid) }

        if (!hasRead){
            docRef?.get()?.addOnSuccessListener { document ->
                if (document != null) {
                    name = document.data?.get("name").toString()
                    city = document.data?.get("city").toString()
                    born = document.data?.get("born").toString()
                } else {
                    Log.d(TAG, "No such document")
                }
            }?.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
            hasRead = true
        }

        val nameFocusRequester = remember { FocusRequester() }
        val cityFocusRequester = remember { FocusRequester() }

        val focusManager = LocalFocusManager.current

        val snackScope = rememberCoroutineScope()
        val openDialog = remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (born.isNotEmpty()) dateToMilliseconds(born) else null
        )
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

        DialogDate(snackScope, openDialog, datePickerState, confirmEnabled) { born = it }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            TextFieldQuestion(
                readOnly = !inEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester),
                text = "Nama",
                value = name,
                onChangeValue = { name = it },
                nextFocusRequester = cityFocusRequester
            )
            TextFieldQuestion(
                readOnly = !inEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(cityFocusRequester),
                text = "Kota Anda Sekarang",
                value = city,
                onChangeValue = { city = it },
                isLast = true,
                performAction = { focusManager.clearFocus() })

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Tanggal Lahir")
                OutlinedButton(
                    enabled = inEditing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    onClick = {
                        focusManager.clearFocus()
                        openDialog.value = true
                    }, shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = born.ifEmpty { "Pilih tanggal" },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "datePicker")
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(.5f)) {
                    if (inEditing) {
                        ScantionButton(
                            modifier = Modifier.fillMaxWidth().padding(end = 5.dp),
                            onClick = {
                                hasRead = false
                                inEditing = false
                            },
                            text = "Batal",
                            outlineButton = true
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ScantionButton(
                        modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
                        onClick = {
                            inEditing = if (!inEditing){
                                true
                            } else {
                                if (user != null) {
                                    if (user.displayName != name){
                                        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                                    }
                                }
                                val updates = hashMapOf<String, Any>(
                                    "name" to name,
                                    "city" to city,
                                    "born" to born
                                )
                                docRef?.update(updates)
                                    ?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                                    ?.addOnFailureListener { exception -> Log.w(TAG, "Error updating document", exception) }
                                false
                            }
                        },
                        text = if (inEditing) "Simpan" else "Edit",
                        outlineButton = false
                    )
                }
            }
        }
    }
}
