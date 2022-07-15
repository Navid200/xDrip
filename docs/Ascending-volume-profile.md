## Ascending Volume Profile  

Try the ascending volume profile if you haven't already.  
When the alarm triggers, it starts silent or at low volume depending on the setting.  Then, the alarm plays at a 1-minute interval.  Every time it plays, the volume increases a little bit.  Eventually, it plays it at the max. volume.  
  
To enable it, go to  
Settings &#8722;> Alarms and Alerts &#8722;> Glucose Alert Settings &#8722;> Alert Volume Profile,  
and choose "Ascending Volume Profile".  

If you cannot hear your alarm when you are in a noisy environment, this can solve the problem because it will eventually play the alarm at the very high volume.  

If your alarm wakes up or annoys others around you, this can solve the problem because it starts playing at a very low volume and you can snooze it before the volume increases.  You will have several minutes before the volume is raised to maximum.  

<br/>  

The following table summarizes the behavior of the ascending volume profile based on the settings.  

| Intended behavior | Vibrate on alert | Delay Ascending Volume | Sound | Vibration | Volume trend |  
|:-----------------:|:----------------:|:----------------------:|:-----:|:---------:|:------------:|  
| Vibrate & delayed sound | Enabled | Enabled | Delayed by 3 minutes | Starts at trigger | 0, 0, 0, 33%, 67%, 100% * |  
| Vibrate & sound | Enabled | Disabled | Starts at trigger | Starts at trigger | 30%, 30%, 30%, 33%, 67%, 100% * |  
| Delayed sound only | Disabled | Enabled | Delayed by 3 minutes | None | 0, 0, 0, 33%, 67%, 100% * |  
| Sound only | Disabled | Disabled | Starts at trigger | None | 30%, 30%, 30%, 33%, 67%, 100% * |  
  
\* The volume reaches maximum (100%) on the next reading if the next reading does not trigger another alert or does not result in the current alert ending.  The sound and vibration end as soon as the alert is snoozed.  
