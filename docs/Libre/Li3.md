---
title: xDrip & Li3 setup
description: "Guide to connecting xDrip directly to a Li3 sensor."
---

# xDrip & Li3
[xDrip](../../) >> [Features](../Features_page.md) >> xDrip & Li3 setup

> **New.** This connection has been tested on a real sensor, but by few people so far.  Use it alongside a meter or another way to see your glucose, not as your only source.
<br/>

xDrip talks to the sensor directly, by NFC and then Bluetooth.  It works with both the 14-day and the 15-day sensor.  No helper app (like OOP2) and no official app are needed.
If you only want to follow the official app, use the [web follower](../Libre3_Follow.md) instead.
<br/>

### What you need

- An xDrip build that includes Li3.  [How to Download & Install](../Installation_page.md)
- A phone with NFC.
- Internet once, to download the app credential (see step 5).
<br/>

### Setup, once

1- Set [Hardware Data Source](../HardwareDataSource.md) to `Li3`.
2- Settings >> Hardware Data Source >> NFC Scan features: turn on `Use NFC feature`.
3- Android settings: Bluetooth on and location on.  Allow xDrip location "All the time".  Android does not let xDrip find the sensor without it.
4- Set xDrip to unrestricted: allowed to run in background and excluded from [Battery optimization](../BatteryOpt.md).
5- xDrip shows the notification "Li3 setup not finished".  Tap it, or open System Status >> Li3 >> Open.  The Li3 screen offers to download the app credential; say yes, or tap `Download app credential`.  The screen should then say "App credential: Downloaded".
6- Only if you will take over a sensor already running in the official app: on the Li3 screen, tap `Look up account ID` (see below), then tap `Sensor app` and choose the official app that started the sensor: `Li3 app` if it was the app made for this sensor only, `Multi-sensor app` if it was the app that also handles other sensors.
The account ID is not your email.  It is the internal ID of the account that started the sensor, not a follower or caregiver account.  `Look up account ID` asks you to log in once with that account (needs internet).  xDrip fills in the ID and keeps neither the email nor the password.  Both can also be set in Settings >> Less common settings >> Other misc. options >> Li3.
<br/>

### Start or take over a sensor

Open System Status >> Li3 >> Open and scan the sensor with NFC while that screen is open.  xDrip explains what will happen and asks you to press OK and scan once more.  Nothing is sent to the sensor before that second scan.

- **New sensor:** xDrip starts it.  You see "Li3 ready, connecting by Bluetooth", then the warm-up counts down on the home screen.
- **Sensor running in the official app:** xDrip takes it over.  The official app stops getting readings and alarms.  This needs the account ID from step 6.

Without an account ID, a new sensor belongs to this xDrip install only.  It can never move to the official app, and it is lost if xDrip is reinstalled or its data is cleared.
<br/>

### What to expect

- Readings every 5 minutes.  With `Libre 1-minute` on (Less common settings >> Other misc. options), every minute.  Restart the phone after changing it.
- The values are the sensor's own.  xDrip does not calibrate them.  Setting `Smooth Li3 readings` (in Li3) is off by default.
- Gaps while the phone was out of range are filled from the sensor's history when it comes back.
- Do not run the official app on the same sensor at the same time.  Only one of them can be connected.
<br/>

### Troubleshooting

Open System Status >> Li3 >> Open.  It shows the sensor, the current stage and the last error.

| You see | Do this |
|---|---|
| No app credential; tap Download on the Li3 screen | Tap `Download app credential` (needs internet). |
| Location permission needed to use Bluetooth! | Tap the notification and allow location "All the time". |
| Location Is Not Enabled | Turn on location in Android settings. |
| The device did not accept the command | The account ID or official app in step 6 does not match the one that started the sensor. |
| xDrip already uses this device | Tap `Forget Device` on the Li3 screen, then scan again. |
| Lost contact with the device | Scan again and hold the phone still. |
| No readings after a restart | Wait 10 minutes; xDrip reconnects by itself. |

To report a problem: on the Li3 screen tap `Copy diagnostics` and paste it into your post in [xDrip GitHub Discussions](https://github.com/NightscoutFoundation/xDrip/discussions), with a description of what you saw and when.  Copy it right away; it holds only the last day of log.
The diagnostics never contain your PIN, keys or account ID.  Do not post your exported settings file or your account password.
