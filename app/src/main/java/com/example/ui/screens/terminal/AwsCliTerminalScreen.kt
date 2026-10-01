package com.example.ui.screens.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun AwsCliTerminalScreen(viewModel: MainViewModel) {
    val history by viewModel.terminalHistory.collectAsState()
    var inputCmd by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickCommands = listOf(
        "aws ec2 describe-instances",
        "aws s3 ls",
        "aws iam list-users",
        "aws lambda list-functions",
        "aws sts get-caller-identity",
        "help",
        "clear"
    )

    LaunchedEffect(history.size) {
        listState.animateScrollToItem(history.size.coerceAtLeast(1) - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalBg)
            .padding(14.dp)
    ) {
        // Terminal Window Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CloudSurfaceDark, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).background(CloudError, RoundedCornerShape(5.dp)))
                Box(modifier = Modifier.size(10.dp).background(CloudWarning, RoundedCornerShape(5.dp)))
                Box(modifier = Modifier.size(10.dp).background(CloudSuccess, RoundedCornerShape(5.dp)))
            }
            Text(
                text = "awiskar@cloud-box: ~ (aws-cli/v2.15)",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                color = TextSecondary
            )
            IconButton(
                onClick = { viewModel.executeCliCommand("clear") },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
            }
        }

        // Terminal Log Area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(TerminalBg)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(history) { line ->
                val isPrompt = line.startsWith("awiskar@cloud-box")
                val isError = line.contains("error:", ignoreCase = true)
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    ),
                    color = when {
                        isPrompt -> TerminalPrompt
                        isError -> CloudError
                        line.startsWith("{") || line.contains("\"") -> CloudBlue
                        else -> TerminalGreen
                    }
                )
            }
        }

        // Autocomplete Quick Chips
        Text(
            text = "Quick Command Autocomplete (Tap to run)",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            items(quickCommands) { cmd ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CloudCardDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CloudBorderDark),
                    modifier = Modifier.clickable {
                        viewModel.executeCliCommand(cmd)
                    }
                ) {
                    Text(
                        text = cmd,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = AwsOrangeLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Input Line
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(CloudSurfaceDark, RoundedCornerShape(8.dp))
                .border(1.dp, CloudBorderDark, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$ ",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                color = TerminalPrompt,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = inputCmd,
                onValueChange = { inputCmd = it },
                placeholder = { Text("type command, e.g. aws s3 ls", color = TextMuted, fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            IconButton(
                onClick = {
                    if (inputCmd.isNotBlank()) {
                        viewModel.executeCliCommand(inputCmd)
                        inputCmd = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = AwsOrange
                )
            }
        }
    }
}
