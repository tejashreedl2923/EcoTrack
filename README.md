# EcoTrack – Smart Water Consumption and Location Tracking Application

## Project Overview

EcoTrack is an Android-based mobile application developed using Kotlin that helps users monitor their daily water consumption, maintain hydration goals, and track the geographical locations where they consume water.

The application allows users to record their water intake, monitor daily progress, view previous consumption records, and identify drinking locations using GPS and Google Maps integration.

EcoTrack aims to encourage healthy hydration habits by providing an easy-to-use platform for tracking and managing daily water consumption.

## Objectives

- Monitor daily water consumption.
- Allow users to set personalized daily hydration goals.
- Track water consumption progress using a visual progress bar.
- Record the date and time of every water consumption entry.
- Capture geographical locations using GPS.
- Store consumption records locally using SQLite.
- Display previous drinking locations using Google Maps.
- Maintain consumption history for future reference.

## Features

### 1. Dashboard

- Displays total water consumed during the current day.
- Shows the daily water consumption goal.
- Calculates the remaining water quantity.
- Displays visual progress using a ProgressBar.
- Provides navigation to other application screens.

### 2. Water Consumption Tracking

- Allows users to enter water consumption in millilitres.
- Records the amount of water consumed.
- Automatically stores the date and time.
- Captures the current geographical location when available.
- Saves consumption records in the SQLite database.

### 3. Daily Water Goal

- Allows users to set a personalized daily water target.
- Stores the target using SharedPreferences.
- Calculates the remaining water quantity.
- Updates the dashboard progress automatically.

### 4. Location Tracking

- Uses Google Fused Location Provider.
- Captures the user's latitude and longitude.
- Requests location permission at runtime.
- Stores geographical coordinates with consumption records when available.
- Allows water consumption records to be saved without location information.

### 5. Consumption History

- Displays previously recorded water consumption entries.
- Shows consumption quantity, date, and time.
- Displays geographical coordinates when available.
- Retrieves historical records from SQLite.
- Uses a custom HistoryAdapter to display consumption records.

### 6. Google Maps Integration

- Uses Google Maps SDK for Android.
- Displays recorded drinking locations as map markers.
- Shows consumption details when a marker is selected.
- Helps users identify geographical locations associated with their water intake.

### 7. Settings

- Allows users to manage application preferences.
- Provides options to configure daily water consumption goals.
- Helps users manage their tracking preferences.

## Technologies Used

| Technology | Purpose |
|---|---|
| Kotlin | Android application development |
| XML | User interface design |
| Android Studio | Development environment |
| SQLite | Local database management |
| SQLiteOpenHelper | Database creation and operations |
| SharedPreferences | Storing daily water goals |
| Google Fused Location Provider | GPS location tracking |
| Google Maps SDK | Displaying drinking locations |
| Material Design Components | User interface components |
| Android Intents | Navigation between activities |
| Runtime Permissions | Accessing device location |

## Application Architecture

The application follows an activity-based Android architecture.

### Main Components

- **MainActivity.kt:** Displays the dashboard and daily water consumption progress.
- **AddWaterActivity.kt:** Allows users to enter and save water consumption.
- **HistoryActivity.kt:** Displays previously stored consumption records.
- **HistoryAdapter.kt:** Manages the display of water consumption history entries.
- **MapActivity.kt:** Displays drinking locations using Google Maps.
- **SettingsActivity.kt:** Handles application settings and preferences.
- **DatabaseHelper.kt:** Manages SQLite database operations.
- **LocationHelper.kt:** Handles geographical location retrieval.
- **WaterEntry.kt:** Represents individual water consumption records.

## Project Structure

