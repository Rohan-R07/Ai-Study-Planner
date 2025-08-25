package com.example.aistudyplanner.Utils

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.aistudyplanner.ui.theme.CDotFocusedColor

data class BottomNavItem(
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    val label: String,
    val onClick: () -> Unit = {}
)

data class FabAction(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit = {}
)

// Bottom Navigation using HorizontalFloatingToolbar
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingBottomNavigation(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
    colors: FloatingToolbarColors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
    scrollBehavior: FloatingToolbarScrollBehavior? = null
) {
    HorizontalFloatingToolbar(
        expanded = true, // Always show all navigation items
        modifier = modifier,
        colors = colors,
        scrollBehavior = scrollBehavior,
        content = {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index

                IconButton(
                    onClick = {
                        onItemSelected(index)
                        item.onClick()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                        if (isSelected) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    )
}


// Expandable FAB Component
@Composable
fun ExpandableFloatingActionButton(
    fabActions: List<FabAction>,
    modifier: Modifier = Modifier,
    fabColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    fabIcon: ImageVector = Icons.Default.Add,
    fabContentDescription: String = "Quick Actions",
    showLabels: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "fab_rotation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
    ) {
        // Expanded action items
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it / 2 }
                    ),
            exit = fadeOut(animationSpec = tween(200)) +
                    slideOutVertically(
                        animationSpec = tween(200),
                        targetOffsetY = { it / 2 }
                    )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                fabActions.forEach { action ->
                    FabActionRow(
                        action = action,
                        showLabel = showLabels
                    )
                }
            }
        }

        // Main FAB
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            containerColor = fabColor,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = fabIcon,
                contentDescription = fabContentDescription,
                modifier = Modifier.rotate(rotationAngle)
            )
        }
    }
}

@Composable
private fun FabActionRow(
    action: FabAction,
    showLabel: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Action label
        if (showLabel) {
            Card(
                modifier = Modifier.wrapContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = CDotFocusedColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(8.dp),

            ) {


                    Text(
                        fontSize = 14.sp,
                        text = action.label,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .background(Unspecified),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = White,
                    )

            }
        }

        // Action button
        SmallFloatingActionButton(
            onClick = {
                action.onClick()
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Complete Layout: Bottom Nav + Expandable FAB
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomNavWithExpandableFab(
    // Bottom Navigation props
    selectedNavIndex: Int,
    onNavItemSelected: (Int) -> Unit,
    navItems: List<BottomNavItem>,

    // Expandable FAB props
    fabActions: List<FabAction>,
    fabIcon: ImageVector = Icons.Default.Add,
    fabColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,

    // Layout props
    modifier: Modifier = Modifier,
    bottomNavScrollBehavior: FloatingToolbarScrollBehavior? = null
) {
    Box(modifier = modifier) {
        // Bottom Navigation
        FloatingBottomNavigation(
            selectedIndex = selectedNavIndex,
            onItemSelected = onNavItemSelected,
            items = navItems,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            scrollBehavior = bottomNavScrollBehavior
        )

        // Expandable FAB positioned above bottom nav
        ExpandableFloatingActionButton(
            fabActions = fabActions,
            fabIcon = fabIcon,
            fabColor = fabColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = (-80).dp) // Position above bottom nav
        )
    }
}


// FIXED: Scaffold-compatible version
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScaffoldCompatibleLayout(
    // Bottom Navigation props
    selectedNavIndex: Int,
    onNavItemSelected: (Int) -> Unit,
    navItems: List<BottomNavItem>,

    // Expandable FAB props
    fabActions: List<FabAction>,
    fabIcon: ImageVector = Icons.Default.Add,
    fabColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,

    // Content
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        // Use only the expandable FAB in Scaffold's fab slot
        floatingActionButton = {
            ExpandableFloatingActionButton(
                fabActions = fabActions,
                fabIcon = fabIcon,
                fabColor = fabColor
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Your main content
                content(innerPadding)

                // Bottom Navigation as overlay
                FloatingBottomNavigation(
                    selectedIndex = selectedNavIndex,
                    onItemSelected = onNavItemSelected,
                    items = navItems,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    )
}

// PREVIEW: Fixed Scaffold usage
@Preview(showBackground = true)
@Composable
fun ScaffoldUsagePreview() {
    var selectedNavIndex by rememberSaveable { mutableIntStateOf(0) }

    MaterialTheme {
        ScaffoldCompatibleLayout(
            selectedNavIndex = selectedNavIndex,
            onNavItemSelected = { selectedNavIndex = it },
            navItems = listOf(
                BottomNavItem(Icons.Default.Home, label = "Home"),
                BottomNavItem(Icons.Default.Search, label = "Search"),
                BottomNavItem(Icons.Default.FavoriteBorder, Icons.Default.Favorite, "Favorites"),
                BottomNavItem(Icons.Default.Person, label = "Profile")
            ),
            fabActions = listOf(
                FabAction(Icons.Default.Edit, "Create Post"),
                FabAction(Icons.Default.Phone, "Take Photo"),
                FabAction(Icons.Default.Edit, "Start Video"),
                FabAction(Icons.Default.Menu, "Send Message")
            ),
            fabColor = MaterialTheme.colorScheme.tertiary
        ) { innerPadding ->
            // Your main content here
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 100.dp, // Space for bottom nav
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(30) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "Content item ${index + 1}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

// PREVIEW: Simple FAB-only usage (for Scaffold floatingActionButton)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SimpleFabInScaffoldPreview() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") }
                )
            },
            floatingActionButton = {
                // Just use the expandable FAB directly
                ExpandableFloatingActionButton(
                    fabActions = listOf(
                        FabAction(Icons.Default.Edit, "Create"),
                        FabAction(Icons.Default.Person, "Photo"),
                        FabAction(Icons.Default.Share, "Share")
                    ),
                    fabColor = MaterialTheme.colorScheme.primary
                )
            },
            bottomBar = {
                // Use traditional BottomNavigationBar or your FloatingBottomNavigation
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search") }
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(20) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Item $index",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentScreen(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Sample content
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 120.dp) // Space for bottom nav + fab
        ) {
            items(20) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "$title Item ${index + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "This is some sample content for $title screen. Item number ${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// PREVIEW: Alternative styling
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun AlternativeStylePreview() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(2) }

    MaterialTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Content
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(30) { index ->
                        ListItem(
                            headlineContent = { Text("List item $index") },
                            supportingContent = { Text("Supporting text for item $index") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                                }
                            }
                        )
                    }
                }

                // Bottom Nav + FAB with vibrant colors
                BottomNavWithExpandableFab(
                    selectedNavIndex = selectedIndex,
                    onNavItemSelected = { selectedIndex = it },
                    navItems = listOf(
                        BottomNavItem(Icons.Default.Phone, label = "Dashboard"),
                        BottomNavItem(Icons.Default.Phone, label = "Analytics"),
                        BottomNavItem(Icons.Default.Notifications, label = "Notifications"),
                        BottomNavItem(Icons.Default.Settings, label = "Settings")
                    ),
                    fabActions = listOf(
                        FabAction(Icons.Default.Add, "Add Item"),
                        FabAction(Icons.Default.ThumbUp, "Upload File"),
                        FabAction(Icons.Default.Share, "Share Content"),
                        FabAction(Icons.Default.Settings, "Add Link"),
                        FabAction(Icons.Default.Refresh, "Schedule Task")
                    ),
                    fabIcon = Icons.Default.Menu,
                    fabColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}