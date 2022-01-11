## Nightscout Settings  
  
To enable Nightscout uploader, go to the following page first.  
Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API)  

At the top of the page, enable the function.  
Tap on Base URL.  Enter the following:  
https://password@userID.herokuapp.com/api/v1/   

"password" is the "API_SECRET" you can find under your heroku config vars.  

This is what you enter in a web browser to see your readings:  
https://userID.herokuapp.com  
That's also all a Nightscout follower needs to follow you.  
