package ee.ut.cs.powerwise.components


import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.Image
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import ee.ut.cs.powerwise.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO - MAKE BUTTONS WORK
@Composable
fun DateSelector(date: LocalDate = LocalDate.now(), callback: (date: LocalDate) -> Unit) {
    val showDatePicker = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Button(modifier = Modifier,
            onClick = {
                callback(date.minusDays(1))
            }) { Text("Prev") }
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Text(
            text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(140.dp)
                .clickable {
                    showDatePicker.value = !showDatePicker.value

                    val datePicker = DatePickerDialog(
                        context,
                        { _: DatePicker, year: Int, month: Int, day: Int ->
                            Log.i("UIElements", "Chosen date $year $month $day")
                            callback(LocalDate.of(year, month+1, day))
                        },
                        date.year,
                        date.monthValue - 1,
                        date.dayOfMonth
                    )
                    datePicker.show()
                },
            fontSize = 20.sp,
        )
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Button(modifier = Modifier,
            onClick = {
                callback(date.plusDays(1))
            }) { Text("Next") }
    }
}

@Preview
@Composable
fun PreviewDateSelector() {
    DateSelector(LocalDate.now(),{})
}


@Composable
fun CurrentPrice(price: Double?) {
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
            "${price ?: "N/A"} senti/kWh",
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(50))
                .padding(top = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


@Preview
@Composable
fun Info() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.info_text),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun Header() {
    Image(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, top = 15.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.powerwise),
        contentDescription = stringResource(id = R.string.app_name)
    )

}
