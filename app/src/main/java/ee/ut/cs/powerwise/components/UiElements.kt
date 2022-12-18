package ee.ut.cs.powerwise.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// TODO - MAKE BUTTONS WORK
@Composable
fun DateSelector(date: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Button(modifier = Modifier,
            onClick = {
                //TODO - Make buttons work
            }) { Text("Prev") }
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Text(
            text = date,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(140.dp),
            fontSize = 20.sp
        )
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Button(modifier = Modifier,
            onClick = {
                //TODO - Make buttons work
            }) { Text("Next") }
    }
}

@Preview
@Composable
fun PreviewDateSelector() {
    DateSelector("12.12.2022")
}


@Composable
fun CurrentPrice(price: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp)
    ) {
        Text(
            "Hetkene hind",
            modifier = Modifier
                .padding(bottom = 3.dp),
            textAlign = TextAlign.Right
        )
        Text(
            "$price senti/kWh",
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(50))
                .padding(top = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun Logo() {

}

@Composable
fun Info(text: String) {

}

@Composable
fun Header() {

}
