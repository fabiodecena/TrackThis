package com.example.trackthis.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trackthis.R
import com.example.trackthis.data.Topic

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
        .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(colorResource(id = R.color.light_grey)),
        elevation = CardDefaults.cardElevation(dimensionResource(id = R.dimen.padding_small))
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = topic.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 68.dp, height = 68.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = stringResource(id = topic.name),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (expanded) {
            Divider()
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.play_circle_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.padding_medium))
                        .clickable {  }
                )
                Text(
                    text = "Start to Track your Progress about ${stringResource(id = topic.name)}",
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}