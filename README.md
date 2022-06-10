=====================================
=== Project Instructions
=====================================
1. Create a Login screen to ask users to login using an email address or a Google account. Upon successful login, navigate the user to the Reminders screen. If there is no account, the app should navigate to a Register screen.
2. Create a Register screen to allow a user to register using an email address or a Google account.
3. Create a screen that displays the reminders retrieved from local storage. If there are no reminders, display a "No Data" indicator. If there are any errors, display an error message.
4. Create a screen that shows a map with the user's current location and asks the user to select a point of interest to create a reminder.
5. Create a screen to add a reminder when a user reaches the selected location. Each reminder should include
  * title
  * description
  * selected location
6. Reminder data should be saved to local storage.
7. For each reminder, create a geofencing request in the background that fires up a notification when the user enters the geofencing area.
8. Provide testing for the ViewModels, Coroutines and LiveData objects.
9. Create a FakeDataSource to replace the Data Layer and test the app in isolation.
10. Use Espresso and Mockito to test each screen of the app:
  * Test DAO (Data Access Object) and Repository classes.
  * Add testing for the error messages.
  * Add End-To-End testing for the Fragments navigation.

Student Deliverables
- APK file of the final project.
- Git Repository or zip file with the code.


=====================================
=== Criteria
=====================================
== User Authentication
1. Create Login and Registration screens.
- Login screen allows users to login using email or a Google Account.
- If the user does not exist, the app navigates to a Registration screen.

2. Enable user accounts using Firebase Authentication and Firebase UI.
- The project includes a FirebaseUI dependency.
- Authentication is enabled through the Firebase console.

== Map View
1. Create a Map view that shows the user's current location
- A screen that shows a map and asks the user to allow the location permission to show his location on the map.
- The app works on all the different Android versions including Android Q.

2. Add functionality to allow the user to select POIs to set reminders


