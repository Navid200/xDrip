## Import data from Heroku Atlas
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >> Import data from Atlas  
  
**This page is being edited and is not complete.**  
Please be patient until this note is removed.  
  
Even though Heroku is not free any longer, you can still transfer your database for free as it is on Atlas and not Heroku.  This page explains how to do it.  
In order to transfer a database from Atlas, you need to have already completed Nightscout [installation](./NS_Install.md).  
<br/>  
  
---  
  
#### **Get mongodump.exe, for Windows**  
First, you need to use mongodump to get a backup of your database.  Let's assume you want to do that using Windows.  Download the Database tools MSI installer zip file from [this](https://www.mongodb.com/docs/database-tools/installation/installation-windows/) page.  
Let's say you download to your desktop.  Extract all.  
Now, you will have the mongodump.exe file inside the uncompressed folder at: Desktop/mongodb-database-tools-windows-x86_64-100.7.0/mongodb-database-tools-windows-x86_64-100.7.0/bin/mongodump.exe.  We will use this later.  
<br/>  
  
---  
  
#### **Get mongodump command parameters**  
You will need the email address and password associated with your Atlas account.  We all had to sign up in 2020.  
Go to [mongodb](https://www.mongodb.com/home) and sign in.  In the left pane, click on Database Access.  
![](./images/Atlas_dbAccess.png)  
  
You will be taken to the Database Access page.  Take note of the user name (enclosed in orange in the following image).  You will need it later.  
![](./images/Atlas_dbAccess2.png)  
On the right, click on "Edit".  
  
You will be taken to the page shown below.  
![](./images/Atlas_pass.png)  
If you don't remember the password (this is not the password you use to log into Atlas) or if your password contains special characters, click on Edit Password and change the password.  Take note of the password because you will need it later.  
<br/>  
  
---  
  
#### **Get mongodump command string**  
In the left pane, click on "Database" to go back to the main page.  Click on the 3-dot menu button and click on "Command Line Tools" as shown in the following image.  
![](./images/Atlas_CLTools.png)  
  
Under "Binary Import and Export Tools", copy the mongodump string.  You can do that by clicking on the copy symbol on the right side of the string, as marked in the image below.  
![](./images/mongodump.png)  
<br/>  
  
---    
  
#### **mongodump**  
Open a "Windows Terminal" after right clicking on Start.  
Type and enter cd Desktop.  Type cd and press TAB to fill the folder name.  Press ENTER.  Again, Type cd and press TAB and ENTER.  
Now, type and enter cd bin.  
Type and enter ls.  You should see the commands, including mongodump.exe that we will use, as shown below.  
![](./images/WinMongodumpExe.png)  
  
Paste the mongodump string you copied, from the binary import and export tools, into the terminal.  It will be similar to what is shown in the followiong figure.  
![](./images/dumpPaste.png)  
  
There are three changes you need to make to this string before executing it.  
1- Replace mongodump with ./mongodump.exe  
2- Replace \<PASSWORD\>, including the brackets, with your Atlas database admin password, which excludes special characters.  
3- Replace \<DATABASE\>, including brackets, with the user name you copied from the Atlas database access page.  
  
What you will have in the terminal after these three changes should look like the following.  
![](./images/dumpPasteMod.png)  
Press Enter.  
  
Wait for the export process to complete.  It could take a few minutes.  If it succeeds, you will see something as shown below.  
![](./images/ExportSuccess.png)  
<br/>  
  
---  
  
#### **Transfer to virtual machine**  
We now need to transfer the output of mongodump to the virtual machine.  The output is a folder located where we ran the mongodump command.  Use windows explorer and navigate to Desktop\mongodb-database-tools-windows-x86_64-100.7.0\mongodb-database-tools-windows-x86_64-100.7.0\bin.  You should see what is shown in the following figure.  
![](./images/WinMongoDumpOut.png)  
  
Right click the dump folder and select "Compress to ZIP file".  Now, we have a ZIP file.  Please [upload](./Upload_Download.md) it to your virtual machine.  
  
<br/>
  
---  
  
#### **mongorestore**  
We first need to unzip the file we uploaded to the virtual machine.  
Go to your home directory, where the dump.zip file should be after the upload.  Type and enter the following.  
  
```
unzip dump.zip
```
  
If you see the following message, it means that the unzip command is not installed.  
![](./images/unzipNotInstalled.png)  
  
If unzip is not installed, type and enter the following two commands in sequence to install it.  

```  
sudo apt-get update
sudo apt-get install unzip
```  
  
If unzip was not installed and you installed it, run the unzip command again.  The dump folder should be in your home directory.  
  
Now, enter the following in the terminal replacing UserName with the user name you copied from the Atlas database access page.  
  
```  
mongorestore -d Nightscout ./dump/UserName 
```  
<br/>  
  
If it succeeds, you will see something as shown below.  
![](./images/RestoreSuccess.png)  
  
Wait a minute.  Reload Nightscout in a browser.  Your imported data from Atlas should be present now.  
  
