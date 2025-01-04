package com.dscreate_app.bookstoreapp.ui.details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dscreate_app.bookstoreapp.R
import com.dscreate_app.bookstoreapp.ui.details_screen.models.DetailsNavObj

@Preview(showBackground = true)
@Composable
fun DetailsScreen(
    detailsNavObj: DetailsNavObj = DetailsNavObj(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = detailsNavObj.imageUrl,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(190.dp)
                        .background(Color.LightGray),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.book_detail_category_title),
                        color = Color.Gray
                    )
                    Text(
                        text = detailsNavObj.category,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(R.string.book_detail_author_title),
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(R.string.book_detail_author),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(R.string.book_detail_data_realise_title),
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(R.string.book_detail_data_realise),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(R.string.book_detail_data_score_title),
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(R.string.book_detail_data_score),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = detailsNavObj.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = detailsNavObj.description,
                fontSize = 16.sp
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            val bookPriceTemplate =
                LocalContext.current.getString(R.string.book_detail_price_title_template)
            Text(text = String.format(bookPriceTemplate, detailsNavObj.price))
        }
    }
}