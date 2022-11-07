package kz.busjol.presentation.driver.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import kz.busjol.R
import kz.busjol.presentation.NotFoundView

@Destination
@Composable
fun DriverMainScreen() {
    val scope = rememberCoroutineScope()
    val list = remember { listOf(Int) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.Black,
            fontWeight = FontWeight.W700,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 22.dp)
        )


        if (list.isEmpty()) {
            NotFoundView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 86.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 6.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 38.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 34.dp),
                content = {
                    item {
                        list.forEach {

                        }
                    }
                }
            )
        }
    }
}