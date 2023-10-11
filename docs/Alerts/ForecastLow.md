## Forecast Low alert
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip Alerts](../Alerts_page.md) >> Forecast Low alert  
  
Glucose level alerts, the main alerts, alert you when your glucose is low or high.  But, they ignore the trend.  
If you are at 6 (108) and your trend is flat, you may not need an alert.  But, if you are at the same level and your glucose is rapidly dropping, you may need an alert.  
  
We also have a fast falling alert, which alerts you if the glucose is dropping very fast.  But, if your glucose is very high, you would want your glucose to be dropping fast and you wouldn't need an alert.  
  
So, it seems neither of those alerts is perfectly complete, and a combination might be what we need.  
Well, the Forecast Low alert is exactly that combination.  
  
The Forecast Low alert considers your current level as well as the trend to extrapolate how low you may be in a certain amount of time and alert you if the prediction is below a threshold.  
  
The following image shows the Extra Alerts page, where you can control the Forecast Low alert, at `Settings` &#8722;> `Alarms and Alerts` &#8722;> `Extra Alerts`.  
![](./images/ForecastLow.png)  
  
The alert will trigger if the extrapolated value drops below your low value in less than the time (in minutes) specified under `Alarm at forecasted low mins`.  
  
The forecast low alert does not take into account insulin or carbs or exercise etc.  
It takes into account the current glucose value and a few of the recent readings to calculate the trend and predict the next few readings.  If the trend is linear, it just extends the line to see when it crosses the threshold.  If the trend is a curve, it will do a polynomial extrapolation to extend the curve to see when it will cross the threshold.  
  
