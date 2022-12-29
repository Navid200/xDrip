## Clear Transmitter Queue  
[xDrip](../README.md) >> [Features](./Features_page) >> [xDrip & Dexcom](./Dexcom_page) >> Clear Transmitter Queue  
  
If you have a command in the [queue](./Transmitter-Queue.md) (G5/G6 status page) that should not be there now, you can clear the queue.  
![](./images/tx-queue.png)  

For example, if you don't have connectivity and there is a start sensor command in the queue, you should clear it.  Otherwise, after the connection is established, even if it is temporary, the command may go through.  

On the other hand, there are times a command in the queue is expected.  Every time you issue a command (start, stop, or calibrate), the command will go into the queue and wait for the next communications time slot.  In that case, you should not clear the queue.  Otherwise, you will interfere with proper operation.  

To clear the queue, tap on the dropper symbol on the main screen.  
![](./images/syringe-symbol.png)  

Long press (hold your finger on it instead of tapping on it) the microphone symbol at the bottom right corner of the form.  
![](./images/treatment-menu.png)  
<br/>  

Enter the following phrase into the form.  
clear transmitter queue  
