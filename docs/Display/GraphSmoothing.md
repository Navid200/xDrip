## Graph Smoothing  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Display](./Display.md) >> Graph smoothing  
  
You can enable a low-pass filter to reduce the transients.  
It is impossible to have a low-pass filter that does not cause delay.  If this was applied to all data including the current reading, it would add a delay, which would be very bad.  It only affects old readings.  It does not affect the current or the recent readings.  
  
To enable the filter, enable:  
`Settings` &#8722;> `xDrip+ Display Settings` &#8722;> `Graph Settings` &#8722;> `Graph Smoothing`  
![](./images/GraphSmoothingEnable.png)  
  
After you enable graph smoothing, you can also see the unfiltered readings if you want.  For that, enable `Settings` &#8722;> `Less common settings` &#8722;> `Other misc options` &#8722;> `Show unsmoothed`.  

  
