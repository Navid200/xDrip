## Heroku is gone! Can I still get my data?  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Import data from Atlas  
  
**Work in progress**  

You will need the email address and password you used when you signed up for Atlas.  We all had to do this in 2020.  
Go to [mongodb](https://www.mongodb.com/home) and sign in.  
Take note of the database name (enclosed in orange in the following image).  
![](./images/Atlas_dbAccess.png)  
In the left pane, click on Database Access.  
  
You will be taken to the Database Access page.  Take note of the user name enclosed in orange in the following image.  You will need it later.  
![](./images/Atlas_dbAccess2.png)  
On the right, click on "Edit".  
  
You will be taken to the page shown below.  
![](./images/Atlas_pass.png)  
If you don't remember the password or if your password contains special characters, click on Edit Password and change the password.  Take note because you will need it later.  
  
With the username, database name, and password (excluding special characters) in hand, you can log out of Atlas.  
  
Open a [terminal](./Terminal.md).  Exit the menu.  Enter the following into the terminal replacing \<un\> with username, \<dn\> with database name, and \<pw\> with password.
  
<mark style="background-color: #eFdFef">mongodump --forceTableScan --uri mongodb+srv://(un):(pw)@(dn).csbuy.mongodb.net/(un) </mark>

Your data is on Atlas, not on Heroku.  Fortunately, you can still get it.  
  
Steps for importing your data from Atlas will be added here shortly.  
