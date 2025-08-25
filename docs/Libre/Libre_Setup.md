---  
title: xDrip & Libre setup   
description: "Guide to using xDrip with Libre devices."  
---  
  
# xDrip & Libre  
[xDrip](../) >> [Features](./Features_page.md) >> xDrip & Libre setup  
  
Most of the content on this page is from a page provide by Phil Thompson on facebook.  
  
You should keep xDrip [updated](../Updates.md).  You should never use a version older than the current stable release.  
[Release Notes](../ReleaseNotes.md) provide a short summary of the changes in each release and highlight the current stable release.  
  
Set [Hardware Data Source](../HardwareDataSource.md) to `Libre Bluetooth`.  
  
Connection to sensor established by NFC scan, not Bluetooth scan or pairing.  
This is enabled in NFC settings of xDrip+. Make sure faster multiblock is off.  

Libre 2/2+ needs OOP2 helper app for decoding, download at: [link](https://drive.google.com/file/d/1hkbs60Bv2udTlMS81UStCdY4RaHR0V57/view?usp=sharing ).  
  
Running Librelink / Libre 2 app at the same time as xDrip+ is not supported.  
  
The Libre 2 reader will interfere with connectivity if it started a sensor, it needs to be isolated by distance or in a metal enclosure.  
  
Setting "calibration" to OFF is recommended at least initially.  
  
xDrip+, OOP2 and the System Bluetooth app all need to be unrestricted / allowed to run in background /
excluded from Battery Management.  
  
If you are using Libre 2+, you will need an xDrip release of, or more up-to-date than, [2025.06.20](https://github.com/NightscoutFoundation/xDrip/releases/tag/2025.06.20).  
[How to Download & Install](../Installation_page.md)  
<br/>  
<br/>  
  
A common mistake with Libre Direct connection is to try to pair or bond the sensor using Bluetooth, the xDrip+ System Status page / BT device (swipe sideways) shows this as Bluetooth Pairing:  
this should be set to Disabled, you can tap it to toggle it off/on or reinstall if it is stuck.  
Typical error message is "Not bonded". Correct display is "Disabled, tap to enable".  
  
If you are still stuck, post your xDrip+ status screens - classic and Bluetooth - and the OOP2 screenshot in the [Facebook Group](https://www.facebook.com/groups/xDripG5/?sorting_setting=CHRONOLOGICAL) or [xDrip GitHub Discussions](https://github.com/NightscoutFoundation/xDrip/discussions).  
Include a description of the problem. Do not obscure serial numbers or other useful data.  
  
