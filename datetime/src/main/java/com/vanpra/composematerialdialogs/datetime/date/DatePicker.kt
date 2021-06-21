package com.vanpra.composematerialdialogs.datetime.date

import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.testTag
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
import com.vanpra.composematerialdialogs.datetime.util.shortLocalName
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle.FULL
import java.util.Locale

/**
 * @brief A date picker body layout
 *
 * @param initialDate time to be shown to the user when the dialog is first shown.
 * Defaults to the current date if this is not set
 * @param yearRange the range of years the user should be allowed to pick from
 * @param waitForPositiveButton if true the [onDateChange] callback will only be called when the
 * positive button is pressed, otherwise it will be called on every input change
 * @param onDateChange callback with a LocalDateTime object when the user completes their input
 */
@Composable
fun MaterialDialog.datepicker(
    initialDate: LocalDate = LocalDate.now(),
    title: String = "SELECT DATE",
    colors: DatePickerColors = DatePickerDefaults.colors(),
    yearRange: IntRange = IntRange(1900, 2100),
    waitForPositiveButton: Boolean = true,
    onDateChange: (LocalDate) -> Unit = {}
) {
    val datePickerState = remember {
        DatePickerState(initialDate, colors, yearRange, dialogBackgroundColor!!)
    }

    DatePickerImpl(title = title, state = datePickerState)

    if (waitForPositiveButton) {
        DialogCallback { onDateChange(datePickerState.selected) }
    } else {
        DisposableEffect(datePickerState.selected) {
            onDateChange(datePickerState.selected)
            onDispose { }
        }
    }
}

@Composable
internal fun DatePickerImpl(title: String, state: DatePickerState) {
    val pagerState = rememberPagerState(
        pageCount = ((state.yearRange.last - state.yearRange.first) + 1) * 12,
        initialPage = (state.selected.year - state.yearRange.first) * 12 + state.selected.monthValue - 1
    )

    Column(Modifier.fillMaxWidth()) {
        CalendarHeader(title, state)
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            flingBehavior = PagerDefaults.defaultPagerFlingConfig(
                state = pagerState,
                snapAnimationSpec = spring(stiffness = 1000f)
            )
        ) { page ->
            val viewDate = remember {
                LocalDate.of(
                    state.yearRange.first + page / 12,
                    page % 12 + 1,
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
                        YearPicker(viewDate, state, pagerState)
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
) {
    val gridState = rememberLazyListState((viewDate.year - state.yearRange.first) / 3)
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        state = gridState,
        modifier = Modifier.background(state.dialogBackground)
    ) {
        itemsIndexed(state.yearRange.toList()) { _, item ->
            val selected = remember { item == viewDate.year }
            YearPickerItem(year = item, selected = selected, colors = state.colors) {
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
private fun YearPickerItem(
    year: Int,
    selected: Boolean,
    colors: DatePickerColors,
    onClick: () -> Unit
) {
    Box(Modifier.size(88.dp, 52.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(72.dp, 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.backgroundColor(selected).value)
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
                    color = colors.textColor(selected).value,
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
    val month = remember { viewDate.month.getDisplayName(FULL, Locale.getDefault()) }
    val yearDropdownIcon = remember(state.yearPickerShowing) {
        if (state.yearPickerShowing) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    }

    Box(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
            .height(24.dp)
            .fillMaxWidth()
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
                Icon(
                    yearDropdownIcon,
                    contentDescription = "Year Selector",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                modifier = Modifier
                    .testTag("dialog_date_prev_month")
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage - 1 >= 0)
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ),
                tint = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.width(24.dp))

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                modifier = Modifier
                    .testTag("dialog_date_next_month")
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage + 1 < pagerState.pageCount)
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    ),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun CalendarView(viewDate: LocalDate, state: DatePickerState) {
    Column(Modifier.padding(start = 12.dp, end = 12.dp).testTag("dialog_date_calendar")) {
        DayOfWeekHeader()
        val calendarDatesData = remember { getDates(viewDate) }
        val datesList = remember { IntRange(1, calendarDatesData.second).toList() }
        val possibleSelected = remember(state.selected) {
            viewDate.year == state.selected.year && viewDate.month == state.selected.month
        }

        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            for (x in 0 until calendarDatesData.first) {
                item { Box(Modifier.size(40.dp)) }
            }

            items(datesList) {
                val selected = remember(state.selected) {
                    possibleSelected && it == state.selected.dayOfMonth
                }

                DateSelectionBox(it, selected, state.colors) {
                    state.selected = viewDate.withDayOfMonth(it)
                }
            }
        }
    }
}

@Composable
private fun DateSelectionBox(date: Int, selected: Boolean, colors: DatePickerColors, onClick: () -> Unit) {
    Box(
        Modifier
            .testTag("dialog_date_selection_$date")
            .size(40.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = onClick,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            date.toString(),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colors.backgroundColor(selected).value)
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                color = colors.textColor(selected).value,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun DayOfWeekHeader() {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            DatePickerState.dayHeaders.forEach { it ->
                item {
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
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(title: String, state: DatePickerState) {
    val month = remember(state.selected) { state.selected.month.shortLocalName }
    val day = remember(state.selected) { state.selected.dayOfWeek.shortLocalName }

    Box(
        Modifier
            .background(state.colors.headerBackgroundColor)
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp)) {
            Text(
                text = title,
                modifier = Modifier.paddingFromBaseline(top = 32.dp),
                color = state.colors.headerTextColor,
                style = TextStyle(fontSize = 12.sp)
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = 64.dp)
            ) {
                Text(
                    text = "$day, $month ${state.selected.dayOfMonth}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = state.colors.headerTextColor,
                    style = TextStyle(fontSize = 30.sp, fontWeight = W400)
                )
            }
        }
    }
}

private fun getDates(date: LocalDate): Pair<Int, Int> {
    val numDays = date.month.length(date.isLeapYear)
    val firstDay = date.withDayOfMonth(1).dayOfWeek.value % 7

    return Pair(firstDay, numDays)
}
