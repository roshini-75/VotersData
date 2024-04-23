package com.example.votersdata

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.votersdata.ui.theme.VotersDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val borderColor = Color(0xFF545687)
        setContent {
            VotersDataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent()
                }
            }
        }
    }
}




//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(viewModel: MainViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val voterData by viewModel.voterData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchVoterData()
    }
    val toolbarColor = Color(0xFF263F9D)
    var ascendingOrder by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "11PB - count : 64",
                        style = MaterialTheme.typography.subtitle1, // Set the desired typography style
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White // Set the text color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation icon click */ }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            ascendingOrder = !ascendingOrder
                            viewModel.sortVoterDataByName(ascendingOrder)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        val icon =
                            if (ascendingOrder) Icons.Default.SortByAlpha else Icons.Default.ArrowDownward
                        Icon(icon, contentDescription = "Sort", tint = Color.White)
                    }
                },
                backgroundColor = toolbarColor,
                contentColor = Color.White
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            )
            {






                  OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( horizontal=20.dp,vertical = 0.dp)
                            .height(50.dp),
                            //.maxWidth(300.dp), // Adjust vertical padding
                      textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                      placeholder = { Text("Search") },
                        singleLine = true,
                        shape = RoundedCornerShape(35.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = Color(0xFFA3A5A8), // Disable focused border color change
                            unfocusedBorderColor =Color(0xFFA3A5A8), // Set the same color for focused and unfocused border
                            disabledBorderColor = Color(0xFFA3A5A8)
                        )

                    )

                Spacer(modifier = Modifier.height(16.dp))

                // Filtered voter data based on search query
                val filteredVoterData = voterData.filter { voter ->
                    val fullName = "${voter.first_name} ${voter.last_name}"
                    val relationFullName = "${voter.relationshipName} ${voter.relationshipSurname}"
                    fullName.contains(searchQuery, ignoreCase = true) ||
                            relationFullName.contains(searchQuery, ignoreCase = true) ||
                            voter.pbNo.contains(searchQuery, ignoreCase = true) ||
                            voter.voter_id.contains(searchQuery, ignoreCase = true)
                }

                // Display filtered voter data
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredVoterData.size) { index ->
                        val voter = filteredVoterData[index]
                        VoterItem(voter = voter)
                    }
                }
            }
        }
    )
}




@Composable
fun VoterItem(voter: VoterData) {
   // val borderColor = Color(0xFFA3A5A8)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 25.dp),
        elevation = 8.dp,
        border = BorderStroke(1.dp,Color.White) ,// Border color
        shape = RoundedCornerShape(16.dp) // Rounded corners
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append("${voter.first_name} ${voter.last_name}, ${voter.age}, ")
                    append("PB:${voter.pbNo}-${voter.sno}\n")
                    append("${voter.relation_type} ${voter.relationshipName} ${voter.relationshipSurname}\n")
                    append("Voter Id: ${voter.voter_id}")
                },

                color = Color(0xFF4D4E4F)
            )
        }
    }
}
