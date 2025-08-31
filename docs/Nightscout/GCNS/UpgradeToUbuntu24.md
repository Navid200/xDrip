---
title: "Upgrade Google Cloud Nightscout to Ubuntu 24"
description: "Step-by-step guide to upgrading your Google Cloud Nightscout (GCNS) setup from Ubuntu 20 to Ubuntu 24. Documentation and help for a smooth upgrade process."
layout: GCNS
---

# Upgrade to Ubuntu 24  &nbsp; &nbsp;  
[Google Cloud Nightscout](../GoogleCloud.md) >> Upgrade to Ubuntu 24  
<br/>  
  
We continuously update the setup and provide an [update mechanism](../NS_SyncExecutables.md) for seamless updates.  
However, some updates—such as upgrading to Ubuntu 24—cannot be performed using this mechanism.  
The following instructions will guide you through the upgrade process.  
<br/>  
  
---  
  
1- Back Up Your Data  
[Create a backup and transfer it to your computer](../DatabaseBackup.md).  
<br/>  
  
2- Set Up a New Virtual Machine and Install Nightscout  
- Follow our [installation instructions](../GoogleCloud.md).  
- [Restore your backup](../DatabaseRestore.md) to the new installation.
- While both original and new virtual machines are active, you will be charged about $0.01 a day.  
<br/>  
  
3- Clean Up  
- [Stop](../StopVM.md) your old virtual machine.  
- Verify that your new installation is fully functional and contains all your data.  
- [Delete your original virtual machine](../DeleteVM.md).  
  
