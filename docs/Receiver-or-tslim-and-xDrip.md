---
title: "Dexcom Device Compatibility and Limitations"
description: "Learn how xDrip can run at the same time as a Dexcom receiver or t:slim pump. Documentation on compatibility and simultaneous data collection."
---

# Dexcom Device Compatibility and Limitations  
[xDrip](../) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Dexcom Device Compatibility and Limitations  
  
If you have a t:slim pump, start your G6 sensor on the pump—not in xDrip.  
There is no need to start the same sensor in xDrip as well.  
For G6 or Dexcom One, you also don’t need to enter the calibration code in xDrip. The transmitter is the only component that requires the calibration code.  
After the warm-up period, xDrip will detect and pick up the session. If it doesn’t, tap **Restart Collector** on the Classic Status page.  
If you use a Dexcom receiver alongside xDrip, you can. Just avoid sending duplicate commands to the transmitter.  
If you must use the Dexcom receiver, start the sensor on the receiver and allow xDrip to pick up the session after warm-up.  
  
Do not stop the sensor in xDrip thinking it only affects xDrip—it will stop the entire sensor session on the transmitter.  
  
For G7 sensors, connect with xDrip first. If you connect with the pump or receiver first, xDrip will not be able to connect.   
  
You cannot use both a t:slim pump and a Dexcom receiver at the same time since they require the same transmitter slot. For the same reason, you also cannot use xDrip and the Dexcom app simultaneously to collect from the transmitter.  
<br/>  
  
| Combination  | Resolution |
|-------------|---------|
| xDrip + Dexcom app | No * |
| xDrip + Dexcom receiver | Yes |
| xDrip + t:slim pump | Yes |
| Dexcom receiver + t:slim pump | No |  
  
<br/>  
  
\*    You can use both xDrip and the Dexcom app if only Dexcom app is collecting from the transmitter. In that case, you can set up xDrip to receive readings from the Dexcom app using the [Companion App feature](./Follow/CompanionApp.md).  
  
