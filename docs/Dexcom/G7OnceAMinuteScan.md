## G7 Once a minute scan mode  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
A G7 (or One+) device transmits once every 5 minutes.  
The communication between a G7 and an app is a two-way communication.  There is an acknowledgement mechanism involved.  
If a G7 misses three consecutive acknowledgements (15 minutes), it switches to once a minute transmissions in order to help to reconnect faster, and this will continue for 12 hours or until the app successfully reconnects.  
  
xDrip scans once every 5 minutes on a regular basis.  However, when there is a disconnect, xDrip scans more frequently if `Settings` &#8722;> `Less common settings` &#8722;> `Aggressive service restarts` is enabled.  This has existed in xDrip since 2016 and is enabled by default.  You should never disable it.  
  
xDrip as is reconnects, to G7, rapidly when you return to your phone after a 15 minute or more away time.  
  
