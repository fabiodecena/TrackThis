package com.example.trackthis.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackthis.R
import com.example.trackthis.component.charts.LineChartScreen
import com.example.trackthis.data.StartedTopicElement
import com.example.trackthis.data.removeStartedTopicElementFromList

@Composable
fun StartedTopic(
    topicElement: StartedTopicElement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    text = stringResource(topicElement.name),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.weekly_effort),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
            FloatingActionButton(
                onClick = {
                    removeStartedTopicElementFromList(topicElement.name)
                },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium2))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Element",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Card(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .height(200.dp)
                .width(350.dp),
            shape = MaterialTheme.shapes.large
        ){
            LineChartScreen()
        }
        Card(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .height(70.dp)
                .width(350.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                    ) {
                        CircleWithLetter("M")
                        CircleWithLetter("T")
                        CircleWithLetter("W")
                        CircleWithLetter("T")
                        CircleWithLetter("F")
                        CircleWithLetter("S")
                        CircleWithLetter("S")
                    }
                }
            }
        }
    }
}

@Composable
fun CircleWithLetter(letter: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.primary)
            .size(dimensionResource(R.dimen.padding_medium2)) // Adjust size as needed
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontSize = 16.sp, // Adjust font size as needed
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
fun StartedTopicPreview() {
    StartedTopic(topicElement = StartedTopicElement(R.string.architecture))
}