```text
EcoTrack/
│
├── app/
│   └── src/
│       └── main/
│           │
│           ├── java/com/example/ecotrack/
│           │   ├── AddWaterActivity.kt
│           │   ├── DatabaseHelper.kt
│           │   ├── HistoryActivity.kt
│           │   ├── HistoryAdapter.kt
│           │   ├── LocationHelper.kt
│           │   ├── MainActivity.kt
│           │   ├── MapActivity.kt
│           │   ├── SettingsActivity.kt
│           │   └── WaterEntry.kt
│           │
│           ├── res/
│           │   ├── layout/
│           │   ├── drawable/
│           │   ├── mipmap/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── gradle/
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Database Design

EcoTrack uses SQLite for storing water consumption information locally on the device.

### Table: water_entries

| Field | Data Type | Description |
|---|---|---|
| id | INTEGER | Primary key |
| amount | INTEGER | Water consumed in millilitres |
| date | TEXT | Consumption date and time |
| latitude | REAL | Geographical latitude |
| longitude | REAL | Geographical longitude |

### SharedPreferences

Preference file: `EcoTrackPrefs`

| Key | Data Type | Description |
|---|---|---|
| dailyGoal | Integer | User's daily water consumption target |

## Application Workflow

1. The user launches the EcoTrack application.
2. The dashboard displays the current day's water consumption and daily goal.
3. The user enters the quantity of water consumed.
4. The application records the current date and time.
5. The application retrieves the geographical location when permission is granted and location is available.
6. Consumption details are stored in SQLite.
7. The dashboard updates the total consumption and remaining quantity.
8. The user can view previous records through the History screen.
9. The user can view recorded drinking locations through Google Maps.
10. The user can manage application preferences through Settings.

## Installation and Setup

### Prerequisites

- Android Studio
- Android SDK
- Kotlin support
- Android emulator or physical Android device
- Internet connection for Google Maps
- Google Maps API key

### Steps to Run the Project

1. Clone or download the project repository.
2. Open Android Studio.
3. Select Open and choose the EcoTrack project folder.
4. Allow Gradle to sync the project.
5. Configure the Google Maps API key.
6. Enable Maps SDK for Android in Google Cloud Console.
7. Connect an Android device or start an emulator.
8. Click Run to install and launch the application.

## Google Maps API Configuration

1. Visit Google Cloud Console.
2. Create a new project.
3. Enable Maps SDK for Android.
4. Generate an API key under Credentials.
5. Add the API key to AndroidManifest.xml.

Configure the following metadata inside the application tag:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

Restrict the API key to the Android application and its signing certificate.

## Permissions Required

The application requires the following permissions:

- `ACCESS_FINE_LOCATION`: For precise geographical location.
- `ACCESS_COARSE_LOCATION`: For approximate geographical location.
- `INTERNET`: For Google Maps connectivity.

Location permissions are requested at runtime. Users can still record water consumption if location permission is denied, but geographical coordinates will not be available for those entries.

## Validation and Error Handling

The application handles the following situations:

- Empty water consumption input.
- Invalid or zero water quantity.
- Invalid daily water goal.
- Location permission denial.
- GPS location unavailable.
- Empty consumption history.
- Database insertion failures.
- Internet connectivity issues affecting Google Maps.

## Results

EcoTrack provides a platform for monitoring and managing daily water consumption.

The expected results include:

- Successful recording of water consumption in millilitres.
- Automatic calculation of daily water intake.
- Calculation of remaining water against the daily goal.
- Storage and retrieval of consumption records using SQLite.
- Persistent storage of daily water goals using SharedPreferences.
- Capture of geographical coordinates when location services are available.
- Display of recorded drinking locations using Google Maps.
- Improved awareness of personal hydration habits.

## Screenshots

Add screenshots of the running EcoTrack application in this section.

### 1. Dashboard

Displays daily water consumption, hydration goal, remaining quantity and progress.

<img width="718" height="1600" alt="image" src="https://github.com/user-attachments/assets/4f64b471-651e-47d0-a86a-90f983105556" />

### 2. Add Water Screen

Allows users to enter and save their water consumption.

<img width="1080" height="2404" alt="image" src="https://github.com/user-attachments/assets/42b3e6ee-5e08-464e-99e9-7d20af2a32b6" />

### 3. Consumption History

Displays previously recorded water consumption details with dates and times.

<img width="718" height="1600" alt="image" src="https://github.com/user-attachments/assets/b0e97593-3bc5-4667-a904-4ab8739cf1c8" />


### 4. Location Tracking

Displays recorded drinking locations using Google Maps markers.

<img width="718" height="1600" alt="image" src="https://github.com/user-attachments/assets/dde0ce20-3d13-4ce6-8a36-0c65f73d78de" />


### 5. Settings Screen

Allows users to manage daily water goals and application preferences.


<img width="718" height="1600" alt="image" src="https://github.com/user-attachments/assets/4bad89a1-8df7-4550-b859-8b6525f374df" />


## Future Enhancements

- User registration and login.
- Quick-add water consumption buttons.
- Daily and weekly consumption charts.
- Date-wise history filtering.
- Hydration reminders and notifications.
- Water consumption statistics.
- Personalized hydration recommendations.
- Cloud database synchronization.
- Export consumption history as PDF or CSV.

## Expected Outcome

EcoTrack provides a convenient way to monitor daily water consumption and encourage healthy hydration habits. It combines local data storage, GPS location tracking, and Google Maps integration to help users manage their water intake and maintain a record of their drinking locations.

## Conclusion

EcoTrack is a smart Android application that integrates mobile application development concepts such as Kotlin programming, XML layouts, SQLite database management, SharedPreferences, runtime permissions, GPS location services, and Google Maps.

The project demonstrates how Android technologies can be combined to develop a practical application that supports daily hydration monitoring and location-based consumption tracking.

## Author

**Project Name:** EcoTrack – Smart Water Consumption and Location Tracking Application

**Platform:** Android

**Programming Language:** Kotlin

**UI Design:** XML

**IDE:** Android Studio
