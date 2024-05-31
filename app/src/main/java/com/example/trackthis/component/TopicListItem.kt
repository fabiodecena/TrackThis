package com.example.trackthis.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement

@Composable
fun TopicListItem(topicListItem: TopicListElement) {
    var checked by remember { mutableStateOf(topicListItem.selected) }
    Surface(
        color = MaterialTheme.colorScheme.background,
        onClick = {
            topicListItem.selected = !topicListItem.selected
            checked = topicListItem.selected
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.light_grey))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = topicListItem.imageRes),
                    contentDescription = stringResource(topicListItem.name),
                    modifier = Modifier,

                    contentScale = ContentScale.Fit
                )
                Text(
                    text = LocalContext.current.getString(topicListItem.name),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Checkbox(
                    checked = checked,
                    onCheckedChange = null
                )
            }
        }
    }
}
@Preview
@Composable
fun TopicListItemPreview() {


    }