package com.vanpra.composematerialdialogs.datetime

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.util.ViewPager
import com.vanpra.composematerialdialogs.datetime.util.ViewPagerScope
import com.vanpra.composematerialdialogs.datetime.util.plusMonthsStartDate
import com.vanpra.composematerialdialogs.datetime.util.shortLocalName
import com.vanpra.composematerialdialogs.datetime.util.yearsDateRange
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.TextStyle.FULL
import java.util.Locale

internal class DatePickerState(val current: LocalDate, val dateRange: ClosedRange<LocalDate>) {
    var selected by mutableStateOf(current)
}

/**
 * @brief A date picker body layout
 *
 * @param initialDate time to be shown to the user when the dialog is first shown.
 * Defaults to the current date if this is not set
 * @param dateRange the range of dates the user should be allowed to pick from
 * @param waitForPositiveButton if true the [onComplete] callback will only be called when the
 * positive button is pressed, otherwise it will be called on every input change
 * @param onComplete callback with a LocalDateTime object when the user completes their input
 */
@Composable
fun MaterialDialog.datepicker(
    initialDate: LocalDate = LocalDate.now(),
    dateRange: ClosedRange<LocalDate> = initialDate.yearsDateRange(75),
    waitForPositiveButton: Boolean = true,
    onComplete: (LocalDate) -> Unit = {}
) {
    if (initialDate !in dateRange) {
        throw IllegalArgumentException("The initialDate supplied is not in the given dateRange")
    }
    val datePickerState = remember { DatePickerState(initialDate, dateRange) }

    DatePickerImpl(
        state = datePickerState,
        backgroundColor = dialogBackgroundColor!!
    )

    val index = remember {
        val callbackIndex = callbackCounter.getAndIncrement()
        callbacks.add(callbackIndex) {}
        callbackIndex
    }

    DisposableEffect(datePickerState.selected) {
        if (waitForPositiveButton) {
            callbacks[index] = { onComplete(datePickerState.selected) }
        } else {
            onComplete(datePickerState.selected)
        }

        onDispose { callbacks[index] = {} }
    }
}

@Composable
internal fun DatePickerImpl(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    backgroundColor: Color
) {
    /* Height doesn't include datepicker button height */
    val yearRange = remember(state.dateRange) {
        IntRange(state.dateRange.start.year, state.dateRange.endInclusive.year)
    }

    Column(modifier.size(328.dp, 460.dp)) {
        CalendarHeader(state)

        val yearPickerShowing = remember { mutableStateOf(false) }
        val indexRange = remember { getIndexRange(state.dateRange) }
        ViewPager(indexRange) {
            val viewDate = remember(index) { state.current.plusMonths(index.toLong()) }
            CalendarViewHeader(viewDate, yearPickerShowing, state.dateRange)

            Box {
                androidx.compose.animation.AnimatedVisibility(
                    yearPickerShowing.value,
                    Modifier
                        .fillMaxSize()
                        .zIndex(0.7f)
                        .clipToBounds(),
                    enter = slideInVertically({ -it }),
                    exit = slideOutVertically({ -it })
                ) {
                    YearPicker(yearRange, viewDate, yearPickerShowing, backgroundColor)
                }
                CalendarView(viewDate, state)
            }
        }
    }
}

