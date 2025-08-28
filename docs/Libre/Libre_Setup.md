---  
title: xDrip & Libre 2/2+ setup   
description: "Guide to using xDrip with Libre devices."  
---  
  
# xDrip & Libre 2/2+  
[xDrip](../) >> [Features](./Features_page.md) >> xDrip & Libre 2/2+ setup  
  
Most of the information on this page is based on content originally provided by Phil Thompson on Facebook.  
  
You should keep xDrip [updated](../Updates.md) and never use a version older than the current stable release.  
The [Release Notes](../ReleaseNotes.md) provide a short summary of the changes in each release and highlight the current stable version.  
  
Set [Hardware Data Source](../HardwareDataSource.md) to `Libre Bluetooth`.  
  
The connection to the sensor is established by an NFC scan, not by a Bluetooth scan or pairing.  
This is enabled in NFC settings of xDrip+. Make sure faster multiblock is turned off.  

Libre 2/2+ needs OOP2 helper app for decoding.  Download it here: [link](https://drive.google.com/file/d/1hkbs60Bv2udTlMS81UStCdY4RaHR0V57/view?usp=sharing ).  
  
Running Librelink / Libre 2 app at the same time as xDrip+ is not supported.  
  
The Libre 2 reader will interfere with connectivity.  If it started the sensor, it needs to be isolated by distance or in a metal enclosure.  
  
Setting "calibration" to OFF is recommended, at least initially.  
  
xDrip+, OOP2 and the System Bluetooth app all need to be unrestricted — allowed to run in background and excluded from Battery Management.  
  
If you are using Libre 2+, you will need xDrip release [2025.06.20](https://github.com/NightscoutFoundation/xDrip/releases/tag/2025.06.20) or newer.  
See: [How to Download & Install](../Installation_page.md)  
<br/>  
  
A common mistake with Libre Direct connection is to try to pair or bond the sensor using Bluetooth, the xDrip+ System Status page / BT device (swipe sideways) shows this as Bluetooth Pairing.  
This should be set to Disabled, you can tap it to toggle it off/on or reinstall if it is stuck.  
Typical error message is "Not bonded". Correct display is "Disabled, tap to enable".  
  
If you are still stuck, post your xDrip+ status screens - classic and Bluetooth - and the OOP2 screenshot in the [Facebook Group](https://www.facebook.com/groups/xDripG5/?sorting_setting=CHRONOLOGICAL) or [xDrip GitHub Discussions](https://github.com/NightscoutFoundation/xDrip/discussions).  
Include a description of the problem. Do not obscure serial numbers or other useful data.  
  
