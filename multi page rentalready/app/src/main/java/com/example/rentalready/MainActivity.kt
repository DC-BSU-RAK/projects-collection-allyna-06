package com.example.rentalready

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay

// --- BRAND COLORS FROM LOGO ---
val ElectricGreen = Color(0xFF4EEB93)
val ElectricTeal = Color(0xFF00E5FF)
val BrandNavy = Color(0xFF0B121E)
val BrandCardBlue = Color(0xFF161F2E)
val BrandMutedBlue = Color(0xFF2C3E50)

val BrandGradient = Brush.linearGradient(
    colors = listOf(ElectricGreen, ElectricTeal)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = ElectricGreen,
                    secondary = ElectricTeal,
                    background = BrandNavy,
                    surface = BrandCardBlue,
                    onPrimary = BrandNavy,
                    onSurface = Color.White
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = BrandNavy) {
                    EVAppNavigation()
                }
            }
        }
    }
}

@Composable
fun EVAppNavigation() {
    val navController = rememberNavController()
    val showInstructions = rememberSaveable { mutableStateOf(true) }

    if (showInstructions.value) {
        AlertDialog(
            onDismissRequest = { showInstructions.value = false },
            containerColor = BrandCardBlue,
            title = { Text("Welcome to Rental-Ready", color = ElectricGreen, fontWeight = FontWeight.Bold) },
            text = { Text("Power your journey with our premium electric fleet. Select your drive and explore.", color = Color.LightGray) },
            confirmButton = {
                Button(
                    onClick = { showInstructions.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricGreen)
                ) {
                    Text("GET STARTED", fontWeight = FontWeight.Black)
                }
            }
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = BrandNavy,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black.copy(alpha = 0.5f),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "showroom" } == true,
                    onClick = {
                        navController.navigate("showroom") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Filled.DirectionsCar, contentDescription = null) },
                    label = { Text("Showroom") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandNavy,
                        indicatorColor = ElectricGreen,
                        selectedTextColor = ElectricGreen,
                        unselectedIconColor = BrandMutedBlue,
                        unselectedTextColor = BrandMutedBlue
                    )
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "settings" } == true,
                    onClick = {
                        navController.navigate("settings") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandNavy,
                        indicatorColor = ElectricGreen,
                        selectedTextColor = ElectricGreen,
                        unselectedIconColor = BrandMutedBlue,
                        unselectedTextColor = BrandMutedBlue
                    )
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("showroom") { ShowroomScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

data class Vehicle(val name: String, val category: String, val range: String, val imageRes: Int)

@Composable
fun ShowroomScreen() {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    val savedCategory = prefsManager.getPreferredCategory()

    val fleet = remember {
        listOf(
            Vehicle("Tesla Model 3", "Luxury", "350 miles", R.drawable.teslamodel3),
            Vehicle("Nissan Leaf", "Economy", "150 miles", R.drawable.nissanleaf),
            Vehicle("Geely Geometry C", "Economy", "285 miles", R.drawable.geelygeomtryc),
            Vehicle("Porsche Taycan", "Luxury", "300 miles", R.drawable.porschetaycan)
        )
    }

    val displayedFleet = remember(savedCategory) {
        if (savedCategory == "All") fleet else fleet.filter { it.category == savedCategory }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "ELECTRIC SHOWROOM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 2.sp
        )
        Text(
            text = "PREFERENCE: ${savedCategory.uppercase()}",
            color = ElectricTeal,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(displayedFleet) { vehicle ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandCardBlue),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = vehicle.imageRes),
                            contentDescription = vehicle.name,
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.03f)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {
                            Text(
                                text = vehicle.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Gradient Category Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.background(BrandGradient, RoundedCornerShape(6.dp)),
                                color = Color.Transparent
                            ) {
                                Text(
                                    text = vehicle.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = BrandNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.BatteryChargingFull,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = ElectricGreen
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = vehicle.range,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefsManager = remember { PreferencesManager(context) }
    var currentSelection by remember { mutableStateOf(prefsManager.getPreferredCategory()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("PREFERENCES", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { prefsManager.savePreferredCategory("Economy"); currentSelection = "Economy" },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentSelection == "Economy") ElectricGreen else BrandCardBlue,
                    contentColor = if (currentSelection == "Economy") BrandNavy else Color.White
                )
            ) {
                Text("ECONOMY", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { prefsManager.savePreferredCategory("Luxury"); currentSelection = "Luxury" },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentSelection == "Luxury") ElectricGreen else BrandCardBlue,
                    contentColor = if (currentSelection == "Luxury") BrandNavy else Color.White
                )
            ) {
                Text("LUXURY", fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { prefsManager.savePreferredCategory("All"); currentSelection = "All" },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, ElectricTeal)
        ) {
            Text("SHOW ALL VEHICLES", color = ElectricTeal, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(64.dp))
        Text("ACTIVE FILTER", color = BrandMutedBlue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(currentSelection.uppercase(), color = ElectricGreen, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true) {
        delay(2500L)
        navController.navigate("showroom") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BrandNavy),
        contentAlignment = Alignment.Center
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(ElectricGreen.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(48.dp))
            )
            Spacer(modifier = Modifier.height(32.dp))
            LinearProgressIndicator(
                modifier = Modifier.width(180.dp).height(2.dp).clip(RoundedCornerShape(1.dp)),
                color = ElectricGreen,
                trackColor = ElectricTeal.copy(alpha = 0.1f)
            )
        }
    }
}
