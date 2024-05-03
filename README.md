# AIDL based Interaction between AudioClient App and ClipServer App.
- Andriod uses Proxy design pattern and Broker Design pattern to set up inter-process communication amongst apps. In this project we explore this communication paradigm.
- Clipserver is a Started as well as Bound Service. ClipServer App holds the music files and exposes an API to play, resume, stop and Pause the music via an Aidl interface file.
- Clipserver starts as a ForeGround Service and hence has a Notification displayed.
- AudioClient creates UI. It also starts/Stops the Clipserver service. It can bound to the service and use the API to play/resume/stop/Pause the required songs.
## Start the Audio Client. You have to start the Service for other options to become active
<img width="205" alt="AudioClient1" src="https://github.com/Jaykakkad82/Andriod_InterApp_Communication1/assets/97722419/987c8aec-0b33-4fa8-9a89-1f6896582cf5">

## Once the service Starts, You can get the list of Audio files
<img width="195" alt="AudioClient2" src="https://github.com/Jaykakkad82/Andriod_InterApp_Communication1/assets/97722419/13446f78-8cfd-48af-83ad-2b6483d8a88f">

## Now Select a music based on your mood and Play (binds to service when play is clicked, unbinds when stop is clicked)
<img width="203" alt="AudioClient3" src="https://github.com/Jaykakkad82/Andriod_InterApp_Communication1/assets/97722419/f5c65b56-aacc-4aea-825a-80bbb34b40c8">
