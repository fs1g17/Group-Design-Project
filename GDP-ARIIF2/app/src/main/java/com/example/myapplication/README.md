# MVVM Architecture 
The application is built using Model-View-ViewModel design pattern. Below is an diagram showing the different parts of the application: 

<img src="/images/wireframes/MVVM Structure.png" width=100%>

## Model 
In this project, the Model is completely remote, and made up of two parts. The NFC tag contains the pressure switch unique identifier, and the pressure switch device stores all the settings information.

## View 
The main entry point for an Android application is an [Activity](https://developer.android.com/reference/android/app/Activity). The Activity can be split up into related elements that can be grouped into [Fragments](https://developer.android.com/guide/fragments). In this app, there are 3 main Fragments, representing the 3 main screens of the applicaiton. The first screen is the connection screen. Once the app successfully connects to the pressure switch, the user is taken to the home screen of the application. There the user can find all the frequently used settings. Additional settings are accessible by going to the settings screen from the navigation drawer (which becomes visible upon swiping from the left of the screen). Below is a screenshot of the navigation graph of the application.

<img src="/images/screenshots/navigation_graph_new.PNG" width=100%>

## ViewModel 
The ViewModel prepares data to be shown by the View. In this application, the model is remote. The NFC tag contains the required information to find the pressure switch with Bluetooth, and the pressure switch itself stores all the state information about the settings. Therefore, in order to show the settings, the application must retrieve the settings using Bluetooth. The ViewModel class handles the Bluetooth communication. In order to ensure smooth operation, the Bluetooth operations are carried out on a separate thread. We designed our packet structure for transferring data between the application and the device, shown below.

<img src="/images/packet.PNG" width=50%>

The opcodes were pre-defined and agreed upon by the whole group. This communication format allowed for seamless integration. 
