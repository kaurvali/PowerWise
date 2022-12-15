package ee.ut.cs.powerwise.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DateSelector(date: String) {

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
                .border(2.dp, MaterialTheme.colorScheme.tertiary,shape= RoundedCornerShape(50))
                .padding(top=10.dp, bottom=10.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun Logo() {

}

@Composable
fun Navigator() {

}

@Composable
fun Info(text: String) {

}

@Composable
fun Header() {

}
