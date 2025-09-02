---
title: "Multiple Nightscout Accounts on Google Cloud"
description: "Learn why each Nightscout instance must use a separate Google account to qualify for free tier discounts on Google Cloud. Avoid unexpected charges when running multiple accounts."
layout: GCNS
---

# Multiple Nightscout accounts  
[Google Cloud Nightscout](./GoogleCloud.md) >> Multiple accounts  

In order to qualify for free tier, you need to have each one of your Nightscout accounts set up in a dedicated Google account.  
There is nothing wrong with having [multiple Google accounts](https://www.androidauthority.com/how-many-google-accounts-3060971/).  
And you can use the same credit card for all of them.  
  
If you have multiple virtual machines in the same Google account and are being charged, choose one of them to move, create a [backup](./DatabaseBackup.md) from it, create a new Google account, create a virtual machine in it, install Google Cloud Nightsout in it, transfer your backup to it, [restore the backup](./DatabaseRestore.md), update your uploader to upload to the new machine, and finally delete the original machine.  
If you do that for every additional Nightscout account you have and move them to dedicated Google accounts, all of your Nightscout accounts will then qualify for free tier.  

  
