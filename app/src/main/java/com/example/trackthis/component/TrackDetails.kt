package com.example.trackthis.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.trackthis.R
import com.example.trackthis.data.Topic
import com.example.trackthis.data.listOfVisualizedTopics


@Composable
fun TrackDetails(modifier: Modifier = Modifier, topic: Topic) {
    var userNameInput by remember { mutableStateOf("") }
    var objectivesInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium)),
            text = stringResource(topic.name),
            style = MaterialTheme.typography.headlineMedium
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            label = R.string.user_name,
            leadingIcon = Icons.Filled.Person,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = userNameInput,
            onValueChanged = { userNameInput = it }
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            label = R.string.daily_effort,
            leadingIcon = Icons.Filled.AccessTime,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = objectivesInput,
            onValueChanged = { objectivesInput = it }
        )
        EditField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            label = R.string.final_goal,
            leadingIcon = Icons.Filled.CheckCircleOutline,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = objectivesInput,
            onValueChanged = { objectivesInput = it }
        )
        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .align(Alignment.End)
        ) {
            Icon(
                /*Add a ToolTip to the FAB*//*TODO*/
                Icons.Filled.Add,
                null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun EditField(
    @StringRes label: Int,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(leadingIcon,null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}



@Preview
@Composable
fun TrackDetailsPreview() {
    TrackDetails(topic = listOfVisualizedTopics[0])
}