# Project Instructions

- [x] Create a Login screen to ask users to login using an email address or a Google account. Upon successful login, navigate the user to the Reminders screen. If there is no account, the app should navigate to a Register screen.
- [x] Create a Register screen to allow a user to register using an email address or a Google account.
- [x] Create a screen that displays the reminders retrieved from local storage. If there are no reminders, display a "No Data" indicator. If there are any errors, display an error message.
- [x] Create a screen that shows a map with the user's current location and asks the user to select a point of interest to create a reminder.
- [x] Create a screen to add a reminder when a user reaches the selected location. Each reminder should include
  * title
  * description
  * selected location
- [x] Reminder data should be saved to local storage.
- [x] For each reminder, create a geofencing request in the background that fires up a notification when the user enters the geofencing area.
- [x] Provide testing for the ViewModels, Coroutines and LiveData objects.
- [x] Create a FakeDataSource to replace the Data Layer and test the app in isolation.
- [x] Use Espresso and Mockito to test each screen of the app:
  * Test DAO (Data Access Object) and Repository classes.
  * Add testing for the error messages.
  * Add End-To-End testing for the Fragments navigation.

# Student Deliverables
- APK file of the final project.
- Git Repository or zip file with the code.

# Criteria
### User Authentication
| Criteria       | Meet specifications | Completed | 
|----------------|---------------------|-----------|
| Create Login and Registration screens|  <ul><li>Login screen allows users to login using email or a Google Account</li><li>If the user does not exist, the app navigates to a Registration screen</li></ul>|<ul><li>- [x]</li><li>- [x]</li></ul>|
| Enable user accounts using Firebase Authentication and Firebase UI.|<ul><li>The project includes a FirebaseUI dependency</li><li>Authentication is enabled through the Firebase console.</li></ul>|<ul><li>- [x]</li><li>- [x]</li></ul>|

### Map View
| Criteria      | Meet specifications | Completed | 
|---------------|---------------------|-----------|
|Create a Map view that shows the user's current location|<ul><li>A screen that shows a map and asks the user to allow the location permission to show his location on the map.</li><li>The app works on all the different Android versions including Android Q.</li></ul>|<ul><li>- [x]</li><li>- [x]</li></ul>|
|Add functionality to allow the user to select POIs to set reminders|<ul><li>The app asks the user to select a location or POI on the map and add a new marker at that location. Upon saving, the selected location is returned to the Save Reminder page and the user is asked to input the title and description for the reminder.</li><li>When the reminder is saved, a geofencing request is created.</li></ul>||
|Style the map|<ul><li>Map Styling has been updated using the map styling wizard to generate a nice looking map.</li><li>Users have the option to change map type.</li></ul>||
|Display a notification when a selected POI is reached|When the user enters a geofence, a reminder is retrieved from the local storage and a notification showing the reminder title will appear, even if the app is not open.||

### Reminders
| Criteria       | Meet specifications | Completed | 
|----------------|---------------------|-----------|
|Add a screen to create reminders|<ul><li>Reminder data includes title and description.</li><li>The user-entered data will be captured using live data and data binding.</li><li>RemindersLocalRepository is used to save the reminder to the local DB. And the geofencing request will be created after confirmation.</li></ul>||
|Add a list view that displays the reminders|<ul><li>All reminders in the location DB is displayed</li><li>If the location DB is empty, a no data indicator is displayed.</li><li>User can navigate from this screen to another screen to create a new reminder.</li></ul>||
|Display details about a reminder when a selected POI is reached and the user clicked on the notification.|When the user clicks a notification, when he clicks on it, a new screen appears to display the reminder details.||



### Testing
| Criteria       | Meet specifications | Completed | 
|----------------|---------------------|-----------|
|Use MVVM and Dependency Injection to architect your app.|<ul><li>The app follows the MVVM design pattern and uses ViewModels to hold the live data objects, do the validation and interact with the data sources.</li><li>The student retrieved the ViewModels and DataSources using Koin.</li></ul>||
|Test the ViewModels, Coroutines, and LiveData|<ul><li>RemindersListViewModelTest or SaveReminderViewModelTest are present in the test package that tests the functions inside the view model.</li><li>Live data objects are tested using shouldReturnError and check_loading testing functions.</li></ul>||
|Create a FakeDataSource to replace the Data Layer and test the app in isolation.|Project repo contains a FakeDataSource class that acts as a test double for the LocalDataSource.||
|Use Espresso and Mockito to test the app UI and Fragments Navigation.|Tests include:<ul><li>Automation Testing using ViewMatchers and ViewInteractions to simulate user interactions with the app.</li><li>Testing for Snackbar and Toast messages.</li><li>Testing the fragments’ navigation.</li><li>The testing classes are at androidTest package.</li></ul>||
|Test DAO and Repository classes|<ul><li>Testing uses Room.inMemoryDatabaseBuilder to create a Room DB instance.</li><li>Testing covers:</li><li>inserting and retrieving data using DAO.</li><li>predictable errors like data not found.</li></ul>||

### Code Quality
| Criteria       | Meet specifications | Completed | 
|----------------|---------------------|-----------|
|Write code using best practices for Android development with Kotlin|Code uses meaningful variable names and method names that indicate what the method does.||

### Extras
1. Test Coverage for the whole app.
2. Update the app styling and map design using material design and map design.
3. Edit and Delete Reminders and Geofence requests.
4. Allow the user to create a shape like polygons or circles on the map to select the area.
5. Allow the user to change the reminding location range.
