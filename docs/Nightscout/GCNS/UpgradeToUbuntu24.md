---
layout: GCNS
---

## Upgrade to Ubuntu 24  &nbsp; &nbsp;  
[Google Cloud Nightscout](../GoogleCloud.md) >> Upgrade to Ubuntu 24  
<br/>  
  
We continuously update the setup and provide an [update mechanism](../NS_SyncExecutables.md) for seamless updates.  
However, some updates—such as upgrading to Ubuntu 24—cannot be performed using this mechanism.  
The following instructions will guide you through the upgrade process.  
<br/>  
  
---  
  
1- Update your existing setup by following [these instructions](../NS_SyncExecutables.md).  
  
2- Create a [backup and transfer it to your computer](../DatabaseBackup.md).   
  
3- Set up a new virtual machine and install Nightscout:  
- Follow our [installation instructions](../GoogleCloud.md).  
- [Restore your backup](../DatabaseRestore.md) to the new installation.
- While both old and new virtual machines are active, you will be charged about $0.01 a day.  
<br/>  
  
4- Cleanup  
- Stop your old virtual machine.  
- Verify that your new installation is fully functional.  
- [Delete your original virtual machine](../DeleteVM.md).  
  
