# Purple Flask - A Lab Inventory Management Application

Managing Lab inventory made easy!! 🧪

![APK Size](https://img.shields.io/static/v1?label=code%20size&message=16,048%20KB&color=blue&logo=github)
![Last Commit](https://img.shields.io/static/v1?label=Last%20Commit&message=11%20June&color=red&logo=git)
![Last Commit](https://img.shields.io/static/v1?label=Language&message=Java&color=blue)
![Android Studio](https://img.shields.io/badge/IDE-Android%20Studio-green?logo=android-studio)

Lab Inventory Management solutions using `firebase`

<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/firebase/firebase-plain.svg" alt="Firebase Logo" width="200" height="200">

## Features and Interfaces

1. Landing Page and splash screen
   - Seamless splash screen and landing page explaining features of the app.
   - <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/splash_screen.jpg" alt="Splash Screen" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/viewpage_adapter.jpg" alt="View Page Adapter" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/store.jpg" alt="Store" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/safe.jpg" alt="Safe" width="200" height="400" style="margin-right: 30px;">
    </div>

2. Sign Up for College admin (on right side) and college students enrolled in each department (left side) using `firebase authentication` for registering a new user and `realtime-database` for storing other details.
   - <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/Admin_signup.jpg" alt="Admin Signup" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Student/Student_signUp.jpg" alt="Student Signup" width="200" height="400" style="margin-right: 30px;">
    </div>
   
3. Dashboard for college admins (on right side) with access to all the departments of the college and dashboard for students (on left side) with `read-only` features.
   - <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/WhatsApp%20Image%202024-05-28%20at%2021.53.55_35dee766.jpg" alt="Admin Signup" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Student/Student_Dashboard.jpg" alt="Student Signup" width="200" height="400" style="margin-right: 30px;">
    </div>
   
4. `CRUD` operations can be performed by the college admin on labs and lab equipments the images are 1. `Creating` Lab 2. `Reading` Lab details 3. `Editing` Lab details 4. `Deleting` the Lab.
   - <div style="display: flex; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/WhatsApp%20Image%202024-05-28%20at%2022.20.35_7e86aad5.jpg" alt="Creating a Lab" width="200" height="400">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Reading_lab.jpg" alt="Reading Lab details" width="200" height="400">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin_edit.jpg" alt="Editing lab details" width="200" height="400">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/Admin_delete.jpg" alt="Deleting a lab" width="200" height="400">
    </div>

5. `Date Picker` for adding establishment dates and Purchase dates of Labs and Lab equipments respectively.
   - <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/WhatsApp%20Image%202024-05-28%20at%2023.19.01_64ffb154.jpg" alt="Admin Signup" width="200" height="400" style="margin-right: 30px;">
    </div>

6. Uploading the documents from the application which are then stored in `firebase-storage`.
   - <div style="display: flex; flex-wrap: wrap; gap: 10px; margin-top: 10px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/Uploading_dialog.jpg" alt="Admin Signup" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/iMAC_config.jpg" alt="Student Signup" width="200" height="400" style="margin-right: 30px;">
        <img src="https://github.com/Sarthak9504/Purple-Flask/blob/master/Purple-Flask/Admin/upload_done.jpg" alt="Student Signup" width="200" height="400" style="margin-right: 30px;">
    </div>

## Tools and Languages

<div style="display: flex; gap: 10px; margin-top: 10px;">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original-wordmark.svg" alt="Java" width="100" height="100">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/androidstudio/androidstudio-original.svg" alt="Android Studio" width="100" height="100">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" alt="Git" width="100" height="100">
        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/xml/xml-original.svg" alt="XML" width="100" height="100">
        <img src="https://github.com/Pedro-Murilo/icons-for-readme/blob/main/.github/firebase-icon.svg" alt="firebase icon" width="100" height="100">
    </div>

## Points to remeber while testing the app
  - Make sure to keep your internet on.
  - Allow the **Storage Permissions** as it requires for uploading the documents.
  - Uploading documents takes time if **INTERNET** is slow.
  - Do not clear the app's **cache**.
  - The application is currently available for android version 11 and above.

## Instructions for contributing to the project
  - Create an issue or look for an existing issue.
  - Fork the repository to your github account using the **Fork** button at top right corner.
  - Clone the forked repository to your local android studio setup.
    ```bash
    git clone https://github.com/Your-Username/Your-Repository.git
  - Make your changes and commit them.
    ```bash
    git add .
    git commit -m "Your commit message"
    git push origin main
  - Create a Pull request.
  - Thank you for contributing!!.

## Video Demo 
- [Click Here](https://drive.google.com/file/d/1oAVTyIZi2A0YamBhVqbi0vlmdhpnmTqr/view?usp=sharing)


## Contact me at
  - [![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/sarthak-p-645381202/)


