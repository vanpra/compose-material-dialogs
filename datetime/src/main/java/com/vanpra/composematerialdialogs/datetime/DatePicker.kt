package com.vanpra.composematerialdialogs.datetime

import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.util.endYear
import com.vanpra.composematerialdialogs.datetime.util.plusMonthsStartDate
import com.vanpra.composematerialdialogs.datetime.util.shortLocalName
import com.vanpra.composematerialdialogs.datetime.util.startYear
import com.vanpra.composematerialdialogs.datetime.util.yearRange
import com.vanpra.composematerialdialogs.datetime.util.yearsDateRange
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle.FULL
import java.util.Locale

internal class DatePickerState(val current: LocalDate, val dateRange: ClosedRange<LocalDate>) {
    var selected by mutableStateOf(current)
    var yearPickerShowing by mutableStateOf(false)
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

    if (waitForPositiveButton) {
        DialogCallback { onComplete(datePickerState.selected) }
    } else {
        DisposableEffect(datePickerState.selected) {
            onComplete(datePickerState.selected)
            onDispose { }
        }
    }
}

@Composable
internal fun DatePickerImpl(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    backgroundColor: Color
) {
    val initialPagerState = remember { getNumberOfPages(state.dateRange, state.current) }
    val pagerState = rememberPagerState(
        pageCount = initialPagerState.first,
        initialPage = initialPagerState.second
    )

    Column(modifier.size(328.dp, 460.dp)) {
        CalendarHeader(state)
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            flingBehavior = PagerDefaults.defaultPagerFlingConfig(
                state = pagerState,
                snapAnimationSpec = spring(stiffness = 1000f)
            )
        ) { page ->
            val viewDate = remember(page) {
                LocalDate.of(
                    state.dateRange.startYear() + (page.toLong() / 12).toInt(),
                    (page.toLong() % 12).toInt() + 1,
                    1
                )
            }

            Column {
                CalendarViewHeader(viewDate, state, pagerState)
                Box {
                    androidx.compose.animation.AnimatedVisibility(
                        state.yearPickerShowing,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0.7f)
                            .clipToBounds(),
                        enter = slideInVertically({ -it }),
                        exit = slideOutVertically({ -it })
                    ) {
                        YearPicker(
                            viewDate,
                            state,
                            pagerState,
                            backgroundColor
                        )
                    }

                    CalendarView(viewDate, state)
                }
            }
        }
    }
}

@Composable
private fun YearPicker(
    viewDate: LocalDate,
    state: DatePickerState,
    pagerState: PagerState,
    backgroundColor: Color
) {
    val yearPickerState = rememberLazyListState((viewDate.year - state.dateRange.start.year) / 3)
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        state = yearPickerState,
        modifier = Modifier.background(backgroundColor)
    ) {

        itemsIndexed(state.dateRange.yearRange().toList()) { _, item ->
            val selected = remember { item == viewDate.year }
            YearPickerItem(year = item, selected = selected) {
                if (!selected) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(
                            pagerState.currentPage + (item - viewDate.year) * 12
                        )
                    }
                }
                state.yearPickerShowing = false
            }
        }
    }
}

@Composable
private fun YearPickerItem(year: Int, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colors

    Box(Modifier.size(88.dp, 52.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(72.dp, 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (selected) colors.primary else Color.Transparent)
                .clickable(
                    onClick = onClick,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                year.toString(),
                style = TextStyle(
                    color = if (selected) colors.onPrimary else colors.onSurface,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
private fun CalendarViewHeader(
    viewDate: LocalDate,
    state: DatePickerState,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    val month = remember(viewDate.month) {
        viewDate.month.getDisplayName(FULL, Locale.getDefault())
    }
    val yearDropdownIcon = remember(state.yearPickerShowing) {
        if (state.yearPickerShowing) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
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
                .clickable(onClick = { state.yearPickerShowing = !state.yearPickerShowing })
        ) {
            Text(
                "$month ${viewDate.year}",
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

        val nextMonth = remember(viewDate) { viewDate.plusMonthsStartDate(1) in state.dateRange }
        val previousMonth =
            remember(viewDate) { viewDate.plusMonthsStartDate(-1) in state.dateRange }

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
                        onClick = {
                            if (previousMonth) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        }
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
                        onClick = {
                            if (nextMonth) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
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
        val calendarDatesData = remember(viewDate) { getDates(viewDate) }
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

        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            for (x in 0 until calendarDatesData.first) {
                item { Box(Modifier.size(40.dp)) }
            }

            items(calendarDatesData.second) {
                val selected = remember(possibleSelected, state.selected) {
                    possibleSelected && it == state.selected.dayOfMonth
                }

                val isValid = remember(it) {
                    when {
                        firstMonthInRange -> it <= state.dateRange.endInclusive.dayOfMonth
                        lastMonthInRange -> it >= state.dateRange.start.dayOfMonth
                        else -> true
                    }
                }

                DateSelectionBox(it, selected, isValid) {
                    state.selected = LocalDate.of(viewDate.year, viewDate.month, it)
                }
            }
        }
    }
}

@Composable
private fun DateSelectionBox(
    date: Int,
    selected: Boolean,
    isValid: Boolean,
    onClick: () -> Unit
) {
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

private fun getDates(date: LocalDate): Pair<Int, List<Int>> {
    val numDays = date.month.length(date.isLeapYear)
    val firstDate = date.withDayOfMonth(1)
    val firstDay = firstDate.dayOfWeek.value % 7

    val dateRange = IntRange(1, numDays).toList()

    return Pair(firstDay, dateRange)
}

private fun getNumberOfPages(
    dateRange: ClosedRange<LocalDate>,
    current: LocalDate
): Pair<Int, Int> {
    val startYear = dateRange.startYear()
    val endYear = dateRange.endYear()

    val rawMonths = (endYear - startYear) * 12
    val startMonths = dateRange.start.monthValue - 1
    val endMonths = 12 - dateRange.endInclusive.monthValue

    val totalMonths = rawMonths - startMonths - endMonths
    val currentMonth = (current.year - startYear) * 12 + current.monthValue - 1

    return Pair(totalMonths, currentMonth)
}
