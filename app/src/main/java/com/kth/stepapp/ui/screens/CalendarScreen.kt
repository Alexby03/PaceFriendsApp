package com.kth.stepapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kth.stepapp.ui.viewmodels.CalendarViewModel
import com.kth.stepapp.ui.viewmodels.FakeCalendarVM
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kth.stepapp.core.entities.DayDetailDto
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    vm: CalendarViewModel,
    onBack: () -> Unit
) {
    val currentMonth by vm.currentMonth.collectAsStateWithLifecycle()
    val selectedDate by vm.selectedDate.collectAsStateWithLifecycle()
    val dayDetail by vm.dayDetail.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = currentMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        ) + " ${currentMonth.year}"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = vm::onPreviousMonth) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous month")
                    }
                    IconButton(onClick = vm::onNextMonth) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next month")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(8.dp))

            CalendarGrid(
                yearMonth = currentMonth,
                selectedDate = selectedDate,
                onDateClick = vm::onDateSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Selected Day ─────────────────────────────
            selectedDate?.let { date ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = date.format(
                            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // ─── Day Details ─────────────────────────────
            dayDetail?.let {
                DayDetailCard(it)
            } ?: run {
                selectedDate?.let {
                    Text(
                        text = "No data for this day",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen(
        vm = FakeCalendarVM(),
        onBack = {}
    )
}

@Composable
private fun DaysOfWeekHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        DayOfWeek.values().forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()

    val startOffset = (firstDayOfMonth.dayOfWeek.value % 7)

    val days = mutableListOf<LocalDate?>().apply {
        repeat(startOffset) { add(null) }
        for (day in 1..daysInMonth) {
            add(yearMonth.atDay(day))
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(days) { date ->
            if (date == null) {
                Spacer(modifier = Modifier.aspectRatio(1f))
            } else {
                DayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateClick(date) }
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        tonalElevation = if (isSelected) 4.dp else 0.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DayDetailCard(detail: DayDetailDto) {

    val isPreview = LocalInspectionMode.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Day summary",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Steps: ${detail.steps}")
            Text("Calories: ${detail.calories}")
            Text("Score: ${detail.score}")

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (isPreview) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Map preview")
                    }

                } else {

                    val geoPoints = remember(detail.routePoints) {
                        detail.routePoints.map {
                            GeoPoint(it.latitude, it.longitude)
                        }
                    }

                    val mapView = rememberMapViewWithLifecycle()

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { mapView },
                        update = { map ->

                            if (geoPoints.isEmpty()) return@AndroidView

                            map.overlays.clear()

                            val polyline = Polyline().apply {
                                outlinePaint.color = android.graphics.Color.RED
                                outlinePaint.strokeWidth = 8f
                                setPoints(geoPoints)
                            }

                            map.overlays.add(polyline)

                            val boundingBox =
                                BoundingBox.fromGeoPointsSafe(geoPoints)
                            map.zoomToBoundingBox(boundingBox, true)

                            map.invalidate()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(false)
            controller.setZoom(17.0)
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}