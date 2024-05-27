package com.example.trackthis.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.trackthis.R
import com.example.trackthis.data.Topic

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        onClick = { /*TODO*/ }) {
        Card(modifier = modifier,
            colors = CardDefaults.cardColors(colorResource(id = R.color.light_grey))
        ) {
            Row {
                Image(
                    painter = painterResource(id = topic.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 68.dp, height = 68.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = stringResource(id = topic.name),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.padding_medium),
                            top = dimensionResource(R.dimen.padding_medium),
                            end = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_small)
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_grain),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = dimensionResource(R.dimen.padding_medium))
                        )
                        Text(
                            text = topic.availableCourses.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }
}