@Composable
private fun ViewPagerScope.YearPicker(
    yearRange: IntRange,
    viewDate: LocalDate,
    yearPickerShowing: MutableState<Boolean>,
    backgroundColor: Color
) {
    val state = rememberLazyListState((viewDate.year - yearRange.first) / 3)
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .background(backgroundColor)
            .padding(start = 24.dp, end = 24.dp),
        state = state
    ) {
        for (i in yearRange step 3) {
            item {
                Row {
                    for (x in 0 until 3) {
                        val year = remember(yearRange) { i + x }
                        if (year in yearRange) {
                            val selected = remember(yearRange, viewDate) { year == viewDate.year }
                            YearPickerItem(year = year, selected = selected) {
                                if (!selected) {
                                    coroutineScope.launch {
                                        plusPages((year - viewDate.year) * 12)
                                    }
                                }
                                yearPickerShowing.value = false
                            }

                            if (x != 2) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YearPickerItem(year: Int, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (selected) MaterialTheme.colors.primary else Color.Transparent
    val textColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface

    Box(Modifier.size(88.dp, 52.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(72.dp, 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .clickable(
                    onClick = onClick,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(year.toString(), style = TextStyle(color = textColor, fontSize = 18.sp))
        }
    }
}

@Composable
private fun ViewPagerScope.CalendarViewHeader(
    viewDate: LocalDate,
    yearPickerShowing: MutableState<Boolean>,
    dateRange: ClosedRange<LocalDate>
) {
    val coroutineScope = rememberCoroutineScope()

    val month = remember(viewDate) { viewDate.month.getDisplayName(FULL, Locale.getDefault()) }
    val year = remember(viewDate) { viewDate.year }
    val yearDropdownIcon = remember(yearPickerShowing.value) {
        if (yearPickerShowing.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    }

    Box(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
            .height(24.dp)
            .fillMaxWidth()
            .zIndex(1f)
    ) {
        Row(
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .clickable(onClick = { yearPickerShowing.value = !yearPickerShowing.value })
        ) {
            Text(
                "$month $year",
                modifier = Modifier
                    .paddingFromBaseline(top = 16.dp)
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(fontSize = 14.sp, fontWeight = W600),
                color = MaterialTheme.colors.onBackground
            )

            Spacer(Modifier.width(4.dp))
            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Image(
                    yearDropdownIcon,
                    contentDescription = "Year Selector",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
        }

        val nextMonth = remember(viewDate) { viewDate.plusMonthsStartDate(1) in dateRange }
        val previousMonth = remember(viewDate) { viewDate.plusMonthsStartDate(-1) in dateRange }

        Row(
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            Image(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = { if (previousMonth) coroutineScope.launch { previous() } },
                        enabled = previousMonth
                    ),
                colorFilter = ColorFilter.tint(
                    if (previousMonth) {
                        MaterialTheme.colors.onBackground
                    } else {
                        MaterialTheme.colors.onBackground.copy(0.2f)
                    }
                )
            )

            Spacer(modifier = Modifier.width(24.dp))

            Image(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = { if (nextMonth) coroutineScope.launch { next() } },
                        enabled = nextMonth
                    ),
                colorFilter = ColorFilter.tint(
                    if (nextMonth) {
                        MaterialTheme.colors.onBackground
                    } else {
                        MaterialTheme.colors.onBackground.copy(0.2f)
                    }
                )
            )
        }
    }
}

@Composable
private fun CalendarView(
    viewDate: LocalDate,
    state: DatePickerState
) {
    Column(Modifier.padding(start = 12.dp, end = 12.dp)) {
        DayOfWeekHeader()
        val month = remember(viewDate) { getDates(viewDate) }
        val possibleSelected = remember(state.selected, viewDate) {
            viewDate.year == state.selected.year && viewDate.month == state.selected.month
        }
        val firstMonthInRange = remember(viewDate) {
            viewDate.month == state.dateRange.endInclusive.month &&
                    viewDate.year == state.dateRange.endInclusive.year
        }
        val lastMonthInRange = remember(viewDate) {
            viewDate.month == state.dateRange.start.month &&
                    viewDate.year == state.dateRange.start.year
        }

        for (y in 0..5) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (x in 0 until 7) {
                    val day = remember(month, x, y) { month[y * 7 + x] }

                    val isValid: Boolean =
                        remember(firstMonthInRange, lastMonthInRange) {
                            when {
                                firstMonthInRange -> day <= state.dateRange.endInclusive.dayOfMonth
                                lastMonthInRange -> day >= state.dateRange.start.dayOfMonth
                                else -> true
                            }
                        }

                    if (day != -1) {
                        val selected = remember(state.selected, possibleSelected) {
                            possibleSelected && day == state.selected.dayOfMonth
                        }

                        DateSelectionBox(day, selected, isValid) {
                            state.selected = LocalDate.of(viewDate.year, viewDate.month, day)
                        }
                    } else {
                        Box(Modifier.size(40.dp))
                    }

                    if (x != 6) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSelectionBox(date: Int, selected: Boolean, isValid: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colors
    val backgroundColor = remember(selected, isValid) {
        if (selected) colors.primary else if (isValid) Color.Transparent else Color.Transparent
    }
    val textColor = remember(selected, isValid) {
        when {
            selected -> colors.onPrimary
            isValid -> colors.onSurface
            else -> colors.onSurface.copy(0.2f)
        }
    }

    Box(
        Modifier
            .size(40.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = onClick,
                enabled = isValid,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            date.toString(),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .wrapContentSize(Alignment.Center),
            style = TextStyle(color = textColor, fontSize = 12.sp)
        )
    }
}

@Composable
private fun DayOfWeekHeader() {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("S", "M", "T", "W", "T", "F", "S").forEachIndexed { index, it ->
            Box(Modifier.size(40.dp)) {
                Text(
                    it,
                    modifier = Modifier
                        .alpha(0.8f)
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    style = TextStyle(fontSize = 14.sp, fontWeight = W600),
                    color = MaterialTheme.colors.onBackground
                )
            }
            if (index != 6) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

// Input: Selected Date
@Composable
private fun CalendarHeader(datePickerData: DatePickerState) {
    val month = remember(datePickerData.selected.month) {
        datePickerData.selected.month.shortLocalName
    }
    val day = remember(datePickerData.selected.dayOfWeek) {
        datePickerData.selected.dayOfWeek.shortLocalName
    }

    Box(
        Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp)) {
            Text(
                text = "SELECT DATE",
                modifier = Modifier.paddingFromBaseline(top = 32.dp),
                color = MaterialTheme.colors.onPrimary,
                style = TextStyle(fontSize = 12.sp)
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 64.dp)
            ) {
                Text(
                    text = "$day, $month ${datePickerData.selected.dayOfMonth}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = MaterialTheme.colors.onPrimary,
                    style = TextStyle(fontSize = 30.sp, fontWeight = W400)
                )
            }
        }
    }
}

private fun getDates(date: LocalDate): List<Int> {
    val dates = mutableListOf<Int>()

    val firstDate = LocalDate.of(date.year, date.monthValue, 1)
    val firstDay = firstDate.dayOfWeek.value % 7
    val numDays = date.month.length(firstDate.isLeapYear)

    var counter = 1
    for (y in 0..5) {
        for (x in 0..6) {
            if ((y == 0 && x < firstDay && firstDay != 0) || counter > numDays) {
                dates.add(-1)
            } else {
                dates.add(counter)
                counter += 1
            }
        }
    }

    return dates
}

private fun getIndexRange(dateRange: ClosedRange<LocalDate>): IntRange {
    val today = LocalDate.now().withDayOfMonth(1)
    val pastMonths = Period.between(dateRange.start.withDayOfMonth(1), today).months
    val pastYear = Period.between(dateRange.start.withDayOfMonth(1), today).years
    val futureMonths = Period.between(today, dateRange.endInclusive.withDayOfMonth(1)).months
    val futureYears = Period.between(today, dateRange.endInclusive.withDayOfMonth(1)).years
    val start = -(pastMonths + (pastYear * 12))
    val end = futureMonths + (futureYears * 12)
    return start..end
}
