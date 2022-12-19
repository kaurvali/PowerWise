package ee.ut.cs.powerwise.components


import androidx.compose.foundation.Image

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialDatePicker
import ee.ut.cs.powerwise.LocalFragmentManagerProvider
import ee.ut.cs.powerwise.R
import ee.ut.cs.powerwise.utils.Utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateSelector(date: LocalDate = LocalDate.now(), callback: (date: LocalDate) -> Unit) {
    val fragmentManager = LocalFragmentManagerProvider.current
    val selectDateText = stringResource(id = R.string.datePicker_select_date)
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
            }) { Text(stringResource(R.string.Datepicker_previous)) }
        Spacer(
            modifier = Modifier
                .width(16.dp)
        )
        Text(
            text = date.format(DateTimeFormatter.ofPattern(stringResource(R.string.DatePicker_datestring_format))),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(140.dp)
                .clickable {
                    fragmentManager?.let {
                        val datePicker =
                            MaterialDatePicker.Builder
                                .datePicker()
                                .setTitleText(selectDateText)
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build()
                        datePicker.addOnPositiveButtonClickListener {
                            callback(LocalDate.ofEpochDay(it / 86400000))
                        }
                        datePicker.show(fragmentManager,selectDateText)
                    }

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
            }) { Text(stringResource(R.string.Datepicker_next)) }
    }
}

@Composable
fun CurrentPrice(price: Double?) {
    val currentPrice = if (price != null) Utils.convertmWhtokWh(price).toString() else "N/A"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp)
    ) {
        Text(
            stringResource(R.string.CurrentPrice_text),
            modifier = Modifier
                .padding(bottom = 3.dp),
            textAlign = TextAlign.Right
        )
        Text(
            "$currentPrice s/kWh",
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
